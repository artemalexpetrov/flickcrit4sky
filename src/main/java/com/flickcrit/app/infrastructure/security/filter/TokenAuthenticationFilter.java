package com.flickcrit.app.infrastructure.security.filter;

import com.flickcrit.app.infrastructure.security.service.Token;
import com.flickcrit.app.infrastructure.security.service.TokenService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
class TokenAuthenticationFilter extends OncePerRequestFilter implements AuthenticationFilter {

    private static final String AUTHENTICATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();

    @Qualifier(DispatcherServlet.HANDLER_EXCEPTION_RESOLVER_BEAN_NAME)
    private final HandlerExceptionResolver exceptionResolver;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(
        @Nonnull HttpServletRequest request,
        @Nonnull HttpServletResponse response,
        @Nonnull FilterChain filterChain) throws ServletException, IOException {

        String authenticationHeader = request.getHeader(AUTHENTICATION_HEADER);
        if (authenticationHeader == null || !authenticationHeader.startsWith(TOKEN_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authenticationHeader.substring(TOKEN_PREFIX_LENGTH);
        try {
            authenticateToken(request, token);
            filterChain.doFilter(request, response);
        } catch (AuthenticationException e) {
            exceptionResolver.resolveException(request, response, null, e);
        }
    }

    /**
     * Attempts to authenticate a user by parsing a token and setting up the security context,
     * if valid user details are extracted and no authentication currently exists.
     *
     * @param request  the HttpServletRequest object representing the HTTP request
     * @param rawToken the raw token string to be parsed and authenticated
     */
    private void authenticateToken(@Nonnull HttpServletRequest request, String rawToken) {
        Token token = tokenService.parseToken(rawToken);
        if (!token.hasUsername() || hasAuthentication()) {
            return;
        }

        if (token.isExpired()) {
            throw new CredentialsExpiredException("Token has expired");
        }

        UsernamePasswordAuthenticationToken authenticationToken
            = new UsernamePasswordAuthenticationToken(token.getUsername(), null, token.getAuthorities());

        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }

    /**
     * Checks if there is an authentication object present in the current security context.
     *
     * @return true if the security context contains an authentication object, false otherwise.
     */
    private boolean hasAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }
}
