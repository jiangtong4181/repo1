package cn.itcast.shop.realtime.etl.bean;
// 组织结构维度样例类
public class DimOrgDBEntity {
    private int orgId;
    private int parentId;
    private String orgName;
    private int orgLevel;

    public DimOrgDBEntity() {
    }

    public DimOrgDBEntity(int orgId, int parentId, String orgName, int orgLevel) {
        this.orgId = orgId;
        this.parentId = parentId;
        this.orgName = orgName;
        this.orgLevel = orgLevel;
    }

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public int getOrgLevel() {
        return orgLevel;
    }

    public void setOrgLevel(int orgLevel) {
        this.orgLevel = orgLevel;
    }
}
