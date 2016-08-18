package th.ac.mahidol.rama.emam.dao.buildPatientDataDAO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mi- on 18/8/2559.
 */
public class ListPatientDataDao {
    @SerializedName("patient")  private List<PatientDataDao> patientDao;

    public List<PatientDataDao> getPatientDao() {
        return patientDao;
    }

    public void setPatientDao(List<PatientDataDao> patientDao) {
        this.patientDao = patientDao;
    }
}
