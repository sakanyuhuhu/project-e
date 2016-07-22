package th.ac.mahidol.rama.emam.dao;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ListMedicalCardDao {
    private int id;
    private String adminTimeHour;
    private Date drugUseDate;
    private String tradeName;
    private String drugImageUH;
    private String MRN;
    private String dose;
    private String unit;
    private String route;
    private String frequency;
    private String adminType;
    private String method;
    private String adminTime;
    private String status;
    private String pm;
    private String drugID;
    private Date registerDate;
    private Date lastVisited;
    private Date lastUpdated;
    @SerializedName("order_id")  private String orderId;
    private String stopdt;
    @SerializedName("prop_help") private String propHelp;
    private String site;
    private Date registerDateOnly;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAdminTimeHour() {
        return adminTimeHour;
    }

    public void setAdminTimeHour(String adminTimeHour) {
        this.adminTimeHour = adminTimeHour;
    }

    public Date getDrugUseDate() {
        return drugUseDate;
    }

    public void setDrugUseDate(Date drugUseDate) {
        this.drugUseDate = drugUseDate;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getDrugImageUH() {
        return drugImageUH;
    }

    public void setDrugImageUH(String drugImageUH) {
        this.drugImageUH = drugImageUH;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public String getAdminType() {
        return adminType;
    }

    public void setAdminType(String adminType) {
        this.adminType = adminType;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAdminTime() {
        return adminTime;
    }

    public void setAdminTime(String adminTime) {
        this.adminTime = adminTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getDrugID() {
        return drugID;
    }

    public void setDrugID(String drugID) {
        this.drugID = drugID;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(Date lastVisited) {
        this.lastVisited = lastVisited;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getStopdt() {
        return stopdt;
    }

    public void setStopdt(String stopdt) {
        this.stopdt = stopdt;
    }

    public String getPropHelp() {
        return propHelp;
    }

    public void setPropHelp(String propHelp) {
        this.propHelp = propHelp;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Date getRegisterDateOnly() {
        return registerDateOnly;
    }

    public void setRegisterDateOnly(Date registerDateOnly) {
        this.registerDateOnly = registerDateOnly;
    }
}
