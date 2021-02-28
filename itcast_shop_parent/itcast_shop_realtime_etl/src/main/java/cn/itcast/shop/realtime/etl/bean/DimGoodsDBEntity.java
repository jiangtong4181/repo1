package cn.itcast.shop.realtime.etl.bean;
// 商品维度样例类
public class DimGoodsDBEntity {

    public DimGoodsDBEntity() {
    }

    public DimGoodsDBEntity(Long goodsId, String goodsName, Long shopId, int goodsCatId, Double shopPrice) {
        this.goodsId = goodsId;
        this.goodsName = goodsName;
        this.shopId = shopId;
        this.goodsCatId = goodsCatId;
        this.shopPrice = shopPrice;
    }

    private Long goodsId;
    private String goodsName;
    private Long shopId;
    private int goodsCatId;
    private Double shopPrice;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public int getGoodsCatId() {
        return goodsCatId;
    }

    public void setGoodsCatId(int goodsCatId) {
        this.goodsCatId = goodsCatId;
    }

    public Double getShopPrice() {
        return shopPrice;
    }

    public void setShopPrice(Double shopPrice) {
        this.shopPrice = shopPrice;
    }
}
