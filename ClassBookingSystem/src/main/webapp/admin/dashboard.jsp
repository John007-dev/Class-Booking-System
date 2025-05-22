<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Class Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; padding-top: 70px; }
        .navbar-brand-custom { font-weight: 600; font-size: 1.5rem; }
        .nav-link-custom { font-weight: 500; }
        .sidebar {
            position: fixed;
            top: 0;
            bottom: 0;
            left: 0;
            z-index: 100; 
            padding: 70px 0 0; 
            box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1);
            background-color: #343a40;
        }
        .sidebar-sticky {
            position: relative;
            top: 0;
            height: calc(100vh - 70px);
            padding-top: .5rem;
            overflow-x: hidden;
            overflow-y: auto; 
        }
        .sidebar .nav-link {
            font-weight: 500;
            color: #adb5bd; /* Light grey for links */
        }
        .sidebar .nav-link .fas {
            margin-right: 8px;
        }
        .sidebar .nav-link.active {
            color: #fff;
        }
        .sidebar .nav-link:hover, .sidebar .nav-link.active {
            color: #fff;
            background-color: #495057;
        }
        .main-content {
            margin-left: 220px; /* Same as sidebar width */
            padding: 20px;
        }
        .page-title { margin-bottom: 1.5rem; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top shadow-sm">
        <div class="container-fluid">
            <a class="navbar-brand navbar-brand-custom" href="${pageContext.request.contextPath}/admin/dashboard.jsp">
                <i class="fas fa-user-shield me-2"></i>Admin Panel - ClassBooking
            </a>
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                <li class="nav-item">
                    <span class="navbar-text me-3">
                        User: <c:out value="${sessionScope.loggedInUser.username}"/> (Admin)
                    </span>
                </li>
                <li class="nav-item">
                    <a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-1"></i>Logout</a>
                </li>
            </ul>
        </div>
    </nav>

    <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block sidebar collapse">
        <div class="position-sticky pt-3 sidebar-sticky">
            <ul class="nav flex-column">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/admin/dashboard.jsp">
                        <i class="fas fa-tachometer-alt"></i> Dashboard
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/manageClasses">
                        <i class="fas fa-chalkboard-teacher"></i> Manage Classes
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/manageSchedules"> <%-- Assuming a future servlet --%>
                        <i class="fas fa-calendar-alt"></i> Manage Schedules
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/viewAllBookings"> <%-- Assuming a future servlet --%>
                        <i class="fas fa-receipt"></i> View All Bookings
                    </a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="${pageContext.request.contextPath}/admin/manageUsers"> <%-- Assuming a future servlet --%>
                        <i class="fas fa-users-cog"></i> Manage Users
                    </a>
                </li>
                 <li class="nav-item mt-3">
                    <a class="nav-link" href="${pageContext.request.contextPath}/classList">
                        <i class="fas fa-arrow-left"></i> Back to Main Site
                    </a>
                </li>
            </ul>
        </div>
    </nav>

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
            <h1 class="h2 page-title">Admin Dashboard</h1>
        </div>

        <p>Welcome to the Admin Dashboard, <c:out value="${sessionScope.loggedInUser.fullName != null ? sessionScope.loggedInUser.fullName : sessionScope.loggedInUser.username}"/>!</p>
        <p>From here you can manage classes, schedules, bookings, and users.</p>

        <div class="row mt-4">
            <div class="col-md-4">
                <div class="card text-center">
                    <div class="card-body">
                        <h5 class="card-title"><i class="fas fa-chalkboard-teacher fa-2x mb-2"></i></h5>
                        <p class="card-text">Manage all class offerings.</p>
                        <a href="${pageContext.request.contextPath}/admin/manageClasses" class="btn btn-primary">Go to Classes</a>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                 <div class="card text-center">
                    <div class="card-body">
                        <h5 class="card-title"><i class="fas fa-calendar-alt fa-2x mb-2"></i></h5>
                        <p class="card-text">Manage class schedules.</p>
                        <a href="#" class="btn btn-primary disabled">Go to Schedules</a> <%-- Link to /admin/manageSchedules later --%>
                    </div>
                </div>
            </div>
            <div class="col-md-4">
                 <div class="card text-center">
                    <div class="card-body">
                        <h5 class="card-title"><i class="fas fa-receipt fa-2x mb-2"></i></h5>
                        <p class="card-text">View all user bookings.</p>
                         <a href="#" class="btn btn-primary disabled">Go to Bookings</a> <%-- Link to /admin/viewAllBookings later --%>
                    </div>
                </div>
            </div>
        </div>
        </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>