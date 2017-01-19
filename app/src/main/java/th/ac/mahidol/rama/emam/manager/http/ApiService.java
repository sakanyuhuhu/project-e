package th.ac.mahidol.rama.emam.manager.http;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.CheckPersonWardDao;
import th.ac.mahidol.rama.emam.dao.buildCheckPersonWard.ListCheckPersonWardBedDao;
import th.ac.mahidol.rama.emam.dao.buildDrugCardDataDAO.CheckLastPRNListDao;
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


    @POST("GetPatientInfo")//@POST("EmamPatientData")
    Call<ListPatientDataDao> getPatientData(@Body MrnTimelineDao mrn);


    @POST("ListMedicalAndLogByMRNCheckTypeAdminTime")//@POST("PatientDrugService")
    Call<ListDrugCardDao> getDrugData(@Body DrugCardDao drugCardDao);



//    @GET("GetWardService")
//    Call<WardCollectionDao> getListWard();
    @GET("ListWard")
    Call<WardCollectionDao> getListWard();

//    @FormUrlEncoded
//    @POST("PersonWardService")
//    Call<CheckPersonWardDao> getPersonWard(@Field("nfcUId") String nfcUId, @Field("sdlocId") String sdlocId);
    @GET("CheckPersonsdloc/{nfcUId}/{sdlocId}")
    Call<CheckPersonWardDao> getPersonWard(@Path("nfcUId") String nfcUId, @Path("sdlocId") String sdlocId);

    @POST("SetCheckerLog")//@POST("PatientDrugUpdateService")
    Call<ListDrugCardDao> updateDrugData(@Body ListDrugCardDao drugCardDao);

    @FormUrlEncoded
    @POST("PatientDataStatus")
    Call<ListPatientDataDao> getPatientInfo(@Field("sdlocId") String sdlocId, @Field("adminTime") String adminTime, @Field("checkType") String checkType, @Field("date") String date);
//    @GET("PatientDataStatus/{sdlocId}/{adminTime}/{checkType}/{date}")
//    Call<ListPatientDataDao> getPatientInfo(@Path("sdlocId") String sdlocId, @Path("adminTime") String adminTime, @Path("checkType") String checkType, @Path("date") String date);

//    @FormUrlEncoded
//    @POST("PatientPrnWardService")
//    Call<MrnTimelineDao> getPatientPRN(@Field("sdlocId") String sdlocId);
      @GET("ListCountPatientTodayBySDloc/{sdlocId}")
      Call<MrnTimelineDao> getPatientPRN(@Path("sdlocId") String sdlocId);

//    @FormUrlEncoded
//    @POST("ListCountPatientPRN")
//    Call<TimelineDao> getListCountPatientPRN(@Field("sdlocId") String sdlocId);
    @GET("ListCountPatientPRNTodayBySDLoc/{sdlocId}")
    Call<TimelineDao> getListCountPatientPRN(@Path("sdlocId") String sdlocId);

//    @FormUrlEncoded
//    @POST("ListCountPatientExcPRN")
//    Call<TimelineDao> getListCountPatientExcPRN(@Field("sdlocId") String sdlocId);
    @GET("ListCountPatientExcPRNTodayBySDLoc/{sdlocId}")
    Call<TimelineDao> getListCountPatientExcPRN(@Path("sdlocId") String sdlocId);


//    @FormUrlEncoded
//    @POST("ListMedicalCardForPreparePRN")
//    Call<ListDrugCardDao> getListMedicalCardForPreparePRN(@Field("mrn") String mrn);

    @POST("CompareMed")
    Call<ListDrugCardDao> getCompareDrug(@Body DrugCardDao drugCardDao);

//    @FormUrlEncoded
//    @POST("LoginService")
//    Call<CheckPersonWardDao> getPersonLogin(@Field("staffId") String staffId, @Field("sdlocId") String sdlocId);
    @GET("CheckPersonSDLocByStaffID/{staffId}/{sdlocId}")
    Call<CheckPersonWardDao> getPersonLogin(@Path("staffId") String staffId, @Path("sdlocId") String sdlocId);

    @FormUrlEncoded
    @POST("HistoryLogByMRNCheckType")
    Call<ListDrugCardDao> getMedicalHistory(@Field("mrn") String mrn, @Field("checkType") String checkType, @Field("startDate") String startDate);
//    @GET("HistoryLogByMRNCheckType/{mrn}/{checkType}/{startDate}")
//    Call<ListDrugCardDao> getMedicalHistory(@Path("mrn") String mrn, @Path("checkType") String checkType, @Path("startDate") String startDate);

    @GET("ListHAD")
    Call<List<String>> getListHAD();

//    @FormUrlEncoded
//    @POST("CheckLastPRNbyMRNDrugIDService")
//    Call<CheckLastPRNListDao> getCheckLastPRNbyMRNDrugID(@Field("mrn") String mrn, @Field("drugID") String DrugID);
    @GET("CheckLastPRNbyMRNDrugID/{mrn}/{drugID}")
    Call<CheckLastPRNListDao> getCheckLastPRNbyMRNDrugID(@Path("mrn") String mrn, @Path("drugID") String DrugID);

//    @FormUrlEncoded
//    @POST("CheckPersonWardBedService")
//    Call<ListCheckPersonWardBedDao> getCheckPersonWardBed(@Field("nfcUId") String nfcUId, @Field("wardId") String wardId);
    @GET("CheckPersonWardBed/{nfcUId}/{wardId}")
    Call<ListCheckPersonWardBedDao> getCheckPersonWardBed(@Path("nfcUId") String nfcUId, @Path("wardId") String wardId);


}
