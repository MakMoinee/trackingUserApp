package com.thesis.trackinguserapp.common;

public class Common {
    public static String currentDeviceID = "";

    public static String getRightStatus(String status) {
        switch (status) {
            case "stop":
            case "Stop":
                return "Online";
            default:
                return "Offline";
        }
    }

    public static String deviceToken = "";
}
