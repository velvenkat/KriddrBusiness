package com.purple.kriddr.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class DocumentModel implements Parcelable{


    public String getDocuments_id() {
        return documents_id;
    }

    public void setDocuments_id(String documents_id) {
        this.documents_id = documents_id;
    }

    public String getDocument_name() {
        return document_name;
    }

    public void setDocument_name(String document_name) {
        this.document_name = document_name;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getDocument_category() {
        return document_category;
    }

    public void setDocument_category(String document_category) {
        this.document_category = document_category;
    }

    public String getDocument_category_id() {
        return document_category_id;
    }

    public void setDocument_category_id(String document_category_id) {
        this.document_category_id = document_category_id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    String documents_id;
    String document_name;
    String document;
    String document_category;
    String document_category_id;
    String created;




    protected DocumentModel(Parcel in) {
        documents_id = in.readString();
        document_name = in.readString();
        document = in.readString();
        document_category = in.readString();
        document_category_id = in.readString();
        created = in.readString();

    }

    public static final Creator<DocumentModel> CREATOR = new Creator<DocumentModel>() {
        @Override
        public DocumentModel createFromParcel(Parcel in) {
            return new DocumentModel(in);
        }

        @Override
        public DocumentModel[] newArray(int size) {
            return new DocumentModel[size];
        }
    };

    public DocumentModel() {
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(documents_id);
        dest.writeString(document_name);
        dest.writeString(document);
        dest.writeString(document_category);
        dest.writeString(document_category_id);
        dest.writeString(created);


    }

}
