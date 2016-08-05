package th.ac.mahidol.rama.emam.dao.patientinfoforperson;


import com.google.gson.annotations.SerializedName;

import th.ac.mahidol.rama.emam.dao.listpatientinfo.ListPatientInfoDao;

public class PatientInfoForPersonCollectionDao {

    @SerializedName("listPatientInfoBean")     private ListPatientInfoDao listPatientInfoBean;

    public ListPatientInfoDao getListPatientInfoBean() {
        return listPatientInfoBean;
    }

    public void setListPatientInfoBean(ListPatientInfoDao listPatientInfoBean) {
        this.listPatientInfoBean = listPatientInfoBean;
    }
}
