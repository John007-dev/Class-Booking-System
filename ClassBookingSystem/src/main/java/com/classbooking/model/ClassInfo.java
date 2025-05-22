package com.classbooking.model;

public class ClassInfo { // Renamed from Class to ClassInfo to avoid conflict with java.lang.Class
    private int classId;
    private String title;
    private String description;
    private String instructorName;
    private double durationHours;
    private double price;
    private String imageUrl;

    // Default constructor
    public ClassInfo() {
    }

    // Constructor for creating new classes (ID might be auto-generated)
    public ClassInfo(String title, String description, String instructorName, double durationHours, double price, String imageUrl) {
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.durationHours = durationHours;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    
    // Constructor for retrieving from DB (includes ID)
    public ClassInfo(int classId, String title, String description, String instructorName, double durationHours, double price, String imageUrl) {
        this.classId = classId;
        this.title = title;
        this.description = description;
        this.instructorName = instructorName;
        this.durationHours = durationHours;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    // Getters and Setters (generate in Eclipse or type them)

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public double getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(double durationHours) {
        this.durationHours = durationHours;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
               "classId=" + classId +
               ", title='" + title + '\'' +
               ", instructorName='" + instructorName + '\'' +
               ", price=" + price +
               '}';
    }
}
