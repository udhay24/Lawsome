package com.expertily.lawsome.API.services;

import com.expertily.lawsome.API.models.responses.AuthRes;
import com.expertily.lawsome.API.models.responses.CasesRes;
import com.expertily.lawsome.API.models.responses.DistrictCourtsRes;
import com.expertily.lawsome.API.models.responses.DistrictsRes;
import com.expertily.lawsome.API.models.responses.HighCourtRes;
import com.expertily.lawsome.API.models.responses.ImportDistrictRes;
import com.expertily.lawsome.API.models.responses.ImportRes;
import com.expertily.lawsome.API.models.responses.NotesRes;
import com.expertily.lawsome.API.models.responses.SupremeCourtRes;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("login.php")
    Call<AuthRes> attemptLogin(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("register.php")
    Call<AuthRes> attemptRegister(@Field("name") String name, @Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("getcases.php")
    Call<List<CasesRes>> getCases(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("addcases.php")
    Call<AuthRes> addCase(@Field("mobile") String mobile, @Field("case_name") String case_name, @Field("case_nature") String case_nature, @Field("case_court") String case_court, @Field("case_type") String case_type, @Field("case_year") String case_year, @Field("case_counsel") String case_counsel, @Field("case_number") String case_number, @Field("case_filing_date") String case_filing_date, @Field("case_practice_area") String case_practice_area, @Field("case_client") String case_client, @Field("case_client_type") String case_client_type, @Field("case_description") String case_description, @Field("case_client_email") String case_client_email, @Field("case_client_mobile") String case_client_mobile);

    @GET("districtcourts.json")
    Call<List<DistrictsRes>> getDistrictCourt();

    @GET("rawdistricttypes.json")
    Call<List<HighCourtRes>> getRawDistrictTypes();

    @GET("highcourts.json")
    Call<List<HighCourtRes>> getHighCourt();

    @GET("supremecourts.json")
    Call<List<SupremeCourtRes>> getSupremeCourts();

    @FormUrlEncoded
    @POST("getdistrictcourts.php")
    Call<DistrictCourtsRes> getDistrictCourts(@Field("district") String district);

    @FormUrlEncoded
    @POST("importcase.php")
    Call<ImportRes> importCase(@Field("court_type") String court_id, @Field("court_id") String court_type, @Field("case_type") String case_type, @Field("case_number") String case_number, @Field("case_year") String case_year);

    @FormUrlEncoded
    @POST("importcase.php")
    Call<ImportDistrictRes> importDistrictCase(@Field("court_type") String court_id, @Field("court_id") String court_type, @Field("case_type") String case_type, @Field("case_number") String case_number, @Field("case_year") String case_year);

    @FormUrlEncoded
    @POST("getallnotes.php")
    Call<List<NotesRes>> getAllNotes(@Field("mobile") String mobile);

    @FormUrlEncoded
    @POST("getnotes.php")
    Call<List<NotesRes>> getNotes(@Field("mobile") String mobile, @Field("case_number") String case_number);

    @FormUrlEncoded
    @POST("addnote.php")
    Call<AuthRes> addNote(@Field("mobile") String mobile, @Field("note_text") String note_text, @Field("note_image") String note_image, @Field("case_number") String case_number, @Field("note_timestamp") String note_timestamp);

    @FormUrlEncoded
    @POST("updateemail.php")
    Call<AuthRes> updateEmail(@Field("mobile") String mobile, @Field("email") String email, @Field("case_number") String case_number);

    @FormUrlEncoded
    @POST("updatemobile.php")
    Call<AuthRes> updateMobile(@Field("mobile") String mobile, @Field("phone") String phone, @Field("case_number") String case_number);

}