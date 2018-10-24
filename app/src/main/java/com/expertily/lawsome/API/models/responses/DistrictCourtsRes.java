package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Rishabh on 10/02/18.
 */

public class DistrictCourtsRes {

    @SerializedName("district_courts")
    private List<DistrictCourt> districtCourts = null;

    public class DistrictCourt {

        @SerializedName("reference_name")
        private String referenceName;

        @SerializedName("name")
        private String name;

        @SerializedName("case_types")
        private List<CaseType> caseTypes = null;


        public String getReferenceName() {
            return referenceName;
        }

        public String getName() {
            return name;
        }

        public List<CaseType> getCaseTypes() {
            return caseTypes;
        }

    }

    public class CaseType {

        @SerializedName("id")
        private Integer id;

        @SerializedName("reference_name")
        private String referenceName;

        @SerializedName("name")
        private String name;


        public String getReferenceName() {
            return referenceName;
        }

        public String getName() {
            return name;
        }

    }

    public List<DistrictCourt> getDistrictCourts() {
        return districtCourts;
    }

}
