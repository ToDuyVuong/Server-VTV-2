package hcmute.kltn.vtv.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.kltn.vtv.util.ErrorResponse;
import hcmute.kltn.vtv.util.exception.UnauthorizedAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
public class CustomAccessDeniedHandler extends SimpleUrlAuthenticationFailureHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        objectMapper.writeValue(response.getWriter(), buildErrorResponse());
    }


    private ErrorResponse buildErrorResponse() {
        return new ErrorResponse(HttpStatus.FORBIDDEN, 403, "Thông báo", "Truy cập bị từ chối!");
    }
}

