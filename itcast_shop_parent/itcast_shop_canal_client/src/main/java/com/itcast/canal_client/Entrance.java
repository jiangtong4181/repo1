package com.itcast.canal_client;

public class Entrance {
    public static void main(String[] args) {
        //实例化canalclient，拉取binlog日志
        CanalClient canalClient = new CanalClient();
        canalClient.start();

    }
}
