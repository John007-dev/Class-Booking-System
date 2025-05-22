package com.classbooking.service;

import com.classbooking.dao.UserDAO;
import com.classbooking.model.User;
// For password hashing, you would typically import a library like BCrypt
// import org.mindrot.jbcrypt.BCrypt; // Example if using jBCrypt

public class UserService {
    private UserDAO userDAO;

    public UserService() {
        this.userDAO = new UserDAO(); // Instantiate the DAO
    }

    /**
     * Registers a new user.
     * In a real application, this method would handle password hashing.
     * @param username The username.
     * @param password The plain text password.
     * @param email The email address.
     * @param fullName The user's full name.
     * @return true if registration is successful, false otherwise (e.g., username or email already exists).
     */
    public boolean registerUser(String username, String password, String email, String fullName) {
        if (userDAO.getUserByUsername(username) != null) {
            System.err.println("Registration failed: Username '" + username + "' already exists.");
            return false; 
        }
        // Add email existence check if needed, e.g., if UserDAO has getUserByEmail()

        String storedPassword = password; // In a real app: BCrypt.hashpw(password, BCrypt.gensalt());

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(storedPassword); 
        newUser.setEmail(email);
        newUser.setFullName(fullName);
        newUser.setAdmin(false); 

        return userDAO.addUser(newUser);
    }

    /**
     * Authenticates a user.
     * Compares plain text password with stored (ideally hashed) password.
     * @param username The username.
     * @param plainTextPassword The plain text password entered by the user.
     * @return The User object if authentication is successful, null otherwise.
     */
    public User loginUser(String username, String plainTextPassword) {
        User user = userDAO.getUserByUsername(username);

        if (user != null) {
            // In a real app with hashing: if (BCrypt.checkpw(plainTextPassword, user.getPassword())) { return user; }
            if (plainTextPassword.equals(user.getPassword())) { // Plain text comparison for lab
                return user; 
            } else {
                System.err.println("Login failed: Incorrect password for user '" + username + "'.");
                return null; 
            }
        }
        System.err.println("Login failed: User '" + username + "' not found.");
        return null; 
    }

    /**
     * Retrieves a user by their username.
     * This is used by the AutoLoginFilter to validate a user from a cookie.
     * @param username The username to search for.
     * @return The User object if found, null otherwise.
     */
    public User getUserByUsername(String username) {
        return userDAO.getUserByUsername(username);
    }

    /**
     * Retrieves user details by user ID.
     * @param userId The ID of the user.
     * @return User object if found, null otherwise.
     */
    public User getUserDetails(int userId) {
        return userDAO.getUserById(userId);
    }

    /**
     * Updates a user's profile (e.g., email, full name).
     * @param user The User object with updated information.
     * @return true if successful, false otherwise.
     */
    public boolean updateUserProfile(User user) {
        return userDAO.updateUserProfile(user);
    }
}
