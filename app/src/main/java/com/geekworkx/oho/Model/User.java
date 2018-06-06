package com.geekworkx.oho.Model;

/**
 * Created by parag on 04/11/17.
 */

public class User {

    public String OTP,Vehicle,Unique_ride,User_from,User_to,User_mobile,Minimum_fare,Hourly_fare;
    public Double User_from_lat,User_from_long,User_to_lat,User_to_long;



    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User( String OTP,String Vehicle,String Unique_ride,String User_from,
                 String User_to,String User_mobile,double User_from_lat,double User_from_long,
                 double User_to_lat,double User_to_long ){
        this.Unique_ride=Unique_ride;
        this.User_from=User_from;
        this.User_to=User_to;
        this.User_mobile=User_mobile;
        this.OTP=OTP;
        this.Vehicle=Vehicle;
        this.User_from_lat=User_from_lat;
        this.User_from_long=User_from_long;
        this.User_to_lat=User_to_lat;
        this.User_to_long=User_to_long;
    }

    public Double getUser_from_lat(int position) {
        return User_from_lat;
    }

    public void setUser_from_lat(Double user_from_lat) {
        User_from_lat = user_from_lat;
    }

    public Double getUser_from_long(int position) {
        return User_from_long;
    }

    public void setUser_from_long(Double user_from_long) {
        User_from_long = user_from_long;
    }

    public Double getUser_to_lat(int position) {
        return User_to_lat;
    }

    public void setUser_to_lat(Double user_to_lat) {
        User_to_lat = user_to_lat;
    }

    public Double getUser_to_long(int position) {
        return User_to_long;
    }

    public void setUser_to_long(Double user_to_long) {
        User_to_long = user_to_long;
    }

    public String getVehicle(int position) {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }

    public String getOTP(int position) {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
    }

    public String getUnique_ride(int position) {
        return Unique_ride;
    }

    public void setUnique_ride(String unique_ride) {
        Unique_ride = unique_ride;
    }

    public String getUser_from(int position) {
        return User_from;
    }

    public void setUser_from(String user_from) {
        User_from = user_from;
    }

    public String getUser_to(int position) {
        return User_to;
    }

    public void setUser_to(String user_to) {
        User_to = user_to;
    }

    public String getUser_mobile(int position) {
        return User_mobile;
    }

    public void setUser_mobile(String user_mobile) {
        User_mobile = user_mobile;
    }
}