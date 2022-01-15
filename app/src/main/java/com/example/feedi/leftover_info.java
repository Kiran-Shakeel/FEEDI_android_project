package com.example.feedi;

public class leftover_info {
    String leftover_key,user_key,image,ins,Expirey_date,deliver_option,status,members,longitude,latitude;


    public leftover_info(String expirey_date, String deliver_option, String status) {
        Expirey_date = expirey_date;
        this.deliver_option = deliver_option;
        this.status = status;
    }

    public leftover_info() {
    }

    public leftover_info(String user_key, String ins) {
        this.user_key = user_key;
        this.ins = ins;
    }

    public void setLeftover_key(String leftover_key) {
        this.leftover_key = leftover_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public String getLeftover_key() {
        return leftover_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public leftover_info(String leftover_key, String user_key, String image, String ins, String expirey_date, String deliver_option, String status, String members, String longitude, String latitude) {
        this.leftover_key=leftover_key;
        this.user_key=user_key;
        this.image = image;
        this.ins=ins;
        this.Expirey_date = expirey_date;
        this.deliver_option = deliver_option;
        this.status = status;
        this.members = members;
        this.longitude = longitude;
        this.latitude = latitude;

    }
    public void setImage(String image) {
        this.image = image;
    }

    public void setIns(String ins) {
        this.ins = ins;
    }

    public String getIns() {
        return ins;
    }
    public void setExpirey_date(String expirey_date) {
        this.Expirey_date = expirey_date;
    }

    public void setDeliver_option(String deliver_option) {
        this.deliver_option = deliver_option;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMembers(String members) {
        this.members = members;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getImage() {
        return image;
    }

    public String getExpirey_date() {
        return Expirey_date;
    }

    public String getDeliver_option() {
        return deliver_option;
    }

    public String getStatus() {
        return status;
    }

    public String getMembers() {
        return members;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }
}
