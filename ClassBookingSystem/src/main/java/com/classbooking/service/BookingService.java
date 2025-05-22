package com.classbooking.service;

import java.sql.Connection;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;       
import java.sql.SQLException;
import java.sql.Statement; 
import java.sql.Timestamp;       
import java.time.LocalDateTime; 
import java.util.List;

import com.classbooking.dao.BookingDAO;
import com.classbooking.dao.ClassScheduleDAO;
import com.classbooking.dao.DBPool; 
import com.classbooking.model.Booking;
// import com.classbooking.model.ClassSchedule; // <<< REMOVED as not directly used as a type in this service's current methods

public class BookingService {
    private BookingDAO bookingDAO;
    private ClassScheduleDAO classScheduleDAO;

    public BookingService() {
        this.bookingDAO = new BookingDAO();
        this.classScheduleDAO = new ClassScheduleDAO();
    }

    public boolean placeBooking(int userId, int scheduleId) throws SQLException {
        Connection conn = null;
        boolean originalAutoCommitState = true;
        boolean bookingSuccessful = false;

        try {
            conn = DBPool.getConnection();
            originalAutoCommitState = conn.getAutoCommit();
            conn.setAutoCommit(false); 

            String checkSlotsSql = "SELECT capacity, booked_slots FROM class_schedules WHERE schedule_id = ? FOR UPDATE";
            int capacity = 0;
            int bookedSlotsNow = 0;
            try (PreparedStatement psCheck = conn.prepareStatement(checkSlotsSql)) {
                psCheck.setInt(1, scheduleId);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next()) {
                        capacity = rs.getInt("capacity");
                        bookedSlotsNow = rs.getInt("booked_slots");
                    } else {
                        throw new SQLException("Schedule ID " + scheduleId + " not found.");
                    }
                }
            }

            if (bookedSlotsNow >= capacity) {
                throw new SQLException("No available slots for schedule ID " + scheduleId + ".");
            }

            boolean slotIncremented = classScheduleDAO.incrementBookedSlots(scheduleId, conn);
            if (!slotIncremented) {
                throw new SQLException("Failed to increment booked slot for schedule ID " + scheduleId + ". Slot might have been taken.");
            }

            Booking newBooking = new Booking(userId, scheduleId);
            newBooking.setBookingTime(Timestamp.valueOf(LocalDateTime.now())); 
            newBooking.setStatus("CONFIRMED");
            
            int bookingId = bookingDAO.createBooking(newBooking, conn); 
            if (bookingId == -1) {
                throw new SQLException("Failed to create booking record for schedule ID " + scheduleId + ".");
            }

            conn.commit(); 
            bookingSuccessful = true;
            System.out.println("Booking successful for user " + userId + ", schedule " + scheduleId + ". Booking ID: " + bookingId);

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    System.err.println("Transaction rolling back for booking by user " + userId + " for schedule " + scheduleId + " due to: " + e.getMessage());
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during booking rollback: " + ex.getMessage());
                }
            }
            throw e; 
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommitState); 
                } catch (SQLException ex) {
                    System.err.println("Error resetting auto-commit: " + ex.getMessage());
                }
                try {
                    conn.close(); 
                } catch (SQLException ex) {
                    System.err.println("Error closing connection: " + ex.getMessage());
                }
            }
        }
        return bookingSuccessful;
    }

    public List<Booking> getUserBookingHistory(int userId) {
        return bookingDAO.getBookingsByUserId(userId);
    }

    public List<Booking> getAllBookingsForAdmin() {
        return bookingDAO.getAllBookings();
    }

    public boolean cancelUserBooking(int bookingId, int userId) throws SQLException {
        // Booking bookingToCancel = null; 
        Connection conn = null;
        boolean originalAutoCommitState = true;
        // boolean cancellationSuccessful = false; 
        try {
            conn = DBPool.getConnection();
            originalAutoCommitState = conn.getAutoCommit();
            conn.setAutoCommit(false);

            throw new UnsupportedOperationException("Cancellation logic needs BookingDAO.getScheduleIdForBooking() or similar to be fully implemented.");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error during cancellation rollback: " + ex.getMessage());
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(originalAutoCommitState);
                } catch (SQLException ex) { /* log */ }
                try {
                    conn.close();
                } catch (SQLException ex) { /* log */ }
            }
        }
        // return cancellationSuccessful; 
    }
}
