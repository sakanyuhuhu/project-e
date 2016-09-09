package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;


import com.google.gson.annotations.SerializedName;

public class DrugCardDao{
    private String id;
    private String adminTimeHour;
    private String drugUseDate;
    private String tradeName;
    private String drugImageUul;
    private String MRN;
    private String dose;
    private String unit;
    private String route;
    private String frequency;
    private String adminType;
    private String method;
    private String adminTime;
    private String prn;
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
    private String checkType;
    private String complete;
    private int idRadio;
    private String strRadio;
    private String description;
    private String descriptionTemplate;
    private String RFID;
    private String firstName;
    private String lastName;
    private String studentName;
    private String actualAdmin;
    private String wardName;
    private String activityHour;
    private String checkNote;
    private String strType;
    private String strSize;
    private String strForget;

    public String getStrType() {
        return strType;
    }

    public void setStrType(String strType) {
        this.strType = strType;
    }

    public String getStrSize() {
        return strSize;
    }

    public void setStrSize(String strSize) {
        this.strSize = strSize;
    }

    public String getStrForget() {
        return strForget;
    }

    public void setStrForget(String strForget) {
        this.strForget = strForget;
    }

    public String getStrRadio() {
        return strRadio;
    }

    public void setStrRadio(String strRadio) {
        this.strRadio = strRadio;
    }

    public String getActivityHour() {
        return activityHour;
    }

    public void setActivityHour(String activityHour) {
        this.activityHour = activityHour;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getCheckNote() {
        return checkNote;
    }

    public void setCheckNote(String checkNote) {
        this.checkNote = checkNote;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDrugImageUul() {
        return drugImageUul;
    }

    public void setDrugImageUul(String drugImageUul) {
        this.drugImageUul = drugImageUul;
    }

    public String getPrn() {
        return prn;
    }

    public void setPrn(String prn) {
        this.prn = prn;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionTemplate() {
        return descriptionTemplate;
    }

    public void setDescriptionTemplate(String descriptionTemplate) {
        this.descriptionTemplate = descriptionTemplate;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getActualAdmin() {
        return actualAdmin;
    }

    public void setActualAdmin(String actualAdmin) {
        this.actualAdmin = actualAdmin;
    }

    public int getIdRadio() {
        return idRadio;
    }

    public void setIdRadio(int idRadio) {
        this.idRadio = idRadio;
    }
}
