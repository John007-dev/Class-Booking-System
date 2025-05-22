package com.classbooking.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp; // <<< This IS used if User model has Timestamp and rs.getTimestamp is called

import com.classbooking.model.User; 

public class UserDAO {

    public boolean addUser(User user) {
        String sql = "INSERT INTO users (username, password, email, full_name, is_admin) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword()); 
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getFullName());
            ps.setBoolean(5, user.isAdmin());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding user: " + user.getUsername());
            e.printStackTrace(); 
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT user_id, username, password, email, full_name, is_admin, created_at FROM users WHERE username = ?";
        User user = null;

        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password")); 
                    user.setEmail(rs.getString("email"));
                    user.setFullName(rs.getString("full_name"));
                    user.setAdmin(rs.getBoolean("is_admin"));
                    user.setCreatedAt(rs.getTimestamp("created_at")); // <<< Usage of Timestamp
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by username: " + username);
            e.printStackTrace(); 
        }
        return user;
    }

    public User getUserById(int userId) {
        String sql = "SELECT user_id, username, password, email, full_name, is_admin, created_at FROM users WHERE user_id = ?";
        User user = null;

        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setUserId(rs.getInt("user_id"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setFullName(rs.getString("full_name"));
                    user.setAdmin(rs.getBoolean("is_admin"));
                    user.setCreatedAt(rs.getTimestamp("created_at")); // <<< Usage of Timestamp
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID: " + userId);
            e.printStackTrace();
        }
        return user;
    }
    
    public boolean updateUserProfile(User user) {
        // Example: Does not update username (usually fixed) or password (separate process)
        String sql = "UPDATE users SET email = ?, full_name = ?, is_admin = ? WHERE user_id = ?";
        try (Connection conn = DBPool.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getEmail());
            ps.setString(2, user.getFullName());
            ps.setBoolean(3, user.isAdmin());
            ps.setInt(4, user.getUserId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating user profile for ID: " + user.getUserId());
            e.printStackTrace();
            return false;
        }
    }
}
