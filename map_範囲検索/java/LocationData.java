package com.example.map_kensaku_app_01;

public class LocationData {
    protected String facilityName; // 施設名
    protected double latitude; // 緯度
    protected double longitude; // 経度

    /**
     * セッター
     */
    public LocationData(String facilityName, double latitude, double longitude) {

        this.facilityName = facilityName; // 施設名
        this.latitude = latitude; // 緯度
        this.longitude = longitude; // 経度

    }

    /**
     * ゲッター
     */
    public String getFacilityName() {
        return facilityName;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

}
