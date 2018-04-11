package com.purple.kriddr.model;

import java.util.ArrayList;

/**
 * Created by pf-05 on 3/8/2018.
 */

public class InvoiceListModel {

    public String getMonth_year() {
        return month_year;
    }

    public void setMonth_year(String month_year) {
        this.month_year = month_year;
    }

    public String getMonth_total() {
        return month_total;
    }

    public void setMonth_total(String month_total) {
        this.month_total = month_total;
    }

    public ArrayList<InvoiceDateValues> getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(ArrayList<InvoiceDateValues> getInfo) {
        this.getInfo = getInfo;
    }

    String month_year ="";
    String month_total = "";



    public ArrayList<InvoiceDateValues> getInfo = new ArrayList<>();

}
