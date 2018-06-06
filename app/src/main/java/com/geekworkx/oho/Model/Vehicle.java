package com.geekworkx.oho.Model;

/**
 * Created by parag on 04/11/17.
 */

public class Vehicle {

    public String Vehicle_no,Vehicle_type,Vehicle_model,Vehicle_company,Vehicle_image_1,Vehicle_image_2;
    public String Vehicle_image_3,Vehicle_image_4;
    public int Minimum_Balance_Status;



    public Vehicle() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Vehicle( String Vehicle_no,String Vehicle_type,String Vehicle_model,String Vehicle_company,
                 String Vehicle_image_1,String Vehicle_image_2,String Vehicle_image_3,String Vehicle_image_4,
                    int Minimum_Balance_Status
                 ){
        this.Vehicle_model=Vehicle_model;
        this.Vehicle_company=Vehicle_company;
        this.Vehicle_image_1=Vehicle_image_1;
        this.Vehicle_image_2=Vehicle_image_2;
        this.Vehicle_no=Vehicle_no;
        this.Vehicle_type=Vehicle_type;
        this.Vehicle_image_3=Vehicle_image_3;
        this.Vehicle_image_4=Vehicle_image_4;
        this.Minimum_Balance_Status=Minimum_Balance_Status;

    }

    public int getMinimum_Balance_Status(int position) {
        return Minimum_Balance_Status;
    }

    public void setMinimum_Balance_Status(int minimum_Balance_Status) {
        Minimum_Balance_Status = minimum_Balance_Status;
    }

    public String getVehicle_image_3(int position) {
        return Vehicle_image_3;
    }

    public void setVehicle_image_3(String Vehicle_image_3) {
        Vehicle_image_3 = Vehicle_image_3;
    }

    public String getVehicle_image_4(int position) {
        return Vehicle_image_4;
    }

    public void setVehicle_image_4(String Vehicle_image_4) {
        Vehicle_image_4 = Vehicle_image_4;
    }




    public String getVehicle_type(int position) {
        return Vehicle_type;
    }

    public void setVehicle_type(String Vehicle_type) {
        this.Vehicle_type = Vehicle_type;
    }

    public String getVehicle_no(int position) {
        return Vehicle_no;
    }

    public void setVehicle_no(String Vehicle_no) {
        this.Vehicle_no = Vehicle_no;
    }

    public String getVehicle_model(int position) {
        return Vehicle_model;
    }

    public void setVehicle_model(String Vehicle_model) {
        this.Vehicle_model = Vehicle_model;
    }

    public String getVehicle_company(int position) {
        return Vehicle_company;
    }

    public void setVehicle_company(String Vehicle_company) {
        this.Vehicle_company = Vehicle_company;
    }

    public String getVehicle_image_1(int position) {
        return Vehicle_image_1;
    }

    public void setVehicle_image_1(String Vehicle_image_1) {
        this.Vehicle_image_1 = Vehicle_image_1;
    }

    public String getVehicle_image_2(int position) {
        return Vehicle_image_2;
    }

    public void setVehicle_image_2(String Vehicle_image_2) {
        this.Vehicle_image_2 = Vehicle_image_2;
    }
}