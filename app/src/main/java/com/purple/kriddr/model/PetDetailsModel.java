package com.purple.kriddr.model;

import java.util.ArrayList;

/**
 * Created by pf-05 on 3/14/2018.
 */

public class PetDetailsModel {

    public String getPet_name() {
        return pet_name;
    }

    public void setPet_name(String pet_name) {
        this.pet_name = pet_name;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    String pet_name = "";
    String owner_name = "";
    String pay_recd_status="";
    String pet_id="";

    public String getPet_id() {
        return pet_id;
    }

    public void setPet_id(String pet_id) {
        this.pet_id = pet_id;
    }

    public String getPay_recd_status() {
        return pay_recd_status;
    }

    public void setPay_recd_status(String pay_recd_status) {
        this.pay_recd_status = pay_recd_status;
    }

    public String getPet_month_amount() {
        return pet_month_amount;
    }

    public void setPet_month_amount(String pet_month_amount) {
        this.pet_month_amount = pet_month_amount;
    }

    String pet_month_amount = "";


    public ArrayList<InvoiceDetailsInfoModel> getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(ArrayList<InvoiceDetailsInfoModel> getInfo) {
        this.getInfo = getInfo;
    }

    public ArrayList<InvoiceDetailsInfoModel> getInfo = new ArrayList<>();



}
