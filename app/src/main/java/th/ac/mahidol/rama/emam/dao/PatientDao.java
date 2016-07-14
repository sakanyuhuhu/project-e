package th.ac.mahidol.rama.emam.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 015240 on 7/13/2016.
 */

public class PatientDao {
    @SerializedName("ward")         private String ward;
    @SerializedName("bedno")        private String bedNo;
    @SerializedName("mrn")          private String mrn;
    @SerializedName("enc_type")     private String encType;
    @SerializedName("enc_id")       private String encId;

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getBedNo() {
        return bedNo;
    }

    public void setBedNo(String bedNo) {
        this.bedNo = bedNo;
    }

    public String getMrn() {
        return mrn;
    }

    public void setMrn(String mrn) {
        this.mrn = mrn;
    }

    public String getEncType() {
        return encType;
    }

    public void setEncType(String encType) {
        this.encType = encType;
    }

    public String getEncId() {
        return encId;
    }

    public void setEncId(String encId) {
        this.encId = encId;
    }
}
