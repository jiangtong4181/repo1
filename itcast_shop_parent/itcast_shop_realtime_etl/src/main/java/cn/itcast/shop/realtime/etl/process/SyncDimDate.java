package cn.itcast.shop.realtime.etl.process;

import cn.itcast.canal.bean.CanalRowDate;
import cn.itcast.shop.realtime.etl.bean.*;
import cn.itcast.shop.realtime.etl.trait.BaseETL;
import cn.itcast.shop.realtime.etl.utils.CanalRowDataDeserialzerSchema;
import cn.itcast.shop.realtime.etl.utils.KafkaProp;
import cn.itcast.shop.realtime.etl.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;
import redis.clients.jedis.Jedis;

//增量更新到redis中
public class SyncDimDate implements BaseETL<CanalRowDate> {
    private static Jedis jedis;

    private StreamExecutionEnvironment env;

    public SyncDimDate(StreamExecutionEnvironment env) {
        this.env = env;
    }


    @Override
    public DataStream<CanalRowDate> getKafkaDataStream(String topic) {
        FlinkKafkaConsumer011<CanalRowDate> kafkaConsumer011 = new FlinkKafkaConsumer011<CanalRowDate>(
                topic,
                new CanalRowDataDeserialzerSchema(),
                KafkaProp.getProperties()
        );
        return env.addSource(kafkaConsumer011);
    }

    @Override
    public void process(String topic) {
        /**
         * 获取数据源
         * 过滤维度表
         * 增量更新
         */
        DataStream<CanalRowDate> kafkaDataStream = getKafkaDataStream(topic);
        SingleOutputStreamOperator<CanalRowDate> filter = kafkaDataStream.filter(new FilterFunction<CanalRowDate>() {
            @Override
            public boolean filter(CanalRowDate rowDate) throws Exception {
                String tableName = rowDate.getTableName();
                if (tableName.equals("itcast_goods") ||
                        tableName.equals("itcast_shops") ||
                        tableName.equals("itcast_goods_cats") ||
                        tableName.equals("itcast_org") ||
                        tableName.equals("itcast_shop_cats")) {
                    return true;
                } else {
                    return false;
                }
            }
        });


        filter.addSink(new RichSinkFunction<CanalRowDate>() {

            @Override
            public void open(Configuration parameters) throws Exception {
                jedis = RedisUtil.getJedis();
                jedis.select(1);
            }

            @Override
            public void close() throws Exception {
                if (jedis.isConnected()) {
                    jedis.close();
                }
            }

            @Override
            public void invoke(CanalRowDate value, Context context) throws Exception {
                String eventType = value.getEventType();
                if (eventType.equals("insert") || eventType.equals("update")) {
                    updateDimData(value);
                } else if (eventType.equals("delete")) {
                    deleteDimData(value);
                } else {

                }
            }
        });
    }

    private static void deleteDimData(CanalRowDate value) {
        String tableName = value.getTableName();
        switch (tableName) {
            case "itcast_goods":
                String goodsId = value.getColumn().get("goodsId");
                jedis.hdel("itcast_shop:dim_goods", goodsId);
                break;
            case "itcast_shops":
                String shopId = value.getColumn().get("shopId");
                jedis.hdel("itcast_shop:dim_shops", shopId);
                break;
            case "itcast_goods_cats":
                String catId = value.getColumn().get("catId");
                jedis.hdel("itcast_shop:dim_goods_cats", catId);
                break;
            case "itcast_org":
                String orgId = value.getColumn().get("orgId");
                jedis.hdel("itcast_shop:dim_org", orgId);
                break;
            case "itcast_shop_cats":
                String catId2 = value.getColumn().get("catId");
                jedis.hdel("itcast_shop:dim_shop_cats", catId2);
                break;
        }
    }

    private static void updateDimData(CanalRowDate value) {
        String tableName = value.getTableName();
        if (tableName.equals("itcast_goods")) {
            String goodsId = value.getColumn().get("goodsId");
            String goodsName = value.getColumn().get("goodsName");
            String shopId = value.getColumn().get("shopId");
            String goodsCatId = value.getColumn().get("goodsCatId");
            String shopPrice = value.getColumn().get("shopPrice");
            DimGoodsDBEntity dimGoodsDBEntity = new DimGoodsDBEntity(Long.parseLong(goodsId),
                    goodsName, Long.parseLong(shopId), Integer.parseInt(goodsCatId), Double.parseDouble(shopPrice));
            String jsonString = JSON.toJSONString(dimGoodsDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_goods", goodsId, jsonString);
        } else if (tableName.equals("itcast_shops")) {
            String shopId = value.getColumn().get("shopId");
            String areaId = value.getColumn().get("areaId");
            String shopName = value.getColumn().get("shopName");
            String shopCompany = value.getColumn().get("shopCompany");
            DimShopsDBEntity dimShopsDBEntity = new DimShopsDBEntity(Integer.parseInt(shopId), Integer.parseInt(areaId), shopName, shopCompany);
            String jsonString = JSON.toJSONString(dimShopsDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_shops", String.valueOf(shopId), jsonString);
        } else if (tableName.equals("itcast_goods_cats")) {
            String catId = value.getColumn().get("catId");
            String parentId = value.getColumn().get("parentId");
            String catName = value.getColumn().get("catName");
            String cat_level = value.getColumn().get("cat_level");
            DimGoodsCatDBEntity dimGoodsCatDBEntity = new DimGoodsCatDBEntity(catId, parentId, catName, cat_level);
            String jsonString = JSON.toJSONString(dimGoodsCatDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_goods_cats", catId, jsonString);
        } else if (tableName.equals("itcast_org")) {
            String orgId = value.getColumn().get("orgId");
            String parentId = value.getColumn().get("parentId");
            String orgName = value.getColumn().get("orgName");
            String orgLevel = value.getColumn().get("orgLevel");
            DimOrgDBEntity dimOrgDBEntity = new DimOrgDBEntity(Integer.parseInt(orgId), Integer.parseInt(parentId), orgName, Integer.parseInt(orgLevel));
            String jsonString = JSON.toJSONString(dimOrgDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_org", String.valueOf(orgId), jsonString);
        } else if (tableName.equals("itcast_shop_cats")) {
            String catId = value.getColumn().get("catId");
            String parentId = value.getColumn().get("parentId");
            String catName = value.getColumn().get("catName");
            String catSort = value.getColumn().get("catSort");
            DimShopCatDBEntity dimShopCatDBEntity = new DimShopCatDBEntity(catId, parentId, catName, catSort);
            String jsonString = JSON.toJSONString(dimShopCatDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_shop_cats", String.valueOf(catId), jsonString);
        }
    }
}
