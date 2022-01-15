package com.example.feedi;

public class leftover_request_info {

    String req_key;
    String ins;
    String req_status;
    String members;
    String donor_key;
    String leftover_key;
    String address;


    public leftover_request_info(String req_key) {
        this.req_key = req_key;
    }

    String needy_key;
    String key;
    String user_key;

    public leftover_request_info() {
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public leftover_request_info(String req_key, String key) {
        this.req_key = req_key;
        this.key = key;
        this.req_status=req_status;
    }

    public leftover_request_info(String needy_key, String req_key, String key) {
        this.req_key = req_key;

        this.needy_key = needy_key;
        this.key=key;
        this.req_status=req_status;
    }

    public leftover_request_info(String req_key, String ins, String req_status, String members, String donor_key, String leftover_key, String address,String user_key) {
        this.req_key = req_key;
        this.ins = ins;
        this.req_status = req_status;
        this.members = members;
        this.donor_key = donor_key;
        this.leftover_key = leftover_key;
        this.address = address;
        this.user_key=user_key;
    }

    public void setReq_key(String req_key) {
        this.req_key = req_key;
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

    public void setDonor_key(String donor_key) {
        this.donor_key = donor_key;
    }

    public void setLeftover_key(String leftover_key) {
        this.leftover_key = leftover_key;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setNeedy_key(String needy_key) {
        this.needy_key = needy_key;
    }

    public String getReq_key() {
        return req_key;
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

    public String getDonor_key() {
        return donor_key;
    }

    public String getLeftover_key() {
        return leftover_key;
    }

    public String getAddress() {
        return address;
    }

    public String getNeedy_key() {
        return needy_key;
    }

    public String getUser_key() {
        return user_key;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public leftover_request_info(String req_key, String ins, String req_status, String members, String donor_key, String leftover_key, String address, String needy_key, String key, String user_key) {
        this.req_key = req_key;
        this.ins = ins;
        this.req_status = req_status;
        this.members = members;
        this.donor_key = donor_key;
        this.leftover_key = leftover_key;
        this.address = address;
        this.needy_key = needy_key;
        this.key = key;
        this.user_key = user_key;
    }
}
