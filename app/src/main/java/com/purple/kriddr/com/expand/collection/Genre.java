package com.purple.kriddr.com.expand.collection;

import com.purple.kriddr.model.InvoiceDetailsInfoModel;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Niranjan Reddy on 16-03-2018.
 */

public class Genre extends ExpandableGroup<InvoiceDetailsInfoModel> {
    int HdrIndex;

    public int getHdrIndex() {
        return HdrIndex;
    }



    List<String> HdrTitle;
    public Genre(String title, int Index,List<String>hdrTitle, List<InvoiceDetailsInfoModel> items) {
        super(title, items);
        HdrTitle=hdrTitle;
        HdrIndex=Index;
    }


}
