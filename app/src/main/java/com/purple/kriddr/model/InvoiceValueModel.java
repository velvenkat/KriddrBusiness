package com.purple.kriddr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pf-05 on 3/5/2018.
 */

public class InvoiceValueModel implements Parcelable {

    protected InvoiceValueModel(Parcel in) {
        invoice_service_id = in.readString();
        amount = in.readString();
    }

    public static final Creator<InvoiceValueModel> CREATOR = new Creator<InvoiceValueModel>() {
        @Override
        public InvoiceValueModel createFromParcel(Parcel in) {
            return new InvoiceValueModel(in);
        }

        @Override
        public InvoiceValueModel[] newArray(int size) {
            return new InvoiceValueModel[size];
        }
    };

    public InvoiceValueModel() {
    }

    public String getInvoice_service_id() {
        return invoice_service_id;
    }

    public void setInvoice_service_id(String invoice_service_id) {
        this.invoice_service_id = invoice_service_id;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    String invoice_service_id ="";
    String amount ="";


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(invoice_service_id);
        parcel.writeString(amount);
    }
}
