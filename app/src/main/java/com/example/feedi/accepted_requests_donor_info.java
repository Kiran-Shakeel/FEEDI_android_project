package com.example.feedi;


public class accepted_requests_donor_info  {
    String req_key,req_status,address,key;

    public accepted_requests_donor_info(String key) {
        this.req_key = req_key;
    }

    public accepted_requests_donor_info() {
    }

    public void setReq_key(String req_key) {
        this.req_key = req_key;
    }

    public void setReq_status(String req_status) {
        this.req_status = req_status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getReq_key() {
        return req_key;
    }

    public String getReq_status() {
        return req_status;
    }

    public String getAddress() {
        return address;
    }

    public String getKey() {
        return key;
    }

    public accepted_requests_donor_info(String req_key, String req_status, String address, String key) {
        this.req_key = req_key;
        this.req_status = req_status;
        this.address = address;
        this.key = key;
    }
}
