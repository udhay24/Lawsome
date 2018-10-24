package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh on 07/02/18.
 */

public class SupremeCourtRes {

    @SerializedName("id")
    private String id;

    @SerializedName("reference_name")
    private String reference_name;

    @SerializedName("name")
    private String name;


    public String getId() {
        return id;
    }

    public String getReference() {
        return reference_name;
    }

    public String getName() {
        return name;
    }

}
