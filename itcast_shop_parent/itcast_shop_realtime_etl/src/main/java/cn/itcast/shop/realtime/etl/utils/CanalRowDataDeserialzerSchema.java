package cn.itcast.shop.realtime.etl.utils;

import cn.itcast.canal.bean.CanalRowDate;
import org.apache.flink.api.common.serialization.AbstractDeserializationSchema;

import java.io.IOException;

public class CanalRowDataDeserialzerSchema extends AbstractDeserializationSchema<CanalRowDate> {

    @Override
    public CanalRowDate deserialize(byte[] bytes) throws IOException {
        return new CanalRowDate(bytes);
    }
}
