package com.purple.kriddr.model;

/**
 * Created by pf-05 on 3/8/2018.
 */

public class InvoiceDateValues {

    public String getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(String invoice_id) {
        this.invoice_id = invoice_id;
    }

    public String getInvoice_details_id() {
        return invoice_details_id;
    }

    public void setInvoice_details_id(String invoice_details_id) {
        this.invoice_details_id = invoice_details_id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String invoice_id = "";
    String invoice_details_id = "";
    String service = "";
    String amount = "";

    public String getInvoice_month_day() {
        return invoice_month_day;
    }

    public void setInvoice_month_day(String invoice_month_day) {
        this.invoice_month_day = invoice_month_day;
    }

    String invoice_month_day = "";



    public String getInvoice_date() {
        return invoice_date;
    }

    public void setInvoice_date(String invoice_date) {
        this.invoice_date = invoice_date;
    }

    String invoice_date= "";



}
