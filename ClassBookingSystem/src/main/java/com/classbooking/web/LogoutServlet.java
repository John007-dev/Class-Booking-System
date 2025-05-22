package com.classbooking.web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/logout")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public LogoutServlet() {
        super();
    }

    /**
        * Handles GET requests to log the user out.
        * It invalidates the session and attempts to clear any "Remember Me" cookie.
        */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Invalidate the current session, if one exists.
        HttpSession session = request.getSession(false); // false = do not create new session if none exists
        if (session != null) {
            session.removeAttribute("loggedInUser"); // Remove the specific user object attribute
            session.invalidate(); // Invalidate all session data and the session itself
            System.out.println("LogoutServlet: Session invalidated successfully.");
        }

        // 2. Remove the "Remember Me" cookie (if your LoginServlet sets one named "rememberUser")
        // To delete a cookie, create a new cookie with the same name and path,
        // set its value to empty (or anything, value doesn't matter for deletion),
        // and set its maxAge to 0.
        Cookie rememberUserCookie = new Cookie("rememberUser", null); // Value can be null or empty
        rememberUserCookie.setMaxAge(0); // This tells the browser to delete the cookie immediately.
        // The path MUST match the path of the cookie you want to delete.
        // Ensure this matches the path used when the cookie was set in LoginServlet.
        rememberUserCookie.setPath(request.getContextPath() + "/"); 
        response.addCookie(rememberUserCookie);
        System.out.println("LogoutServlet: 'rememberUser' cookie removal instruction sent.");
        
        // 3. Redirect the user to the login page.
        // Optionally, set a success message for logout to be displayed on login.jsp
        // request.getSession().setAttribute("successMessage", "You have been logged out successfully.");
        // However, since the session is invalidated, a new session would be created for this message.
        // A query parameter is simpler if login.jsp is set up to display it.
        response.sendRedirect(request.getContextPath() + "/login.jsp?logoutSuccess=true");
    }
}
