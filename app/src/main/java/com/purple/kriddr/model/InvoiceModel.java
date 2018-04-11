package com.purple.kriddr.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by pf-05 on 3/5/2018.
 */

public class InvoiceModel implements Parcelable{

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    String user_id ="";
    String pet_id = "";

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    String comments = "";


    protected InvoiceModel(Parcel in) {
        user_id = in.readString();
        pet_id = in.readString();
        comments = in.readString();
        services = new ArrayList<InvoiceValueModel>();
        in.readList(services,null);


    }

    public InvoiceModel()
    {

    }

    public static final Creator<InvoiceModel> CREATOR = new Creator<InvoiceModel>() {
        @Override
        public InvoiceModel createFromParcel(Parcel in) {
            return new InvoiceModel(in);
        }

        @Override
        public InvoiceModel[] newArray(int size) {
            return new InvoiceModel[size];
        }
    };



    public ArrayList<InvoiceValueModel> getServices() {
        return services;
    }

    public void setServices(ArrayList<InvoiceValueModel> invoices) {
        this.services = invoices;
    }

    public ArrayList<InvoiceValueModel> services = new ArrayList<>();


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(user_id);
        parcel.writeString(pet_id);
        parcel.writeString(comments);
        parcel.writeList(services);


    }
}
