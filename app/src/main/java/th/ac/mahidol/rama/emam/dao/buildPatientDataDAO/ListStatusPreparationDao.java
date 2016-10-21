package th.ac.mahidol.rama.emam.dao.buildPatientDataDAO;


import java.util.List;

public class ListStatusPreparationDao {
    private List<StatusPreparationDao> statusPreparationDaoList;
    private String datetoDay;

    public String getDatetoDay() {
        return datetoDay;
    }

    public void setDatetoDay(String datetoDay) {
        this.datetoDay = datetoDay;
    }

    public List<StatusPreparationDao> getStatusPreparationDaoList() {
        return statusPreparationDaoList;
    }

    public void setStatusPreparationDaoList(List<StatusPreparationDao> statusPreparationDaoList) {
        this.statusPreparationDaoList = statusPreparationDaoList;
    }
}
