package cn.itcast.shop.realtime.etl.trait;

import org.apache.flink.streaming.api.datastream.DataStream;

public interface BaseETL<T> {

    DataStream<T> getKafkaDataStream(String topic);

    void process(String topic);

}
