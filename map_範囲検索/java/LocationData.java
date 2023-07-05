package com.example.map_kensaku_app_01;

public class LocationData {
    protected String facilityName; // 施設名　
    protected double latitude; // 緯度
    protected double longitude; // 経度
    protected String zyuusyo; // 住所

    /**
     *  セッター
     */
    public LocationData(String facilityName, double latitude, double longitude, String zyuusyo) {

        this.facilityName = facilityName; // 施設名
        this.latitude = latitude; // 緯度
        this.longitude = longitude; // 経度
        this.zyuusyo = zyuusyo;  // 住所
    }

    /**
     *  ゲッター
     */
    public String getFacilityName() {
        return facilityName;
    } // === 施設名

    public double getLatitude() {
        return latitude;
    } // === 緯度

    public double getLongitude() {
        return longitude;
    } // === 経度

    public String getZyuusyo() {return zyuusyo;} // === 住所


}
