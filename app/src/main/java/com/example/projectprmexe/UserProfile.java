package com.example.projectprmexe;

import com.google.gson.annotations.SerializedName;

public class UserProfile {
    @SerializedName("Id")
    private String id;

    @SerializedName("FullName")
    private String fullName;

    @SerializedName("Email")
    private String email;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Address")
    private String address;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("BirthDate")
    private String birthDate;

    public UserProfile() {}

    public UserProfile(String id, String fullName, String email, String phone, String address, String gender, String birthDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    // Getters
    public String getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getGender() { return gender; }
    public String getBirthDate() { return birthDate; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setAddress(String address) { this.address = address; }
    public void setGender(String gender) { this.gender = gender; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }
} 