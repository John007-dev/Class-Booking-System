    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order Failed - Class Booking System</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
        <style>
            body { font-family: 'Segoe UI', sans-serif; display: flex; align-items: center; justify-content: center; min-height: 100vh; background-color: #f0f2f5; }
            .status-container { text-align: center; background-color: #fff; padding: 40px; border-radius: 10px; box-shadow: 0 0.5rem 1rem rgba(0,0,0,.1); }
            .status-icon { font-size: 4rem; color: #dc3545; /* Bootstrap danger red */ margin-bottom: 20px; }
            .error-details { background-color: #f8d7da; color: #721c24; border: 1px solid #f5c6cb; padding: 15px; border-radius: 5px; margin-top: 20px; text-align: left; }
        </style>
    </head>
    <body>
        <div class="status-container">
            <i class="fas fa-times-circle status-icon"></i>
            <h1 class="display-5">Order Placement Failed</h1>
            
            <c:if test="${not empty requestScope.error}">
                <div class="error-details">
                    <strong>Details:</strong> <c:out value="${requestScope.error}"/>
                </div>
            </c:if>
            <c:if test="${empty requestScope.error}">
                <p>An unexpected error occurred. Please try again or contact support.</p>
            </c:if>
            <hr>
            <p class="mb-0">
                <a href="${pageContext.request.contextPath}/add_advanced_order.jsp" class="btn btn-warning"><i class="fas fa-redo-alt me-1"></i>Try Placing Order Again</a>
                <a href="${pageContext.request.contextPath}/classList" class="btn btn-outline-secondary ms-2"><i class="fas fa-calendar-alt me-1"></i>Browse Classes</a>
            </p>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    </html>
    