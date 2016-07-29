package th.ac.mahidol.rama.emam.dao;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListPatientInfoCollectionDao {

    @SerializedName("listPatienstInfoBean")     private List<ListPatientInfoDao> listPatientInfoBean = new ArrayList<ListPatientInfoDao>();

    public List<ListPatientInfoDao> getListPatientInfoBean() {
        return listPatientInfoBean;
    }

    public void setListPatientInfoBean(List<ListPatientInfoDao> listPatientInfoBean) {
        this.listPatientInfoBean = listPatientInfoBean;
    }
}
