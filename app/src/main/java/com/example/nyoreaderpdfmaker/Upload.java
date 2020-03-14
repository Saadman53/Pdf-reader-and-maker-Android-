package com.example.nyoreaderpdfmaker;

import android.net.Uri;

public class Upload {

    private String imageName;
    private String imageUri;

    public Upload(Upload upload){
        this.imageName=upload.getImageName();
        this.imageUri=upload.getImageUri();
    }

    public  Upload(){

    }

    public Upload(String imageName, String imageUri) {
        this.imageName = imageName;
        this.imageUri = imageUri;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String  getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
