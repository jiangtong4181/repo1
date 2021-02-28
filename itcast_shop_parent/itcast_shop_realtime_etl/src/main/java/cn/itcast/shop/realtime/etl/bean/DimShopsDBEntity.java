package cn.itcast.shop.realtime.etl.bean;
// 店铺维度样例类
public class DimShopsDBEntity {

    private int shopId;
    private int areaId;
    private String shopName;
    private String shopCompany;

    public DimShopsDBEntity() {
    }

    public DimShopsDBEntity(int shopId, int areaId, String shopName, String shopCompany) {
        this.shopId = shopId;
        this.areaId = areaId;
        this.shopName = shopName;
        this.shopCompany = shopCompany;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopCompany() {
        return shopCompany;
    }

    public void setShopCompany(String shopCompany) {
        this.shopCompany = shopCompany;
    }
}
