package com.example.mvvmbyshahrukh.ui.model;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class MaterialMaster  implements Serializable {
    @SerializedName("material_name")
    @Expose
    private String materialName = "";

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }
}