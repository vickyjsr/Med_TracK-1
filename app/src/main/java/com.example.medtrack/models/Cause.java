package com.example.medapp.models;

public class Cause {
    String BodyTemp,Pulse,OxygenSaturation,RespiratoryRate,BloodPressure;
    String Symptoms;
    String CallRequest;
    String Description;
    String Suggestions;

    public Cause() {
    }

    public Cause(String bodyTemp, String pulse, String oxygenSaturation, String respiratoryRate, String bloodPressure, String symptoms, String callRequest, String description, String suggestions) {
        BodyTemp = bodyTemp;
        Pulse = pulse;
        OxygenSaturation = oxygenSaturation;
        RespiratoryRate = respiratoryRate;
        BloodPressure = bloodPressure;
        Symptoms = symptoms;
        CallRequest = callRequest;
        Description = description;
        Suggestions = suggestions;
    }

    public String getBodyTemp() {
        return BodyTemp;
    }

    public void setBodyTemp(String bodyTemp) {
        BodyTemp = bodyTemp;
    }

    public String getPulse() {
        return Pulse;
    }

    public void setPulse(String pulse) {
        Pulse = pulse;
    }

    public String getOxygenSaturation() {
        return OxygenSaturation;
    }

    public void setOxygenSaturation(String oxygenSaturation) {
        OxygenSaturation = oxygenSaturation;
    }

    public String getRespiratoryRate() {
        return RespiratoryRate;
    }

    public void setRespiratoryRate(String respiratoryRate) {
        RespiratoryRate = respiratoryRate;
    }

    public String getBloodPressure() {
        return BloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        BloodPressure = bloodPressure;
    }

    public String getSymptoms() {
        return Symptoms;
    }

    public void setSymptoms(String symptoms) {
        Symptoms = symptoms;
    }

    public String getCallRequest() {
        return CallRequest;
    }

    public void setCallRequest(String callRequest) {
        CallRequest = callRequest;
    }

    public String getSuggestions() {
        return Suggestions;
    }

    public void setSuggestions(String suggestions) {
        Suggestions = suggestions;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
