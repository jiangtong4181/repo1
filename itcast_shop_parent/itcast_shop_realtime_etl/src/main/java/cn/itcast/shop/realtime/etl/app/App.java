package cn.itcast.shop.realtime.etl.app;
import cn.itcast.canal.bean.CanalRowDate;
import cn.itcast.shop.realtime.etl.process.SyncDimDate;
import cn.itcast.shop.realtime.etl.utils.GlobalConfigUtil;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.common.time.Time;
import org.apache.flink.runtime.state.filesystem.FsStateBackend;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.CheckpointConfig;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class App {
    public static void main(String[] args) throws Exception {
        System.setProperty("HADOOP_USER_NAME", "root");
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        //每隔5秒钟进行一次checkpoint
        env.enableCheckpointing(5000);
        //任务取消时，保留以前的checkpoint，避免数据丢失
        env.getCheckpointConfig().enableExternalizedCheckpoints(CheckpointConfig.ExternalizedCheckpointCleanup.RETAIN_ON_CANCELLATION);
        //同一时间只能有一个检查点
        env.getCheckpointConfig().setMaxConcurrentCheckpoints(1);
        //设置EXACTLY_ONCE
        env.getCheckpointConfig().setCheckpointingMode(CheckpointingMode.EXACTLY_ONCE);
        //设置statebackend位置
        env.setStateBackend(new FsStateBackend("hdfs://hadoop101:8020/flinkck"));
        //设定重启策略
        env.setRestartStrategy(RestartStrategies.fixedDelayRestart(3,Time.seconds(5)));
        //设置两次checkpoint的最小时间间隔
        env.getCheckpointConfig().setMinPauseBetweenCheckpoints(1000);
        //设置checkpoint超时时间
        env.getCheckpointConfig().setCheckpointTimeout(60000);
        SyncDimDate syncDimDate = new SyncDimDate(env);
        //处理维度表增量数据
        //DataStream<CanalRowDate> kafkaDataStream = syncDimDate.getKafkaDataStream(GlobalConfigUtil.getKeyInputTopicCanal());
        //kafkaDataStream.print();
//        kafkaDataStream.map(new MapFunction<CanalRowDate, String>() {
//            @Override
//            public String map(CanalRowDate rowDate) throws Exception {
//                return rowDate.getTableName();
//            }
//        }).print();
        syncDimDate.process(GlobalConfigUtil.getKeyInputTopicCanal());
        env.execute();
    }
}
