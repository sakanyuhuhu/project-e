package th.ac.mahidol.rama.emam.dao;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListWardCollectionDao {
    @SerializedName("listwardBean") private List<ListWardDao> listwardBean = new ArrayList<ListWardDao>();

    public List<ListWardDao> getListwardBean() {
        return listwardBean;
    }

    public void setListwardBean(List<ListWardDao> listwardBean) {
        this.listwardBean = listwardBean;
    }
}
