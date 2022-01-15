package com.example.feedi;

public class all_profiles {
    String profile_pic,status,first_name,last_name,gender,city,cnic,email,address,about,id,profile_by,phone_no;

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public all_profiles() {
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setProfile_by(String profile_by) {
        this.profile_by = profile_by;
    }

    public String getProfile_by() {
        return profile_by;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public all_profiles(String profile_pic, String status, String first_name, String last_name, String gender, String city, String cnic, String email, String address, String about, String id, String profile_by, String phone_no) {
        this.profile_pic = profile_pic;
        this.status = status;
        this.first_name = first_name;
        this.last_name = last_name;
        this.gender = gender;
        this.city = city;
        this.cnic = cnic;
        this.email = email;
        this.address = address;
        this.about = about;
        this.id=id;
        this.profile_by=profile_by;
        this.phone_no=phone_no;
    }
}
