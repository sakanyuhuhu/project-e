package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DrugCardDao implements Parcelable{
    private String id;
    private String adminTimeHour;
    private String drugUseDate;
    private String tradeName;
    private String drugImageUrl;
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
    private String strBP;
    private String strHR;
    private String strCBG;
    private String link;

    public DrugCardDao() {
    }

    protected DrugCardDao(Parcel in) {
        id = in.readString();
        adminTimeHour = in.readString();
        drugUseDate = in.readString();
        tradeName = in.readString();
        drugImageUrl = in.readString();
        MRN = in.readString();
        dose = in.readString();
        unit = in.readString();
        route = in.readString();
        frequency = in.readString();
        adminType = in.readString();
        method = in.readString();
        adminTime = in.readString();
        prn = in.readString();
        status = in.readString();
        drugID = in.readString();
        registerDate = in.readString();
        lastVisited = in.readString();
        lastUpdated = in.readString();
        orderId = in.readString();
        stopdt = in.readString();
        propHelp = in.readString();
        site = in.readString();
        registerDateOnly = in.readString();
        checkType = in.readString();
        complete = in.readString();
        idRadio = in.readInt();
        strRadio = in.readString();
        description = in.readString();
        descriptionTemplate = in.readString();
        RFID = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        studentName = in.readString();
        actualAdmin = in.readString();
        wardName = in.readString();
        activityHour = in.readString();
        checkNote = in.readString();
        strType = in.readString();
        strSize = in.readString();
        strForget = in.readString();
        strBP = in.readString();
        strHR = in.readString();
        strCBG = in.readString();
        link = in.readString();
    }

    public static final Creator<DrugCardDao> CREATOR = new Creator<DrugCardDao>() {
        @Override
        public DrugCardDao createFromParcel(Parcel in) {
            return new DrugCardDao(in);
        }

        @Override
        public DrugCardDao[] newArray(int size) {
            return new DrugCardDao[size];
        }
    };

    public String getStrBP() {
        return strBP;
    }

    public void setStrBP(String strBP) {
        this.strBP = strBP;
    }

    public String getStrHR() {
        return strHR;
    }

    public void setStrHR(String strHR) {
        this.strHR = strHR;
    }

    public String getStrCBG() {
        return strCBG;
    }

    public void setStrCBG(String strCBG) {
        this.strCBG = strCBG;
    }

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

    public String getDrugImageUrl() {
        return drugImageUrl;
    }

    public void setDrugImageUrl(String drugImageUrl) {
        this.drugImageUrl = drugImageUrl;
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(adminTimeHour);
        dest.writeString(drugUseDate);
        dest.writeString(tradeName);
        dest.writeString(drugImageUrl);
        dest.writeString(MRN);
        dest.writeString(dose);
        dest.writeString(unit);
        dest.writeString(route);
        dest.writeString(frequency);
        dest.writeString(adminType);
        dest.writeString(method);
        dest.writeString(adminTime);
        dest.writeString(prn);
        dest.writeString(status);
        dest.writeString(drugID);
        dest.writeString(registerDate);
        dest.writeString(lastVisited);
        dest.writeString(lastUpdated);
        dest.writeString(orderId);
        dest.writeString(stopdt);
        dest.writeString(propHelp);
        dest.writeString(site);
        dest.writeString(registerDateOnly);
        dest.writeString(checkType);
        dest.writeString(complete);
        dest.writeInt(idRadio);
        dest.writeString(strRadio);
        dest.writeString(description);
        dest.writeString(descriptionTemplate);
        dest.writeString(RFID);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(studentName);
        dest.writeString(actualAdmin);
        dest.writeString(wardName);
        dest.writeString(activityHour);
        dest.writeString(checkNote);
        dest.writeString(strType);
        dest.writeString(strSize);
        dest.writeString(strForget);
        dest.writeString(strBP);
        dest.writeString(strHR);
        dest.writeString(strCBG);
        dest.writeString(link);
    }
}
