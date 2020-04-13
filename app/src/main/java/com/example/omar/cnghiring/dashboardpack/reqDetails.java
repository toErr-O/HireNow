package com.example.omar.cnghiring.dashboardpack;

import java.io.Serializable;

public class reqDetails implements Serializable {
    private Double sourceLat,sourceLong, destinationLat, destinationLong;
    private Double fair;
    private Long phoneNumber;
    private Double distance;
    private String duration;
    private String insertTime;

    public reqDetails() {

    }

    public reqDetails(Double sourceLat, Double sourceLong, Double destionationLat, Double destionationLong, Double fair, Long phoneNumber, Double distance, String duration, String insertTime) {
        this.sourceLat = sourceLat;
        this.sourceLong = sourceLong;
        this.destinationLat = destionationLat;
        this.destinationLong = destionationLong;
        this.fair = fair;
        this.phoneNumber = phoneNumber;
        this.distance = distance;
        this.duration = duration;
        this.insertTime = insertTime;
    }

    public Double getSourceLat() {
        return sourceLat;
    }

    public Double getSourceLong() {
        return sourceLong;
    }

    public Double getDestinationLat() {
        return destinationLat;
    }

    public Double getDestinationLong() {
        return destinationLong;
    }

    public Double getFair() {
        return fair;
    }

    public Long getPhoneNumber() {
        return phoneNumber;
    }

    public Double getDistance() {
        return distance;
    }

    public String getDuration() {
        return duration;
    }

    public String getInsertTime() {
        return insertTime;
    }

    public void setSourceLat(Double sourceLat) {
        this.sourceLat = sourceLat;
    }

    public void setSourceLong(Double sourceLong) {
        this.sourceLong = sourceLong;
    }

    public void setDestinationLat(Double destinationLat) {
        this.destinationLat = destinationLat;
    }

    public void setDestinationLong(Double destinationLong) {
        this.destinationLong = destinationLong;
    }

    public void setFair(Double fair) {
        this.fair = fair;
    }

    public void setPhoneNumber(Long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setInsertTime(String insertTime) {
        this.insertTime = insertTime;
    }
}
