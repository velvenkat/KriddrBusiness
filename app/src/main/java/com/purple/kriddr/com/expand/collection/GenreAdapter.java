package com.purple.kriddr.com.expand.collection;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.purple.kriddr.R;
import com.purple.kriddr.model.InvoiceDetailsInfoModel;
import com.thoughtbot.expandablerecyclerview.MultiTypeExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Niranjan Reddy on 16-03-2018.
 */

public class GenreAdapter extends MultiTypeExpandableRecyclerViewAdapter<GenreViewHolder, ChildViewHolder> {
    Context context;

    public static final int PaymentRecdType = 3;
    public static final int INVOICE_DETAIL_TYPE = 4;
    String Status_Flg;
     int UpdateHdrPos;
    List<HashMap<Integer, List<String>>> hdrDtls;

    public List<? extends ExpandableGroup<InvoiceDetailsInfoModel>> getGroupCollection() {
        return groupCollection;
    }

    public void setGroupCollection(List<? extends ExpandableGroup<InvoiceDetailsInfoModel>> groupCollection) {
        this.groupCollection = groupCollection;
    }

    List<? extends ExpandableGroup<InvoiceDetailsInfoModel>> groupCollection;
    PaymentRecivedViewHolder.checkChangeListener mListenr;

    public int getAdapter_Pos() {
        return Adapter_Pos;
    }



    int Adapter_Pos;

    public GenreAdapter(int Pos,List<HashMap<Integer, List<String>>> hdr, List<? extends ExpandableGroup<InvoiceDetailsInfoModel>> groups, Context _context,PaymentRecivedViewHolder.checkChangeListener listener,String ststus_flg) {
        super(groups);
        context = _context;
        hdrDtls = hdr;
        Adapter_Pos=Pos;
        Status_Flg=ststus_flg;
        mListenr=listener;
        groupCollection = groups;
    }

    @Override
    public GenreViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.scrn_invoice_expand_hdr, parent, false);
        return new GenreViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public ChildViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        switch (viewType) {
            case INVOICE_DETAIL_TYPE:
                View artist = inflater.inflate(R.layout.scrn_inv_expand_child, parent, false);
                return new InvoiceViewHolder(artist);
            case PaymentRecdType:
                View favorite = inflater.inflate(R.layout.bottom_payment_recd, parent, false);
                PaymentRecivedViewHolder payRecdHolder = new PaymentRecivedViewHolder(getAdapter_Pos(),favorite, mListenr);
                return payRecdHolder;
            default:
                throw new IllegalArgumentException("Invalid viewType");
        }
    }

    public List<HashMap<Integer, List<String>>> getHdrDtls() {
        return hdrDtls;
    }

    public void setHdrDtls(List<HashMap<Integer, List<String>>> hdrDtls) {
        this.hdrDtls = hdrDtls;
    }

    @Override
    public int getChildViewType(int position, ExpandableGroup group, int childIndex) {
        if (((Genre) group).getItems().get(childIndex).isCreateViewPaymentRecd()) {
            return PaymentRecdType;
        } else {
            return INVOICE_DETAIL_TYPE;
        }
    }

    @Override
    public boolean isChild(int viewType) {
        return viewType == PaymentRecdType || viewType == INVOICE_DETAIL_TYPE;
    }

    @Override
    public void onBindChildViewHolder(ChildViewHolder holder, int flatPosition, ExpandableGroup group,
                                      int childIndex) {

        int viewType = getItemViewType(flatPosition);

        InvoiceDetailsInfoModel artist = ((Genre) group).getItems().get(childIndex);
        int HdrIndex=((Genre) group).getHdrIndex();
        switch (viewType) {
            case INVOICE_DETAIL_TYPE:
                ((InvoiceViewHolder) holder).setArtistName(artist);

                break;
            case PaymentRecdType:

               ((PaymentRecivedViewHolder) holder).setChkPaymentRecd(artist.isPaymentRecd(),HdrIndex);
               if(Status_Flg.equalsIgnoreCase("2")){
                   ((PaymentRecivedViewHolder) holder).itemView.setVisibility(View.GONE);
               }


        }
    }


    @Override
    public void onBindGroupViewHolder(GenreViewHolder holder, int flatPosition,
                                      ExpandableGroup group) {
        try {


            HashMap<Integer, List<String>> dtls = hdrDtls.get(flatPosition);
            UpdateHdrPos = flatPosition;
            List<String> TitleList = dtls.get(flatPosition);
            holder.setGenreTitle(TitleList.get(0), TitleList.get(1), TitleList.get(2));

            if (TitleList.get(3).equalsIgnoreCase("pending")) {
                holder.setImgTickChange(false);
            } else {
                holder.setImgTickChange(true);
            }
        }
        catch (Exception e){

        }

        }



}
