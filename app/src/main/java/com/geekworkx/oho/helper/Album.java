package com.geekworkx.oho.helper;

/**
 * Created by Ashok on 07/11/2016.
 */
public class Album {
    private String Driver_Name;
    private String Driver_Vehicle_Insurance_Certificate_NoOf_Birth;
    private String Driver_Identification_Mark;
    private String Driver_Photo;
    private int position;
    private String Driver_Email;
    private String Driver_Driver_Address;
    private String Driver_Country;
    private String Driver_State;
    private String Verify_Driver;
    private String Verify_Owner;
    private int Owner_ID;
    private int Driver_ID;
    private String Driver_City;
    private String Driver_Pin;
    private String Driving_License_Photo;
    private String Pancard_Photo;
    private String Driver_Addressproof_Photo;
    private String Driver_Rating,Vehicle_Registration_Certificate_No,Vehicle_Registration_Certificate_Photo;
    private String Vehicle_Insurance_Certificate_No,Vehicle_Insurance_Certificate_Photo,Vehicle_Pancard_No,Vehicle_Pancard_Photo,Driver_Driver_Addressproof_Document;
    private String Vehicle_No,Vehicle_Type,Vehicle_Model;
    private String Aadhar_Card_Photo;
    private String Cancel_Cheque_Photo;
    private String Driver_Phone_No;
    private String Unique_Id;
    private String Vehicle_Company;
    private String Driver_Address;
    private String Vehicle_Image_1,Vehicle_Image_2,Vehicle_Image_3,Vehicle_Image_4;
    private String Date;
    private double Lat;
    private double Long;
    public int Minimum_Balance_Status;

    public Album() {
    }

    public Album(  int Minimum_Balance_Status,String Driver_Name, String Driver_Identification_Mark, String Driver_Country, String Driver_City, String Driver_Pin,
                 int Owner_ID, String Driver_State, String Driver_Driver_Address, String Driver_Email, String Driver_Vehicle_Insurance_Certificate_NoOf_Birth, String Driver_Photo, int position,
                 String Driving_License_Photo,int Driver_ID,String Verify_Driver,String Verify_Owner,
                 String Pancard_Photo, String Driver_Addressproof_Photo, String Driver_Rating, String Vehicle_Registration_Certificate_No, String Vehicle_Registration_Certificate_Photo, String Vehicle_Insurance_Certificate_No, String Vehicle_Insurance_Certificate_Photo,
                 String Vehicle_Pancard_No, String Vehicle_Pancard_Photo, String Driver_Driver_Addressproof_Document, String Vehicle_No, String Vehicle_Type, String Vehicle_Model, String Aadhar_Card_Photo, String Cancel_Cheque_Photo,
                 String Driver_Phone_No, String Vehicle_Image_4, String Unique_Id, String Date, String Vehicle_Company, String Driver_Address, String Vehicle_Image_1, String Vehicle_Image_2, String Vehicle_Image_3,double Lat,double Long){
        this.Driver_Name = Driver_Name;
        this.Driver_Identification_Mark=Driver_Identification_Mark;
        this.Driver_Photo = Driver_Photo;
        this.Driver_Vehicle_Insurance_Certificate_NoOf_Birth=Driver_Vehicle_Insurance_Certificate_NoOf_Birth;
        this.Driver_Email=Driver_Email;
        this.position=position;
        this.Driver_Driver_Address=Driver_Driver_Address;
        this.Driver_Country=Driver_Country;
        this.Driver_State=Driver_State;
        this.Owner_ID=Owner_ID;
        this.Minimum_Balance_Status=Minimum_Balance_Status;
        this.Driver_City=Driver_City;
        this.Driver_Pin=Driver_Pin;
        this.Driving_License_Photo=Driving_License_Photo;
        this.Pancard_Photo=Pancard_Photo;
        this.Driver_Addressproof_Photo=Driver_Addressproof_Photo;
        this.Driver_Rating=Driver_Rating;
        this.Vehicle_Registration_Certificate_No=Vehicle_Registration_Certificate_No;
        this.Vehicle_Registration_Certificate_Photo=Vehicle_Registration_Certificate_Photo;
        this.Vehicle_Insurance_Certificate_No=Vehicle_Insurance_Certificate_No;
        this.Vehicle_Insurance_Certificate_Photo=Vehicle_Insurance_Certificate_Photo;
        this.Vehicle_Pancard_No=Vehicle_Pancard_No;
        this.Vehicle_Pancard_Photo=Vehicle_Pancard_Photo;
        this.Driver_Driver_Addressproof_Document=Driver_Driver_Addressproof_Document;
        this.Vehicle_No=Vehicle_No;
        this.Vehicle_Model=Vehicle_Model;
        this.Vehicle_Type=Vehicle_Type;
        this.Aadhar_Card_Photo=Aadhar_Card_Photo;
        this.Cancel_Cheque_Photo=Cancel_Cheque_Photo;
        this.Driver_Phone_No=Driver_Phone_No;
        this.Vehicle_Image_4=Vehicle_Image_4;
        this.Unique_Id=Unique_Id;
        this.Vehicle_Company=Vehicle_Company;
        this.Driver_Address=Driver_Address;
        this.Vehicle_Image_1=Vehicle_Image_1;
        this.Vehicle_Image_2=Vehicle_Image_2;
        this.Vehicle_Image_3=Vehicle_Image_3;
        this.Date=Date;
        this.Lat=Lat;
        this.Long=Long;
        this.Driver_ID=Driver_ID;
        this.Verify_Driver=Verify_Driver;
        this.Verify_Owner=Verify_Owner;

    }
    public int getMinimum_Balance_Status(int position) {
        return Minimum_Balance_Status;
    }

    public void setMinimum_Balance_Status(int minimum_Balance_Status) {
        Minimum_Balance_Status = minimum_Balance_Status;
    }

    public String getDriver_Name(int position) {
        return Driver_Name;
    }

    public void setDriver_Name(String driver_Name) {
        this.Driver_Name = driver_Name;
    }

    public String getDriver_Vehicle_Insurance_Certificate_NoOf_Birth(int position) {
        return Driver_Vehicle_Insurance_Certificate_NoOf_Birth;
    }

    public void setDriver_Vehicle_Insurance_Certificate_NoOf_Birth(String driver_Vehicle_Insurance_Certificate_NoOf_Birth) {
        this.Driver_Vehicle_Insurance_Certificate_NoOf_Birth = driver_Vehicle_Insurance_Certificate_NoOf_Birth;
    }

    public String getDriver_Identification_Mark(int position) {
        return Driver_Identification_Mark;
    }

    public void setDriver_Identification_Mark(String driver_Identification_Mark) {
        this.Driver_Identification_Mark = driver_Identification_Mark;
    }

    public String getDriver_Photo(int position) {
        return Driver_Photo;
    }

    public void setDriver_Photo(String Driver_Photo) {
        this.Driver_Photo = Driver_Photo;
    }

    public int getPosition(int position) {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getDriver_Email(int position) {
        return Driver_Email;
    }

    public void setDriver_Email(String driver_Email) {
        this.Driver_Email = driver_Email;
    }

    public String getDriver_Driver_Address(int position) {
        return Driver_Driver_Address;
    }

    public void setDriver_Driver_Address(String driver_Driver_Address) {
        this.Driver_Driver_Address = driver_Driver_Address;
    }

    public String getDriver_Country(int position) {
        return Driver_Country;
    }

    public void setDriver_Country(String driver_Country) {
        this.Driver_Country = driver_Country;
    }

    public String getDriver_State(int position) {
        return Driver_State;
    }

    public void setDriver_State(String driver_State) {
        this.Driver_State = driver_State;
    }

    public String getVerify_Driver(int position) {
        return Verify_Driver;
    }

    public void setVerify_Driver(String verify_Driver) {
        this.Verify_Driver = verify_Driver;
    }

    public String getVerify_Owner(int position) {
        return Verify_Owner;
    }

    public void setVerify_Owner(String verify_Owner) {
        this.Verify_Owner = verify_Owner;
    }

    public int getOwner_ID(int position) {
        return Owner_ID;
    }

    public void setOwner_ID(int owner_ID) {
        this.Owner_ID = owner_ID;
    }

    public int getDriver_ID(int position) {
        return Driver_ID;
    }

    public void setDriver_ID(int driver_ID) {
        this.Driver_ID = driver_ID;
    }

    public String getDriver_City(int position) {
        return Driver_City;
    }

    public void setDriver_City(String driver_City) {
        this.Driver_City = driver_City;
    }

    public String getDriver_Pin(int position) {
        return Driver_Pin;
    }

    public void setDriver_Pin(String driver_Pin) {
        this.Driver_Pin = driver_Pin;
    }

    public String getDriving_License_Photo(int position) {
        return Driving_License_Photo;
    }

    public void setDriving_License_Photo(String driving_License_Photo) {
        this.Driving_License_Photo = driving_License_Photo;
    }

    public String getPancard_Photo(int position) {
        return Pancard_Photo;
    }

    public void setPancard_Photo(String pancard_Photo) {
        this.Pancard_Photo = pancard_Photo;
    }

    public String getDriver_Addressproof_Photo(int position) {
        return Driver_Addressproof_Photo;
    }

    public void setDriver_Addressproof_Photo(String driver_Addressproof_Photo) {
        this.Driver_Addressproof_Photo = driver_Addressproof_Photo;
    }

    public String getDriver_Rating(int position) {
        return Driver_Rating;
    }

    public void setDriver_Rating(String driver_Rating) {
        this.Driver_Rating = driver_Rating;
    }

    public String getVehicle_Registration_Certificate_No(int position) {
        return Vehicle_Registration_Certificate_No;
    }

    public void setVehicle_Registration_Certificate_No(String vehicle_Registration_Certificate_No) {
        this.Vehicle_Registration_Certificate_No = vehicle_Registration_Certificate_No;
    }

    public String getVehicle_Registration_Certificate_Photo(int position) {
        return Vehicle_Registration_Certificate_Photo;
    }

    public void setVehicle_Registration_Certificate_Photo(String vehicle_Registration_Certificate_Photo) {
        this.Vehicle_Registration_Certificate_Photo = vehicle_Registration_Certificate_Photo;
    }

    public String getVehicle_Insurance_Certificate_No(int position) {
        return Vehicle_Insurance_Certificate_No;
    }

    public void setVehicle_Insurance_Certificate_No(String vehicle_Insurance_Certificate_No) {
        this.Vehicle_Insurance_Certificate_No = vehicle_Insurance_Certificate_No;
    }

    public String getVehicle_Insurance_Certificate_Photo(int position) {
        return Vehicle_Insurance_Certificate_Photo;
    }

    public void setVehicle_Insurance_Certificate_Photo(String vehicle_Insurance_Certificate_Photo) {
        this.Vehicle_Insurance_Certificate_Photo = vehicle_Insurance_Certificate_Photo;
    }

    public String getVehicle_Pancard_No(int position) {
        return Vehicle_Pancard_No;
    }

    public void setVehicle_Pancard_No(String vehicle_Pancard_No) {
        this.Vehicle_Pancard_No = vehicle_Pancard_No;
    }

    public String getVehicle_Pancard_Photo(int position) {
        return Vehicle_Pancard_Photo;
    }

    public void setVehicle_Pancard_Photo(String vehicle_Pancard_Photo) {
        this.Vehicle_Pancard_Photo = vehicle_Pancard_Photo;
    }

    public String getDriver_Driver_Addressproof_Document(int position) {
        return Driver_Driver_Addressproof_Document;
    }

    public void setDriver_Driver_Addressproof_Document(String driver_Driver_Addressproof_Document) {
        this.Driver_Driver_Addressproof_Document = driver_Driver_Addressproof_Document;
    }

    public String getVehicle_No(int position) {
        return Vehicle_No;
    }

    public void setVehicle_No(String vehicle_No) {
        this.Vehicle_No = vehicle_No;
    }

    public String getVehicle_Type(int position) {
        return Vehicle_Type;
    }

    public void setVehicle_Type(String vehicle_Type) {
        this.Vehicle_Type = vehicle_Type;
    }

    public String getVehicle_Model(int position) {
        return Vehicle_Model;
    }

    public void setVehicle_Model(String vehicle_Model) {
        this.Vehicle_Model = vehicle_Model;
    }

    public String getAadhar_Card_Photo(int position) {
        return Aadhar_Card_Photo;
    }

    public void setAadhar_Card_Photo(String aadhar_Card_Photo) {
        this.Aadhar_Card_Photo = aadhar_Card_Photo;
    }

    public String getCancel_Cheque_Photo(int position) {
        return Cancel_Cheque_Photo;
    }

    public void setCancel_Cheque_Photo(String cancel_Cheque_Photo) {
        this.Cancel_Cheque_Photo = cancel_Cheque_Photo;
    }

    public String getDriver_Phone_No(int position) {
        return Driver_Phone_No;
    }

    public void setDriver_Phone_No(String driver_Phone_No) {
        this.Driver_Phone_No = driver_Phone_No;
    }

    public String getUnique_Id(int position) {
        return Unique_Id;
    }

    public void setUnique_Id(String unique_Id) {
        this.Unique_Id = unique_Id;
    }

    public String getVehicle_Company(int position) {
        return Vehicle_Company;
    }

    public void setVehicle_Company(String vehicle_Company) {
        this.Vehicle_Company = vehicle_Company;
    }

    public String getDriver_Address(int position) {
        return Driver_Address;
    }

    public void setDriver_Address(String driver_Address) {
        this.Driver_Address = driver_Address;
    }

    public String getVehicle_Image_1(int position) {
        return Vehicle_Image_1;
    }

    public void setVehicle_Image_1(String vehicle_Image_1) {
        this.Vehicle_Image_1 = vehicle_Image_1;
    }

    public String getVehicle_Image_2(int position) {
        return Vehicle_Image_2;
    }

    public void setVehicle_Image_2(String vehicle_Image_2) {
        this.Vehicle_Image_2 = vehicle_Image_2;
    }

    public String getVehicle_Image_3(int position) {
        return Vehicle_Image_3;
    }

    public void setVehicle_Image_3(String vehicle_Image_3) {
        this.Vehicle_Image_3 = vehicle_Image_3;
    }

    public String getVehicle_Image_4(int position) {
        return Vehicle_Image_4;
    }

    public void setVehicle_Image_4(String vehicle_Image_4) {
        this.Vehicle_Image_4 = vehicle_Image_4;
    }

    public String getDate(int position) {
        return Date;
    }

    public void setDate(String date) {
        this.Date = date;
    }

    public double getLat(int position) {
        return Lat;
    }

    public void setLat(double lat) {
        this.Lat = lat;
    }

    public double getLong(int position) {
        return Long;
    }

    public void setLong(double aLong) {
        this.Long = aLong;
    }
}
