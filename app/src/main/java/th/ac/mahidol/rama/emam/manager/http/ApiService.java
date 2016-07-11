package th.ac.mahidol.rama.emam.manager.http;

import retrofit2.Call;
import retrofit2.http.GET;
import th.ac.mahidol.rama.emam.dao.ListWardCollectionDao;


public interface ApiService {

    @GET("list_ward/EMAM")
    Call<ListWardCollectionDao> loadListWard();



}
