package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListDrugCardDao{
    @SerializedName("drugCard") private List<DrugCardDao> listDrugCardDao;

    public List<DrugCardDao> getListDrugCardDao() {
        return listDrugCardDao;
    }

    public void setListDrugCardDao(List<DrugCardDao> listDrugCardDao) {
        this.listDrugCardDao = listDrugCardDao;
    }
}
