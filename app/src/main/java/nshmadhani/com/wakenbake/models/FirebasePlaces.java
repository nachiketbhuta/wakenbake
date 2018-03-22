package nshmadhani.com.wakenbake.models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

public class FirebasePlaces extends Places{

    @SerializedName("name")
    public String mVendorName;

    @SerializedName("id")
    public int mVendorId;

    @SerializedName("open")
    public int mVendorOpenTime;

    @SerializedName("close")
    public int mVendorCloseTime;

    @SerializedName("fooditems")
    public String mVendorFoodItems;

    @SerializedName("ratings")
    public float mVendorRatings;

    @SerializedName("number")
    public String mVendorPhoneNumber;

    @SerializedName("latitude")
    public double mVendorLatitude;

    @SerializedName("longitude")
    public double mVendorLongitude;

    public Uri getImageURL() {
        return imageURL;
    }

    public void setImageURL(Uri imageURL) {
        this.imageURL = imageURL;
    }

    public Uri imageURL;

    public String getmVendorName() {
        return mVendorName;
    }

    public void setmVendorName(String mVendorName) {
        this.mVendorName = mVendorName;
    }

    public int getmVendorId() {
        return mVendorId;
    }

    public void setmVendorId(int mVendorId) {
        this.mVendorId = mVendorId;
    }

    public int getmVendorOpenTime() {
        return mVendorOpenTime;
    }

    public void setmVendorOpenTime(int mVendorOpenTime) {
        this.mVendorOpenTime = mVendorOpenTime;
    }

    public int getmVendorCloseTime() {
        return mVendorCloseTime;
    }

    public void setmVendorCloseTime(int mVendorCloseTime) {
        this.mVendorCloseTime = mVendorCloseTime;
    }

    public String getmVendorFoodItems() {
        return mVendorFoodItems;
    }

    public void setmVendorFoodItems(String mVendorFoodItems) {
        this.mVendorFoodItems = mVendorFoodItems;
    }

    public float getmVendorRatings() {
        return mVendorRatings;
    }

    public void setmVendorRatings(float mVendorRatings) {
        this.mVendorRatings = mVendorRatings;
    }

    public String getmVendorPhoneNumber() {
        return mVendorPhoneNumber;
    }

    public void setmVendorPhoneNumber(String mVendorPhoneNumber) {
        this.mVendorPhoneNumber = mVendorPhoneNumber;
    }

    public double getmVendorLatitude() {
        return mVendorLatitude;
    }

    public void setmVendorLatitude(double mVendorLatitude) {
        this.mVendorLatitude = mVendorLatitude;
    }

    public double getmVendorLongitude() {
        return mVendorLongitude;
    }

    public void setmVendorLongitude(double mVendorLongitude) {
        this.mVendorLongitude = mVendorLongitude;
    }
}
