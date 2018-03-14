package nshmadhani.com.wakenbake.interfaces;

import java.util.List;

import nshmadhani.com.wakenbake.models.Places;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nachiket on 21-Feb-18.
 */

public interface RetrofitApiInterface {

    String BASE_URL = "http://192.168.0.102:8085/Project/";

    @GET("search.php")
    Call<List<Places>> getPlacesFromFirebase(@Query("search") String name);

}
