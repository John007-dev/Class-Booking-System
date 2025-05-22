package com.classbooking.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classbooking.model.ClassSchedule; // Your ClassSchedule model
import com.classbooking.service.ClassService;  // Your ClassService

@WebServlet("/classList") // This annotation maps this servlet to the /classList URL
public class ClassListServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClassService classService; // Service to fetch class data

    @Override
    public void init() throws ServletException {
        // Initialize services in init() method for better practice
        // It's called once when the servlet is first loaded.
        super.init();
        classService = new ClassService();
    }

    public ClassListServlet() {
        super();
        // Constructor - service initialization moved to init()
    }

    /**
     * Handles GET requests to display the list of available class schedules.
     * It also retrieves and clears any "flash messages" (like success messages
     * after login or registration) from the session to display them once on the JSP.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Handle potential flash messages from session (e.g., after login/registration redirect)
        HttpSession session = request.getSession(false); // Get existing session, don't create new
        if (session != null) {
            String successMessage = (String) session.getAttribute("successMessage");
            if (successMessage != null) {
                request.setAttribute("successMessage", successMessage); // Pass to request scope
                session.removeAttribute("successMessage"); // Remove from session to show only once
            }
            
            String errorMessage = (String) session.getAttribute("errorMessage");
            if (errorMessage != null) {
                request.setAttribute("errorMessage", errorMessage);
                session.removeAttribute("errorMessage");
            }
            // You could also check for sessionScope.loggedInUser here if you want to pass it
            // to the JSP explicitly, though JSTL can access sessionScope directly.
        }

        // Fetch the list of available class schedules (which includes ClassInfo details)
        // from the ClassService.
        List<ClassSchedule> availableSchedules = classService.getAvailableClassSchedulesWithDetails();

        // Set the list of schedules as a request attribute.
        // The JSP page (classList.jsp) will use this attribute to display the data.
        request.setAttribute("schedules", availableSchedules);

        // Forward the request (along with request attributes) to the JSP page
        // that will render the HTML for the class list.
        RequestDispatcher dispatcher = request.getRequestDispatcher("/classList.jsp"); // Path to your JSP
        dispatcher.forward(request, response);
    }

    // doPost is not implemented for this servlet in this version,
    // as it primarily serves to display a list via GET requests.
    // If you add features like searching/filtering the class list via a POST form
    // that submits to this URL, then you would implement doPost.
}
