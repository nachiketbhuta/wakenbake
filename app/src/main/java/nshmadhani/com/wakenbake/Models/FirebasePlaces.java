package nshmadhani.com.wakenbake.Models;

import com.google.gson.annotations.SerializedName;

public class FirebasePlaces{

    @SerializedName("name")
    public String mVendorName;

    @SerializedName("id")
    public String mVendorId;

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

    @SerializedName("base_url")
    public String mVendorUrl;

    public String getmVendorId() {
        return mVendorId;
    }

    public void setmVendorId(String mVendorId) {
        this.mVendorId = mVendorId;
    }

    public String getmVendorUrl() {
        return mVendorUrl;
    }

    public void setmVendorUrl(String mVendorUrl) {
        this.mVendorUrl = mVendorUrl;
    }

    public String getmVendorName() {
        return mVendorName;
    }

    public void setmVendorName(String mVendorName) {
        this.mVendorName = mVendorName;
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
