package th.ac.mahidol.rama.emam.manager.http;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import th.ac.mahidol.rama.emam.dao.CheckPersonWardCollectionDao;
import th.ac.mahidol.rama.emam.dao.ListWardCollectionDao;


public interface ApiService {

    @GET("list_ward/EMAM")
    Call<ListWardCollectionDao> loadListWard();

    @GET("check_person_ward/{nfcUId}/{sdlocId}")
    Call<CheckPersonWardCollectionDao> loadCheckPersonWard(@Path("nfcUId") String nfcUId, @Path("sdlocId") String sdlocId);

}
