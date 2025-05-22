package com.classbooking.model;

import java.time.LocalDateTime; // For modern date-time handling
import java.sql.Timestamp;       // For conversion for JSTL
// import java.time.ZoneId; // Needed if converting to java.util.Date directly

public class ClassSchedule {
    private int scheduleId;
    private int classId;        // Foreign key to ClassInfo
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String location;
    private int capacity;
    private int bookedSlots;

    private ClassInfo classInfo; 

    public ClassSchedule() {
    }

    public ClassSchedule(int classId, LocalDateTime startTime, LocalDateTime endTime, String location, int capacity) {
        this.classId = classId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.capacity = capacity;
        this.bookedSlots = 0; 
    }
    
    public ClassSchedule(int scheduleId, int classId, LocalDateTime startTime, LocalDateTime endTime, String location, int capacity, int bookedSlots) {
        this.scheduleId = scheduleId;
        this.classId = classId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.capacity = capacity;
        this.bookedSlots = bookedSlots;
    }

    // --- Original Getters and Setters ---
    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public int getClassId() { return classId; }
    public void setClassId(int classId) { this.classId = classId; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }
    public int getBookedSlots() { return bookedSlots; }
    public void setBookedSlots(int bookedSlots) { this.bookedSlots = bookedSlots; }
    public ClassInfo getClassInfo() { return classInfo; }
    public void setClassInfo(ClassInfo classInfo) { this.classInfo = classInfo; }
    public int getAvailableSlots() { return capacity - bookedSlots; }

    // --- NEW GETTERS for JSTL <fmt:formatDate> ---
    public Timestamp getStartTimeAsTimestamp() {
        return (this.startTime != null) ? Timestamp.valueOf(this.startTime) : null;
    }

    public Timestamp getEndTimeAsTimestamp() {
        return (this.endTime != null) ? Timestamp.valueOf(this.endTime) : null;
    }

    @Override
    public String toString() {
        return "ClassSchedule{" +
               "scheduleId=" + scheduleId +
               ", classId=" + classId +
               ", startTime=" + startTime +
               ", location='" + location + '\'' +
               ", availableSlots=" + getAvailableSlots() +
               '}';
    }
}
