package com.example.projectprmexe;

public class UpdateProfileRequest {
    private String FullName;
    private String Phone;
    private String Address;
    private String Gender;
    private String BirthDate;
    private String OldPassword;
    private String NewPassword;
    private String ConfirmPassword;

    public UpdateProfileRequest(String fullName, String phone, String address, String gender, String birthDate) {
        this.FullName = fullName;
        this.Phone = phone;
        this.Address = address;
        this.Gender = gender;
        this.BirthDate = birthDate;
    }

    public UpdateProfileRequest(String fullName, String phone, String address, String gender, String birthDate, 
                              String oldPassword, String newPassword, String confirmPassword) {
        this.FullName = fullName;
        this.Phone = phone;
        this.Address = address;
        this.Gender = gender;
        this.BirthDate = birthDate;
        this.OldPassword = oldPassword;
        this.NewPassword = newPassword;
        this.ConfirmPassword = confirmPassword;
    }

    // Getters
    public String getFullName() { return FullName; }
    public String getPhone() { return Phone; }
    public String getAddress() { return Address; }
    public String getGender() { return Gender; }
    public String getBirthDate() { return BirthDate; }
    public String getOldPassword() { return OldPassword; }
    public String getNewPassword() { return NewPassword; }
    public String getConfirmPassword() { return ConfirmPassword; }

    // Setters
    public void setFullName(String fullName) { this.FullName = fullName; }
    public void setPhone(String phone) { this.Phone = phone; }
    public void setAddress(String address) { this.Address = address; }
    public void setGender(String gender) { this.Gender = gender; }
    public void setBirthDate(String birthDate) { this.BirthDate = birthDate; }
    public void setOldPassword(String oldPassword) { this.OldPassword = oldPassword; }
    public void setNewPassword(String newPassword) { this.NewPassword = newPassword; }
    public void setConfirmPassword(String confirmPassword) { this.ConfirmPassword = confirmPassword; }
} 