package cn.itcast.canal.protobuf;

import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

/*
实现kafkavalue的自定义序列化对象
 */
public class ProtoBufSerializer implements Serializer<ProtoBufable> {
    @Override
    public void configure(Map<String, ?> map, boolean b) {

    }

    @Override
    public byte[] serialize(String s, ProtoBufable data) {
        return data.toBytes();
    }

    @Override
    public void close() {

    }
}
