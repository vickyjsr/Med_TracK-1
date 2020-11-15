package com.example.medapp.models;

public class Prescription {
    String Medicines,Tests,VideoCallLink,CallTime,Precautions,OtherDescription,PrescriptionTimeAndDate;

    public Prescription() {
    }

    public Prescription(String medicines, String tests, String videoCallLink, String callTime, String precautions, String otherDescription, String prescriptionTimeAndDate) {
        Medicines = medicines;
        Tests = tests;
        VideoCallLink = videoCallLink;
        CallTime = callTime;
        Precautions = precautions;
        OtherDescription = otherDescription;
        PrescriptionTimeAndDate = prescriptionTimeAndDate;
    }

    public String getMedicines() {
        return Medicines;
    }

    public void setMedicines(String medicines) {
        Medicines = medicines;
    }

    public String getTests() {
        return Tests;
    }

    public void setTests(String tests) {
        Tests = tests;
    }

    public String getVideoCallLink() {
        return VideoCallLink;
    }

    public void setVideoCallLink(String videoCallLink) {
        VideoCallLink = videoCallLink;
    }

    public String getCallTime() {
        return CallTime;
    }

    public void setCallTime(String callTime) {
        CallTime = callTime;
    }

    public String getPrecautions() {
        return Precautions;
    }

    public void setPrecautions(String precautions) {
        Precautions = precautions;
    }

    public String getOtherDescription() {
        return OtherDescription;
    }

    public void setOtherDescription(String otherDescription) {
        OtherDescription = otherDescription;
    }

    public String getPrescriptionTimeAndDate() {
        return PrescriptionTimeAndDate;
    }

    public void setPrescriptionTimeAndDate(String prescriptionTimeAndDate) {
        PrescriptionTimeAndDate = prescriptionTimeAndDate;
    }
}
