package com.classbooking.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
// No HttpSession needed for just listing if AuthenticationFilter handles auth
// import javax.servlet.http.HttpSession;

import com.classbooking.model.ClassInfo;
import com.classbooking.service.ClassService;

@WebServlet("/admin/manageClasses") // Note the /admin/ path
public class AdminClassServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ClassService classService;

    @Override
    public void init() throws ServletException {
        super.init();
        classService = new ClassService();
    }

    public AdminClassServlet() {
        super();
    }

    /**
     * Handles GET requests to display the list of all classes for admin.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Authentication and Authorization should be handled by AuthenticationFilter
        // for paths starting with "/admin/"

        String action = request.getParameter("action");
        if (action == null) {
            action = "list"; // Default action
        }

        try {
            switch (action) {
                case "list":
                    listAllClasses(request, response);
                    break;
                case "showAddForm":
                    showAddClassForm(request, response);
                    break;
                // case "showEditForm":
                //    showEditClassForm(request, response);
                //    break;
                // case "delete":
                //    deleteClass(request, response);
                //    break;
                default:
                    listAllClasses(request, response);
            }
        } catch (Exception e) {
            // Log and redirect to a generic error page for admin
            e.printStackTrace();
            request.setAttribute("adminErrorMessage", "An error occurred: " + e.getMessage());
            RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/errorAdmin.jsp"); // Create this error page
            dispatcher.forward(request, response);
        }
    }

    private void listAllClasses(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        List<ClassInfo> allClassInfos = classService.getAllClassInfos();
        request.setAttribute("allClasses", allClassInfos);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/listClasses.jsp"); // Create this JSP
        dispatcher.forward(request, response);
    }

    private void showAddClassForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Just forward to the JSP that contains the form for adding a new class
        RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/classForm.jsp"); // Create this JSP
        dispatcher.forward(request, response);
    }

    /**
     * Handles POST requests, e.g., for adding or updating a class.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Authentication and Authorization should be handled by AuthenticationFilter
        String action = request.getParameter("formAction"); // Assuming form uses a hidden field "formAction"

        // For now, let's stub this. We'll implement add/edit logic later.
        // Example for adding a class:
        // if ("addClass".equals(action)) {
        //     String title = request.getParameter("title");
        //     // ... get other parameters ...
        //     boolean success = classService.addClass(title, ...);
        //     if (success) {
        //         response.sendRedirect(request.getContextPath() + "/admin/manageClasses?message=Class+added+successfully");
        //     } else {
        //         request.setAttribute("errorMessage", "Failed to add class.");
        //         showAddClassForm(request, response); // Show form again with error
        //     }
        // } else if ("updateClass".equals(action)) {
        //     // ... logic for updating ...
        // } else {
        //     response.sendRedirect(request.getContextPath() + "/admin/manageClasses?error=Unknown+action");
        // }

        // For now, just redirect back to the list after any POST
         response.sendRedirect(request.getContextPath() + "/admin/manageClasses?message=Action+Processed+(POST)");
    }
}