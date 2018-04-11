package com.purple.kriddr.com.expand.collection;

import android.view.View;
import android.widget.TextView;

import com.purple.kriddr.R;
import com.purple.kriddr.model.InvoiceDetailsInfoModel;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.List;

/**
 * Created by Niranjan Reddy on 16-03-2018.
 */

public class InvoiceViewHolder extends ChildViewHolder {


    public void setArtistName(InvoiceDetailsInfoModel dtlList) {
        txtDate.setText(dtlList.getInvoice_month_day());
        txtSerName.setText(dtlList.getService());
        txtInvPrice.setText("$"+dtlList.getAmount());

    }

    private TextView txtDate;
    private TextView txtSerName;
    private TextView txtInvPrice;

    public InvoiceViewHolder(View itemView) {
        super(itemView);
        txtDate = itemView.findViewById(R.id.txtDate);
        txtSerName = itemView.findViewById(R.id.txtSerName);
        txtInvPrice = itemView.findViewById(R.id.txtInvPrice);
    }

    public void onBind(InvoiceDetailsInfoModel artist) {
        txtDate.setText(artist.getInvoice_month_day());
        txtSerName.setText(artist.getService());
        txtInvPrice.setText("$"+artist.getAmount());
    }
}