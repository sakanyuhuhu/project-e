package th.ac.mahidol.rama.emam.manager.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.DrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.ListDrugCardDao;
import th.ac.mahidol.rama.emam.dao.buildListWard.WardCollectionDao;
import th.ac.mahidol.rama.emam.dao.buildPatientDataDAO.ListPatientDataDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.MrnTimelineDao;
import th.ac.mahidol.rama.emam.dao.buildTimelineDAO.TimelineDao;



public interface ApiService {

//    @FormUrlEncoded
//    @POST("EmamTimelineService")
//    Call<TimelineDao> getTimeline(@Field("sdlocId") String sdlocId);

    @POST("EmamPatientData")
    Call<ListPatientDataDao> getPatientData(@Body MrnTimelineDao mrn);

    @POST("PatientDrugService")
    Call<ListDrugCardDao> getDrugData(@Body DrugCardDao drugCardDao);

    @GET("GetWardService")
    Call<WardCollectionDao> getListWard();

    @FormUrlEncoded
    @POST("PersonWardService")
    Call<CheckPersonWardDao> getPersonWard(@Field("nfcUId") String nfcUId, @Field("sdlocId") String sdlocId);

    @POST("PatientDrugUpdateService")
    Call<ListDrugCardDao> updateDrugData(@Body ListDrugCardDao drugCardDao);

    @FormUrlEncoded
    @POST("PatientDataStatus")
    Call<ListPatientDataDao> getPatientInfo(@Field("sdlocId") String sdlocId, @Field("adminTime") String adminTime, @Field("checkType") String checkType, @Field("date") String date);

    @FormUrlEncoded
    @POST("PatientPrnWardService")
    Call<MrnTimelineDao> getPatientPRN(@Field("sdlocId") String sdlocId);

    @FormUrlEncoded
    @POST("ListCountPatientPRN")
    Call<TimelineDao> getListCountPatientPRN(@Field("sdlocId") String sdlocId);

    @FormUrlEncoded
    @POST("ListCountPatientExcPRN")
    Call<TimelineDao> getListCountPatientExcPRN(@Field("sdlocId") String sdlocId);

    @FormUrlEncoded
    @POST("ListMedicalCardForPreparePRN")
    Call<ListDrugCardDao> getListMedicalCardForPreparePRN(@Field("mrn") String mrn);

    @POST("CompareDrug")
    Call<ListDrugCardDao> getCompareDrug(@Body DrugCardDao drugCardDao);

    @FormUrlEncoded
    @POST("LoginService")
    Call<CheckPersonWardDao> getPersonLogin(@Field("staffId") String staffId, @Field("sdlocId") String sdlocId);

    @FormUrlEncoded
    @POST("DrugHistory")
    Call<ListDrugCardDao> getMedicalHistory(@Field("mrn") String mrn, @Field("checkType") String checkType, @Field("startDate") String startDate);


}
