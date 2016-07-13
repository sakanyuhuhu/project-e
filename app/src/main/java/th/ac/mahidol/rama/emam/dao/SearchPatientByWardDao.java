package th.ac.mahidol.rama.emam.dao;


public class SearchPatientByWardDao {

    private String wardName;
    private String dedno;
    private String mrn;
    private String encType;
    private String encID;

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getDedno() {
        return dedno;
    }

    public void setDedno(String dedno) {
        this.dedno = dedno;
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

    public String getEncID() {
        return encID;
    }

    public void setEncID(String encID) {
        this.encID = encID;
    }
}
