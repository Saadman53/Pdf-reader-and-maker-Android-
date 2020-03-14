package com.example.nyoreaderpdfmaker;

import android.media.Image;

public class User {

   String email,firstname,secondname;

    public User(String email, String firstname, String secondname) {
        this.email = email;
        this.firstname = firstname;
        this.secondname = secondname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getSecondname() {
        return secondname;
    }

    public void setSecondname(String secondname) {
        this.secondname = secondname;
    }
}
