<%-- Document : verify-otp Created on : Mar 18, 2026 Author : TrungNT - CE200064 --%>

    <%@page contentType="text/html" pageEncoding="UTF-8" %>
        <%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Verify OTP - Bookverse</title>

                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/forgot-password.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/footer-index.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/boostrap/bootstrap.min.css"
                    type="text/css" />
            </head>

            <body>
                <div class="header-logo">
                    <a href="${pageContext.request.contextPath}/home">
                        <img src="${pageContext.request.contextPath}/assets/images/logo.png" alt="Bookverse Logo">
                    </a>
                </div>

                <div class="fp-container">
                    <h2>Verify OTP</h2>
                    <p class="subtitle">Enter the 6-digit code sent to your email.</p>

                    <!-- Step Indicator -->
                    <div class="step-indicator">
                        <div class="step-dot done"></div>
                        <div class="step-dot active"></div>
                        <div class="step-dot"></div>
                    </div>

                    <!-- Email Display -->
                    <c:if test="${not empty sessionScope.otp_email}">
                        <div class="email-display">
                            <span><i class="fa-solid fa-envelope"></i> ${sessionScope.otp_email}</span>
                        </div>
                    </c:if>

                    <!-- Error Message -->
                    <c:if test="${not empty requestScope.errorMessage}">
                        <div class="fp-alert fp-alert-error">
                            <i class="fa-solid fa-circle-exclamation"></i> ${requestScope.errorMessage}
                        </div>
                    </c:if>

                    <!-- Success Message -->
                    <c:if test="${not empty requestScope.successMessage}">
                        <div class="fp-alert fp-alert-success">
                            <i class="fa-solid fa-circle-check"></i> ${requestScope.successMessage}
                        </div>
                    </c:if>

                    <!-- OTP Verification Form -->
                    <form action="${pageContext.request.contextPath}/verify-otp" method="POST" id="otpForm">
                        <div class="otp-input-wrapper">
                            <input type="text" id="otp" name="otp" maxlength="6" pattern="[0-9]{6}" placeholder="------"
                                required autocomplete="off" />
                        </div>

                        <button type="submit" class="fp-btn-primary">
                            <i class="fa-solid fa-shield-check"></i> Verify OTP
                        </button>
                    </form>

                    <!-- Resend OTP Form -->
                    <form action="${pageContext.request.contextPath}/verify-otp" method="POST" id="resendForm">
                        <input type="hidden" name="action" value="resend" />
                        <button type="submit" class="fp-btn-secondary" id="resendBtn">
                            <i class="fa-solid fa-rotate-right"></i> Resend OTP
                        </button>
                    </form>
                    <div class="resend-timer" id="timerDisplay"></div>

                    <a href="${pageContext.request.contextPath}/forgot-password" class="fp-link">
                        <i class="fa-solid fa-arrow-left"></i> Try a different email
                    </a>
                </div>

                <jsp:include page="footer-index.jsp" />

                <script src="${pageContext.request.contextPath}/boostrap/bootstrap.bundle.min.js"
                    type="text/javascript"></script>
                <script>
                    // OTP input: allow only digits
                    document.getElementById('otp').addEventListener('input', function (e) {
                        this.value = this.value.replace(/[^0-9]/g, '');
                    });

                    // Resend cooldown (30 seconds)
                    (function () {
                        var resendBtn = document.getElementById('resendBtn');
                        var timerDisplay = document.getElementById('timerDisplay');
                        var cooldownKey = 'otp_resend_cooldown';
                        var cooldownSeconds = 30;

                        function startCooldown() {
                            var endTime = Date.now() + cooldownSeconds * 1000;
                            localStorage.setItem(cooldownKey, endTime);
                            runTimer(endTime);
                        }

                        function runTimer(endTime) {
                            resendBtn.disabled = true;
                            var interval = setInterval(function () {
                                var remaining = Math.ceil((endTime - Date.now()) / 1000);
                                if (remaining <= 0) {
                                    clearInterval(interval);
                                    resendBtn.disabled = false;
                                    timerDisplay.textContent = '';
                                    localStorage.removeItem(cooldownKey);
                                } else {
                                    timerDisplay.textContent = 'Resend available in ' + remaining + 's';
                                }
                            }, 1000);
                        }

                        // Check if cooldown is active from previous page load
                        var stored = localStorage.getItem(cooldownKey);
                        if (stored && Date.now() < parseInt(stored)) {
                            runTimer(parseInt(stored));
                        }

                        // Start cooldown on resend
                        document.getElementById('resendForm').addEventListener('submit', function () {
                            startCooldown();
                        });
                    })();
                </script>
            </body>

            </html>