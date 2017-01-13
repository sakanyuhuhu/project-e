package th.ac.mahidol.rama.emam.dao.buildCheckPersonWard;

/**
 * Created by mac-mini-1 on 1/12/2017 AD.
 */
public class CheckPersonWardBedDao {

    private String RFID;
    private int teamId;
    private String userName;
    private String NFCNumber;
    private String role;
    private String firstName;
    private String lastName;
    private String nfcUID;
    private int wardID;
    private String bedID;

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getNFCNumber() {
        return NFCNumber;
    }

    public void setNFCNumber(String NFCNumber) {
        this.NFCNumber = NFCNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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

    public String getNfcUID() {
        return nfcUID;
    }

    public void setNfcUID(String nfcUID) {
        this.nfcUID = nfcUID;
    }

    public int getWardID() {
        return wardID;
    }

    public void setWardID(int wardID) {
        this.wardID = wardID;
    }

    public String getBedID() {
        return bedID;
    }

    public void setBedID(String bedID) {
        this.bedID = bedID;
    }
}
