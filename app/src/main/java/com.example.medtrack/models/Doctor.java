package com.example.medapp.models;

public class Doctor {
    String Name;
    String Degree;
    String Sex;
    String Dob;
    String Password;
    String MobNo;
    String EmailId;
    String RegNo;
    String Hospital;
    String Address;
    String Experience;
    String AuthId;
    String ImgUrl;

    public Doctor() {
    }

    public Doctor(String name, String degree, String sex, String dob, String password, String mobNo, String emailId, String regNo, String hospital, String address, String experience, String authId, String imgUrl) {
        Name = name;
        Degree = degree;
        Sex = sex;
        Dob = dob;
        Password = password;
        MobNo = mobNo;
        EmailId = emailId;
        RegNo = regNo;
        Hospital = hospital;
        Address = address;
        Experience = experience;
        AuthId = authId;
        ImgUrl = imgUrl;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMobNo() {
        return MobNo;
    }

    public void setMobNo(String mobNo) {
        MobNo = mobNo;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getRegNo() {
        return RegNo;
    }

    public void setRegNo(String regNo) {
        RegNo = regNo;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getExperience() {
        return Experience;
    }

    public void setExperience(String experience) {
        Experience = experience;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }

    public String getAuthId() {
        return AuthId;
    }

    public void setAuthId(String authId) {
        AuthId = authId;
    }

    public String getDegree() {
        return Degree;
    }

    public void setDegree(String degree) {
        Degree = degree;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getDob() {
        return Dob;
    }

    public void setDob(String dob) {
        Dob = dob;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
