package cn.itcast.shop.realtime.etl.trait;

import cn.itcast.shop.realtime.etl.trait.BaseETL;
import cn.itcast.shop.realtime.etl.utils.KafkaProp;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

abstract class MQBaseETL implements BaseETL<String> {

    private StreamExecutionEnvironment env;

    public MQBaseETL(StreamExecutionEnvironment env) {
        this.env = env;
    }


    @Override
    public DataStream<String> getKafkaDataStream(String topic) {
        FlinkKafkaConsumer011<String> kafkaConsumer011 = new FlinkKafkaConsumer011<>(
                topic,
                new SimpleStringSchema(),
                KafkaProp.getProperties()
        );
        return env.addSource(kafkaConsumer011);
    }
}
