package com.example.projectprmexe;

public class RegisterRequest {
    private String FullName;
    private String Email;
    private String Password;
    private String Phone;
    private String Address;
    private String Gender;
    private String BirthDate;

    public RegisterRequest(String fullName, String email, String password, String phone, String address, String gender, String birthDate) {
        this.FullName = fullName;
        this.Email = email;
        this.Password = password;
        this.Phone = phone;
        this.Address = address;
        this.Gender = gender;
        this.BirthDate = birthDate;
    }

    public String getFullName() { return FullName; }
    public void setFullName(String fullName) { this.FullName = fullName; }
    public String getEmail() { return Email; }
    public void setEmail(String email) { this.Email = email; }
    public String getPassword() { return Password; }
    public void setPassword(String password) { this.Password = password; }
    public String getPhone() { return Phone; }
    public void setPhone(String phone) { this.Phone = phone; }
    public String getAddress() { return Address; }
    public void setAddress(String address) { this.Address = address; }
    public String getGender() { return Gender; }
    public void setGender(String gender) { this.Gender = gender; }
    public String getBirthDate() { return BirthDate; }
    public void setBirthDate(String birthDate) { this.BirthDate = birthDate; }
} 