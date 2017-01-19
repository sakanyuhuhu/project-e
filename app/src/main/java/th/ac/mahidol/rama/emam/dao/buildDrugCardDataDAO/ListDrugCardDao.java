package th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListDrugCardDao implements Parcelable{
    @SerializedName("drugCardListBean") private List<DrugCardDao> listDrugCardDao;

    public ListDrugCardDao() {
    }

    protected ListDrugCardDao(Parcel in) {
        listDrugCardDao = in.createTypedArrayList(DrugCardDao.CREATOR);
    }

    public static final Creator<ListDrugCardDao> CREATOR = new Creator<ListDrugCardDao>() {
        @Override
        public ListDrugCardDao createFromParcel(Parcel in) {
            return new ListDrugCardDao(in);
        }

        @Override
        public ListDrugCardDao[] newArray(int size) {
            return new ListDrugCardDao[size];
        }
    };

    public List<DrugCardDao> getListDrugCardDao() {
        return listDrugCardDao;
    }

    public void setListDrugCardDao(List<DrugCardDao> listDrugCardDao) {
        this.listDrugCardDao = listDrugCardDao;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(listDrugCardDao);
    }
}
