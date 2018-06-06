package com.geekworkx.oho.Model;

/**
 * Created by parag on 02/01/18.
 */

public class Book
{

    private String Book_From_Latitude;
    private String Book_From_Longitude;
    private String Driver_First_Latitude;
    private String Driver_First_Longitude;
    private String Driver_Second_Latitude;
    private String Driver_Second_Longitude;
    private String Book_To_Latitude;
    private String Book_To_Longitude;
    private String User_Latitude;
    private String User_Longitude;
    private String Distance_travel;
    private String Cost;
    private String Estimated_fare;
    private String Vehicle_Choosen;
    private String Distance;
    private int Hourly_rate;
    private int Minimum_rate;
    private String UserMobile;
    private String DriverMobile;
    private String OTP;
    private String UserAccept;
    private String DriverAccept;
    private String Stop;
    private String Paid;
    private String Rate_user;
    private String Coupon_code;
    private String Coupon_value;
    private String START;
    private String Tax_names;
    private String Tax_percentage;


    public Book() {
    }


    public Book( String Book_From_Latitude, String Book_From_Longitude,String Driver_First_Latitude, String Driver_First_Longitude,String Book_To_Latitude,
                 String Book_To_Longitude,String UserMobile,String DriverMobile,
                 String Vehicle_Choosen,String Distance,int Hourly_rate,int Minimum_rate,String OTP,
                 String UserAccept,String DriverAccept,String Stop,String Paid,String Rate_user,
                 String Distance_travel,String Cost,String Estimated_fare,String Coupon_code,String Coupon_value,String START,
                 String Tax_names,String Tax_percentage,String Driver_Second_Latitude, String Driver_Second_Longitude) {


        this.Book_From_Latitude=Book_From_Latitude;
        this.Book_From_Longitude=Book_From_Longitude;
        this.Driver_First_Latitude=Driver_First_Latitude;
        this.Driver_First_Longitude=Driver_First_Longitude;
        this.Driver_Second_Latitude=Driver_Second_Latitude;
        this.Driver_Second_Longitude=Driver_Second_Longitude;
        this.Book_To_Latitude=Book_To_Latitude;
        this.Book_To_Longitude=Book_To_Longitude;
        this.Vehicle_Choosen=Vehicle_Choosen;
        this.Distance=Distance;
        this.Hourly_rate=Hourly_rate;
        this.UserMobile=UserMobile;
        this.DriverMobile=DriverMobile;
        this.OTP=OTP;
        this.UserAccept=UserAccept;
        this.DriverAccept=DriverAccept;
        this.Stop=Stop;
        this.Paid=Paid;
        this.Rate_user=Rate_user;
        this.Minimum_rate=Minimum_rate;
        this.Distance_travel=Distance_travel;
        this.Cost=Cost;
        this.Estimated_fare=Estimated_fare;
        this.Coupon_code=Coupon_code;
        this.Coupon_value=Coupon_value;
        this.START=START;
        this.Tax_names=Tax_names;
        this.Tax_percentage=Tax_percentage;

    }

    public String getDriver_Second_Latitude() {
        return Driver_Second_Latitude;
    }

    public void setDriver_Second_Latitude(String driver_Second_Latitude) {
        Driver_Second_Latitude = driver_Second_Latitude;
    }

    public String getDriver_Second_Longitude() {
        return Driver_Second_Longitude;
    }

    public void setDriver_Second_Longitude(String driver_Second_Longitude) {
        Driver_Second_Longitude = driver_Second_Longitude;
    }

    public String getTax_names() {
        return Tax_names;
    }

    public void setTax_names(String tax_names) {
        Tax_names = tax_names;
    }

    public String getTax_percentage() {
        return Tax_percentage;
    }

    public void setTax_percentage(String tax_percentage) {
        Tax_percentage = tax_percentage;
    }

    public String getSTART() {
        return START;
    }

    public void setSTART(String START) {
        this.START = START;
    }

    public String getCoupon_code() {
        return Coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        Coupon_code = coupon_code;
    }

    public String getCoupon_value() {
        return Coupon_value;
    }

    public void setCoupon_value(String coupon_value) {
        Coupon_value = coupon_value;
    }

    public String getEstimated_fare() {
        return Estimated_fare;
    }

    public void setEstimated_fare(String estimated_fare) {
        Estimated_fare = estimated_fare;
    }

    public String getDistance_travel() {
        return Distance_travel;
    }

    public void setDistance_travel(String distance_travel) {
        Distance_travel = distance_travel;
    }

    public String getCost() {
        return Cost;
    }

    public void setCost(String cost) {
        Cost = cost;
    }

    public String getBook_From_Latitude() {
        return Book_From_Latitude;
    }

    public void setBook_From_Latitude(String book_From_Latitude) {
        Book_From_Latitude = book_From_Latitude;
    }

    public String getBook_From_Longitude() {
        return Book_From_Longitude;
    }

    public void setBook_From_Longitude(String book_From_Longitude) {
        Book_From_Longitude = book_From_Longitude;
    }

    public String getDriver_First_Latitude() {
        return Driver_First_Latitude;
    }

    public void setDriver_First_Latitude(String Driver_First_Latitude) {
        Driver_First_Latitude = Driver_First_Latitude;
    }

    public String getDriver_First_Longitude() {
        return Driver_First_Longitude;
    }

    public void setDriver_First_Longitude(String Driver_First_Longitude) {
        Driver_First_Longitude = Driver_First_Longitude;
    }

    public String getBook_To_Latitude() {
        return Book_To_Latitude;
    }

    public void setBook_To_Latitude(String book_To_Latitude) {
        Book_To_Latitude = book_To_Latitude;
    }

    public String getBook_To_Longitude() {
        return Book_To_Longitude;
    }

    public void setBook_To_Longitude(String book_To_Longitude) {
        Book_To_Longitude = book_To_Longitude;
    }

    public String getUser_Latitude() {
        return User_Latitude;
    }

    public void setUser_Latitude(String user_Latitude) {
        User_Latitude = user_Latitude;
    }

    public String getUser_Longitude() {
        return User_Longitude;
    }

    public void setUser_Longitude(String user_Longitude) {
        User_Longitude = user_Longitude;
    }

    public String getVehicle_Choosen() {
        return Vehicle_Choosen;
    }

    public void setVehicle_Choosen(String vehicle_Choosen) {
        Vehicle_Choosen = vehicle_Choosen;
    }

    public String getDistance() {
        return Distance;
    }

    public void setDistance(String distance) {
        Distance = distance;
    }

    public int getHourly_rate() {
        return Hourly_rate;
    }

    public void setHourly_rate(int hourly_rate) {
        Hourly_rate = hourly_rate;
    }

    public int getMinimum_rate() {
        return Minimum_rate;
    }

    public void setMinimum_rate(int minimum_rate) {
        Minimum_rate = minimum_rate;
    }

    public String getUserMobile() {
        return UserMobile;
    }

    public void setUserMobile(String userMobile) {
        UserMobile = userMobile;
    }

    public String getDriverMobile() {
        return DriverMobile;
    }

    public void setDriverMobile(String driverMobile) {
        DriverMobile = driverMobile;
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getUserAccept() {
        return UserAccept;
    }

    public void setUserAccept(String userAccept) {
        UserAccept = userAccept;
    }

    public String getDriverAccept() {
        return DriverAccept;
    }

    public void setDriverAccept(String driverAccept) {
        DriverAccept = driverAccept;
    }

    public String getStop() {
        return Stop;
    }

    public void setStop(String stop) {
        Stop = stop;
    }

    public String getPaid() {
        return Paid;
    }

    public void setPaid(String paid) {
        Paid = paid;
    }

    public String getRate_user() {
        return Rate_user;
    }

    public void setRate_user(String rate_user) {
        Rate_user = rate_user;
    }
}

