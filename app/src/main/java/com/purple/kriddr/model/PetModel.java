package com.purple.kriddr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pf-05 on 2/14/2018.
 */

public class PetModel implements Parcelable{

    protected PetModel(Parcel in) {
        pet_id = in.readString();
        owwner_id = in.readString();
        owner_name = in.readString();
        mobile = in.readString();
        email = in.readString();
        address = in.readString();
        preferred_contact = in.readString();
        photo = in.readString();
        pet_name = in.readString();
        dob = in.readString();
        brand = in.readString();
        protein = in.readString();
        portion_size = in.readString();
        shared_with_business = in.readString();
        profile_status=in.readString();
        type = in.readString();
        user_id = in.readString();
        created=in.readString();
    }

    public static final Creator<PetModel> CREATOR = new Creator<PetModel>() {
        @Override
        public PetModel createFromParcel(Parcel in) {
            return new PetModel(in);
        }

        @Override
        public PetModel[] newArray(int size) {
            return new PetModel[size];
        }
    };

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getPortion_size() {
        return portion_size;
    }

    public void setPortion_size(String portion_size) {
        this.portion_size = portion_size;
    }



    public String getOwwner_id() {
        return owwner_id;
    }

    public void setOwwner_id(String owwner_id) {
        this.owwner_id = owwner_id;
    }



    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }





    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPreferred_contact() {
        return preferred_contact;
    }

    public void setPreferred_contact(String preferred_contact) {
        this.preferred_contact = preferred_contact;
    }

    public String getShared_with_business() {
        return shared_with_business;
    }

    public void setShared_with_business(String shared_with_business) {
        this.shared_with_business = shared_with_business;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    String pet_id;
    String owwner_id;
    String owner_name;
    String mobile;
    String email;
    String address;
    String preferred_contact;
    String photo;
    String pet_name;
    String dob;
    String brand;
    String protein;
    String portion_size;
    String shared_with_business;
    String type;
    String user_id;
    String profile_status;

    public String getProfile_status() {
        return profile_status;
    }

    public void setProfile_status(String profile_status) {
        this.profile_status = profile_status;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    String created;



    public PetModel() {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pet_id);
        dest.writeString(owwner_id);
        dest.writeString(owner_name);
        dest.writeString(mobile);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(preferred_contact);
        dest.writeString(photo);
        dest.writeString(pet_name);
        dest.writeString(dob);
        dest.writeString(brand);
        dest.writeString(protein);
        dest.writeString(portion_size);
        dest.writeString(shared_with_business);
        dest.writeString(type);
        dest.writeString(user_id);
        dest.writeString(created);
        dest.writeString(profile_status);
    }
}
