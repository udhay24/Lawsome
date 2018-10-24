package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh on 19/01/18.
 */

public class OTPRes {

    @SerializedName("message")
    private String message;

    @SerializedName("type")
    private String type;

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

}
