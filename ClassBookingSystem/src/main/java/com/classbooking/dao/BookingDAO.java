package com.classbooking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.classbooking.model.Booking;
import com.classbooking.model.User;        // For populating userInfo in Booking
import com.classbooking.model.ClassInfo;   // For populating classDetails in Booking
import com.classbooking.model.ClassSchedule; // For populating scheduleInfo in Booking

public class BookingDAO {

    /**
        * Creates a new booking record in the database.
        * This method is designed to be called within an existing transaction,
        * so it takes a Connection object as a parameter and does NOT close it.
        * @param booking The Booking object to persist (user_id and schedule_id must be set).
        * @param conn The existing database connection (part of a transaction).
        * @return The auto-generated booking_id if successful, -1 otherwise.
        * @throws SQLException if a database access error occurs.
        */
    public int createBooking(Booking booking, Connection conn) throws SQLException {
        String sql = "INSERT INTO bookings (user_id, schedule_id, booking_time, status) VALUES (?, ?, ?, ?)";
        int generatedBookingId = -1;

        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, booking.getUserId());
            ps.setInt(2, booking.getScheduleId());
            // Set booking_time; if your Booking object doesn't set it, use current time
            if (booking.getBookingTime() != null) {
                ps.setTimestamp(3, booking.getBookingTime());
            } else {
                ps.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            }
            // Set status; if your Booking object doesn't set it, use a default
            if (booking.getStatus() != null) {
                ps.setString(4, booking.getStatus());
            } else {
                ps.setString(4, "CONFIRMED"); // Default status
            }

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedBookingId = generatedKeys.getInt(1);
                        booking.setBookingId(generatedBookingId); // Set ID back on the object
                    }
                }
            }
        }
        // No catch here for SQLException, let it propagate to be handled by the
        // transactional method in OrderService (which will trigger a rollback).
        // Connection is not closed here as it's managed by the calling transactional method.
        return generatedBookingId;
    }

    /**
        * Retrieves all bookings made by a specific user.
        * Also fetches related ClassSchedule and ClassInfo details.
        * @param userId The ID of the user.
        * @return A list of Booking objects with populated details.
        */
    public List<Booking> getBookingsByUserId(int userId) {
        List<Booking> bookings = new ArrayList<>();
        // SQL to join bookings with class_schedules and classes
        String sql = "SELECT b.booking_id, b.user_id, b.schedule_id, b.booking_time, b.status, " +
                        "cs.start_time, cs.end_time, cs.location, cs.capacity, cs.booked_slots, " +
                        "c.class_id, c.title AS class_title, c.instructor_name, c.price AS class_price " +
                        "FROM bookings b " +
                        "JOIN class_schedules cs ON b.schedule_id = cs.schedule_id " +
                        "JOIN classes c ON cs.class_id = c.class_id " +
                        "WHERE b.user_id = ? " +
                        "ORDER BY b.booking_time DESC";

        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setBookingId(rs.getInt("booking_id"));
                    booking.setUserId(rs.getInt("user_id"));
                    booking.setScheduleId(rs.getInt("schedule_id"));
                    booking.setBookingTime(rs.getTimestamp("booking_time"));
                    booking.setStatus(rs.getString("status"));

                    ClassInfo classInfo = new ClassInfo();
                    classInfo.setClassId(rs.getInt("class_id"));
                    classInfo.setTitle(rs.getString("class_title"));
                    classInfo.setInstructorName(rs.getString("instructor_name"));
                    classInfo.setPrice(rs.getDouble("class_price"));
                    // Populate other ClassInfo fields if needed

                    ClassSchedule schedule = new ClassSchedule();
                    schedule.setScheduleId(rs.getInt("schedule_id"));
                    schedule.setClassId(rs.getInt("class_id"));
                    schedule.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                    schedule.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());
                    schedule.setLocation(rs.getString("location"));
                    schedule.setCapacity(rs.getInt("capacity"));
                    schedule.setBookedSlots(rs.getInt("booked_slots"));
                    schedule.setClassInfo(classInfo); // Nest ClassInfo in ClassSchedule

                    booking.setScheduleInfo(schedule); // Nest ClassSchedule (with ClassInfo) in Booking
                    // booking.setClassDetails(classInfo); // Or set ClassInfo directly if preferred

                    bookings.add(booking);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching bookings for user ID: " + userId);
            e.printStackTrace();
        }
        return bookings;
    }

    /**
        * Retrieves all bookings (for administrator view).
        * Populates User, ClassSchedule, and ClassInfo details.
        * @return A list of all Booking objects.
        */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.booking_id, b.user_id, b.schedule_id, b.booking_time, b.status, " +
                        "u.username, u.full_name AS user_full_name, " +
                        "cs.start_time, cs.location, " +
                        "c.title AS class_title " +
                        "FROM bookings b " +
                        "JOIN users u ON b.user_id = u.user_id " +
                        "JOIN class_schedules cs ON b.schedule_id = cs.schedule_id " +
                        "JOIN classes c ON cs.class_id = c.class_id " +
                        "ORDER BY b.booking_time DESC";

        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("user_full_name"));

                ClassInfo classInfo = new ClassInfo();
                classInfo.setTitle(rs.getString("class_title"));
                // Populate other ClassInfo fields if needed

                ClassSchedule schedule = new ClassSchedule();
                schedule.setScheduleId(rs.getInt("schedule_id"));
                schedule.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
                schedule.setLocation(rs.getString("location"));
                schedule.setClassInfo(classInfo);

                Booking booking = new Booking();
                booking.setBookingId(rs.getInt("booking_id"));
                booking.setUserId(rs.getInt("user_id"));
                booking.setScheduleId(rs.getInt("schedule_id"));
                booking.setBookingTime(rs.getTimestamp("booking_time"));
                booking.setStatus(rs.getString("status"));
                
                booking.setUserInfo(user);
                booking.setScheduleInfo(schedule);
                // booking.setClassDetails(classInfo); // Redundant if ClassInfo is in ScheduleInfo

                bookings.add(booking);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all bookings");
            e.printStackTrace();
        }
        return bookings;
    }

    /**
        * Updates the status of a booking (e.g., to 'CANCELLED_BY_USER').
        * This method is designed to be called within an existing transaction,
        * so it takes a Connection object as a parameter and does NOT close it.
        * @param bookingId The ID of the booking to update.
        * @param newStatus The new status string.
        * @param conn The existing database connection (part of a transaction).
        * @return true if successful, false otherwise.
        * @throws SQLException if a database access error occurs.
        */
    public boolean updateBookingStatus(int bookingId, String newStatus, Connection conn) throws SQLException {
        String sql = "UPDATE bookings SET status = ? WHERE booking_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        }
        // No catch here, let SQLException propagate
    }

    // You might add other methods like:
    // - Booking getBookingById(int bookingId)
}
