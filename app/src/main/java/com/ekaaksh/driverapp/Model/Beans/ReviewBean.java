package com.ekaaksh.driverapp.Model.Beans;

public class ReviewBean {
    /**
     * id : 42
     * username : mukesh
     * image : paaport_photo.jpg
     * review_text : good food
     * ratting : 4
     * created_at : 5092019
     * login_with : ios
     */

    private String id;
    private String username;
    private String image;
    private String review_text;
    private String ratting;
    private String created_at;
    private String login_with;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getReview_text() {
        return review_text;
    }

    public void setReview_text(String review_text) {
        this.review_text = review_text;
    }

    public String getRatting() {
        return ratting;
    }

    public void setRatting(String ratting) {
        this.ratting = ratting;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLogin_with() {
        return login_with;
    }

    public void setLogin_with(String login_with) {
        this.login_with = login_with;
    }
}
