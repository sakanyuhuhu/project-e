package th.ac.mahidol.rama.emam.dao.listpatienttime;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListPatientTimeCollectionDao {

    @SerializedName("listPatientTimeBean") private List<ListPatientTimeDao> listPatientTimeBean = new ArrayList<ListPatientTimeDao>();

    public List<ListPatientTimeDao> getListPatientTimeBean() {
        return listPatientTimeBean;
    }

    public void setListPatientTimeBean(List<ListPatientTimeDao> listPatientTimeBean) {
        this.listPatientTimeBean = listPatientTimeBean;
    }
}
