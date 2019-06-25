package com.example.zaimzamrii.psmmasjid;

public class User {
    public String name;
    public String email;
    public String phone;
    public String profileimage2;
    public String profiledescription;
    public String devicetoken;




    public User(String name, String email, String phone, String profileimage2, String profiledescription, String devicetoken) {

        this.name = name;
        this.email = email;
        this.phone = phone;

        this.profileimage2 = profileimage2;
        this.profiledescription = profiledescription;
        this.devicetoken = devicetoken;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getProfileimage2() {
        return profileimage2;
    }

    public void setProfileimage2(String profileimage2) {
        this.profileimage2 = profileimage2;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


}

