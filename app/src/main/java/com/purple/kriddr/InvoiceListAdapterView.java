package com.purple.kriddr;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.purple.kriddr.adapter.NotesAdapter;
import com.purple.kriddr.model.InvoiceDateValues;
import com.purple.kriddr.model.InvoiceListModel;
import com.purple.kriddr.model.InvoiceModel;
import com.purple.kriddr.model.NotesModel;

import java.util.ArrayList;

/**
 * Created by pf-05 on 3/8/2018.
 */

public class InvoiceListAdapterView extends RecyclerView.Adapter<InvoiceListAdapterView.MyViewHolder>
{


    ArrayList<InvoiceDateValues> invoiceList;
    Context context;

    public InvoiceListAdapterView(ArrayList<InvoiceDateValues> invoiceList, Context context)
    {
        this.invoiceList = invoiceList;
        this.context = context;


    }


    @Override
    public InvoiceListAdapterView.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_header,parent,false);
        return new InvoiceListAdapterView.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder , int position) {

        String inv_date = "", inv_service = "", inv_value = "";

        final InvoiceDateValues group_model = invoiceList.get(position);



            inv_date = group_model.getInvoice_date();
            inv_service = group_model.getService();
            inv_value = group_model.getAmount();





        holder.invoice_date.setText(inv_date);
        holder.invoice_service.setText(inv_service);
        if(inv_value.trim().equalsIgnoreCase(""))
        {
            holder.invoice_value.setText("--");
        }
        else
        holder.invoice_value.setText("$"+inv_value);
    }


    @Override
    public int getItemCount() {
        return invoiceList.size();
    }



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView invoice_date, invoice_service, invoice_value;


        public MyViewHolder(View itemView) {
            super(itemView);

            invoice_date = (TextView) itemView.findViewById(R.id.head_month_year);
            invoice_service = (TextView)itemView.findViewById(R.id.head_invoice_name);
            invoice_value = (TextView)itemView.findViewById(R.id.head_invoice_price);

        }
    }
}
