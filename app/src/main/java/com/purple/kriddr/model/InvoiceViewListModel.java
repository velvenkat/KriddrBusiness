package com.purple.kriddr.model;

import java.util.ArrayList;

/**
 * Created by pf-05 on 3/14/2018.
 */

public class InvoiceViewListModel {

    public String getMonth_year() {
        return month_year;
    }

    public void setMonth_year(String month_year) {
        this.month_year = month_year;
    }

    public String getMonth_amount() {
        return month_amount;
    }

    public void setMonth_amount(String month_amount) {
        this.month_amount = month_amount;
    }

    public ArrayList<PetDetailsModel> getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(ArrayList<PetDetailsModel> getInfo) {
        this.getInfo = getInfo;
    }

    String month_year = "";
    String month_amount = "";

    public String getMonth_int() {
        return month_int;
    }

    public void setMonth_int(String month_int) {
        this.month_int = month_int;
    }

    public String getInvoice_year() {
        return invoice_year;
    }

    public void setInvoice_year(String invoice_year) {
        this.invoice_year = invoice_year;
    }

    String month_int = "";
    String invoice_year = "";

    public ArrayList<PetDetailsModel> getInfo = new ArrayList<>();



}
