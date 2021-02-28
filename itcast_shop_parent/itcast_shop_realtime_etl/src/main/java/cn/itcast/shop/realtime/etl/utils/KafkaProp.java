package cn.itcast.shop.realtime.etl.utils;

import java.util.Properties;

public class KafkaProp {
    public static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", GlobalConfigUtil.getBootstrapServers());
        properties.setProperty("zookeeper.connect", GlobalConfigUtil.getZookeeperConnect());
        properties.setProperty("group.id", GlobalConfigUtil.getGroupId());
        properties.setProperty("enable.auto.commit", GlobalConfigUtil.getEnableAutoCommit());
        properties.setProperty("auto.commit.interval.ms", GlobalConfigUtil.getAutoCommitIntervalMs());
        properties.setProperty("auto.offset.reset", GlobalConfigUtil.getAutoOffsetReset());
        properties.setProperty("key.serializer", GlobalConfigUtil.getKeySerializer());
        properties.setProperty("key.deserializer", GlobalConfigUtil.getKeyDeserializer());
        return properties;
    }
}
