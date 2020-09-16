package com.sci.sogbia.Chat;

import android.net.Uri;
import android.widget.ImageView;

public class Profail {
    String nameprofil ,password,numtel,Email,Gender,typtV ,ImagID,note,id_profail;
    Uri photoUrl ;
    ImageView imageView;

    public String getId_profail() {
        return id_profail;
    }

    public void setId_profail(String id_profail) {
        this.id_profail = id_profail;
    }

    public Profail() {
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getImagID() {
        return ImagID;
    }

    public void setImagID(String imagID) {
        ImagID = imagID;
    }

    public Uri getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(Uri photoUrl) {
        this.photoUrl = photoUrl;
    }

    public Profail(String nameprofil, String password, String numtel, String email, String gender, String typtV) {
        this.nameprofil = nameprofil;
        this.password = password;
        this.numtel = numtel;
        this.Email = email;
        this.Gender = gender;
        this.typtV = typtV;
//        this.photoUrl = photoUrl;
    }

    public String getNameprofil() {
        return nameprofil;
    }

    public void setNameprofil(String nameprofil) {
        this.nameprofil = nameprofil;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNumtel() {
        return numtel;
    }

    public void setNumtel(String numtel) {
        this.numtel = numtel;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getTyptV() {
        return typtV;
    }

    public void setTyptV(String typtV) {
        this.typtV = typtV;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
