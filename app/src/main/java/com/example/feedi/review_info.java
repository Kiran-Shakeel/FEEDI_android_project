package com.example.feedi;

public class review_info {
    float rating;
    String comment, added_by, review_id, user_key,req_key;


    public review_info(String review_id, String user_key,String req_key) {
        this.review_id=review_id;
        this.user_key=user_key;
        this.req_key=req_key;
    }

    public review_info() {
    }

    public review_info(float rating,String comment)
    {
        this.rating=rating;
        this.comment=comment;
    }


    public void setReq_key(String req_key) {
        this.req_key = req_key;
    }

    public String getReq_key() {
        return req_key;
    }

    public review_info(float rating, String comment, String added_by, String review_id, String req_key) {
        this.rating = rating;
        this.comment = comment;
        this.added_by = added_by;
        this.review_id = review_id;
        this.req_key=req_key;
    }



    public String getComment() {
        return comment;
    }

    public String getAdded_by() {
        return added_by;
    }

    public String getReview_id() {
        return review_id;
    }

    public String getUser_key() {
        return user_key;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    public void setReview_id(String review_id) {
        this.review_id = review_id;
    }

    public void setUser_key(String user_key) {
        this.user_key = user_key;
    }

    public review_info(float rating, String comment, String added_by, String review_id, String user_key,String req_key) {
        this.rating = rating;
        this.comment = comment;
        this.added_by = added_by;
        this.review_id = review_id;
        this.user_key = user_key;
        this.req_key=req_key;
    }
}
