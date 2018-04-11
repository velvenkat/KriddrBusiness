package com.purple.kriddr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pf-05 on 1/31/2018.
 */

public class UserModel implements Parcelable {

    String id;
    String name;
    String mobile;
    String email;
    String status;
    String business_status;

    String business_id;
    String logo_url;
    String business_name;
    String business_phone;
    String business_address;


    protected UserModel(Parcel in) {
        id = in.readString();
        name = in.readString();
        mobile = in.readString();
        email = in.readString();
        status = in.readString();
        business_status = in.readString();
        business_id = in.readString();

        logo_url = in.readString();
        business_name = in.readString();
        business_phone = in.readString();
        business_address = in.readString();
    }

    public static final Creator<UserModel> CREATOR = new Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public UserModel() {
    }

    public String getId() {
        return id;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusiness_id() {
        return business_id;
    }

    public void setBusiness_id(String business_id) {
        this.business_id = business_id;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public String getBusiness_name() {
        return business_name;
    }

    public void setBusiness_name(String business_name) {
        this.business_name = business_name;
    }

    public String getBusiness_phone() {
        return business_phone;
    }

    public void setBusiness_phone(String business_phone) {
        this.business_phone = business_phone;
    }

    public String getBusiness_address() {
        return business_address;
    }

    public void setBusiness_address(String business_address) {
        this.business_address = business_address;
    }


    public String getBusiness_status() {
        return business_status;
    }

    public void setBusiness_status(String business)
    {
        this.business_status = business;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(status);
        dest.writeString(business_status);
        dest.writeString(business_id);
        dest.writeString(logo_url);
        dest.writeString(business_name);
        dest.writeString(business_phone);
        dest.writeString(business_address);

    }
}
