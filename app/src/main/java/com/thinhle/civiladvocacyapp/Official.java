package com.thinhle.civiladvocacyapp;
import java.io.Serializable;



public class Official implements Serializable {
    private String name;
    private String office;
    private String party;
    private String facebook;
    private  String twitter;
    private String youtube;
    private String officeAddress;
    private String phoneNumber;
    private String emailAddress;
    private String website;
    private String imageID;

    public Official(String name, String office, String party, String facebook,String twitter, String youtube, String officeAddress,
                    String phoneNumber, String emailAddress, String website, String imageID) {
        this.name = name;
        this.office = office;
        this.party = party;
        this.facebook = facebook;
        this.twitter = twitter;
        this.youtube=  youtube;
        this.officeAddress = officeAddress;
        this.phoneNumber = phoneNumber;
        this.emailAddress = emailAddress;
        this.website = website;
        this.imageID = imageID;
    }
    public String getYoutube(){
        return youtube;
    }

    public String getTwitter(){
        return twitter;
    }
    public String getName() {
        return name;
    }

    public String getOffice() {
        return office;
    }

    public String getParty() {
        return party;
    }

    public String getFacebook() {
        return facebook;
    }


    public String getOfficeAddress() {
        return officeAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getWebsite() {
        return website;
    }

    public String getImageID() {
        return imageID;
    }
}
