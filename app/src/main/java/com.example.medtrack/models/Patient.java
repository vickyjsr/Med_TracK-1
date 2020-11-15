package com.example.medapp.models;

public class Patient {
    String Name;
    String EmailId;
    String Password;
    String MobNo;
    String Address;
    String DOB;
    String Height;
    String Weight;
    String AuthId;
    String BloodGroup;
    String Allergy;
    String ChronicDisease;
    String Gender;
    String ImgUrl;

    public Patient() {
    }

    public Patient(String name, String emailId, String password, String mobNo, String address, String DOB, String height, String weight, String authId, String bloodGroup, String allergy, String chronicDisease, String gender, String imgUrl) {
        Name = name;
        EmailId = emailId;
        Password = password;
        MobNo = mobNo;
        Address = address;
        this.DOB = DOB;
        Height = height;
        Weight = weight;
        AuthId = authId;
        BloodGroup = bloodGroup;
        Allergy = allergy;
        ChronicDisease = chronicDisease;
        Gender = gender;
        ImgUrl = imgUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getMobNo() {
        return MobNo;
    }

    public void setMobNo(String mobNo) {
        MobNo = mobNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWeight() {
        return Weight;
    }

    public void setWeight(String weight) {
        Weight = weight;
    }

    public String getAuthId() {
        return AuthId;
    }

    public void setAuthId(String authId) {
        AuthId = authId;
    }

    public String getBloodGroup() {
        return BloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        BloodGroup = bloodGroup;
    }

    public String getAllergy() {
        return Allergy;
    }

    public void setAllergy(String allergy) {
        Allergy = allergy;
    }

    public String getChronicDisease() {
        return ChronicDisease;
    }

    public void setChronicDisease(String chronicDisease) {
        ChronicDisease = chronicDisease;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
