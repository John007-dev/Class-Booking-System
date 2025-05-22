package com.classbooking.web;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classbooking.model.Booking; // Your Booking model
import com.classbooking.model.User;    // Your User model
import com.classbooking.service.BookingService; // Your BookingService

@WebServlet("/myBookings")
public class MyBookingsServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingService bookingService;

    @Override
    public void init() throws ServletException {
        super.init();
        // Initialize services in init() method for better practice
        bookingService = new BookingService();
    }

    public MyBookingsServlet() {
        super();
    }

    /**
        * Handles GET requests to display the current user's booking history.
        */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false); // Get existing session, don't create new

        // Check if user is logged in
        if (session == null || session.getAttribute("loggedInUser") == null) {
            // User is not logged in, redirect to login page with a message
            // Optionally, store the intended destination to redirect back after login
            response.sendRedirect(request.getContextPath() + "/login.jsp?errorMessage=Please+login+to+view+your+bookings.");
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        int userId = loggedInUser.getUserId();
        List<Booking> userBookings = Collections.emptyList(); // Default to empty list

        try {
            // Fetch booking history for the logged-in user
            userBookings = bookingService.getUserBookingHistory(userId);
            request.setAttribute("userBookings", userBookings); // Set bookings for the JSP

        } catch (Exception e) { 
            // Catch broader exceptions from service/DAO if not specifically SQLException
            // For example, if BookingService methods don't declare SQLException but might throw runtime ones
            e.printStackTrace(); // Log the error for server-side diagnosis
            request.setAttribute("pageErrorMessage", "Error retrieving your booking history: " + e.getMessage());
            // Still forward to myBookings.jsp, which can display this error
        }
        
        // Forward to the JSP page that will display the booking history
        RequestDispatcher dispatcher = request.getRequestDispatcher("/myBookings.jsp"); 
        dispatcher.forward(request, response);
    }
}
