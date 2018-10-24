package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rishabh on 07/02/18.
 */

public class ImportRes {

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

        @SerializedName("pAdv")
        private String pAdv;

        @SerializedName("rAdv")
        private String rAdv;

        @SerializedName("actions")
        private List<Actions> actions;

        @SerializedName("dailyOrders")
        private List<ImportDistrictRes.DailyOrders> dailyOrders;

    }

    public class Actions {

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

    public String getpAdv() {
        if (case_details.pAdv == null) {
            case_details.pAdv = "N/A";
        }
        return case_details.pAdv;
    }

    public String getrAdv() {
        if (case_details.rAdv == null) {
            case_details.rAdv = "N/A";
        }
        return case_details.rAdv;
    }

    public List<Actions> getActions() {
        if (case_details.actions == null) {
            case_details.actions = new ArrayList<>();
        }
        return case_details.actions;
    }

    public List<ImportDistrictRes.DailyOrders> getDailyOrders() {
        if (case_details.dailyOrders == null) {
            case_details.dailyOrders = new ArrayList<>();
        }
        return case_details.dailyOrders;
    }

}
