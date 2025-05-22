<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${not empty requestScope.classToEdit ? 'Edit Class' : 'Add New Class'} - Admin</title>
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
            <h1 class="h2 page-title">${not empty requestScope.classToEdit ? 'Edit Class' : 'Add New Class'}</h1>
        </div>

        <c:if test="${not empty requestScope.errorMessage}">
            <div class="alert alert-danger" role="alert">
                <c:out value="${requestScope.errorMessage}"/>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/admin/manageClasses" method="post">
            <c:choose>
                <c:when test="${not empty requestScope.classToEdit}">
                    <input type="hidden" name="formAction" value="updateClass">
                    <input type="hidden" name="classId" value="<c:out value="${requestScope.classToEdit.classId}"/>">
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="formAction" value="addClass">
                </c:otherwise>
            </c:choose>

            <div class="mb-3">
                <label for="title" class="form-label">Class Title:</label>
                <input type="text" class="form-control" id="title" name="title" value="<c:out value="${requestScope.classToEdit.title}"/>" required>
            </div>
            <div class="mb-3">
                <label for="description" class="form-label">Description:</label>
                <textarea class="form-control" id="description" name="description" rows="3" required><c:out value="${requestScope.classToEdit.description}"/></textarea>
            </div>
            <div class="mb-3">
                <label for="instructorName" class="form-label">Instructor Name:</label>
                <input type="text" class="form-control" id="instructorName" name="instructorName" value="<c:out value="${requestScope.classToEdit.instructorName}"/>">
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label for="durationHours" class="form-label">Duration (hours):</label>
                    <input type="number" class="form-control" id="durationHours" name="durationHours" step="0.1" min="0" value="<c:out value="${requestScope.classToEdit.durationHours}"/>">
                </div>
                <div class="col-md-6 mb-3">
                    <label for="price" class="form-label">Price ($):</label>
                    <input type="number" class="form-control" id="price" name="price" step="0.01" min="0" value="<c:out value="${requestScope.classToEdit.price}"/>" required>
                </div>
            </div>
            <div class="mb-3">
                <label for="imageUrl" class="form-label">Image URL (e.g., images/your-image.png):</label>
                <input type="text" class="form-control" id="imageUrl" name="imageUrl" value="<c:out value="${requestScope.classToEdit.imageUrl}"/>">
            </div>

            <button type="submit" class="btn btn-primary">${not empty requestScope.classToEdit ? 'Update Class' : 'Add Class'}</button>
            <a href="${pageContext.request.contextPath}/admin/manageClasses" class="btn btn-secondary">Cancel</a>
        </form>
    </main>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>