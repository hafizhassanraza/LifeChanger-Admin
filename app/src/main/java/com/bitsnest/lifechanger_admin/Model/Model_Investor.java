package com.bitsnest.lifechanger_admin.Model;

public class Model_Investor {
    private String id,profile,name,cnic,phone;

    public Model_Investor(String id, String profile, String name, String cnic, String phone) {
        this.id = id;
        this.profile = profile;
        this.name = name;
        this.cnic = cnic;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCnic() {
        return cnic;
    }

    public void setCnic(String cnic) {
        this.cnic = cnic;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
