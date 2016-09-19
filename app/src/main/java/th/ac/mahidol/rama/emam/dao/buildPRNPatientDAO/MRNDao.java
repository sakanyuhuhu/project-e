package th.ac.mahidol.rama.emam.dao.buildPRNPatientDAO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mac-mini-1 on 9/19/2016 AD.
 */
public class MRNDao {
    private List<String> mrn = new ArrayList<String>();

    public List<String> getMrn() {
        return mrn;
    }

    public void setMrn(List<String> mrn) {
        this.mrn = mrn;
    }
}
