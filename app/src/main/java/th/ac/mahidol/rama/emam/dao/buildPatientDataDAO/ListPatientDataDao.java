package th.ac.mahidol.rama.emam.dao.buildPatientDataDAO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class ListPatientDataDao implements Parcelable {
    @SerializedName("patient")  private List<PatientDataDao> patientDao;

    protected ListPatientDataDao(Parcel in) {
        patientDao = in.createTypedArrayList(PatientDataDao.CREATOR);
    }

    public ListPatientDataDao() {
    }

    public static final Creator<ListPatientDataDao> CREATOR = new Creator<ListPatientDataDao>() {
        @Override
        public ListPatientDataDao createFromParcel(Parcel in) {
            return new ListPatientDataDao(in);
        }

        @Override
        public ListPatientDataDao[] newArray(int size) {
            return new ListPatientDataDao[size];
        }
    };

    public List<PatientDataDao> getPatientDao() {
        return patientDao;
    }

    public void setPatientDao(List<PatientDataDao> patientDao) {
        this.patientDao = patientDao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(patientDao);
    }
}
