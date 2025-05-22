package com.classbooking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp; 
import java.time.LocalDateTime; // <<< This IS used for conversions
import java.util.ArrayList;
import java.util.List;

import com.classbooking.model.ClassInfo; 
import com.classbooking.model.ClassSchedule; 

public class ClassScheduleDAO {

    public boolean addSchedule(ClassSchedule schedule) {
        String sql = "INSERT INTO class_schedules (class_id, start_time, end_time, location, capacity, booked_slots) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, schedule.getClassId());
            ps.setTimestamp(2, Timestamp.valueOf(schedule.getStartTime())); 
            ps.setTimestamp(3, Timestamp.valueOf(schedule.getEndTime()));   
            ps.setString(4, schedule.getLocation());
            ps.setInt(5, schedule.getCapacity());
            ps.setInt(6, schedule.getBookedSlots()); 

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        schedule.setScheduleId(generatedKeys.getInt(1)); 
                    }
                }
                return true;
            }
            return false;

        } catch (SQLException e) {
            System.err.println("Error adding class schedule for class_id: " + schedule.getClassId());
            e.printStackTrace();
            return false;
        }
    }

    public ClassSchedule getScheduleById(int scheduleId) {
        String sql = "SELECT schedule_id, class_id, start_time, end_time, location, capacity, booked_slots FROM class_schedules WHERE schedule_id = ?";
        ClassSchedule schedule = null;

        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, scheduleId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    schedule = new ClassSchedule();
                    schedule.setScheduleId(rs.getInt("schedule_id"));
                    schedule.setClassId(rs.getInt("class_id"));
                    schedule.setStartTime(rs.getTimestamp("start_time").toLocalDateTime()); 
                    schedule.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());   
                    schedule.setLocation(rs.getString("location"));
                    schedule.setCapacity(rs.getInt("capacity"));
                    schedule.setBookedSlots(rs.getInt("booked_slots"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching schedule by ID: " + scheduleId);
            e.printStackTrace();
        }
        return schedule;
    }

    public List<ClassSchedule> getSchedulesByClassId(int classId) {
        List<ClassSchedule> schedules = new ArrayList<>();
        String sql = "SELECT schedule_id, class_id, start_time, end_time, location, capacity, booked_slots " +
                        "FROM class_schedules WHERE class_id = ? ORDER BY start_time";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ClassSchedule schedule = new ClassSchedule();
                    schedule.setScheduleId(rs.getInt("schedule_id"));
                    schedule.setClassId(rs.getInt("class_id"));
                    schedule.setStartTime(rs.getTimestamp("start_time").toLocalDateTime()); 
                    schedule.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());   
                    schedule.setLocation(rs.getString("location"));
                    schedule.setCapacity(rs.getInt("capacity"));
                    schedule.setBookedSlots(rs.getInt("booked_slots"));
                    schedules.add(schedule);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching schedules for class ID: " + classId);
            e.printStackTrace();
        }
        return schedules;
    }

    public List<ClassSchedule> getAllAvailableSchedulesWithClassTitle() {
        List<ClassSchedule> schedules = new ArrayList<>();
        String sql = "SELECT cs.schedule_id, cs.class_id, cs.start_time, cs.end_time, cs.location, cs.capacity, cs.booked_slots, " +
                        "c.title AS class_title, c.description AS class_description, c.instructor_name AS class_instructor, " +
                        "c.duration_hours AS class_duration, c.price AS class_price, c.image_url AS class_image_url " +
                        "FROM class_schedules cs " +
                        "JOIN classes c ON cs.class_id = c.class_id " +
                        "WHERE cs.start_time > NOW() AND cs.booked_slots < cs.capacity " +
                        "ORDER BY cs.start_time";

        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassId(rs.getInt("class_id"));
                classInfo.setTitle(rs.getString("class_title"));
                classInfo.setDescription(rs.getString("class_description"));
                classInfo.setInstructorName(rs.getString("class_instructor"));
                classInfo.setDurationHours(rs.getDouble("class_duration"));
                classInfo.setPrice(rs.getDouble("class_price"));
                classInfo.setImageUrl(rs.getString("class_image_url"));

                ClassSchedule schedule = new ClassSchedule();
                schedule.setScheduleId(rs.getInt("schedule_id"));
                schedule.setClassId(rs.getInt("class_id"));
                schedule.setStartTime(rs.getTimestamp("start_time").toLocalDateTime()); 
                schedule.setEndTime(rs.getTimestamp("end_time").toLocalDateTime());   
                schedule.setLocation(rs.getString("location"));
                schedule.setCapacity(rs.getInt("capacity"));
                schedule.setBookedSlots(rs.getInt("booked_slots"));
                schedule.setClassInfo(classInfo); 

                schedules.add(schedule);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all available schedules with class titles");
            e.printStackTrace();
        }
        return schedules;
    }

    public boolean updateSchedule(ClassSchedule schedule) {
        String sql = "UPDATE class_schedules SET class_id = ?, start_time = ?, end_time = ?, location = ?, capacity = ?, booked_slots = ? WHERE schedule_id = ?";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, schedule.getClassId());
            ps.setTimestamp(2, Timestamp.valueOf(schedule.getStartTime())); 
            ps.setTimestamp(3, Timestamp.valueOf(schedule.getEndTime()));   
            ps.setString(4, schedule.getLocation());
            ps.setInt(5, schedule.getCapacity());
            ps.setInt(6, schedule.getBookedSlots());
            ps.setInt(7, schedule.getScheduleId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating schedule ID: " + schedule.getScheduleId());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSchedule(int scheduleId) {
        String sql = "DELETE FROM class_schedules WHERE schedule_id = ?";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting schedule ID: " + scheduleId);
            e.printStackTrace();
            return false;
        }
    }

    public boolean incrementBookedSlots(int scheduleId, Connection conn) throws SQLException {
        String sql = "UPDATE class_schedules SET booked_slots = booked_slots + 1 WHERE schedule_id = ? AND booked_slots < capacity";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            return ps.executeUpdate() > 0; 
        }
    }

    public boolean decrementBookedSlots(int scheduleId, Connection conn) throws SQLException {
        String sql = "UPDATE class_schedules SET booked_slots = booked_slots - 1 WHERE schedule_id = ? AND booked_slots > 0";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, scheduleId);
            return ps.executeUpdate() > 0;
        }
    }
}
