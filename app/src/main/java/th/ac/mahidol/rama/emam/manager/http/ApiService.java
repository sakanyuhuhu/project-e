package th.ac.mahidol.rama.emam.manager.http;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import th.ac.mahidol.rama.emam.dao.checkpersonward.CheckPersonWardCollectionDao;
import th.ac.mahidol.rama.emam.dao.listmedicalcard.ListMedicalCardCollectionDao;
import th.ac.mahidol.rama.emam.dao.listpatientinfo.ListPatientInfoCollectionDao;
import th.ac.mahidol.rama.emam.dao.listpatienttime.ListPatientTimeCollectionDao;
import th.ac.mahidol.rama.emam.dao.listward.ListWardCollectionDao;
import th.ac.mahidol.rama.emam.dao.mrnforprepare.MRNListBean;
import th.ac.mahidol.rama.emam.dao.mrnforprepare.MRNListBean2;
import th.ac.mahidol.rama.emam.dao.patientinfoforperson.PatientInfoForPersonCollectionDao;


public interface ApiService {

    @GET("list_ward/EMAM")
    Call<ListWardCollectionDao> loadListWard();

    @GET("check_person_ward/{nfcUId}/{sdlocId}")
    Call<CheckPersonWardCollectionDao> loadCheckPersonWard(@Path("nfcUId") String nfcUId, @Path("sdlocId") String sdlocId);

    @GET("list_medical_card/{mrn}")
    Call<ListMedicalCardCollectionDao> loadListMedicalCard(@Path("mrn") String mrn);

    @GET("list_patient_time/{sdlocId}")
    Call<ListPatientTimeCollectionDao> loadListPatientTime(@Path("sdlocId") String sdlocId);

    @GET("list_patient_info/{mrn}")
    Call<PatientInfoForPersonCollectionDao> loadListPatientInfo(@Path("mrn") String mrn);

    @POST("list_patient_info_post")
    Call<ListPatientInfoCollectionDao> loadListPatientInfoPost(@Body MRNListBean mrn);

    @POST("list_patient_info_post")
    Call<ListPatientInfoCollectionDao> loadListPatientInfoPost2(@Body MRNListBean2 mrn);

}
