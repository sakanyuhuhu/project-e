package th.ac.mahidol.rama.emam.dao;


import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class CheckPersonWardDao {

    private int id;
    private String RFID;
    @SerializedName("team_id") private int teamId;
    private String userName;
    private String password;
    private String NFCNumber;
    private String role;
    private Date registerDate;
    private Date lastVisited;
    private Date lastUpdated;
    @SerializedName("first_name") private String firstName;
    @SerializedName("last_name") private String lastName;
    @SerializedName("nfc_uid") private String nfcUId;
    @SerializedName("ward_id") private int wardId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getLastVisited() {
        return lastVisited;
    }

    public void setLastVisited(Date lastVisited) {
        this.lastVisited = lastVisited;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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

    public String getNfcUId() {
        return nfcUId;
    }

    public void setNfcUId(String nfcUId) {
        this.nfcUId = nfcUId;
    }

    public int getWardId() {
        return wardId;
    }

    public void setWardId(int wardId) {
        this.wardId = wardId;
    }
}
