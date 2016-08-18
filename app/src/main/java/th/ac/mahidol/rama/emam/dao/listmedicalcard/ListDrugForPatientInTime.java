package th.ac.mahidol.rama.emam.dao.listmedicalcard;


import java.util.ArrayList;
import java.util.List;

public class ListDrugForPatientInTime {
    private List<DrugForPatientInTime> drugForPatientInTimeList = new ArrayList<DrugForPatientInTime>();

    public List<DrugForPatientInTime> getDrugForPatientInTimeList() {
        return drugForPatientInTimeList;
    }

    public void setDrugForPatientInTimeList(List<DrugForPatientInTime> drugForPatientInTimeList) {
        this.drugForPatientInTimeList = drugForPatientInTimeList;
    }
}
