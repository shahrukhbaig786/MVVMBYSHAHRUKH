package com.example.mvvmbyshahrukh.repository;


import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

/**
 * Error response of api call <br />
 * <coede>{"code": 2001, "message": "Some error"}</coede>
 */
public class ErrorResponse {
    private static Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public static ErrorResponse fromJson(JsonObject error) {
        return gson.fromJson(error, ErrorResponse.class);
    }

    public static ErrorResponse unknownError() {
        return new ErrorResponse("Unknown error occurred");
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return gson.toJson(this);
    }

}

