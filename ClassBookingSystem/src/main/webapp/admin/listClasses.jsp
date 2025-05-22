<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Classes - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; padding-top: 70px; }
        .navbar-brand-custom { font-weight: 600; font-size: 1.5rem; }
        .sidebar { position: fixed; top: 0; bottom: 0; left: 0; z-index: 100; padding: 70px 0 0; box-shadow: inset -1px 0 0 rgba(0, 0, 0, .1); background-color: #343a40; }
        .sidebar-sticky { position: relative; top: 0; height: calc(100vh - 70px); padding-top: .5rem; overflow-x: hidden; overflow-y: auto; }
        .sidebar .nav-link { font-weight: 500; color: #adb5bd; }
        .sidebar .nav-link .fas { margin-right: 8px; }
        .sidebar .nav-link.active { color: #fff; }
        .sidebar .nav-link:hover, .sidebar .nav-link.active { color: #fff; background-color: #495057; }
        .main-content { margin-left: 220px; padding: 20px; }
        .page-title { margin-bottom: 1.5rem; }
        .table th, .table td { vertical-align: middle; }
        .action-buttons .btn { margin-right: 5px; }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top shadow-sm">
        <div class="container-fluid">
            <a class="navbar-brand navbar-brand-custom" href="${pageContext.request.contextPath}/admin/dashboard.jsp"><i class="fas fa-user-shield me-2"></i>Admin Panel</a>
            <ul class="navbar-nav ms-auto mb-2 mb-lg-0 align-items-center">
                <li class="nav-item"><span class="navbar-text me-3">User: <c:out value="${sessionScope.loggedInUser.username}"/> (Admin)</span></li>
                <li class="nav-item"><a class="btn btn-outline-light btn-sm" href="${pageContext.request.contextPath}/logout"><i class="fas fa-sign-out-alt me-1"></i>Logout</a></li>
            </ul>
        </div>
    </nav>

    <nav id="sidebarMenu" class="col-md-3 col-lg-2 d-md-block sidebar collapse">
        <div class="position-sticky pt-3 sidebar-sticky">
            <ul class="nav flex-column">
                <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/admin/dashboard.jsp"><i class="fas fa-tachometer-alt"></i> Dashboard</a></li>
                <li class="nav-item"><a class="nav-link active" aria-current="page" href="${pageContext.request.contextPath}/admin/manageClasses"><i class="fas fa-chalkboard-teacher"></i> Manage Classes</a></li>
                <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-calendar-alt"></i> Manage Schedules</a></li>
                <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-receipt"></i> View All Bookings</a></li>
                <li class="nav-item"><a class="nav-link" href="#"><i class="fas fa-users-cog"></i> Manage Users</a></li>
                <li class="nav-item mt-3"><a class="nav-link" href="${pageContext.request.contextPath}/classList"><i class="fas fa-arrow-left"></i> Back to Main Site</a></li>
            </ul>
        </div>
    </nav>

    <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4 main-content">
        <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
            <h1 class="h2 page-title">Manage Classes</h1>
            <div class="btn-toolbar mb-2 mb-md-0">
                <a href="${pageContext.request.contextPath}/admin/manageClasses?action=showAddForm" class="btn btn-success"><i class="fas fa-plus-circle me-1"></i> Add New Class</a>
            </div>
        </div>

        <c:if test="${not empty requestScope.adminSuccessMessage}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i> <c:out value="${requestScope.adminSuccessMessage}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>
        <c:if test="${not empty requestScope.adminErrorMessage}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i> <c:out value="${requestScope.adminErrorMessage}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <div class="table-responsive">
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                    <tr>
                        <th>ID</th>
                        <th>Title</th>
                        <th>Instructor</th>
                        <th>Duration (hrs)</th>
                        <th>Price</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:choose>
                        <c:when test="${not empty allClasses}">
                            <c:forEach var="classInfo" items="${allClasses}">
                                <tr>
                                    <td><c:out value="${classInfo.classId}"/></td>
                                    <td><c:out value="${classInfo.title}"/></td>
                                    <td><c:out value="${classInfo.instructorName}"/></td>
                                    <td><c:out value="${classInfo.durationHours}"/></td>
                                    <td><fmt:formatNumber value="${classInfo.price}" type="currency" currencySymbol="$"/></td>
                                    <td class="action-buttons">
                                        <a href="${pageContext.request.contextPath}/admin/listSchedules?classId=${classInfo.classId}" class="btn btn-sm btn-info" title="Manage Schedules"><i class="fas fa-calendar-alt"></i> Schedules</a>
                                        <a href="${pageContext.request.contextPath}/admin/manageClasses?action=showEditForm&classId=${classInfo.classId}" class="btn btn-sm btn-warning" title="Edit Class"><i class="fas fa-edit"></i> Edit</a>
                                        <%-- Delete action would typically require a POST or a confirmation --%>
                                        <a href="${pageContext.request.contextPath}/admin/manageClasses?action=delete&classId=${classInfo.classId}" 
                                           onclick="return confirm('Are you sure you want to delete this class and all its schedules?');" 
                                           class="btn btn-sm btn-danger" title="Delete Class"><i class="fas fa-trash-alt"></i> Delete</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="text-center">No classes found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                </tbody>
            </table>
        </div>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>