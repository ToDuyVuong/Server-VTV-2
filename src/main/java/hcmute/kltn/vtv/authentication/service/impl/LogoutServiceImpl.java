package hcmute.kltn.vtv.authentication.service.impl;

import hcmute.kltn.vtv.repository.user.TokenRepository;
import hcmute.kltn.vtv.util.exception.BadRequestException;
import hcmute.kltn.vtv.util.exception.InternalServerErrorException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository.findByToken(jwt)
                .orElseThrow(() -> new BadRequestException("Token không hợp lệ!"));
        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            try {
                tokenRepository.save(storedToken);

                // Xóa refreshToken trong cookie
                Cookie cookie = new Cookie("refreshToken", "null");
                cookie.setHttpOnly(true);
                cookie.setPath("/"); // Đặt đúng path mà bạn muốn
                cookie.setMaxAge(0); // Set thời gian sống của cookie (ví dụ: 30 ngày)
                response.addCookie(cookie); // Thêm cookie vào response

                SecurityContextHolder.clearContext();

            } catch (Exception e) {
                throw new InternalServerErrorException("Lỗi hệ thống!");
            }
        }
    }
}