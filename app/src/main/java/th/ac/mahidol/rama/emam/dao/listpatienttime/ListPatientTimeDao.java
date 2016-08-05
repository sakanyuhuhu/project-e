package th.ac.mahidol.rama.emam.dao.listpatienttime;


public class ListPatientTimeDao {

    private String adminTimeHour;
    private String drugUseDate;
    private String MRN;

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

    public String getMRN() {
        return MRN;
    }

    public void setMRN(String MRN) {
        this.MRN = MRN;
    }
}
