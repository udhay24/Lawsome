package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh on 11/02/18.
 */

public class NotesRes {

    @SerializedName("0")
    private String id;

    @SerializedName("note_text")
    private String note_text;

    @SerializedName("note_image")
    private String note_image;

    @SerializedName("case_number")
    private String case_number;

    @SerializedName("note_timestamp")
    private String note_timestamp;

    public String getId() {
        return id;
    }

    public String getText() {
        return note_text;
    }

    public String getImage() {
        return note_image;
    }

    public String getCaseNumber() {
        return case_number;
    }

    public String getNoteTimestamp() {
        return note_timestamp;
    }

}
