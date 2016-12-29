package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;


public class CheckLastPRNDao {
    private String adminTimeHour;
    private String drugUseDate;
    private String activityHour;
    private String firstName;
    private String lastName;
    private String studentName;

    public String getAdminTimeHour() {
        return adminTimeHour;
    }

    public void setAdminTimeHour(String adminTimeHour) {
        this.adminTimeHour = adminTimeHour;
    }

    public String getDrugUseDate() {
        return drugUseDate;
    }

    public void setDrugUseDate(String drugUseDate) {
        this.drugUseDate = drugUseDate;
    }

    public String getActivityHour() {
        return activityHour;
    }

    public void setActivityHour(String activityHour) {
        this.activityHour = activityHour;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
