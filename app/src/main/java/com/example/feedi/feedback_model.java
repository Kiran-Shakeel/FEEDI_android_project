package com.example.feedi;

public class feedback_model {
    String feedback,id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public feedback_model() {
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getFeedback() {
        return feedback;
    }



    public feedback_model(String feedback, String user_key) {
        this.feedback = feedback;
        this.id = user_key;
    }
}
