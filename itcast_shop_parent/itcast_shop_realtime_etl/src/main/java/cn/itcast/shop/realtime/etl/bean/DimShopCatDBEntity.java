package cn.itcast.shop.realtime.etl.bean;

// 门店商品分类维度样例类
public class DimShopCatDBEntity {
    private String catId;
    private String parentId;
    private String catName;
    private String catSort;

    public DimShopCatDBEntity() {
    }

    public DimShopCatDBEntity(String catId, String parentId, String catName, String catSort) {
        this.catId = catId;
        this.parentId = parentId;
        this.catName = catName;
        this.catSort = catSort;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getCatSort() {
        return catSort;
    }

    public void setCatSort(String catSort) {
        this.catSort = catSort;
    }
}
