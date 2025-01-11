package com.demo.hungnguyendev.helpers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.demo.hungnguyendev.modules.users.services.impls.CustomUserDetailService;
import com.demo.hungnguyendev.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtService jwtService;
    private final CustomUserDetailService customUserDetailService;
    private final ObjectMapper objectMapper;

    @Override
    protected boolean shouldNotFilter(@NotNull HttpServletRequest request){
        String path = request.getRequestURI();
        return path.startsWith("/api/v1/auth/login");
    }

    @Override
    public void doFilterInternal(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull FilterChain filterChain) throws ServletException, IOException{

        try {
            final String authHeader = request.getHeader("Authorization");
            final String jwt;
            final String userId;

            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                sendErrorResponse(response, request, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed", "Token not found");
                return;
            }

            jwt = authHeader.substring(7);
            userId = jwtService.getUserIdFromJwt(jwt);

            if(!jwtService.isTokenFormatValid(jwt)){
                sendErrorResponse(response, request, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed", "Invalid token format");
                return;
            }

            if(!jwtService.isSignatureValid(jwt)){
                sendErrorResponse(response, request, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed", "Invalid signature");
                return;
            }

            if(!jwtService.isIssuerToken(jwt)){
                sendErrorResponse(response, request, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed", "Invalid token issuer");
                return;
            }

            if(jwtService.isTokenExpired(jwt)){
                sendErrorResponse(response, request, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed", "Token has expired");
                return;
            }

            if(userId != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = customUserDetailService.loadUserByUsername(userId);
                final String emailFromToken = jwtService.getEmailFromJwt(jwt);
                if(!emailFromToken.equals(userDetails.getUsername())){
                    sendErrorResponse(response, request, HttpServletResponse.SC_UNAUTHORIZED, "Authentication failed", "User token is incorrect");
                    return;
                }

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
                );

                authToken.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        } catch (ServletException | IOException e) {
            sendErrorResponse(response, request, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Network Error", e.getMessage());
        }
    }

    private void sendErrorResponse(@NotNull HttpServletResponse response, @NotNull HttpServletRequest request, int statusCode, String error, String message) throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json;charset=UTF-8");
        
        Map<String, Object> errorResponse = new HashMap<>();

        errorResponse.put("timestamp", System.currentTimeMillis());
        errorResponse.put("status", statusCode);
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());

        String jsonResponse = objectMapper.writeValueAsString(errorResponse);

        response.getWriter().write(jsonResponse);
    }
}
