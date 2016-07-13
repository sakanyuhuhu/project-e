package th.ac.mahidol.rama.emam.dao;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class CheckPersonWardCollectionDao {
    @SerializedName("listCheckPersonWardBean") private List<CheckPersonWardDao> listCheckPersonWardBean = new ArrayList<CheckPersonWardDao>();

    public List<CheckPersonWardDao> getListCheckPersonWardBean() {
        return listCheckPersonWardBean;
    }

    public void setListCheckPersonWardBean(List<CheckPersonWardDao> listCheckPersonWardBean) {
        this.listCheckPersonWardBean = listCheckPersonWardBean;
    }
}
