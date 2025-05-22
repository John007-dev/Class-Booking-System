<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Class Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <style>
        body { background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; min-height: 100vh; padding-top: 20px; padding-bottom: 20px; font-family: 'Segoe UI', sans-serif; }
        .login-container { background-color: #ffffff; padding: 2rem 2.5rem; border-radius: 0.75rem; box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1); width: 100%; max-width: 420px; }
        .login-container h2 { text-align: center; color: #333; margin-bottom: 1.5rem; font-weight: 600; }
        .form-floating label { padding-left: 0.5rem; }
        .form-floating > .form-control { height: calc(3.5rem + 2px); line-height: 1.25; }
        .form-check-label { font-weight: normal; }
        .btn-login { font-size: 1.1rem; padding: 0.75rem; font-weight: 500; }
        .error-message { font-size: 0.9rem; margin-bottom: 15px; text-align:center; /* Centered error text */ }
        .success-message { font-size: 0.9rem; margin-bottom: 15px; text-align:center; }
        .register-link { display: block; text-align: center; margin-top: 1.5rem; color: #007bff; }
        .register-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="login-container">
        <h2><i class="fas fa-sign-in-alt me-2"></i>User Login</h2>

        <c:if test="${not empty sessionScope.successMessage}">
            <%-- Using sessionScope for messages set before a redirect (e.g., after registration) --%>
            <div id="flashLoginSuccessMessage" class="alert alert-success success-message" role="alert">
                <i class="fas fa-check-circle me-1"></i> <c:out value="${sessionScope.successMessage}"/>
            </div>
            <c:remove var="successMessage" scope="session"/>
        </c:if>

        <c:if test="${not empty requestScope.errorMessage}">
            <%-- Using requestScope for messages set after a forward (e.g., failed login attempt) --%>
            <div id="flashLoginErrorMessage" class="alert alert-danger error-message" role="alert">
                <i class="fas fa-exclamation-triangle me-1"></i> <c:out value="${requestScope.errorMessage}"/>
            </div>
        </c:if>
        
        <%-- Display redirect message from AuthenticationFilter --%>
        <c:if test="${not empty param.message}">
             <div id="flashRedirectMessage" class="alert alert-info error-message" role="alert">
                <i class="fas fa-info-circle me-1"></i> <c:out value="${param.message}"/>
            </div>
        </c:if>


        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="username" name="username" placeholder="Your Username" value="<c:out value='${requestScope.username != null ? requestScope.username : param.username}'/>" required autofocus>
                <label for="username">Username</label>
            </div>
            <div class="form-floating mb-3">
                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                <label for="password">Password</label>
            </div>
            <div class="form-check mb-3">
                <input class="form-check-input" type="checkbox" id="rememberMe" name="rememberMe">
                <label class="form-check-label" for="rememberMe">Remember Me</label>
            </div>
            
            <button class="btn btn-primary w-100 btn-login" type="submit">Login</button>
        </form>
        <a href="${pageContext.request.contextPath}/register.jsp" class="register-link">Don't have an account? Register</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript">
        document.addEventListener('DOMContentLoaded', function() {
            function autoHideMessage(elementId, delay) {
                const messageElement = document.getElementById(elementId);
                if (messageElement && messageElement.classList.contains('show')) { // Check if it's a Bootstrap alert still visible
                    setTimeout(function() {
                        var alertInstance = bootstrap.Alert.getInstance(messageElement);
                        if (alertInstance) {
                            alertInstance.close();
                        } else {
                             // Fallback for non-Bootstrap elements or if getInstance fails
                            messageElement.style.transition = 'opacity 0.5s ease-out';
                            messageElement.style.opacity = '0';
                            setTimeout(() => { messageElement.style.display = 'none'; }, 500);
                        }
                    }, delay);
                } else if (messageElement) { // For non-Bootstrap alert elements that don't have 'show'
                     setTimeout(function() {
                        messageElement.style.transition = 'opacity 0.5s ease-out';
                        messageElement.style.opacity = '0';
                        setTimeout(() => { messageElement.style.display = 'none'; }, 500);
                    }, delay);
                }
            }
            autoHideMessage('flashLoginSuccessMessage', 5000); 
            autoHideMessage('flashLoginErrorMessage', 7000);
            autoHideMessage('flashRedirectMessage', 7000); // For messages from AuthenticationFilter redirect
        });
    </script>
</body>
</html>