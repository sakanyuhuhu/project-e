package th.ac.mahidol.rama.emam.dao.buildTimelineDAO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mi- on 17/8/2559.
 */
public class MrnTimelineDao {
    @SerializedName("mrn")  private List<String> mrn = new ArrayList<String>();

    public List<String> getMrn() {
        return mrn;
    }

    public void setMrn(List<String> mrn) {
        this.mrn = mrn;
    }
}
