package th.ac.mahidol.rama.emam.dao;


import com.google.gson.annotations.SerializedName;

public class PatientInfoForPersonCollectionDao {

    @SerializedName("listPatientInfoBean")     private  ListPatientInfoDao listPatientInfoBean;

    public ListPatientInfoDao getListPatientInfoBean() {
        return listPatientInfoBean;
    }

    public void setListPatientInfoBean(ListPatientInfoDao listPatientInfoBean) {
        this.listPatientInfoBean = listPatientInfoBean;
    }
}
