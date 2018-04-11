package com.purple.kriddr.model;

import java.util.ArrayList;

/**
 * Created by pf-05 on 4/5/2018.
 */

public class PaymentReceivedModel {

    public String getMonth_id() {
        return month_id;
    }

    public void setMonth_id(String month_id) {
        this.month_id = month_id;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    String month_id = "";
    String year = "";

    public ArrayList<String> getPet_id() {
        return pet_id;
    }

    public void setPet_id(ArrayList<String> pet_id) {
        this.pet_id = pet_id;
    }

    public ArrayList<String> pet_id = new ArrayList<>();

}
