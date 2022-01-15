package com.example.feedi;

public class request_info {

    String req_key,user_key,ins,req_status,members,longitude,latitude,donor_key;





    public request_info() {
    }

    public request_info(String user_key, String ins) {
        this.user_key = user_key;
        this.ins = ins;
    }



    public request_info(String req_key, String user_key, String ins, String req_status, String members, String longitude, String latitude,String donor_key) {
        this.req_key = req_key;
        this.user_key = user_key;
        this.ins = ins;
        this.req_status = req_status;
        this.members = members;
        this.longitude = longitude;
        this.latitude = latitude;
        this.donor_key=donor_key;
    }

    public request_info(String ins, String req_status, String members) {
        this.ins = ins;
        this.req_status = req_status;
        this.members = members;
    }

    public String getReq_key() {
        return req_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public String getIns() {
        return ins;
    }

    public String getReq_status() {
        return req_status;
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

    public void setReq_key(String req_key) {
        this.req_key = req_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public void setIns(String ins) {
        this.ins = ins;
    }

    public void setReq_status(String req_status) {
        this.req_status = req_status;
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
}
