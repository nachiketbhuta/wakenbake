package nshmadhani.com.wakenbake.main_screens.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Nachiket on 03-Feb-18.
 */

public class Places {

    @SerializedName("name")
    private String name;

    @SerializedName("number")
    private String phoneNumber;

    @SerializedName("ratings")
    private double ratings;

    @SerializedName("open")
    private int openTime;

    @SerializedName("close")
    private int closeTime;

    @SerializedName("fooditems")
    private String foodItems;

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
