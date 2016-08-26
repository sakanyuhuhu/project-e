package th.ac.mahidol.rama.emam.dao.listward;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class ListWardDao {
    private String id;
    private String wardName;
    @SerializedName("sdloc_id") private String sdlocId;
    private Date registerDate;
    private Date lastVisited;
    private Date lastUpdated;
    private String independenRule;
    private boolean trolley;
    private boolean active;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWardName() {
        return wardName;
    }

    public void setWardName(String wardName) {
        this.wardName = wardName;
    }

    public String getSdlocId() {
        return sdlocId;
    }

    public void setSdlocId(String sdlocId) {
        this.sdlocId = sdlocId;
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

    public String getIndependenRule() {
        return independenRule;
    }

    public void setIndependenRule(String independenRule) {
        this.independenRule = independenRule;
    }

    public boolean isTrolley() {
        return trolley;
    }

    public void setTrolley(boolean trolley) {
        this.trolley = trolley;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
