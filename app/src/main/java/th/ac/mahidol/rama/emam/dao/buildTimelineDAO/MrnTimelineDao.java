package th.ac.mahidol.rama.emam.dao.buildTimelineDAO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class MrnTimelineDao implements Parcelable {

    @SerializedName("mrn")
    private List<String> mrn = new ArrayList<String>();

    public MrnTimelineDao(){

    }

    protected MrnTimelineDao(Parcel in) {
        mrn = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(mrn);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MrnTimelineDao> CREATOR = new Creator<MrnTimelineDao>() {
        @Override
        public MrnTimelineDao createFromParcel(Parcel in) {
            return new MrnTimelineDao(in);
        }

        @Override
        public MrnTimelineDao[] newArray(int size) {
            return new MrnTimelineDao[size];
        }
    };

    public List<String> getMrn() {
        return mrn;
    }

    public void setMrn(List<String> mrn) {
        this.mrn = mrn;
    }

}
