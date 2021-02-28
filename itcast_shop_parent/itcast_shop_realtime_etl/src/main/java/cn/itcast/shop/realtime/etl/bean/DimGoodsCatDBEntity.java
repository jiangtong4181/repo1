package cn.itcast.shop.realtime.etl.bean;
// 商品分类维度样例类
public class DimGoodsCatDBEntity {
    private String catId;
    private String parentId;
    private String catName;
    private String cat_level;

    public DimGoodsCatDBEntity() {
    }

    public DimGoodsCatDBEntity(String catId, String parentId, String catName, String cat_level) {
        this.catId = catId;
        this.parentId = parentId;
        this.catName = catName;
        this.cat_level = cat_level;
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

    public String getCat_level() {
        return cat_level;
    }

    public void setCat_level(String cat_level) {
        this.cat_level = cat_level;
    }
}
