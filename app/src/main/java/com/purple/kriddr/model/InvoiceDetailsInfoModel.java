package com.purple.kriddr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pf-05 on 3/14/2018.
 */

public class InvoiceDetailsInfoModel implements Parcelable {
    public InvoiceDetailsInfoModel(){

    }

    protected InvoiceDetailsInfoModel(Parcel in) {
        invoice_id = in.readString();
        invoice_details_id = in.readString();
        service = in.readString();
        amount = in.readString();
        invoice_month_day = in.readString();
        CreateViewPaymentRecd = in.readByte() != 0;
        isPaymentRecd = in.readByte() != 0;
    }

    public static final Creator<InvoiceDetailsInfoModel> CREATOR = new Creator<InvoiceDetailsInfoModel>() {
        @Override
        public InvoiceDetailsInfoModel createFromParcel(Parcel in) {
            return new InvoiceDetailsInfoModel(in);
        }

        @Override
        public InvoiceDetailsInfoModel[] newArray(int size) {
            return new InvoiceDetailsInfoModel[size];
        }
    };

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

    public String getInvoice_month_day() {
        return invoice_month_day;
    }

    public void setInvoice_month_day(String invoice_month_day) {
        this.invoice_month_day = invoice_month_day;
    }

    String invoice_id = "";
    String invoice_details_id = "";
    String service = "";
    String amount = "";
    String invoice_month_day = "";

    public boolean isCreateViewPaymentRecd() {
        return CreateViewPaymentRecd;
    }

    public void setCreateFooterViewPaymentRecd(boolean createViewPaymentRecd) {
        CreateViewPaymentRecd = createViewPaymentRecd;
    }

    boolean CreateViewPaymentRecd=false;

    public boolean isPaymentRecd() {
        return isPaymentRecd;
    }

    public void setPaymentRecd(boolean paymentRecd) {
        isPaymentRecd = paymentRecd;
    }

    boolean isPaymentRecd=false;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(invoice_id);
        parcel.writeString(invoice_details_id);
        parcel.writeString(service);
        parcel.writeString(amount);
        parcel.writeString(invoice_month_day);
        parcel.writeByte((byte) (CreateViewPaymentRecd ? 1 : 0));
        parcel.writeByte((byte) (isPaymentRecd ? 1 : 0));
    }
}
