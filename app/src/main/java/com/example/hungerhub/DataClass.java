package com.example.hungerhub;

public class DataClass {

    private String dataTitle;
    private String dataDesc;
    private String dataQty;
    private String dataExpiry;
    private String dataLocation;
    private String dataImage;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDesc() {
        return dataDesc;
    }

    public String getDataQty() {
        return dataQty;
    }

    public String getDataExpiry() {
        return dataExpiry;
    }

    public String getDataLocation() {
        return dataLocation;
    }

    public String getDataImage() {
        return dataImage;
    }

    public DataClass(String dataTitle, String dataDesc, String dataQty, String dataExpiry, String dataLocation, String dataImage) {
        this.dataTitle = dataTitle;
        this.dataDesc = dataDesc;
        this.dataQty = dataQty;
        this.dataExpiry = dataExpiry;
        this.dataLocation = dataLocation;
        this.dataImage = dataImage;
    }

    public DataClass() {
        // Required empty constructor
    }
}
