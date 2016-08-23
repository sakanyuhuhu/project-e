package th.ac.mahidol.rama.emam.dao.buildstatusCheckDAO;

/**
 * Created by mi- on 23/8/2559.
 */
public class StatusCheckDao {
    private int drugID;
    private boolean checkDrug;
    private boolean checkDrugNote;

    public int getDrugID() {
        return drugID;
    }

    public void setDrugID(int drugID) {
        this.drugID = drugID;
    }

    public boolean isCheckDrug() {
        return checkDrug;
    }

    public void setCheckDrug(boolean checkDrug) {
        this.checkDrug = checkDrug;
    }

    public boolean isCheckDrugNote() {
        return checkDrugNote;
    }

    public void setCheckDrugNote(boolean checkDrugNote) {
        this.checkDrugNote = checkDrugNote;
    }
}
