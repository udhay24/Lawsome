package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rishabh on 07/02/18.
 */

public class ImportDistrictRes {

    @SerializedName("case_details")
    private Inside case_details;

    @SerializedName("message")
    private String message;

    class Inside {

        @SerializedName("caseNumber")
        private String caseNumber;

        @SerializedName("status")
        private String status;

        @SerializedName("party")
        private String party;

        @SerializedName("petitioners")
        private List<Petitioners> getpetitioners;

        @SerializedName("respondents")
        private List<Respondant> getrespondents;

        @SerializedName("actions")
        private List<DistrictActions> actions;

        @SerializedName("dailyOrders")
        private List<DailyOrders> dailyOrders;

    }

    public class Petitioners {

        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

    }

    public class Respondant {

        @SerializedName("name")
        private String name;

        public String getName() {
            return name;
        }

    }

    public class DistrictActions {

        @SerializedName("action")
        private String action;

        @SerializedName("date")
        private String date;

        public String getAction() {
            return action;
        }

        public String getDate() {
            return date;
        }

    }

    public class DailyOrders {

        @SerializedName("date")
        private String date;

        @SerializedName("cloudLink")
        private String cloudLink;

        public String getDate() {
            return date;
        }

        public String getCloudLink() {
            return cloudLink;
        }

    }

    public String getMessage() {
        return message;
    }

    public String getCaseNumber() {
        if (case_details.caseNumber == null) {
            case_details.caseNumber = "N/A";
        }
        return case_details.caseNumber;
    }

    public String getStatus() {
        if (case_details.status == null) {
            case_details.status = "N/A";
        }
        return case_details.status;
    }

    public String getParty() {
        if (case_details.party == null) {
            case_details.party = "N/A";
        }
        return case_details.party;
    }

    public List<Petitioners> getpAdv() {
        if (case_details.getpetitioners == null) {
            case_details.getpetitioners = new ArrayList<>();
        }
        return case_details.getpetitioners;
    }

    public List<Respondant> getrAdv() {
        if (case_details.getrespondents == null) {
            case_details.getrespondents = new ArrayList<>();
        }
        return case_details.getrespondents;
    }

    public List<DistrictActions> getActions() {
        if (case_details.actions == null) {
            case_details.actions = new ArrayList<>();
        }
        return case_details.actions;
    }

    public List<DailyOrders> getDailyOrders() {
        if (case_details.dailyOrders == null) {
            case_details.dailyOrders = new ArrayList<>();
        }
        return case_details.dailyOrders;
    }

}
