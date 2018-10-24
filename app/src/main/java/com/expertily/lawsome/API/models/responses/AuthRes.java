package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

public class AuthRes {

    @SerializedName("status")
    private String status;

    @SerializedName("name")
    private String name;

    public String getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

}
