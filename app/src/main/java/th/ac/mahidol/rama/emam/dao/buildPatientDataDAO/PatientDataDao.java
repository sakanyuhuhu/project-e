package th.ac.mahidol.rama.emam.dao.buildPatientDataDAO;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PatientDataDao implements Parcelable {

    private String MRN;
    private String bedID;
    @SerializedName("initial_name")
    private String initialName;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("id_card_no")
    private String idCardNo;
    private String maritalstatus;
    private String dob;
    private String gender;
    private String wardName;
    @SerializedName("sdloc_id")
    private String sdlocId;
    private String age;
    private String time;
    private String date;
    private String complete;

    public PatientDataDao() {
    }

    protected PatientDataDao(Parcel in) {
        MRN = in.readString();
        bedID = in.readString();
        initialName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        idCardNo = in.readString();
        maritalstatus = in.readString();
        dob = in.readString();
        gender = in.readString();
        wardName = in.readString();
        sdlocId = in.readString();
        age = in.readString();
        time = in.readString();
        date = in.readString();
        complete = in.readString();
    }

    public static final Creator<PatientDataDao> CREATOR = new Creator<PatientDataDao>() {
        @Override
        public PatientDataDao createFromParcel(Parcel in) {
            return new PatientDataDao(in);
        }

        @Override
        public PatientDataDao[] newArray(int size) {
            return new PatientDataDao[size];
        }
    };

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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(MRN);
        dest.writeString(bedID);
        dest.writeString(initialName);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(idCardNo);
        dest.writeString(maritalstatus);
        dest.writeString(dob);
        dest.writeString(gender);
        dest.writeString(wardName);
        dest.writeString(sdlocId);
        dest.writeString(age);
        dest.writeString(time);
        dest.writeString(date);
        dest.writeString(complete);
    }
}