package com.purple.kriddr.model;

/**
 * Created by pf-05 on 2/22/2018.
 */

public class NotesModel {

    public String getPet_notes_id() {
        return pet_notes_id;
    }

    public void setPet_notes_id(String pet_notes_id) {
        this.pet_notes_id = pet_notes_id;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    String pet_notes_id;
    String notes;
    String created;



}
