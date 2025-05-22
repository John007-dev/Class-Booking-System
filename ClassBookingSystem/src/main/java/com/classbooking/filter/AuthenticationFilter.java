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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.classbooking.model.User;

// @WebFilter("/*") // <<< COMMENTED OUT or REMOVED
public class AuthenticationFilter implements Filter {

    private Set<String> publicUris;
    private Set<String> publicStartsWithUris;

    public AuthenticationFilter() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        publicUris = new HashSet<>(Arrays.asList(
            "/login.jsp", 
            "/register.jsp", 
            "/login",       
            "/register",    
            "/error.jsp",   
            "/add_advanced_order.jsp", 
            "/orderSuccess.jsp",       
            "/orderError.jsp",
            "/classList" 
        ));
        publicStartsWithUris = new HashSet<>(Arrays.asList(
            "/css/",      
            "/js/",       
            "/images/",   
            "/fonts/"     
        ));
        System.out.println("AuthenticationFilter: Initialized with public URIs via " + (filterConfig.getFilterName() != null ? "web.xml" : "annotation"));
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false); 

        String requestURI = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath(); 
        String pathWithinApp = requestURI.substring(contextPath.length()); 

        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        
        boolean isPublicResource = publicUris.contains(pathWithinApp);
        if (!isPublicResource) {
            for (String publicStartPath : publicStartsWithUris) {
                if (pathWithinApp.startsWith(publicStartPath)) {
                    isPublicResource = true;
                    break;
                }
            }
        }
        
        // System.out.println("AuthenticationFilter: Accessing URI: " + pathWithinApp + " | LoggedIn: " + isLoggedIn + " | IsPublic: " + isPublicResource);

        if (isLoggedIn || isPublicResource) {
            if (pathWithinApp.startsWith("/admin/") && isLoggedIn) {
                User user = (User) session.getAttribute("loggedInUser");
                if (user != null && user.isAdmin()) {
                    chain.doFilter(request, response); 
                } else {
                    System.err.println("AuthenticationFilter: Access DENIED for user '" + (user != null ? user.getUsername() : "Unknown") + "' to admin resource: " + pathWithinApp);
                    httpResponse.sendRedirect(contextPath + "/error.jsp?message=Access+Denied.+Admin+privileges+required.");
                }
            } else {
                chain.doFilter(request, response); 
            }
        } else {
            System.out.println("AuthenticationFilter: User not logged in. Redirecting to login page from URI: " + pathWithinApp);
            
            HttpSession newSessionForRedirect = httpRequest.getSession(); 
            String queryString = httpRequest.getQueryString(); 
            String originalURL = requestURI + (queryString == null ? "" : "?" + queryString); 
            newSessionForRedirect.setAttribute("redirectUrl", originalURL);
            
            httpResponse.sendRedirect(contextPath + "/login.jsp?message=Please+login+to+access+this+page.");
        }
    }

    @Override
    public void destroy() {
        System.out.println("AuthenticationFilter: Destroyed.");
    }
}