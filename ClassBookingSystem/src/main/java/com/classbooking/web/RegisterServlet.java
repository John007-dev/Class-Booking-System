package com.classbooking.web;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.classbooking.service.UserService; // Assuming UserService is in this package

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    public RegisterServlet() {
        super();
        this.userService = new UserService(); // Initialize UserService
    }

    /**
        * Handles the HTTP GET request.
        * Can be used to forward to the registration page if accessed directly via GET /register.
        */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Forward to the registration JSP page
        RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
        dispatcher.forward(request, response);
    }

    /**
        * Handles the HTTP POST request from the registration form.
        */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");

        // Basic Validation (more can be added in UserService)
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.isEmpty() ||
            fullName == null || fullName.trim().isEmpty()) {
            
            request.setAttribute("errorMessage", "All fields are required.");
            request.setAttribute("username", username); // Send back for repopulation
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
            dispatcher.forward(request, response);
            return;
        }

        if (!password.equals(confirmPassword)) {
            request.setAttribute("errorMessage", "Passwords do not match.");
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
            dispatcher.forward(request, response);
            return;
        }

        // Attempt to register the user using the UserService
        // UserService should handle checking for existing username/email and password hashing
        boolean isRegistered = userService.registerUser(username, password, email, fullName);

        if (isRegistered) {
            // Registration successful, set a success message and redirect to login page
            request.getSession().setAttribute("successMessage", "Registration successful! Please login.");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } else {
            // Registration failed (e.g., username/email already exists, or other service/DAO error)
            request.setAttribute("errorMessage", "Registration failed. Username or email might already be in use, or an internal error occurred.");
            // Send back submitted values to repopulate the form
            request.setAttribute("username", username);
            request.setAttribute("email", email);
            request.setAttribute("fullName", fullName);
            RequestDispatcher dispatcher = request.getRequestDispatcher("/register.jsp");
            dispatcher.forward(request, response);
        }
    }
}
