package th.ac.mahidol.rama.emam.dao.buildPatientDataDAO;


import com.google.gson.annotations.SerializedName;

public class PatientDataDao {

    private String MRN;
    private String bedID;
    @SerializedName("initial_name") private String initialName;
    @SerializedName("first_name") private String firstName;
    @SerializedName("last_name")private String lastName;
    @SerializedName("id_card_no")private String idCardNo;
    private String maritalstatus;
    private String dob;
    private String gender;
    private String wardName;
    @SerializedName("sdloc_id")private String sdlocId;
    private String age;
    private String time;
    private String complete;

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }

    public String getBedID() {
        return bedID;
    }

    public void setBedID(String bedID) {
        this.bedID = bedID;
    }

    public String getInitialName() {
        return initialName;
    }

    public void setInitialName(String initialName) {
        this.initialName = initialName;
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

    public String getIdCardNo() {
        return idCardNo;
    }

    public void setIdCardNo(String idCardNo) {
        this.idCardNo = idCardNo;
    }

    public String getMaritalstatus() {
        return maritalstatus;
    }

    public void setMaritalstatus(String maritalstatus) {
        this.maritalstatus = maritalstatus;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getSdlocId() {
        return sdlocId;
    }

    public void setSdlocId(String sdlocId) {
        this.sdlocId = sdlocId;
    }
}
