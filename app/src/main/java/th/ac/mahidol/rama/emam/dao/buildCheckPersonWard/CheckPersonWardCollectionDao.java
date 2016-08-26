package th.ac.mahidol.rama.emam.dao.buildCheckPersonWard;


import com.google.gson.annotations.SerializedName;

public class CheckPersonWardCollectionDao {


   @SerializedName("CheckPersonWardBean")     private CheckPersonWardDao checkPersonWardBean;

    public CheckPersonWardDao getCheckPersonWardBean() {
        return checkPersonWardBean;
    }

    public void setCheckPersonWardBean(CheckPersonWardDao checkPersonWardBean) {
        this.checkPersonWardBean = checkPersonWardBean;
    }
}
