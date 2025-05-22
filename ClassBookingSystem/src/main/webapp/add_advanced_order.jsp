<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Advanced Order - Class Booking System</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.2.0/css/all.min.css">
    <style>
        body { background-color: #f8f9fa; padding-top: 20px; font-family: 'Segoe UI', sans-serif; }
        .container { max-width: 600px; background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 0.5rem 1rem rgba(0,0,0,.15); }
        .form-label { font-weight: 500; }
        .btn-submit { background-color: #198754; border-color: #198754; }
        .btn-submit:hover { background-color: #157347; border-color: #146c43; }
        .error-message { margin-top: 1rem; } /* For messages directly on this page */
        .nav-link-back { margin-top: 1.5rem; display: inline-block;}
    </style>
</head>
<body>
    <div class="container">
        <h2 class="text-center mb-4"><i class="fas fa-cart-plus me-2"></i>Test Transactional Order (with Seats)</h2>

        <c:if test="${not empty requestScope.error}">
            <div id="flashAdvancedOrderError" class="alert alert-danger error-message alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i><strong>Error:</strong> <c:out value="${requestScope.error}"/>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
        </c:if>

        <form action="${pageContext.request.contextPath}/advancedOrder" method="post">
            <div class="mb-3">
                <label for="userId" class="form-label">User ID (must exist, e.g., 1 for admin):</label>
                <input type="number" class="form-control" id="userId" name="userId" value="1" required>
            </div>
            <div class="mb-3">
                <label for="pizzaId" class="form-label">Class ID (must exist, e.g., 1 for Java):</label>
                <input type="number" class="form-control" id="pizzaId" name="pizzaId" value="1" required>
                <small class="form-text text-muted">Refers to a Class ID for this system.</small>
            </div>
            <div class="mb-3">
                <label for="qty" class="form-label">Quantity (e.g., 1 for class booking):</label>
                <input type="number" class="form-control" id="qty" name="qty" value="1" min="1" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Select Seat(s) (IDs must exist and be available):</label>
                <div class="form-check"><input class="form-check-input" type="checkbox" name="seatIds" value="1" id="seat1"><label class="form-check-label" for="seat1">Seat A1 (ID 1)</label></div>
                <div class="form-check"><input class="form-check-input" type="checkbox" name="seatIds" value="2" id="seat2"><label class="form-check-label" for="seat2">Seat A2 (ID 2)</label></div>
                <div class="form-check"><input class="form-check-input" type="checkbox" name="seatIds" value="3" id="seat3"><label class="form-check-label" for="seat3">Seat A3 (ID 3)</label></div>
                <div class="form-check"><input class="form-check-input" type="checkbox" name="seatIds" value="4" id="seat4"><label class="form-check-label" for="seat4">Seat B1 (ID 4)</label></div>
            </div>
            <button type="submit" class="btn btn-success btn-submit w-100"><i class="fas fa-check-circle me-2"></i>Place Advanced Order</button>
        </form>
        <div class="text-center">
             <a href="${pageContext.request.contextPath}/classList" class="nav-link-back"><i class="fas fa-arrow-left me-1"></i>Back to Class List</a>
        </div>
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
            autoHideMessage('flashAdvancedOrderError', 7000); 
        });
    </script>
</body>
</html>