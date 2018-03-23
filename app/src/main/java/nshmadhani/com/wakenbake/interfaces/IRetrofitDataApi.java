package nshmadhani.com.wakenbake.interfaces;

import com.google.gson.JsonObject;

import nshmadhani.com.wakenbake.holders.FirebasePlacesHolder;
import nshmadhani.com.wakenbake.holders.TiffinPlacesHolder;
import nshmadhani.com.wakenbake.models.RatingsResponse;
import nshmadhani.com.wakenbake.models.TiffinReview;
import nshmadhani.com.wakenbake.models.UserReview;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRetrofitDataApi {

    @GET("search.php")
    Call<FirebasePlacesHolder> getPlacesFromFirebase(@Query("search") String name);

    @GET("searchtiffin.php")
    Call<TiffinPlacesHolder> getmTiffinPlaces(@Query("search") String name);

    @POST("reviewpost.php")
    Call<UserReview> saveReview(@Body UserReview userReview);

    @POST("dynamicratings.php")
    Call<RatingsResponse> saveRatings(
            @Query("vendors") String vendorName,
            @Query("ratings") float ratings);

    @POST("tiffinreview.php")
    Call<TiffinReview> saveTiffinReview(@Body TiffinReview tiffinReview);
}
