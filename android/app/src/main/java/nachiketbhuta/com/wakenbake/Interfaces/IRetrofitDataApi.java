package nachiketbhuta.com.wakenbake.Interfaces;

import nachiketbhuta.com.wakenbake.Holders.FirebasePlacesHolder;
import nachiketbhuta.com.wakenbake.Holders.ReviewResponse;
import nachiketbhuta.com.wakenbake.Holders.TiffinPlacesHolder;
import nachiketbhuta.com.wakenbake.Holders.RatingsResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface IRetrofitDataApi {

    @GET("search.php")
    Call<FirebasePlacesHolder> getPlacesFromFirebase(@Query("search") String name);

    @GET("searchtiffin.php")
    Call<TiffinPlacesHolder> getmTiffinPlaces(@Query("search") String name);

    @POST("dynamicratings.php")
    @FormUrlEncoded
    Call<RatingsResponse> saveRatings(
            @Field("vendors") String vendorName,
            @Field("ratings") float ratings);

    @POST("tiffinratings.php")
    @FormUrlEncoded
    Call<RatingsResponse> saveTiffinRatings(
            @Field("vendor") String tiffinName,
            @Field("ratings") float ratings);

    @POST("reviewpost.php")
    @FormUrlEncoded
    Call<ResponseBody> saveReviews (
            @Field("review") String review,
            @Field("usrname") String username,
            @Field("vendorname") String vendorName);


    @POST("reviewretrieve.php")
    @FormUrlEncoded
    Call<ReviewResponse> getReviews(@Field("vendorname") String mVendorName);

}
