package com.purple.kriddr.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.purple.kriddr.R;
import com.purple.kriddr.model.InvoiceDateValues;
import com.purple.kriddr.model.InvoiceListModel;

import java.util.List;

/**
 * Created by pf-05 on 3/8/2018.
 */

public class InvocieListAdapter extends BaseExpandableListAdapter {

    Context context;
    List<InvoiceListModel> invoiceList;

    public InvocieListAdapter(Context context, List<InvoiceListModel> invoiceList) {

        this.context = context;
        this.invoiceList = invoiceList;
    }

    @Override
    public int getGroupCount() {
        return invoiceList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 0;
    }

    @Override
    public InvoiceListModel getGroup(int i) {
        return invoiceList.get(i);
    }

    @Override
    public InvoiceDateValues getChild(int i, int i1) {
        return invoiceList.get(i).getGetInfo().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup)
    {

        if(view == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.header_view,null);
        }

        TextView txtMonthYear = (TextView)view.findViewById(R.id.head_month_year);
        TextView txtInvoiceName = (TextView)view.findViewById(R.id.head_invoice_price);

        txtMonthYear.setText(invoiceList.get(i).getMonth_year());
        txtInvoiceName.setText(invoiceList.get(i).getMonth_total());



        for(int index=0; index < invoiceList.get(i).getGetInfo().size(); index++)
        {
            String amout = invoiceList.get(i).getGetInfo().get(index).getAmount();
            String service_name = invoiceList.get(i).getGetInfo().get(index).getService();
        }


        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {


        InvoiceListModel currentParent = getGroup(i);


        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //the first row is used as header
        if(i1 ==0)
        {

            view = inflater.inflate(R.layout.child_header, null);
            TextView head_month_year = (TextView)view.findViewById(R.id.head_month_year);
            TextView head_invoice_name  = (TextView)view.findViewById(R.id.head_invoice_name);
            TextView head_invoice_value = (TextView)view.findViewById(R.id.head_invoice_price);
        }

        //Here is the ListView of the ChildView
        if(i1>0 && i1<getChildrenCount(i)-1)
        {
            InvoiceDateValues currentChild = getChild(i,i1-1);
            view = inflater.inflate(R.layout.child_header,null);

            TextView head_month_year = (TextView)view.findViewById(R.id.head_month_year);
            TextView head_invoice_name  = (TextView)view.findViewById(R.id.head_invoice_name);
            TextView head_invoice_value = (TextView)view.findViewById(R.id.head_invoice_price);

            head_month_year.setText(currentChild.getInvoice_date());
            head_invoice_name.setText(currentChild.getService());
            head_invoice_value.setText(currentChild.getAmount());


        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
