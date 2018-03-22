package nshmadhani.com.wakenbake.interfaces;

import nshmadhani.com.wakenbake.holders.FirebasePlacesHolder;
import nshmadhani.com.wakenbake.holders.TiffinPlacesHolder;
import nshmadhani.com.wakenbake.models.FirebasePlaces;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRetrofitDataApi {

    String BASE_URL = "http://45.77.180.218:5965/";

    @GET("search.php")
    Call<FirebasePlacesHolder> getPlacesFromFirebase(@Query("search") String name);

    @GET("searchtiffin.php")
    Call<TiffinPlacesHolder> getmTiffinPlaces(@Query("search") String name);

    @POST("reviewpost.php")
    @FormUrlEncoded
    Call<FirebasePlaces> saveReviewInFirebase(
            @Field("usrname") String username,
            @Field("review") String review ,
            @Field("vendorname") String vendorName);

}
