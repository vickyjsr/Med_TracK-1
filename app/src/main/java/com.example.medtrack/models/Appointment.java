package com.example.medapp.models;

public class Appointment {
    String To,From,AppointmentDate,AppointmentTime,AppointmentStatus,AppointmentId,ToAuthId,FromAuthId;

    public Appointment() {
    }

    public Appointment(String to, String from, String appointmentDate, String appointmentTime, String appointmentStatus, String appointmentId, String toAuthId, String fromAuthId) {
        To = to;
        From = from;
        AppointmentDate = appointmentDate;
        AppointmentTime = appointmentTime;
        AppointmentStatus = appointmentStatus;
        AppointmentId = appointmentId;
        ToAuthId = toAuthId;
        FromAuthId = fromAuthId;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getAppointmentDate() {  
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        AppointmentTime = appointmentTime;
    }

    public String getAppointmentStatus() {
        return AppointmentStatus;
    }

    public void setAppointmentStatus(String appointmentStatus) {
        AppointmentStatus = appointmentStatus;
    }

    public String getAppointmentId() {
        return AppointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        AppointmentId = appointmentId;
    }

    public String getToAuthId() {
        return ToAuthId;
    }

    public void setToAuthId(String toAuthId) {
        ToAuthId = toAuthId;
    }

    public String getFromAuthId() {
        return FromAuthId;
    }

    public void setFromAuthId(String fromAuthId) {
        FromAuthId = fromAuthId;
    }
}
