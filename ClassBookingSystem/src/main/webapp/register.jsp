<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register - Class Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <style>
        body { background-color: #f0f2f5; display: flex; justify-content: center; align-items: center; min-height: 100vh; padding-top: 20px; padding-bottom: 20px; font-family: 'Segoe UI', sans-serif; }
        .register-container { background-color: #ffffff; padding: 2rem 2.5rem; border-radius: 0.75rem; box-shadow: 0 8px 25px rgba(0, 0, 0, 0.1); width: 100%; max-width: 450px; }
        .register-container h2 { text-align: center; color: #333; margin-bottom: 1.5rem; font-weight: 600; }
        .form-floating label { padding-left: 0.5rem; }
        .form-floating > .form-control { height: calc(3.5rem + 2px); line-height: 1.25; }
        .btn-register { font-size: 1.1rem; padding: 0.75rem; font-weight: 500; }
        .error-message { font-size: 0.9rem; margin-bottom:15px; /* Added margin-bottom */ text-align:center; }
        .login-link { display: block; text-align: center; margin-top: 1.5rem; color: #007bff; }
        .login-link:hover { text-decoration: underline; }
    </style>
</head>
<body>
    <div class="register-container">
        <h2><i class="fas fa-user-plus me-2"></i>Create Your Account</h2>

        <c:if test="${not empty requestScope.errorMessage}">
            <div id="flashRegisterError" class="alert alert-danger error-message" role="alert">
                <i class="fas fa-exclamation-triangle me-1"></i> <c:out value="${requestScope.errorMessage}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/register" method="post">
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="fullName" name="fullName" placeholder="Your Full Name" value="<c:out value='${requestScope.fullName}'/>" required autofocus>
                <label for="fullName">Full Name</label>
            </div>
            <div class="form-floating mb-3">
                <input type="text" class="form-control" id="username" name="username" placeholder="Choose a Username" value="<c:out value='${requestScope.username}'/>" required>
                <label for="username">Username</label>
            </div>
            <div class="form-floating mb-3">
                <input type="email" class="form-control" id="email" name="email" placeholder="name@example.com" value="<c:out value='${requestScope.email}'/>" required>
                <label for="email">Email address</label>
            </div>
            <div class="form-floating mb-3">
                <input type="password" class="form-control" id="password" name="password" placeholder="Password" required>
                <label for="password">Password</label>
            </div>
            <div class="form-floating mb-4">
                <input type="password" class="form-control" id="confirmPassword" name="confirmPassword" placeholder="Confirm Password" required>
                <label for="confirmPassword">Confirm Password</label>
            </div>
            
            <button class="btn btn-primary w-100 btn-register" type="submit">Register</button>
        </form>
        <a href="${pageContext.request.contextPath}/login.jsp" class="login-link">Already have an account? Login</a>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript">
        document.addEventListener('DOMContentLoaded', function() {
            function autoHideMessage(elementId, delay) {
                const messageElement = document.getElementById(elementId);
                if (messageElement && messageElement.classList.contains('show')) {
                    setTimeout(function() {
                        var alertInstance = bootstrap.Alert.getInstance(messageElement);
                        if (alertInstance) {
                            alertInstance.close();
                        } else {
                            messageElement.style.transition = 'opacity 0.5s ease-out';
                            messageElement.style.opacity = '0';
                            setTimeout(() => { messageElement.style.display = 'none'; }, 500);
                        }
                    }, delay);
                } else if (messageElement) { 
                     setTimeout(function() {
                        messageElement.style.transition = 'opacity 0.5s ease-out';
                        messageElement.style.opacity = '0';
                        setTimeout(() => { messageElement.style.display = 'none'; }, 500);
                    }, delay);
                }
            }
            autoHideMessage('flashRegisterError', 7000); // Registration errors hide after 7 seconds
        });
    </script>
</body>
</html>