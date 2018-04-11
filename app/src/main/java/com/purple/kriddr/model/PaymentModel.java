package com.purple.kriddr.model;

import java.util.ArrayList;

/**
 * Created by pf-05 on 4/5/2018.
 */

public class PaymentModel {

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }



    String user_id = "";
    String status = "";

    public ArrayList<PaymentReceivedModel> getPay_recd() {
        return pay_recd;
    }

    public void setPay_recd(ArrayList<PaymentReceivedModel> pay_recd) {
        this.pay_recd = pay_recd;
    }

    public ArrayList<PaymentReceivedModel> pay_recd = new ArrayList<>();

}
