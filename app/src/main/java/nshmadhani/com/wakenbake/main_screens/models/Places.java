package nshmadhani.com.wakenbake.main_screens.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nachiket on 03-Feb-18.
 */

public class Places {

    @SerializedName("name")
    public String name;

    @SerializedName("number")
    public String phoneNumber;

    @SerializedName("ratings")
    public double ratings;

    @SerializedName("open")
    public int openTime;

    @SerializedName("close")
    public int closeTime;

    @SerializedName("fooditems")
    public String foodItems;

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getRatings() {
        return ratings;
    }

    public void setRatings(double ratings) {
        this.ratings = ratings;
    }

    public int getOpenTime() {
        return openTime;
    }

    public void setOpenTime(int openTime) {
        this.openTime = openTime;
    }

    public int getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(int closeTime) {
        this.closeTime = closeTime;
    }

    public String getFoodItems() {
        return foodItems;
    }

    public void setFoodItems(String foodItems) {
        this.foodItems = foodItems;
    }
}
