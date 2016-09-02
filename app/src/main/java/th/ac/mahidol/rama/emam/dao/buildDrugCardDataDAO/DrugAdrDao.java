package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;

import com.google.gson.annotations.SerializedName;

/**
 * Created by mac-mini-1 on 9/1/2016 AD.
 */
public class DrugAdrDao {
    private String drugname;
    @SerializedName("side_effect") private String sideEffect;
    private String naranjo;

    public String getDrugname() {
        return drugname;
    }

    public void setDrugname(String drugname) {
        this.drugname = drugname;
    }

    public String getSideEffect() {
        return sideEffect;
    }

    public void setSideEffect(String sideEffect) {
        this.sideEffect = sideEffect;
    }

    public String getNaranjo() {
        return naranjo;
    }

    public void setNaranjo(String naranjo) {
        this.naranjo = naranjo;
    }
}
