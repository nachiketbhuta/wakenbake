package nshmadhani.com.wakenbake.main_screens.interfaces;

import java.util.List;

import nshmadhani.com.wakenbake.main_screens.models.Places;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Nachiket on 21-Feb-18.
 */

public interface RetrofitApiInterface {

    String BASE_URL = "http://wakenbake.epizy.com/wakenbake/";

    @GET("search.php")
    Call<List<Places>> getPlacesFromFirebase();

}
