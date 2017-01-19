package th.ac.mahidol.rama.emam.dao.buildTimelineDAO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class TimelineDao implements Parcelable{

    @SerializedName("mrn")
    private List<MrnTimelineDao> timelineDao;

    public TimelineDao(){

    }

    protected TimelineDao(Parcel in) {
        timelineDao = in.createTypedArrayList(MrnTimelineDao.CREATOR);
    }

    public static final Creator<TimelineDao> CREATOR = new Creator<TimelineDao>() {
        @Override
        public TimelineDao createFromParcel(Parcel in) {
            return new TimelineDao(in);
        }

        @Override
        public TimelineDao[] newArray(int size) {
            return new TimelineDao[size];
        }
    };

    public List<MrnTimelineDao> getTimelineDao() {
        return timelineDao;
    }

    public void setTimelineDao(List<MrnTimelineDao> timelineDao) {
        this.timelineDao = timelineDao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(timelineDao);
    }
}
