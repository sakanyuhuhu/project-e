package th.ac.mahidol.rama.emam.dao.buildListWard;

import com.google.gson.annotations.SerializedName;

public class WardDao {
    private String id;
    private String wardName;
    @SerializedName("sdloc_id") private String sdlocId;
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
