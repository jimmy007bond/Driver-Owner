package com.geekworkx.oho.URLS;

//This class is for storing all URLs as a model of URLs
public class Config_URL
{
    public static final String URL = "https://geekworkx.app/IndCab";

    public static final String GET_SETTINGS =URL + "/get_settings.php";
    public static final String URL_ADD_DRIVER = URL + "/app_add_driver.php";
    public static final String URL_COMMENT = URL + "/user_comment.php";
    public static final String GET_ALL_DATA = URL + "/get_all_data.php";
    public static final String GET_ALL_DATA_DRIVER = URL + "/get_all_data_driver.php";
    public static final String URL_STORE_DRIVER_LATLNG =URL + "/store_driver_lat_long.php";
    public static final String SMS_ORIGIN = "BDLIFT";
    public static final String OTP_DELIMITER = ":";
    public static final String URL_REQUEST_SMS_OWNER = URL + "/owner_login.php";
    public static final String URL_REQUEST_SMS_DRIVER = URL + "/driver_login.php";
    public static final String URL_VERIFY_OTP_OWNER = URL + "/owner_verify_otp.php";
    public static final String URL_VERIFY_OTP_DRIVER =URL + "/driver_verify_otp.php" ;
    public static final String URL_VALID_OWNER=URL + "/owner_valid.php";
    public static final String URL_VALID_DRIVER =URL + "/driver_valid.php" ;
    public static final String URL_OWNER_DRIVER =URL + "/Driver_details_add_by_owner.php" ;
    public static final String URL_DRIVER_DELETE = URL + "/driver_delete.php";
    public static final String URL_OWNER_VEHICLE = URL + "/upload_vehicle_owner.php";
    public static final String URL_ACCEPT_OWNER =URL + "/Driver_accept.php" ;
    public static final String URL_DRIVER_UPDATE = URL + "/driver_update.php";
    public static final String URL_OWNER_DRIVER_DOCUMENTS =URL + "/Driver_documets.php"  ;
    public static final String URL_OWNER_DOCUMENTS=URL + "/owner_documets.php"  ;
    public static final String URL_CAR_DELETE=URL + "/vehicle_delete.php";
    public static final String URL_CAR_UPDATE = URL + "/car_update.php";
    public static final String URL_OWNER_VEHICLE_IMAGE_ADD =URL + "/upload_vehicle_image.php" ;
    public static final String IMAGE_DIRECTORY_NAME = "uploads";
    public static final String BOOKING_RIDE_DRIVER = URL + "/update_driver_ride_now.php";
    public static final String STOP_BOOKING = URL + "/stop_ride_driver.php";
    public static final int NOTIFICATION_ID = 100;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String TOPIC_GLOBAL = "global";
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    public static final String PUSH_NOTIFICATION = "pushNotification";
    public static final String SHARED_PREF = "ah_firebase";
    public static final String FCM_SENT =URL + "/fcm_sent.php" ;
    public static final String URL_FCM_DRIVER=URL + "/fcm_driver.php";
    public static final String URL_FCM_OWNER=URL + "/fcm_owner.php";
    public static final String GET_ALL_BOOKING=URL + "/get_all_booking.php";
    public static final String STOP_RIDE =URL + "/stop_ride_image.php" ;
    public static final String START_RIDE_AFTER_OTP =URL + "/start_ride_after_otp.php" ;
    public static final String GET_STOP_BOOKING = URL + "/get_stop_booking.php";
    public static final String URL_STORE_ALL_LATLNG =URL + "/store_lat.php";
    public static final String URL_ADD_OWNER_INFORMATION =URL + "/add_owner_information.php";
    public static final String URL_OWNER_AS_DRIVER = URL + "/add_owner_as_driver.php";
    public static final String URL_OWNER_AS_DRIVER_DOCUMENTS =URL + "/owner_as_driver_documets.php"  ;
    public static final String DELETE_RIDE =URL + "/ride_delete_driver.php";

}
