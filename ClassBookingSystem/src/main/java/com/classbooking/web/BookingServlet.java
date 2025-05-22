package com.classbooking.web;

import java.io.IOException;
import java.sql.SQLException;
// ArrayList and List are not directly used in this version of BookingServlet,
// but might be if you were handling multiple seatIds directly here.
// import java.util.ArrayList;
// import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classbooking.model.ClassSchedule;
import com.classbooking.model.User;
import com.classbooking.service.BookingService;
import com.classbooking.service.ClassService; // To fetch schedule details for confirmation

@WebServlet("/booking")
public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookingService bookingService;
    private ClassService classService; // To fetch schedule details

    @Override
    public void init() throws ServletException {
        super.init();
        bookingService = new BookingService();
        classService = new ClassService();
    }

    public BookingServlet() {
        super();
    }

    /**
        * Handles GET requests.
        * If action=prepare, it fetches schedule details and forwards to confirmBooking.jsp.
        */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);

        // All actions in this servlet require login
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?errorMessage=Please+login+to+manage+bookings.");
            return;
        }

        if ("prepare".equals(action)) {
            try {
                String scheduleIdStr = request.getParameter("scheduleId");
                if (scheduleIdStr == null || scheduleIdStr.trim().isEmpty()) {
                    session.setAttribute("errorMessage", "Schedule ID is missing for booking preparation.");
                    response.sendRedirect(request.getContextPath() + "/classList");
                    return;
                }
                int scheduleId = Integer.parseInt(scheduleIdStr);
                ClassSchedule scheduleToBook = classService.getScheduleDetailsById(scheduleId);

                if (scheduleToBook == null) {
                    session.setAttribute("errorMessage", "The selected class schedule (ID: " + scheduleId + ") could not be found.");
                    response.sendRedirect(request.getContextPath() + "/classList");
                    return;
                }
                
                if (scheduleToBook.getAvailableSlots() <= 0) {
                        session.setAttribute("errorMessage", "Sorry, the class '" + scheduleToBook.getClassInfo().getTitle() + "' at the selected time is fully booked.");
                        response.sendRedirect(request.getContextPath() + "/classList");
                        return;
                }

                request.setAttribute("scheduleToBook", scheduleToBook);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/confirmBooking.jsp"); // You will create this JSP
                dispatcher.forward(request, response);

            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid schedule ID format provided.");
                response.sendRedirect(request.getContextPath() + "/classList");
            } catch (Exception e) {
                e.printStackTrace(); // Log for server admin
                session.setAttribute("errorMessage", "Error preparing booking details: " + e.getMessage());
                response.sendRedirect(request.getContextPath() + "/classList"); // Redirect to a safe page
            }
        } else {
            // Default GET action if not "prepare" could be to redirect to class list or dashboard
            response.sendRedirect(request.getContextPath() + "/classList");
        }
    }

    /**
        * Handles POST requests, specifically for confirming and placing a booking
        * after user confirms on confirmBooking.jsp.
        */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect(request.getContextPath() + "/login.jsp?errorMessage=Your+session+expired.+Please+login+again+to+confirm+booking.");
            return;
        }

        User loggedInUser = (User) session.getAttribute("loggedInUser");
        int userId = loggedInUser.getUserId();
        
        String scheduleIdStr = request.getParameter("scheduleId"); // From hidden field in confirmBooking.jsp

        if (scheduleIdStr == null || scheduleIdStr.trim().isEmpty()) {
            session.setAttribute("errorMessage", "Booking failed: Schedule ID was missing from confirmation.");
            response.sendRedirect(request.getContextPath() + "/classList"); // Or back to confirmBooking with error
            return;
        }

        try {
            int scheduleId = Integer.parseInt(scheduleIdStr);

            // The BookingService.placeBooking method handles the transaction
            boolean bookingSuccess = bookingService.placeBooking(userId, scheduleId);

            if (bookingSuccess) {
                session.setAttribute("successMessage", "Booking confirmed successfully for schedule ID " + scheduleId + "!");
                response.sendRedirect(request.getContextPath() + "/myBookings"); // Redirect to user's booking history
            } else {
                // This case implies placeBooking returned false without throwing an SQLException,
                // which might indicate a business rule failure not caught by SQLException (e.g. race condition where slots filled up).
                session.setAttribute("errorMessage", "Booking could not be completed. The class may have just become fully booked or another issue occurred.");
                response.sendRedirect(request.getContextPath() + "/classList"); 
            }

        } catch (NumberFormatException e) {
            e.printStackTrace(); // Log error
            session.setAttribute("errorMessage", "Booking failed: Invalid schedule ID format during confirmation.");
            response.sendRedirect(request.getContextPath() + "/classList"); 
        } catch (SQLException e) {
            e.printStackTrace(); // Log error
            session.setAttribute("errorMessage", "Booking failed due to a database error: " + e.getMessage());
            // It's often better to forward to an error page or back to the confirmation form with the error
            // For simplicity here, redirecting to classList with a session message.
            // To show error on confirmBooking.jsp, you'd need to fetch scheduleToBook again and forward.
            response.sendRedirect(request.getContextPath() + "/classList"); 
        } catch (Exception e) { // Catch any other unexpected errors
            e.printStackTrace();
            session.setAttribute("errorMessage", "An unexpected error occurred during booking: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/classList");
        }
    }
}
