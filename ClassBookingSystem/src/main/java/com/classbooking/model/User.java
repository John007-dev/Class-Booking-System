    package com.classbooking.model;

    import java.sql.Timestamp; // For createdAt field

    public class User {
        private int userId;
        private String username;
        private String password; // Should store a hashed password in a real application
        private String email;
        private String fullName;
        private boolean isAdmin;
        private Timestamp createdAt;

        // Default constructor
        public User() {
        }

        // Constructor with all fields (except possibly auto-generated ones like userId, createdAt)
        public User(String username, String password, String email, String fullName, boolean isAdmin) {
            this.username = username;
            this.password = password;
            this.email = email;
            this.fullName = fullName;
            this.isAdmin = isAdmin;
        }
        
        // Constructor often used when retrieving from DB (includes ID and createdAt)
        public User(int userId, String username, String password, String email, String fullName, boolean isAdmin, Timestamp createdAt) {
            this.userId = userId;
            this.username = username;
            this.password = password;
            this.email = email;
            this.fullName = fullName;
            this.isAdmin = isAdmin;
            this.createdAt = createdAt;
        }


        // Getters and Setters for all fields
        // You can auto-generate these in Eclipse: Right-click in editor -> Source -> Generate Getters and Setters...

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public boolean isAdmin() { // Getter for boolean is often "isPropertyName"
            return isAdmin;
        }

        public void setAdmin(boolean isAdmin) {
            this.isAdmin = isAdmin;
        }

        public Timestamp getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Timestamp createdAt) {
            this.createdAt = createdAt;
        }

        // Optional: toString() method for debugging
        @Override
        public String toString() {
            return "User{" +
                   "userId=" + userId +
                   ", username='" + username + '\'' +
                   ", email='" + email + '\'' +
                   ", fullName='" + fullName + '\'' +
                   ", isAdmin=" + isAdmin +
                   ", createdAt=" + createdAt +
                   '}';
        }
    }
    