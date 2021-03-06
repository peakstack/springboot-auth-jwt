package com.sample.springbootauth.security.filters;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import static com.sample.springbootauth.security.SecurityConstants.HTTP_AUTHORIZATION_HEADER;
import static com.sample.springbootauth.security.SecurityConstants.SECRET;
import static com.sample.springbootauth.security.SecurityConstants.TOKEN_PREFIX;

/*
 *  Extended the BasicAuthenticationFilter to make Spring replace it in the filter chain with our custom implementation.
 * */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
  private static final Logger logger = Logger.getAnonymousLogger();

  public JWTAuthorizationFilter(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain chain) throws IOException, ServletException {
    String header = request.getHeader(HTTP_AUTHORIZATION_HEADER);

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(request, response);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(request, response);
  }

  /*
   * This method reads the JWT from the Authorization header, and then uses JWT to validate the token.
   * If everything is in place, we set the user in the SecurityContext and allow the request to move on.
   * */
  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HTTP_AUTHORIZATION_HEADER);
    if (token != null) {
      // parse the token.
      DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
        .build()
        .verify(token.replace(TOKEN_PREFIX, ""));

      String uuid = decodedJWT.getClaim("uuid").asString();

      if (uuid != null) {
        return new UsernamePasswordAuthenticationToken(uuid, null, new ArrayList<>()); // TODO what to put here as authenticated user when authorizing?
      }
      return null;
    }
    return null;
  }
}
