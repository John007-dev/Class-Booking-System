<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Bookings - Class Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <style>
        body { background-color: #eef1f5; padding-top: 80px; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; display: flex; flex-direction: column; min-height: 100vh; }
        .main-content { flex: 1; }
        .navbar-brand-custom { font-weight: 600; font-size: 1.5rem; }
        .page-title { color: #212529; margin-bottom: 2.5rem; font-weight: 600; }
        .booking-card { margin-bottom: 1.5rem; box-shadow: 0 0.25rem 0.75rem rgba(0,0,0,0.075); border-radius: 0.5rem; border: 1px solid #dee2e6; }
        .booking-card .card-header { background-color: #0056b3; color: white; font-weight: 500; font-size: 1.1rem; }
        .booking-card .card-body { font-size: 0.95rem; }
        .booking-card .list-group-item { border-color: rgba(0,0,0,0.075); padding-top: 0.6rem; padding-bottom: 0.6rem; }
        .badge-confirmed { background-color: #198754 !important; }
        .badge-cancelled { background-color: #dc3545 !important; }
        .badge-completed { background-color: #6c757d !important; }
        .nav-link-custom { font-weight: 500; }
        .btn-admin-panel { background-color: #ffc107; border-color: #ffc107; color: #212529; }
        .btn-admin-panel:hover { background-color: #e0a800; border-color: #d39e00; }
        .flash-message-container { position: fixed; top: 80px; left: 50%; transform: translateX(-50%); z-index: 1055; width: auto; min-width: 350px; max-width: 90%; }
        .no-bookings-alert { margin-top: 2rem; }
        footer.footer-custom { padding-top: 1rem; padding-bottom: 1rem; background-color: #343a40; color: rgba(255, 255, 255, 0.5); margin-top: auto; }
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
                            <li class="nav-item"><span class="navbar-text me-3">Hello, <c:out value="${sessionScope.loggedInUser.fullName != null && !sessionScope.loggedInUser.fullName.trim().isEmpty() ? sessionScope.loggedInUser.fullName : sessionScope.loggedInUser.username}"/>!</span></li>
                            <li class="nav-item"><a class="nav-link nav-link-custom active" aria-current="page" href="${pageContext.request.contextPath}/myBookings"><i class="fas fa-list-check me-1"></i>My Bookings</a></li>
                            <c:if test="${sessionScope.loggedInUser.isAdmin()}"><li class="nav-item"><a class="nav-link nav-link-custom btn btn-admin-panel btn-sm me-2" href="${pageContext.request.contextPath}/admin/dashboard.jsp"><i class="fas fa-user-shield me-1"></i>Admin Panel</a></li></c:if>
                            <li class="nav-item"><a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-1"></i>Logout</a></li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item"><a class="nav-link nav-link-custom" href="${pageContext.request.contextPath}/login.jsp"><i class="fas fa-sign-in-alt me-1"></i>Login</a></li>
                            <li class="nav-item"><a class="nav-link nav-link-custom" href="${pageContext.request.contextPath}/register.jsp"><i class="fas fa-user-plus me-1"></i>Register</a></li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>

    <div class="container mt-4 main-content">
        <div class="flash-message-container">
            <c:if test="${not empty requestScope.pageErrorMessage}">
                <div id="flashMyBookingsPageError" class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i><c:out value="${requestScope.pageErrorMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${not empty sessionScope.successMessage}"> 
                <div id="flashMyBookingsSuccess" class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i><c:out value="${sessionScope.successMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
                <c:remove var="successMessage" scope="session"/>
            </c:if>
        </div>

        <div class="text-center mb-5">
            <h1 class="page-title"><i class="fas fa-history me-2"></i>My Booking History</h1>
        </div>

        <c:choose>
            <c:when test="${not empty userBookings}">
                <div class="row row-cols-1 row-cols-md-2 g-4">
                    <c:forEach var="booking" items="${userBookings}">
                        <div class="col">
                            <div class="card booking-card">
                                <div class="card-header">
                                    <i class="fas fa-ticket-alt me-2"></i>Booking ID: <c:out value="${booking.bookingId}"/> 
                                    <span class="float-end">
                                        Status: 
                                        <c:choose>
                                            <c:when test="${booking.status == 'CONFIRMED'}"><span class="badge badge-confirmed">Confirmed</span></c:when>
                                            <c:when test="${booking.status == 'CANCELLED_BY_USER' || booking.status == 'CANCELLED_BY_ADMIN'}"><span class="badge badge-cancelled">Cancelled</span></c:when>
                                            <c:when test="${booking.status == 'COMPLETED'}"><span class="badge badge-completed">Completed</span></c:when>
                                            <c:otherwise><span class="badge bg-secondary"><c:out value="${booking.status}"/></span></c:otherwise>
                                        </c:choose>
                                    </span>
                                </div>
                                <div class="card-body">
                                    <h5 class="card-title mb-3"><i class="fas fa-chalkboard-teacher me-2"></i><c:out value="${booking.scheduleInfo.classInfo.title}"/></h5>
                                    <ul class="list-group list-group-flush">
                                        <li class="list-group-item"><strong><i class="fas fa-user-tie me-2 text-muted"></i>Instructor:</strong> <c:out value="${booking.scheduleInfo.classInfo.instructorName}"/></li>
                                        <li class="list-group-item"><strong><i class="fas fa-map-marker-alt me-2 text-muted"></i>Location:</strong> <c:out value="${booking.scheduleInfo.location}"/></li>
                                        <li class="list-group-item"><strong><i class="fas fa-calendar-day me-2 text-muted"></i>Scheduled:</strong> 
                                            <fmt:formatDate value="${booking.scheduleInfo.startTimeAsTimestamp}" type="both" pattern="EEE, MMM d, yy h:mm a"/>
                                            to 
                                            <fmt:formatDate value="${booking.scheduleInfo.endTimeAsTimestamp}" type="time" pattern="h:mm a z"/>
                                        </li>
                                        <li class="list-group-item"><strong><i class="fas fa-calendar-plus me-2 text-muted"></i>Booked On:</strong> <fmt:formatDate value="${booking.bookingTime}" type="both" dateStyle="medium" timeStyle="medium"/></li>
                                        <li class="list-group-item"><strong><i class="fas fa-dollar-sign me-2 text-muted"></i>Price Paid:</strong> <fmt:formatNumber value="${booking.scheduleInfo.classInfo.price}" type="currency" currencySymbol="$"/></li>
                                    </ul>
                                </div>
                                <div class="card-footer text-center text-muted">
                                    <small>
                                        <%-- <a href="${pageContext.request.contextPath}/classDetail?scheduleId=${booking.scheduleInfo.scheduleId}" class="btn btn-sm btn-outline-info ms-2">View Class Details</a> --%>
                                    </small>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info text-center no-bookings-alert" role="alert">
                    <i class="fas fa-info-circle me-2"></i>You currently have no bookings. 
                    <a href="${pageContext.request.contextPath}/classList" class="alert-link">Browse available classes</a> to make a booking!
                </div>
            </c:otherwise>
        </c:choose>
        
        <div class="text-center mt-5">
            <a href="${pageContext.request.contextPath}/classList" class="btn btn-lg btn-secondary"><i class="fas fa-arrow-left me-2"></i>Back to Class List</a>
        </div>
    </div>

    <footer class="py-3 bg-dark text-white-50 mt-auto footer-custom">
        <div class="container text-center">
            <small>ClassBooking System &copy; 2025. Designed for J2EE Lab.</small>
        </div>
    </footer>

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
            autoHideMessage('flashMyBookingsPageError', 7000); // For page-specific errors
            autoHideMessage('flashMyBookingsSuccess', 5000); // For success messages from session
        });
    </script>
</body>
</html>