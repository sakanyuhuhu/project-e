package th.ac.mahidol.rama.emam.dao.buildListWard;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class WardCollectionDao {
    @SerializedName("listwardBean") private List<WardDao> listwardBean = new ArrayList<WardDao>();

    public List<WardDao> getListwardBean() {
        return listwardBean;
    }

    public void setListwardBean(List<WardDao> listwardBean) {
        this.listwardBean = listwardBean;
    }
}
