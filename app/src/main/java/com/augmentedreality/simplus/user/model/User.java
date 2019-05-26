package com.augmentedreality.simplus.user.model;

public class User {
    private String bannerImage;
    private String biography;
    private String email;
    private String fbUserId;
    private String fullName;
    private String photo;
    private String role;

    public User() {
        biography = "";
        email = "";
        fullName = "";
        photo = "";
        role = "";
    }

    public User(String mFbUserId, String mFullName, String mPhoto, String mEmail) {
        this.fbUserId = mFbUserId;
        this.fullName = mFullName;
        this.photo = mPhoto;
        this.email = mEmail;

    }

    public String getBannerImage() {
        return bannerImage ;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getFbUserId() {
        return fbUserId;
    }

    public void setFbUserId(String fbUserId) {
        this.fbUserId = fbUserId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
