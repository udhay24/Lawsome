package com.expertily.lawsome.API.models.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rishabh on 04/02/18.
 */

public class CasesRes {

    @SerializedName("case_name")
    private String case_name;

    @SerializedName("case_nature")
    private String case_nature;

    @SerializedName("case_court")
    private String case_court;

    @SerializedName("case_type")
    private String case_type;

    @SerializedName("case_year")
    private String case_year;

    @SerializedName("case_counsel")
    private String case_counsel;

    @SerializedName("case_number")
    private String case_number;

    @SerializedName("case_filing_date")
    private String case_filing_date;

    @SerializedName("case_practice_area")
    private String case_practice_area;

    @SerializedName("case_client")
    private String case_client;

    @SerializedName("case_client_type")
    private String case_client_type;

    @SerializedName("case_description")
    private String case_description;

    @SerializedName("case_client_email")
    private String case_client_email;

    @SerializedName("case_client_mobile")
    private String case_client_mobile;


    public String getCaseName() {
        return case_name;
    }

    public String getCaseNature() {
        return case_nature;
    }

    public String getCaseCourt() {
        return case_court;
    }

    public String getCaseType() {
        return case_type;
    }

    public String getCaseYear() {
        return case_year;
    }

    public String getCaseCounsel() {
        return case_counsel;
    }

    public String getCaseNumber() {
        return case_number;
    }

    public String getCaseFilingDate() {
        return case_filing_date;
    }

    public String getCasePracticeArea() {
        return case_practice_area;
    }

    public String getCaseClient() {
        return case_client;
    }

    public String getCaseClientType() {
        return case_client_type;
    }

    public String getCaseDescription() {
        return case_description;
    }

    public String getCaseClientEmail() {
        return case_client_email;
    }

    public String getCaseClientMobile() {
        return case_client_mobile;
    }

}
