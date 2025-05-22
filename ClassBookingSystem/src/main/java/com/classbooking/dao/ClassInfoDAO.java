package com.classbooking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement; // For Statement.RETURN_GENERATED_KEYS if needed for add
import java.util.ArrayList;
import java.util.List;

import com.classbooking.model.ClassInfo; // Import your ClassInfo model

public class ClassInfoDAO {

    /**
        * Adds a new class to the database.
        * @param classInfo The ClassInfo object containing class details.
        * @return true if the class was added successfully, false otherwise.
        * Can be modified to return the generated class_id.
        */
    public boolean addClass(ClassInfo classInfo) {
        String sql = "INSERT INTO classes (title, description, instructor_name, duration_hours, price, image_url) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, classInfo.getTitle());
            ps.setString(2, classInfo.getDescription());
            ps.setString(3, classInfo.getInstructorName());
            ps.setDouble(4, classInfo.getDurationHours());
            ps.setDouble(5, classInfo.getPrice());
            ps.setString(6, classInfo.getImageUrl());

            int rowsAffected = ps.executeUpdate();

            // Optionally, retrieve and set the generated class_id back to the object
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        classInfo.setClassId(generatedKeys.getInt(1));
                    }
                }
            }
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding class: " + classInfo.getTitle());
            e.printStackTrace();
            return false;
        }
    }

    /**
        * Retrieves a class from the database by its ID.
        * @param classId The ID of the class to retrieve.
        * @return ClassInfo object if found, null otherwise.
        */
    public ClassInfo getClassById(int classId) {
        String sql = "SELECT class_id, title, description, instructor_name, duration_hours, price, image_url FROM classes WHERE class_id = ?";
        ClassInfo classInfo = null;

        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    classInfo = new ClassInfo();
                    classInfo.setClassId(rs.getInt("class_id"));
                    classInfo.setTitle(rs.getString("title"));
                    classInfo.setDescription(rs.getString("description"));
                    classInfo.setInstructorName(rs.getString("instructor_name"));
                    classInfo.setDurationHours(rs.getDouble("duration_hours"));
                    classInfo.setPrice(rs.getDouble("price"));
                    classInfo.setImageUrl(rs.getString("image_url"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching class by ID: " + classId);
            e.printStackTrace();
        }
        return classInfo;
    }

    /**
        * Retrieves all classes from the database.
        * @return A list of all ClassInfo objects.
        */
    public List<ClassInfo> getAllClasses() {
        List<ClassInfo> classes = new ArrayList<>();
        String sql = "SELECT class_id, title, description, instructor_name, duration_hours, price, image_url FROM classes ORDER BY title";

        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ClassInfo classInfo = new ClassInfo();
                classInfo.setClassId(rs.getInt("class_id"));
                classInfo.setTitle(rs.getString("title"));
                classInfo.setDescription(rs.getString("description"));
                classInfo.setInstructorName(rs.getString("instructor_name"));
                classInfo.setDurationHours(rs.getDouble("duration_hours"));
                classInfo.setPrice(rs.getDouble("price"));
                classInfo.setImageUrl(rs.getString("image_url"));
                classes.add(classInfo);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all classes");
            e.printStackTrace();
        }
        return classes;
    }

    /**
        * Updates an existing class's information in the database.
        * @param classInfo The ClassInfo object with updated information (classId must be set).
        * @return true if the update was successful, false otherwise.
        */
    public boolean updateClass(ClassInfo classInfo) {
        String sql = "UPDATE classes SET title = ?, description = ?, instructor_name = ?, duration_hours = ?, price = ?, image_url = ? WHERE class_id = ?";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, classInfo.getTitle());
            ps.setString(2, classInfo.getDescription());
            ps.setString(3, classInfo.getInstructorName());
            ps.setDouble(4, classInfo.getDurationHours());
            ps.setDouble(5, classInfo.getPrice());
            ps.setString(6, classInfo.getImageUrl());
            ps.setInt(7, classInfo.getClassId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating class ID: " + classInfo.getClassId());
            e.printStackTrace();
            return false;
        }
    }

    /**
        * Deletes a class from the database by its ID.
        * (Note: ON DELETE CASCADE for fk_schedule_class in class_schedules table will delete related schedules)
        * @param classId The ID of the class to delete.
        * @return true if the deletion was successful, false otherwise.
        */
    public boolean deleteClass(int classId) {
        String sql = "DELETE FROM classes WHERE class_id = ?";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, classId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting class ID: " + classId);
            e.printStackTrace();
            return false;
        }
    }
}
