package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CheckLastPRNListDao {
    @SerializedName("checkLastPRNBean") private List<CheckLastPRNDao> checkLastPRNDao;


    public List<CheckLastPRNDao> getCheckLastPRNDao() {
        return checkLastPRNDao;
    }

    public void setCheckLastPRNDao(List<CheckLastPRNDao> checkLastPRNDao) {
        this.checkLastPRNDao = checkLastPRNDao;
    }
}
