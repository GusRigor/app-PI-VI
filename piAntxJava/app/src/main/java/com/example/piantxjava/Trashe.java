package com.example.piantxjava;

public class Trashe {
    private int id;
    private String name;
    private double percentage;
    private String priority;
    private double latitude;
    private double longitude;
    private String last_update;

    public int getId() {
        return id;
    }
    public void setId(int id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public double getPercentage() {
        return percentage;
    }
    public void setPercentage(double percentage){
        this.percentage = percentage;
    }

    public String getPriority() {
        return priority;
    }
    public void setPriority(String priority){
        this.priority = priority;
    }

    public double getLatitude() {
        return latitude;
    }
    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }
    public void setLongitude(double longitude){
        this.longitude = longitude;
    }

    public String getLast_update() {
        return last_update;
    }
    public void setLast_update(String last_update){
        this.last_update = last_update;
    }


}
