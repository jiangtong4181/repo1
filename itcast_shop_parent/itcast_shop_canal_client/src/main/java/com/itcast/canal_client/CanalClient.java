package com.itcast.canal_client;

import cn.itcast.canal.bean.CanalRowDate;
import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import com.itcast.canal_client.kafka.KafkaSender;
import com.itcast.canal_client.util.ConfigUtil;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
canal客户端，获取canalserver端的binlog日志
 */
public class CanalClient {

    private static final int BATCH_SIZE = 1024 * 5;
    private CanalConnector canalConnector;
    private KafkaSender kafkaSender;


    //构造方法，初始化canal链接
    public CanalClient() {
        canalConnector = CanalConnectors.newSingleConnector(new InetSocketAddress(ConfigUtil.canalServerIp(), ConfigUtil.canalServerPort()),
                ConfigUtil.canalServerDestination(),
                ConfigUtil.canalServerUsername(),
                ConfigUtil.canalServerPassword());
        kafkaSender = new KafkaSender();
    }


    public void start() {
        try {
            canalConnector.connect();
            //回滚数据
            canalConnector.rollback();
            //订阅数据库
            canalConnector.subscribe(ConfigUtil.canalSubscribeFilter());
            while (true) {
                Message message = canalConnector.getWithoutAck(BATCH_SIZE);
                long batchId = message.getId();
                int size = message.getEntries().size();
                if (size == 0 || size == -1) {
                    //没有拉取到数据
                } else {
                    Map messageToMap = binlogMessageToMap(message);
                    //将map对象转换成protobuf文件
                    CanalRowDate canalRowDate = new CanalRowDate(messageToMap);
                    if (messageToMap.size() > 0) {
                        System.out.println(JSON.toJSONString(canalRowDate));
                        kafkaSender.send(canalRowDate);
                    }
                }
                canalConnector.ack(batchId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            canalConnector.disconnect();
        }

    }


    //将binlog转换成map结构
    private Map binlogMessageToMap(Message message) throws InvalidProtocolBufferException {
        Map rowDataMap = new HashMap();

        // 1. 遍历message中的所有binlog实体
        for (CanalEntry.Entry entry : message.getEntries()) {
            // 只处理事务型binlog
            if (entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONBEGIN ||
                    entry.getEntryType() == CanalEntry.EntryType.TRANSACTIONEND) {
                continue;
            }

            // 获取binlog文件名
            String logfileName = entry.getHeader().getLogfileName();
            // 获取logfile的偏移量
            long logfileOffset = entry.getHeader().getLogfileOffset();
            // 获取sql语句执行时间戳
            long executeTime = entry.getHeader().getExecuteTime();
            // 获取数据库名
            String schemaName = entry.getHeader().getSchemaName();
            // 获取表名
            String tableName = entry.getHeader().getTableName();
            // 获取事件类型 insert/update/delete
            String eventType = entry.getHeader().getEventType().toString().toLowerCase();

            rowDataMap.put("logfileName", logfileName);
            rowDataMap.put("logfileOffset", logfileOffset);
            rowDataMap.put("executeTime", executeTime);
            rowDataMap.put("schemaName", schemaName);
            rowDataMap.put("tableName", tableName);
            rowDataMap.put("eventType", eventType);

            // 获取所有行上的变更
            Map<String, String> columnDataMap = new HashMap<>();
            CanalEntry.RowChange rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            List<CanalEntry.RowData> columnDataList = rowChange.getRowDatasList();
            for (CanalEntry.RowData rowData : columnDataList) {
                if (eventType.equals("insert") || eventType.equals("update")) {
                    for (CanalEntry.Column column : rowData.getAfterColumnsList()) {
                        columnDataMap.put(column.getName(), column.getValue().toString());
                    }
                } else if (eventType.equals("delete")) {
                    for (CanalEntry.Column column : rowData.getBeforeColumnsList()) {
                        columnDataMap.put(column.getName(), column.getValue().toString());
                    }
                }
            }

            rowDataMap.put("columns", columnDataMap);
        }

        return rowDataMap;
    }
}
