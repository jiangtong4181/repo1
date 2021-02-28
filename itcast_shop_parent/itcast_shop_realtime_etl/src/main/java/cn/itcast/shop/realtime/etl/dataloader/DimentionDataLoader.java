package cn.itcast.shop.realtime.etl.dataloader;

import cn.itcast.shop.realtime.etl.bean.*;
import cn.itcast.shop.realtime.etl.utils.GlobalConfigUtil;
import cn.itcast.shop.realtime.etl.utils.RedisUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import redis.clients.jedis.Jedis;

import java.sql.*;

//维度表数据全量装载redis
//五个维度表数据
public class DimentionDataLoader {
    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");
        String keyMysqlServerIp = GlobalConfigUtil.getKeyMysqlServerIp();
        String keyMysqlServerPort = GlobalConfigUtil.getKeyMysqlServerPort();
        String keyMysqlServerUsername = GlobalConfigUtil.getKeyMysqlServerUsername();
        String keyMysqlServerPassword = GlobalConfigUtil.getKeyMysqlServerPassword();
        String keyMysqlServerDatabase = GlobalConfigUtil.getKeyMysqlServerDatabase();

        Connection conn = DriverManager.getConnection("jdbc:mysql://" + keyMysqlServerIp + ":" +
                        keyMysqlServerPort + "/" + keyMysqlServerDatabase +
                        "?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC",
                keyMysqlServerUsername,
                keyMysqlServerPassword);
        Jedis jedis = RedisUtil.getJedis();
        jedis.select(1);
        loadDimGoods(conn, jedis);
        loadDimShops(conn, jedis);
        loadDimGoodsCats(conn, jedis);
        loadDimOrg(conn, jedis);
        LoadDimShopCats(conn, jedis);
        System.out.println("全部完成");
        System.exit(0);
    }


    public static void loadDimGoods(Connection conn, Jedis jedis) throws SQLException {
        PreparedStatement ppst;
        String sql = "SELECT\n" +
                "goodsId,\n" +
                "goodsName,\n" +
                "goodsCatId,\n" +
                "shopPrice,\n" +
                "shopId\n" +
                "from\n" +
                "itcast_goods";
        ppst = conn.prepareStatement(sql);
        ResultSet resultSet = ppst.executeQuery();
        while (resultSet.next()) {
            long goodsId = resultSet.getLong("goodsId");
            String goodsName = resultSet.getString("goodsName");
            long shopId = resultSet.getLong("shopId");
            int goodsCatId = resultSet.getInt("goodsCatId");
            double shopPrice = resultSet.getDouble("shopPrice");
            DimGoodsDBEntity dimGoodsDBEntity = new DimGoodsDBEntity(goodsId, goodsName, shopId, goodsCatId, shopPrice);
            String jsonString = JSON.toJSONString(dimGoodsDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_goods", String.valueOf(goodsId), jsonString);
        }
        System.out.println("dim_goods完成");
        resultSet.close();
        ppst.close();
        //conn.close();
    }

    public static void loadDimShops(Connection conn, Jedis jedis) throws SQLException {
        PreparedStatement ppst;
        String sql = "SELECT\n" +
                "shopId,\n" +
                "areaId,\n" +
                "shopName,\n" +
                "shopCompany\n" +
                "from\n" +
                "itcast_shops\n";
        ppst = conn.prepareStatement(sql);
        ResultSet resultSet = ppst.executeQuery();
        while (resultSet.next()) {
            int shopId = resultSet.getInt("shopId");
            int areaId = resultSet.getInt("areaId");
            String shopName = resultSet.getString("shopName");
            String shopCompany = resultSet.getString("shopCompany");
            DimShopsDBEntity dimShopsDBEntity = new DimShopsDBEntity(shopId, areaId, shopName, shopCompany);
            String jsonString = JSON.toJSONString(dimShopsDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_shops", String.valueOf(shopId), jsonString);
        }
        System.out.println("dim_shops完成");
        resultSet.close();
        ppst.close();
        //conn.close();
    }

    public static void loadDimGoodsCats(Connection conn, Jedis jedis) throws SQLException {
        PreparedStatement ppst;
        String sql = "SELECT\n" +
                "catId,\n" +
                "parentId,\n" +
                "catName,\n" +
                "cat_level\n" +
                "FROM\n" +
                "itcast_goods_cats";
        ppst = conn.prepareStatement(sql);
        ResultSet resultSet = ppst.executeQuery();
        while (resultSet.next()) {
            String catId = resultSet.getString("catId");
            String parentId = resultSet.getString("parentId");
            String catName = resultSet.getString("catName");
            String cat_level = resultSet.getString("cat_level");
            DimGoodsCatDBEntity dimGoodsCatDBEntity = new DimGoodsCatDBEntity(catId, parentId, catName, cat_level);
            String jsonString = JSON.toJSONString(dimGoodsCatDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_goods_cats", catId, jsonString);
        }
        System.out.println("dim_goods_cats完成");
        resultSet.close();
        ppst.close();
        //conn.close();
    }

    public static void loadDimOrg(Connection conn, Jedis jedis) throws SQLException {
        PreparedStatement ppst;
        String sql = "SELECT\n" +
                "orgid,\n" +
                "parentid,\n" +
                "orgName,\n" +
                "orgLevel\n" +
                "FROM\n" +
                "itcast_org";
        ppst = conn.prepareStatement(sql);
        ResultSet resultSet = ppst.executeQuery();
        while (resultSet.next()) {
            int orgId = resultSet.getInt("orgId");
            int parentId = resultSet.getInt("parentId");
            String orgName = resultSet.getString("orgName");
            int orgLevel = resultSet.getInt("orgLevel");
            DimOrgDBEntity dimOrgDBEntity = new DimOrgDBEntity(orgId, parentId, orgName, orgLevel);
            String jsonString = JSON.toJSONString(dimOrgDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_org", String.valueOf(orgId), jsonString);
        }
        System.out.println("dim_org完成");
        resultSet.close();
        ppst.close();
        //conn.close();
    }

    public static void LoadDimShopCats(Connection conn, Jedis jedis) throws SQLException {
        PreparedStatement ppst;
        String sql = "SELECT\n" +
                "catId,\n" +
                "parentId,\n" +
                "catName,\n" +
                "catSort\n" +
                "FROM\n" +
                "itcast_shop_cats";
        ppst = conn.prepareStatement(sql);
        ResultSet resultSet = ppst.executeQuery();
        while (resultSet.next()) {
            String catId = resultSet.getString("catId");
            String parentId = resultSet.getString("parentId");
            String catName = resultSet.getString("catName");
            String catSort = resultSet.getString("catSort");
            DimShopCatDBEntity dimShopCatDBEntity = new DimShopCatDBEntity(catId, parentId, catName, catSort);
            String jsonString = JSON.toJSONString(dimShopCatDBEntity, SerializerFeature.DisableCircularReferenceDetect);
            jedis.hset("itcast_shop:dim_shop_cats", String.valueOf(catId), jsonString);
        }
        System.out.println("dim_shop_cats完成");
        resultSet.close();
        ppst.close();
        conn.close();
    }
}