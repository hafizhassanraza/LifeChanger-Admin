package com.bitsnest.lifechanger_admin.Model;

public class Model_unNotify {
    private String id,data,date,heading;

    public Model_unNotify(String id, String data, String date, String heading) {
        this.id = id;
        this.data = data;
        this.date = date;
        this.heading = heading;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}
