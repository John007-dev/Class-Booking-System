package com.classbooking.web;

import java.io.IOException;
import javax.servlet.RequestDispatcher; // Keep for doGet if used
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classbooking.model.User; 
import com.classbooking.service.UserService; 

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;

    @Override
    public void init() throws ServletException {
        super.init();
        userService = new UserService();
    }

    public LoginServlet() {
        super();
    }

    /**
     * Displays the login page.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
        dispatcher.forward(request, response);
    }

    /**
     * Processes the login form submission.
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String username = request.getParameter("username");
        String password = request.getParameter("password"); 
        String rememberMe = request.getParameter("rememberMe"); 

        User user = userService.loginUser(username, password); 

        if (user != null) {
            // Login successful
            HttpSession session = request.getSession(); 
            session.setAttribute("loggedInUser", user); 
            session.setMaxInactiveInterval(30 * 60); 

            // Set a success flash message
            session.setAttribute("successMessage", "Login successful! Welcome back, " + (user.getFullName() != null && !user.getFullName().trim().isEmpty() ? user.getFullName() : user.getUsername()) + ".");

            if ("on".equals(rememberMe)) {
                Cookie userCookie = new Cookie("rememberUser", username); // Storing username in cookie
                userCookie.setMaxAge(7 * 24 * 60 * 60); 
                userCookie.setPath(request.getContextPath() + "/"); 
                response.addCookie(userCookie);
                System.out.println("LoginServlet: 'rememberUser' cookie CREATED for user: " + username);
            } else {
                Cookie userCookie = new Cookie("rememberUser", null); 
                userCookie.setMaxAge(0); 
                userCookie.setPath(request.getContextPath() + "/");
                response.addCookie(userCookie);
                System.out.println("LoginServlet: 'rememberUser' cookie instructed to be REMOVED (if it existed).");
            }
            
            // Redirect to a main page after successful login
            // Check if there was a redirectUrl stored by AuthenticationFilter
            String redirectUrl = (String) session.getAttribute("redirectUrl");
            if (redirectUrl != null && !redirectUrl.isEmpty()) {
                session.removeAttribute("redirectUrl"); // Clear it after use
                response.sendRedirect(redirectUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/classList"); 
            }
        } else {
            // Login failed
            request.setAttribute("errorMessage", "Invalid username or password.");
            // Pass back username to repopulate form
            request.setAttribute("username", username); 
            RequestDispatcher dispatcher = request.getRequestDispatcher("/login.jsp");
            dispatcher.forward(request, response);
        }
    }
}
