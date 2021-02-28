package cn.itcast.shop.realtime.etl.utils;

import java.io.IOException;
import java.util.Properties;

public class GlobalConfigUtil {
    private static Properties properties;
    static {
        properties = new Properties();
        try {
            properties.load(GlobalConfigUtil.class.getClassLoader().getResourceAsStream("application.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getBootstrapServers(){
        return properties.getProperty("bootstrap.servers");
    }

    public static String getZookeeperConnect(){
        return properties.getProperty("zookeeper.connect");
    }

    public static String getGroupId(){
        return properties.getProperty("group.id");
    }

    public static String getEnableAutoCommit(){
        return properties.getProperty("enable.auto.commit");
    }

    public static String getAutoCommitIntervalMs(){
        return properties.getProperty("auto.commit.interval.ms");
    }

    public static String getAutoOffsetReset(){
        return properties.getProperty("auto.offset.reset");
    }

    public static String getKeySerializer(){
        return properties.getProperty("key.serializer");
    }

    public static String getKeyDeserializer(){
        return properties.getProperty("key.deserializer");
    }

    public static String getKeyMysqlServerUsername(){
        return properties.getProperty("mysql.server.username");
    }

    public static String getKeyMysqlServerPassword(){
        return properties.getProperty("mysql.server.password");
    }

    public static String getKeyMysqlServerIp(){
        return properties.getProperty("mysql.server.ip");
    }

    public static String getKeyMysqlServerPort(){
        return properties.getProperty("mysql.server.port");
    }

    public static String getKeyMysqlServerDatabase(){
        return properties.getProperty("mysql.server.database");
    }

    public static String getKeyRedisServerIp(){
        return properties.getProperty("redis.server.ip");
    }

    public static String getKeyRedisServerPort(){
        return properties.getProperty("redis.server.port");
    }

    public static String getKeyInputTopicCanal(){
        return properties.getProperty("input.topic.canal");
    }

}
