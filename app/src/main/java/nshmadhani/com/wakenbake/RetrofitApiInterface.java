package nshmadhani.com.wakenbake;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitApiInterface {

    String BASE_URL = "http://45.77.180.218:5965/";

    @GET("search.php")
    Call<PlacesHolder> getPlacesFromFirebase(@Query("search") String name);

    @GET("searchtiffin.php")
    Call<PlacesHolder> getTiffinPlaces(@Query("search") String name);
}
