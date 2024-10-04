package hcmute.kltn.vtv.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hcmute.kltn.vtv.authentication.service.IJwtService;
import hcmute.kltn.vtv.util.ErrorResponse;
import hcmute.kltn.vtv.util.exception.JwtExpiredException;
import hcmute.kltn.vtv.util.exception.UnauthorizedAccessException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final IJwtService jwtService;
    private final UserDetailsService userDetailsService;


    private static final String[] NO_AUTH = {
            "/api/auth/",
            "/api/product/",
            "/api/search/",
            "/api/shop/",
            "/api/voucher/",
            "/api/review/",
            "/api/comment",
            "/api/chat/",
            "/api/location/",

            "/api/payment/",
            "/api/category/",
            "/api/brand/",

            "/api/chat/room/",
            "/api/chat/message/",

            "/api/product-suggestion/",

            "/api/customer/notification",
            "/api/product-filter/",

            "/api/category-shop/",


            "/api/customer/reset-password",
            "/api/customer/active-account",
            "/api/customer/active-account/send-email",
            "/api/customer/forgot-password",

            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/",
            "/swagger-resources",
            "/swagger-resources/",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/",
            "/webjars/",
            "/swagger-ui.html"
    };


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
//            String requestPath = request.getServletPath();
//
//            if (Arrays.stream(NO_AUTH).anyMatch(requestPath::startsWith)) {
//                filterChain.doFilter(request, response);
//                return;
//            }

            if (request.getServletPath().contains("/api/auth")) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String username;


            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);


            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                var isTokenValid = jwtService.isTokenValid(jwt, userDetails);

                if (!isTokenValid) {
                    throw new JwtExpiredException("Token hết hạn.");
                }

                request.setAttribute("username", username);

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);


            }
        } catch (ExpiredJwtException ex) {
            String errorMessage = "Phiên đăng nhập đã hết hạn.";
            ErrorResponse errorResponse = new ErrorResponse(HttpStatus.UNAUTHORIZED, 401, "Thông báo", errorMessage);


            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json;charset=UTF-8");
            new ObjectMapper().writeValue(response.getWriter(), errorResponse);

            return; // Make sure the request stops here
        }

        filterChain.doFilter(request, response);

    }
}