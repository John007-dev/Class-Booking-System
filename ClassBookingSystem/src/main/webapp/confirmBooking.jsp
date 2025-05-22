<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> <%-- For formatting dates and numbers --%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Confirm Your Booking - Class Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-T3c6CoIi6uLrA9TneNEoa7RxnatzjcDSCmG1MXxSR1GAsXEV/Dwwykc2MPK8M2HN" crossorigin="anonymous">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css" integrity="sha512-xh6O/CkQoPOWDdYTDqeRdPCVd1SpvCA9XXcUnZS2FmJNp1coAFzvtCN9BmamE+4aHK8yyUHUSCcJHgXloTyT2A==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <style>
        body {
            background-color: #eef1f5; 
            padding-top: 80px; 
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; 
            display: flex;
            flex-direction: column;
            min-height: 100vh; 
        }
        .main-content {
            flex: 1; 
        }
        .navbar-brand-custom {
            font-weight: 600; 
            font-size: 1.5rem;
        }
        .page-title {
            color: #212529; 
            margin-bottom: 2rem;
            font-weight: 600;
        }
        .confirmation-container {
            max-width: 700px; 
            margin: 20px auto; 
            background-color: #fff; 
            padding: 30px;
            border-radius: 8px; 
            box-shadow: 0 0.5rem 1rem rgba(0,0,0,0.1); 
        }
        .details-list {
            font-size: 1.05em; 
        }
        .details-list .list-group-item {
            border-left: 0; 
            border-right: 0;
            padding-left: 0; 
            padding-right: 0;
            border-bottom: 1px solid #f0f0f0; 
        }
        .details-list .list-group-item:last-child {
            border-bottom: none; 
        }
        .details-list .list-group-item strong {
            min-width: 150px; 
            display: inline-block;
            color: #495057; 
        }
        .btn-confirm-booking {
            font-size: 1.1rem;
            padding: 0.75rem 1.5rem; 
            background-color: #198754; 
            border-color: #198754;
        }
        .btn-confirm-booking:hover {
            background-color: #157347; 
            border-color: #146c43;
        }
        .btn-cancel-booking {
             font-size: 1.1rem;
             padding: 0.75rem 1.5rem;
        }
        .nav-link-custom { font-weight: 500; }
        .btn-admin-panel { background-color: #ffc107; border-color: #ffc107; color: #212529; }
        .btn-admin-panel:hover { background-color: #e0a800; border-color: #d39e00; }
        footer.footer-custom { 
            padding-top: 1rem;
            padding-bottom: 1rem;
            background-color: #343a40; 
            color: rgba(255, 255, 255, 0.5); 
        }
        .alert-custom { margin-top: 1.5rem; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top shadow-sm">
        <div class="container">
            <a class="navbar-brand navbar-brand-custom" href="${pageContext.request.contextPath}/classList">
                <i class="fas fa-calendar-check me-2"></i>ClassBooking Pro
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNavMain" aria-controls="navbarNavMain" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNavMain">
                <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                    <c:choose>
                        <c:when test="${not empty sessionScope.loggedInUser}">
                            <li class="nav-item">
                                <span class="navbar-text me-3">
                                    Hello, <c:out value="${sessionScope.loggedInUser.fullName != null && !sessionScope.loggedInUser.fullName.trim().isEmpty() ? sessionScope.loggedInUser.fullName : sessionScope.loggedInUser.username}"/>!
                                </span>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link nav-link-custom" href="${pageContext.request.contextPath}/myBookings"><i class="fas fa-list-check me-1"></i>My Bookings</a>
                            </li>
                            <c:if test="${sessionScope.loggedInUser.isAdmin()}">
                                <li class="nav-item">
                                    <a class="nav-link nav-link-custom btn btn-admin-panel btn-sm me-2" href="${pageContext.request.contextPath}/admin/dashboard.jsp"><i class="fas fa-user-shield me-1"></i>Admin Panel</a>
                                </li>
                            </c:if>
                            <li class="nav-item">
                                <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-1"></i>Logout</a>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link nav-link-custom" href="${pageContext.request.contextPath}/login.jsp"><i class="fas fa-sign-in-alt me-1"></i>Login</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4 main-content">
        <div class="confirmation-container">
            <div class="text-center mb-4">
                <h1 class="page-title"><i class="fas fa-clipboard-check me-2"></i>Confirm Your Booking</h1>
            </div>

            <c:if test="${not empty requestScope.scheduleToBook}">
                <c:set var="schedule" value="${requestScope.scheduleToBook}" />
                <c:set var="classInfo" value="${schedule.classInfo}" /> 

                <h4 class="mb-3 text-center">You are about to book the following class session:</h4>
                
                <div class="card mb-4">
                    <div class="card-header bg-primary text-white fs-5 fw-bold text-center">
                       <i class="fas fa-chalkboard-teacher me-2"></i> <c:out value="${classInfo.title}"/>
                    </div>
                    <ul class="list-group list-group-flush details-list">
                        <li class="list-group-item"><strong><i class="fas fa-user-tie me-2 text-muted"></i>Instructor:</strong> <c:out value="${classInfo.instructorName}"/></li>
                        <li class="list-group-item"><strong><i class="fas fa-map-marker-alt me-2 text-muted"></i>Location:</strong> <c:out value="${schedule.location}"/></li>
                        <li class="list-group-item"><strong><i class="fas fa-calendar-alt me-2 text-muted"></i>Date & Time:</strong> 
                            <%-- === MODIFIED LINES START === --%>
                            <fmt:formatDate value="${schedule.startTimeAsTimestamp}" type="both" pattern="EEEE, MMMM d, yy 'at' h:mm a"/>
                            to 
                            <fmt:formatDate value="${schedule.endTimeAsTimestamp}" type="time" pattern="h:mm a z"/>
                            <%-- === MODIFIED LINES END === --%>
                        </li>
                        <li class="list-group-item"><strong><i class="fas fa-clock me-2 text-muted"></i>Duration:</strong> <c:out value="${classInfo.durationHours}"/> hours</li>
                        <li class="list-group-item"><strong><i class="fas fa-dollar-sign me-2 text-muted"></i>Price:</strong> <fmt:formatNumber value="${classInfo.price}" type="currency" currencySymbol="$" minFractionDigits="2" maxFractionDigits="2"/></li>
                        <li class="list-group-item"><strong><i class="fas fa-users me-2 text-muted"></i>Slots Available:</strong> <c:out value="${schedule.capacity - schedule.bookedSlots}"/></li>
                    </ul>
                </div>

                <form action="${pageContext.request.contextPath}/booking" method="post">
                    <input type="hidden" name="scheduleId" value="${schedule.scheduleId}">
                    
                    <div class="d-grid gap-2 d-md-flex justify-content-md-center mt-4">
                        <button type="submit" class="btn btn-success btn-lg btn-confirm-booking"><i class="fas fa-check-circle me-2"></i>Confirm & Book Now</button>
                        <a href="${pageContext.request.contextPath}/classList" class="btn btn-outline-secondary btn-lg btn-cancel-booking"><i class="fas fa-times-circle me-2"></i>Cancel</a>
                    </div>
                </form>
            </c:if>

            <c:if test="${empty requestScope.scheduleToBook}">
                <div class="alert alert-warning text-center mt-4 alert-custom" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i>No schedule details found to confirm. Please <a href="${pageContext.request.contextPath}/classList" class="alert-link">select a class</a> first.
                </div>
            </c:if>
        </div>
    </div>

    <footer class="py-3 bg-dark text-white-50 mt-auto footer-custom">
        <div class="container text-center">
            <small>ClassBooking System &copy; 2025. Excellence in J2EE.</small>
        </div>
    </footer>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js" integrity="sha384-C6RzsynM9kWDrMNeT87bh95OGNyZPhcTNXj1NW7RuBCsyN/o0jlpcV8Qyq46cDfL" crossorigin="anonymous"></script>
</body>
</html>