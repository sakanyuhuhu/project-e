package th.ac.mahidol.rama.emam.dao.buildTimelineDAO;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by mi- on 17/8/2559.
 */
public class TimelineDao {
    @SerializedName("time")         private List<MrnTimelineDao> timelineDao;

    public List<MrnTimelineDao> getTimelineDao() {
        return timelineDao;
    }

    public void setTimelineDao(List<MrnTimelineDao> timelineDao) {
        this.timelineDao = timelineDao;
    }
}
