package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;


import com.google.gson.annotations.SerializedName;

public class DrugCardDao {
    private int id;
    private String adminTimeHour;
    private String drugUseDate;
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
    private String pm;
    private String status;
    private String drugID;
    private String registerDate;
    private String lastVisited;
    private String lastUpdated;
    @SerializedName("order_id")  private String orderId;
    private String stopdt;
    @SerializedName("prop_help") private String propHelp;
    private String site;
    private String registerDateOnly;

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

    public String getDrugUseDate() {
        return drugUseDate;
    }

    public void setDrugUseDate(String drugUseDate) {
        this.drugUseDate = drugUseDate;
    }

    public String getTradeName() {
        return tradeName;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
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

    public String getDrugID() {
        return drugID;
    }

    public void setDrugID(String drugID) {
        this.drugID = drugID;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public String getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(String lastVisited) {
        this.lastVisited = lastVisited;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
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

    public String getRegisterDateOnly() {
        return registerDateOnly;
    }

    public void setRegisterDateOnly(String registerDateOnly) {
        this.registerDateOnly = registerDateOnly;
    }

    public String getDrugImageUH() {
        return drugImageUH;
    }

    public void setDrugImageUH(String drugImageUH) {
        this.drugImageUH = drugImageUH;
    }

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }
}
