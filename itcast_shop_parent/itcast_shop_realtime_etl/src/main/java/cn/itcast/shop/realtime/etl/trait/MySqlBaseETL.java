package cn.itcast.shop.realtime.etl.trait;
import cn.itcast.canal.bean.CanalRowDate;
import cn.itcast.shop.realtime.etl.utils.CanalRowDataDeserialzerSchema;
import cn.itcast.shop.realtime.etl.utils.KafkaProp;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer011;

abstract class MySqlBaseETL implements BaseETL<CanalRowDate> {

    public StreamExecutionEnvironment env;

    public MySqlBaseETL(StreamExecutionEnvironment env) {
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

}
