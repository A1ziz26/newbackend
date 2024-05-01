package com.teleconsulting.demo.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
public class CallHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "did")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "pid")
    private Patient patient;

    private LocalDate callDate;
    private LocalTime callTime;
    private String prescription;
    private LocalTime endTime;
    private boolean scheduleconsent=false;
    private boolean recordingconsent=false;

    private String reason;
    private Float callRating;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Float getCallRating() {
        return callRating;
    }

    public void setCallRating(Float callRating) {
        this.callRating = callRating;
    }

    public boolean isScheduleconsent() {
        return scheduleconsent;
    }

    public void setScheduleconsent(boolean scheduleconsent) {
        this.scheduleconsent = scheduleconsent;
    }

    public boolean isRecordingconsent() {
        return recordingconsent;
    }

    public void setRecordingconsent(boolean recordingconsent) {
        this.recordingconsent = recordingconsent;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

// Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public LocalDate getCallDate() {
        return callDate;
    }

    public void setCallDate(LocalDate callDate) {
        this.callDate = callDate;
    }

    public LocalTime getCallTime() {
        return callTime;
    }

    public void setCallTime(LocalTime callTime) {
        this.callTime = callTime;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }
}
