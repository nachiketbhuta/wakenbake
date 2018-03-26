package nshmadhani.com.wakenbake.Interfaces;

import nshmadhani.com.wakenbake.Holders.FirebasePlacesHolder;
import nshmadhani.com.wakenbake.Holders.TiffinPlacesHolder;
import nshmadhani.com.wakenbake.Models.FirebaseReview;
import nshmadhani.com.wakenbake.Models.RatingsResponse;
import nshmadhani.com.wakenbake.Models.TiffinReview;
import nshmadhani.com.wakenbake.Models.UserReviewResponse;
import retrofit2.Call;
import retrofit2.http.Body;
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

    @POST("tiffinreview.php")
    Call<TiffinReview> saveTiffinUserReview(@Body TiffinReview tiffinReview);

    @POST("reviewpost.php")
    Call<FirebaseReview> saveFirebaseUserReview(@Body FirebaseReview userReview);

    @POST("reviewretrieve.php")
    @FormUrlEncoded
    Call<UserReviewResponse> getReviews(@Field("vendorname") String mVendorName);

}
