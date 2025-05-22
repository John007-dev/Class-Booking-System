package com.classbooking.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
// import javax.servlet.annotation.WebFilter; // <<< COMMENTED OUT or REMOVED
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classbooking.model.User;
import com.classbooking.service.UserService; 

// @WebFilter(filterName = "AutoLoginFilter", urlPatterns = {"/*"}) // <<< COMMENTED OUT or REMOVED
public class AutoLoginFilter implements Filter {

    private UserService userService;
    private Set<String> excludedFromAutoLoginCheckPaths; 
    private Set<String> staticResourcePrefixes; 

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userService = new UserService(); 
        
        excludedFromAutoLoginCheckPaths = new HashSet<>(Arrays.asList(
            "/login.jsp", 
            "/register.jsp", 
            "/login",       
            "/register"    
        ));

        staticResourcePrefixes = new HashSet<>(Arrays.asList(
            "/css/",
            "/js/",
            "/images/",
            "/fonts/"
        ));
        System.out.println("AutoLoginFilter: Initialized via " + (filterConfig.getFilterName() != null ? "web.xml" : "annotation"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpSession session = httpRequest.getSession(false); 

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String pathWithinApp = requestURI.substring(contextPath.length());

        // System.out.println("AutoLoginFilter: Processing request for: " + pathWithinApp); 

        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        // System.out.println("AutoLoginFilter: isLoggedIn = " + isLoggedIn); 
        
        boolean isExcludedPath = excludedFromAutoLoginCheckPaths.contains(pathWithinApp);
        // System.out.println("AutoLoginFilter: isExcludedPath = " + isExcludedPath); 

        boolean isStaticResource = false;
        for (String prefix : staticResourcePrefixes) {
            if (pathWithinApp.startsWith(prefix)) {
                isStaticResource = true;
                break;
            }
        }
        // System.out.println("AutoLoginFilter: isStaticResource = " + isStaticResource); 

        if (!isLoggedIn && !isExcludedPath && !isStaticResource) {
            // System.out.println("AutoLoginFilter: Attempting cookie-based auto-login for: " + pathWithinApp); 
            Cookie[] cookies = httpRequest.getCookies();
            if (cookies != null && cookies.length > 0) {
                // System.out.println("AutoLoginFilter: Found " + cookies.length + " cookie(s)."); 
                boolean rememberUserCookieFound = false;
                for (Cookie cookie : cookies) {
                    // System.out.println("AutoLoginFilter: Checking cookie: " + cookie.getName() + "=" + cookie.getValue()); 
                    if ("rememberUser".equals(cookie.getName())) {
                        rememberUserCookieFound = true;
                        String usernameFromCookie = cookie.getValue();
                        if (usernameFromCookie != null && !usernameFromCookie.isEmpty()) {
                            // System.out.println("AutoLoginFilter: 'rememberUser' cookie found with value: " + usernameFromCookie); 
                            User user = userService.getUserByUsername(usernameFromCookie); 
                            
                            if (user != null) { 
                                session = httpRequest.getSession(); 
                                session.setAttribute("loggedInUser", user); 
                                session.setMaxInactiveInterval(30 * 60); 
                                System.out.println("AutoLoginFilter: Session RECREATED for user '" + usernameFromCookie + "' from cookie."); 
                            } else {
                                System.out.println("AutoLoginFilter: User '" + usernameFromCookie + "' from cookie NOT FOUND in DB. Removing cookie."); 
                                Cookie invalidCookie = new Cookie("rememberUser", null); 
                                invalidCookie.setMaxAge(0); 
                                invalidCookie.setPath(httpRequest.getContextPath() + "/"); 
                                if (response instanceof HttpServletResponse) { 
                                    ((HttpServletResponse) response).addCookie(invalidCookie);
                                }
                            }
                        } else {
                            // System.out.println("AutoLoginFilter: 'rememberUser' cookie found but value is empty/null."); 
                        }
                        break; 
                    }
                }
                // if (!rememberUserCookieFound) {
                //     System.out.println("AutoLoginFilter: 'rememberUser' cookie NOT found."); 
                // }
            } else {
                // System.out.println("AutoLoginFilter: No cookies found in request for " + pathWithinApp); 
            }
        } else {
            // if (isLoggedIn) System.out.println("AutoLoginFilter: User already logged in. Skipping auto-login for " + pathWithinApp); 
            // else System.out.println("AutoLoginFilter: Public, excluded, or static path. Skipping auto-login for " + pathWithinApp); 
        }

        chain.doFilter(request, response); 
    }

    @Override
    public void destroy() {
        System.out.println("AutoLoginFilter: Destroyed.");
    }
}