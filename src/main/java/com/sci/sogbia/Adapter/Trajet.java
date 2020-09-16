package com.sci.sogbia.Adapter;


import android.widget.ImageView;

import java.io.Serializable;

public class Trajet implements Serializable {

   private String  Line ,Line2 ,time,time2,date,date2,nump,marque,Line_Line2, name ,Idimg,id_User ;
   int prix;

    public String getName() {
        return name;
    }

    public String getId_User() {
        return id_User;
    }

    public void setId_User(String id_User) {
        this.id_User = id_User;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdimg() {
        return Idimg;
    }

    public void setIdimg(String idimg) {
        Idimg = idimg;
    }

    private ImageView image ;

    public String getLine_Line2() {
        return Line_Line2;
    }

    public void setLine_Line2(String line_Line2) {
        Line_Line2 = line_Line2;
    }

    public String getMarque() {
        return marque;
    }

    public void setMarque(String marque) {
        this.marque = marque;
    }

    public Trajet() {
    }

    public String getLine() {
        return Line;
    }


    public void setLine(String line) {
        Line = line;
    }

    public String getLine2() {
        return Line2;
    }

    public void setLine2(String line2) {
        Line2 = line2;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTime2() {
        return time2;
    }

    public void setTime2(String time2) {
        this.time2 = time2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate2() {
        return date2;
    }


    public void setDate2(String date2) {
        this.date2 = date2;
    }

    public String getNump() {
        return nump;
    }

    public void setNump(String nump) {
        this.nump = nump;
    }


    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }
}
