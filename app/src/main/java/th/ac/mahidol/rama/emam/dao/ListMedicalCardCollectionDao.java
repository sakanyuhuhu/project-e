package th.ac.mahidol.rama.emam.dao;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListMedicalCardCollectionDao {
    @SerializedName("listMedicalCardBean") private List<ListMedicalCardDao> listMedicalCardBean = new ArrayList<ListMedicalCardDao>();

    public List<ListMedicalCardDao> getListMedicalCardBean() {
        return listMedicalCardBean;
    }

    public void setListMedicalCardBean(List<ListMedicalCardDao> listMedicalCardBean) {
        this.listMedicalCardBean = listMedicalCardBean;
    }
}
