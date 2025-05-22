<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Available Classes - Class Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <style>
        body { background-color: #eef1f5; padding-top: 80px; font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; }
        .navbar-brand-custom { font-weight: 600; font-size: 1.5rem; }
        .class-card { margin-bottom: 2rem; border: 1px solid #dee2e6; border-radius: 0.75rem; box-shadow: 0 0.25rem 0.75rem rgba(0, 0, 0, 0.075); transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out; display: flex; flex-direction: column; }
        .class-card:hover { transform: translateY(-8px); box-shadow: 0 0.5rem 1.5rem rgba(0, 0, 0, 0.1); }
        .card-img-top-placeholder { height: 220px; background-color: #495057; display: flex; align-items: center; justify-content: center; color: white; font-size: 2.5rem; border-top-left-radius: 0.75rem; border-top-right-radius: 0.75rem; }
        .card-img-custom { height: 220px; object-fit: cover; border-top-left-radius: 0.75rem; border-top-right-radius: 0.75rem; }
        .card-title-custom { color: #004085; font-weight: 600; margin-bottom: 0.75rem; }
        .card-body { flex-grow: 1; }
        .card-footer { background-color: #f8f9fa; border-top: 1px solid #e9ecef; }
        .btn-book { font-weight: 500; }
        .flash-message-container { position: fixed; top: 80px; left: 50%; transform: translateX(-50%); z-index: 1055; width: auto; min-width: 350px; max-width: 90%; }
        .page-title { color: #212529; margin-bottom: 2.5rem; font-weight: 600; }
        .nav-link-custom { font-weight: 500; }
        .btn-admin-panel { background-color: #ffc107; border-color: #ffc107; color: #212529; }
        .btn-admin-panel:hover { background-color: #e0a800; border-color: #d39e00; }
        .badge.bg-success { background-color: #198754 !important; }
        .badge.bg-danger { background-color: #dc3545 !important; }
        .badge.bg-warning { background-color: #ffc107 !important; color: #000 !important; }
        .list-group-item { border-color: rgba(0,0,0,0.075); }
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
                            <li class="nav-item"><a class="nav-link nav-link-custom" href="${pageContext.request.contextPath}/myBookings"><i class="fas fa-list-check me-1"></i>My Bookings</a></li>
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

    <div class="container mt-4"> 
        <div class="flash-message-container">
            <c:if test="${not empty requestScope.successMessage}">
                <div id="flashSuccessMessage" class="alert alert-success alert-dismissible fade show" role="alert">
                    <i class="fas fa-check-circle me-2"></i><c:out value="${requestScope.successMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
            <c:if test="${not empty requestScope.errorMessage}">
                <div id="flashErrorMessage" class="alert alert-danger alert-dismissible fade show" role="alert">
                    <i class="fas fa-exclamation-triangle me-2"></i><c:out value="${requestScope.errorMessage}"/>
                    <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                </div>
            </c:if>
        </div>

        <div class="text-center mb-5">
            <h1 class="page-title"><i class="fas fa-graduation-cap me-2"></i>Explore Our Classes</h1>
        </div>

        <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
            <c:choose>
                <c:when test="${not empty schedules}">
                    <c:forEach var="schedule" items="${schedules}">
                        <div class="col d-flex align-items-stretch"> 
                            <div class="card class-card w-100"> 
                                <c:choose>
                                    <c:when test="${not empty schedule.classInfo.imageUrl && schedule.classInfo.imageUrl != ''}">
                                        <img src="${pageContext.request.contextPath}/${schedule.classInfo.imageUrl}" class="card-img-top card-img-custom" alt="<c:out value='${schedule.classInfo.title}'/>">
                                    </c:when>
                                    <c:otherwise>
                                        <div class="card-img-top-placeholder"><i class="fas fa-photo-video"></i></div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="card-body d-flex flex-column">
                                    <h5 class="card-title card-title-custom"><c:out value="${schedule.classInfo.title}"/></h5>
                                    <p class="card-text text-muted mb-2"><small><i class="fas fa-user-tie me-1"></i>Instructor: <c:out value="${schedule.classInfo.instructorName}"/></small></p>
                                    <p class="card-text flex-grow-1 small"><c:out value="${schedule.classInfo.description}"/></p>
                                    <ul class="list-group list-group-flush mt-auto">
                                        <li class="list-group-item"><i class="fas fa-map-marker-alt me-2 text-secondary"></i><strong>Location:</strong> <c:out value="${schedule.location}"/></li>
                                        <li class="list-group-item"><i class="fas fa-calendar-alt me-2 text-secondary"></i><strong>Schedule:</strong> 
                                            <fmt:formatDate value="${schedule.startTimeAsTimestamp}" type="both" pattern="EEE, MMM d, yy h:mm a"/>
                                            to 
                                            <fmt:formatDate value="${schedule.endTimeAsTimestamp}" type="time" pattern="h:mm a z"/>
                                        </li>
                                        <li class="list-group-item"><i class="fas fa-clock me-2 text-secondary"></i><strong>Duration:</strong> <c:out value="${schedule.classInfo.durationHours}"/> hours</li>
                                        <li class="list-group-item"><i class="fas fa-dollar-sign me-2 text-secondary"></i><strong>Price:</strong> <fmt:formatNumber value="${schedule.classInfo.price}" type="currency" currencySymbol="$"/></li>
                                        <li class="list-group-item">
                                            <i class="fas fa-users me-2 text-secondary"></i><strong>Availability:</strong> 
                                            <c:set var="availableSlots" value="${schedule.capacity - schedule.bookedSlots}"/>
                                            <c:choose>
                                                <c:when test="${availableSlots > 5}"><span class="badge bg-success">Available (<c:out value="${availableSlots}"/> slots)</span></c:when>
                                                <c:when test="${availableSlots > 0 && availableSlots <= 5}"><span class="badge bg-warning text-dark">Few Slots Left (<c:out value="${availableSlots}"/>)</span></c:when>
                                                <c:otherwise><span class="badge bg-danger">Fully Booked</span></c:otherwise>
                                            </c:choose>
                                        </li>
                                    </ul>
                                </div>
                                <div class="card-footer text-center">
                                    <c:choose>
                                        <c:when test="${not empty sessionScope.loggedInUser}">
                                            <c:if test="${(schedule.capacity - schedule.bookedSlots) > 0}"><a href="${pageContext.request.contextPath}/booking?action=prepare&scheduleId=${schedule.scheduleId}" class="btn btn-primary btn-book w-100"><i class="fas fa-calendar-plus me-2"></i>Book This Session</a></c:if>
                                            <c:if test="${(schedule.capacity - schedule.bookedSlots) <= 0}"><button class="btn btn-secondary w-100" disabled>Fully Booked</button></c:if>
                                        </c:when>
                                        <c:otherwise><a href="${pageContext.request.contextPath}/login.jsp?redirectUrl=${pageContext.request.contextPath}/classList" class="btn btn-info w-100">Login to Book</a></c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise><div class="col"><div class="alert alert-info text-center" role="alert"><i class="fas fa-info-circle me-2"></i>No classes or schedules are currently available. Please check back later!</div></div></c:otherwise>
            </c:choose>
        </div>

        <c:if test="${not empty sessionScope.loggedInUser && sessionScope.loggedInUser.isAdmin()}">
             <p class="text-center mt-5 mb-5"><a href="${pageContext.request.contextPath}/admin/addClass.jsp" class="btn btn-lg btn-outline-success"><i class="fas fa-plus-circle me-2"></i>Add New Class (Admin)</a></p>
        </c:if>
         <c:if test="${not empty sessionScope.loggedInUser && !sessionScope.loggedInUser.isAdmin()}">
             <p class="text-center mt-5 mb-5"><a href="${pageContext.request.contextPath}/add_advanced_order.jsp" class="btn btn-lg btn-outline-primary"><i class="fas fa-ticket-alt me-2"></i>Test Transactional Booking (Seats)</a></p>
        </c:if>
    </div>

    <footer class="py-4 bg-dark text-white-50 mt-5"><div class="container text-center"><small>ClassBooking System &copy; 2025. Excellence in J2EE.</small></div></footer>
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
                            messageElement.style.display = 'none';
                        }
                    }, delay);
                }
            }
            autoHideMessage('flashSuccessMessage', 5000); // Success messages hide after 5 seconds
            autoHideMessage('flashErrorMessage', 7000);  // Error messages hide after 7 seconds
        });
    </script>
</body>
</html>