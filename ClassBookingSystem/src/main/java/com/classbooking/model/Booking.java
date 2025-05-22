package com.classbooking.model;

import java.sql.Timestamp; // For bookingTime
// import java.time.LocalDateTime; // <<< REMOVED as Timestamp is used for bookingTime

public class Booking {
    private int bookingId;
    private int userId;
    private int scheduleId;
    private Timestamp bookingTime; 
    private String status; 

    // Optional: For displaying richer information in JSPs
    private User userInfo;
    private ClassSchedule scheduleInfo; 
    private ClassInfo classDetails; 


    public Booking() {
    }

    public Booking(int userId, int scheduleId) {
        this.userId = userId;
        this.scheduleId = scheduleId;
        // bookingTime and status might be set by DAO/DB or service
    }
    
    public Booking(int bookingId, int userId, int scheduleId, Timestamp bookingTime, String status) {
        this.bookingId = bookingId;
        this.userId = userId;
        this.scheduleId = scheduleId;
        this.bookingTime = bookingTime;
        this.status = status;
    }

    // --- Getters and Setters ---
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }

    public Timestamp getBookingTime() { return bookingTime; }
    public void setBookingTime(Timestamp bookingTime) { this.bookingTime = bookingTime; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public User getUserInfo() { return userInfo; }
    public void setUserInfo(User userInfo) { this.userInfo = userInfo; }

    public ClassSchedule getScheduleInfo() { return scheduleInfo; }
    public void setScheduleInfo(ClassSchedule scheduleInfo) { this.scheduleInfo = scheduleInfo; }
    
    public ClassInfo getClassDetails() { return classDetails; }
    public void setClassDetails(ClassInfo classDetails) { this.classDetails = classDetails; }

    @Override
    public String toString() {
        return "Booking{" +
               "bookingId=" + bookingId +
               ", userId=" + userId +
               ", scheduleId=" + scheduleId +
               ", bookingTime=" + bookingTime +
               ", status='" + status + '\'' +
               '}';
    }
}
