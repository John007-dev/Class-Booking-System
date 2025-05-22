    <%@ page contentType="text/html;charset=UTF-8" language="java" %>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order Successful - Class Booking System</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
        <style>
            body { font-family: 'Segoe UI', sans-serif; display: flex; align-items: center; justify-content: center; min-height: 100vh; background-color: #f0f2f5; }
            .status-container { text-align: center; background-color: #fff; padding: 40px; border-radius: 10px; box-shadow: 0 0.5rem 1rem rgba(0,0,0,.1); }
            .status-icon { font-size: 4rem; color: #198754; /* Bootstrap success green */ margin-bottom: 20px; }
        </style>
    </head>
    <body>
        <div class="status-container">
            <i class="fas fa-check-circle status-icon"></i>
            <h1 class="display-5">Order Placed Successfully!</h1>
            <p class="lead">Thank you. Your class booking and any seat reservations have been confirmed.</p>
            <hr>
            <p class="mb-0">
                <a href="${pageContext.request.contextPath}/myBookings" class="btn btn-primary"><i class="fas fa-list-check me-1"></i>View My Bookings</a>
                <a href="${pageContext.request.contextPath}/classList" class="btn btn-outline-secondary ms-2"><i class="fas fa-calendar-alt me-1"></i>Browse More Classes</a>
            </p>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
    </html>
    