package th.ac.mahidol.rama.emam.dao;

import com.google.gson.annotations.SerializedName;

public class PatientDao {
    @SerializedName("ward")         private String ward;
    @SerializedName("bedno")        private String bedno;
    @SerializedName("mrn")          private String mrn;
    @SerializedName("enc_type")     private String encType;
    @SerializedName("enc_id")       private String encId;

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getBedno() {
        return bedno;
    }

    public void setBedno(String bedno) {
        this.bedno = bedno;
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
