package cn.itcast.shop.realtime.etl.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class test {
    public static void main(String[] args) {

        String s = "jdbc:mysql://"+GlobalConfigUtil.getKeyMysqlServerIp()+":"+
                GlobalConfigUtil.getKeyMysqlServerPort()+"/"+GlobalConfigUtil.getKeyMysqlServerDatabase()+"?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC";
        System.out.println(s);
    }
}
