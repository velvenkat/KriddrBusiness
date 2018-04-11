package com.purple.kriddr.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.purple.kriddr.R;
import com.purple.kriddr.model.InvoiceDetailsInfoModel;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<HashMap<Integer,List<String>>> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<Integer, List<InvoiceDetailsInfoModel>> _listDataChild;
    private int VIEW_CHILD_INVOICE_ITEM;
    private int VIEW_CHILD_PAYMENT_RECD;

    public ExpandableAdapter(Context context, List<HashMap<Integer,List<String>>> listDataHeader,
                                 HashMap<Integer, List<InvoiceDetailsInfoModel>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {

        return this._listDataChild.get(groupPosition)
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /*@Override
    public int getChildType(int groupPosition, int childPosition) {
        return super.getChildType(groupPosition, childPosition);
    }*/

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        InvoiceDetailsInfoModel childText = (InvoiceDetailsInfoModel) getChild(groupPosition, childPosition);
        //List<InvoiceDetailsInfoModel> invoiceDetailsInfoModels= childText.get(childPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.scrn_inv_expand_child, null);
        }

        TextView txtListChild = (TextView) convertView
                .findViewById(R.id.txtDate);

        txtListChild.setText(childText.getInvoice_month_day());
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(groupPosition)
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        HashMap<Integer,List<String>> hash_hdrDtl  = (HashMap<Integer,List<String>>) getGroup(groupPosition);

        List<String> list_hdr_dtl=hash_hdrDtl.get(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.scrn_invoice_expand_hdr, null);
        }

        ImageView imgIndicator = (ImageView)convertView.findViewById(R.id.imgIndicator);

        TextView txtPetName = (TextView) convertView
                .findViewById(R.id.txtPetName);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        txtPetName.setText(list_hdr_dtl.get(0));

        if(isExpanded)
        {
            Log.d("EXPAMDA","EXPAMDA");
            imgIndicator.setImageResource(R.drawable.dxpan);
        }
        else
        {
            Log.d("REUMDSMDA","REUMDSMDA");
            imgIndicator.setImageResource(R.drawable.expan);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}