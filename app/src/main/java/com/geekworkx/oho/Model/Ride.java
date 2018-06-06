package com.geekworkx.oho.Model;

/**
 * Created by parag on 13/09/17.
 */

public class Ride  {

    private double Lat;
    private double Long;
    private double Latp;
    private double Longp;
    private double Latn;
    private double Longn;
    private String Vehicle;
    private String _PhoneNo;
    private String Paid;
    private String Rate_user;
    private String Owner;


    public Ride() {
    }


    public Ride( double Lat, double Long,double Latp, double Longp,double Latn, double Longn,String Vehicle,String _PhoneNo,String Paid,String Rate_user,String Owner) {


        this.Lat=Lat;
        this.Long=Long;
        this.Latp=Latp;
        this.Longp=Longp;
        this.Latn=Latn;
        this.Longn=Longn;
        this.Vehicle=Vehicle;
        this._PhoneNo=_PhoneNo;
        this.Paid=Paid;
        this.Rate_user=Rate_user;
        this.Owner=Owner;
    }

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }

    public String getRate_user() {
        return Rate_user;
    }

    public void setRate_user(String rate_user) {
        Rate_user = rate_user;
    }

    public String getPaid() {
        return Paid;
    }

    public void setPaid(String paid) {
        Paid = paid;
    }

    public String get_PhoneNo() {
        return _PhoneNo;
    }

    public void set_PhoneNo(String mobile) {
        _PhoneNo = mobile;
    }

    public String getVehicle() {
        return Vehicle;
    }

    public void setVehicle(String vehicle) {
        Vehicle = vehicle;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLong() {
        return Long;
    }

    public void setLong(double aLong) {
        Long = aLong;
    }

    public double getLatp() {
        return Latp;
    }

    public void setLatp(double latp) {
        Latp = latp;
    }

    public double getLongp() {
        return Longp;
    }

    public void setLongp(double longp) {
        Longp = longp;
    }

    public double getLatn() {
        return Latn;
    }

    public void setLatn(double latn) {
        Latn = latn;
    }

    public double getLongn() {
        return Longn;
    }

    public void setLongn(double longn) {
        Longn = longn;
    }
}
