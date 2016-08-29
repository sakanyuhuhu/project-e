package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;

/**
 * Created by mac-mini-1 on 8/29/2016 AD.
 */
public class StatusCheckDrugDao {
    private int adminID;
    private String checkType;
    private String activityHour;
    private String status;
    private int complete;
    private String wardName;
    private String RFID;
    private String firstName;
    private String lastName;
    private String studentName;
    private String MRN;
    private String description;
    private String descriptionTemplate;
    private String actualAdmin;


    public int getAdminID() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    public String getCheckType() {
        return checkType;
    }

    public void setCheckType(String checkType) {
        this.checkType = checkType;
    }

    public String getActivityHour() {
        return activityHour;
    }

    public void setActivityHour(String activityHour) {
        this.activityHour = activityHour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getComplete() {
        return complete;
    }

    public void setComplete(int complete) {
        this.complete = complete;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
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

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
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

    public String getActualAdmin() {
        return actualAdmin;
    }

    public void setActualAdmin(String actualAdmin) {
        this.actualAdmin = actualAdmin;
    }
}
