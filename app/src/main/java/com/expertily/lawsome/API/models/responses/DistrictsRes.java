package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh on 07/02/18.
 */

public class DistrictsRes {

    @SerializedName("id")
    private String id;

    @SerializedName("name")
    private String name;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
