package cn.itcast.canal.bean;

//这个类是canal数据的protobuf的实现类
//能够使用protobuf序列化为bean对象
//将binlog解析后的map对象，转换成protobuf字节码数据

import cn.itcast.canal.protobuf.CanalModel;
import cn.itcast.canal.protobuf.ProtoBufable;
import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;

import java.util.HashMap;
import java.util.Map;

public class CanalRowDate implements ProtoBufable {

    private String logfileName;
    private Long logfileOffset;
    private Long executeTime;
    private String schemaName;
    private String tableName;
    private String eventType;
    private Map<String, String> column;

    public String getLogfileName() {
        return logfileName;
    }

    public void setLogfileName(String logfileName) {
        this.logfileName = logfileName;
    }

    public Long getLogfileOffset() {
        return logfileOffset;
    }

    public void setLogfileOffset(Long logfileOffset) {
        this.logfileOffset = logfileOffset;
    }

    public Long getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(Long executeTime) {
        this.executeTime = executeTime;
    }

    public String getSchemaName() {
        return schemaName;
    }

    public void setSchemaName(String schemaName) {
        this.schemaName = schemaName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Map<String, String> getColumn() {
        return column;
    }

    public void setColumn(Map<String, String> column) {
        this.column = column;
    }

    public CanalRowDate(Map map) {
        if (map.size() > 0) {
            this.logfileName = map.get("logfileName").toString();
            this.logfileOffset = Long.parseLong(map.get("logfileOffset").toString());
            this.executeTime = Long.parseLong(map.get("executeTime").toString());
            this.schemaName = map.get("schemaName").toString();
            this.tableName = map.get("tableName").toString();
            this.eventType = map.get("eventType").toString();
            this.column = (Map<String, String>) map.get("columns");
        }
    }

    public CanalRowDate(byte[] bys) {
        try {
            CanalModel.RowData rowData = CanalModel.RowData.parseFrom(bys);
            this.logfileName = rowData.getLogfileName();
            this.logfileOffset = rowData.getLogfileOffset();
            this.executeTime = rowData.getExecuteTime();
            this.schemaName = rowData.getSchemaName();
            this.tableName = rowData.getTableName();
            this.eventType = rowData.getEventType();
            this.column = new HashMap<>();
            this.column.putAll(rowData.getColumnsMap());
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
    }


    @Override
    public byte[] toBytes() {
        CanalModel.RowData.Builder builder = CanalModel.RowData.newBuilder();
        builder.setLogfileName(this.logfileName);
        builder.setLogfileOffset(this.logfileOffset);
        builder.setExecuteTime(this.executeTime);
        builder.setSchemaName(this.schemaName);
        builder.setTableName(this.tableName);
        builder.setEventType(this.eventType);
        for (String s : this.getColumn().keySet()) {
            builder.putColumns(s, this.getColumn().get(s));
        }
        return builder.build().toByteArray();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
