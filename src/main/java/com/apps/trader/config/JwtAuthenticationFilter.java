package com.apps.trader.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.apps.trader.serivice.JwtService;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;


@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends  OncePerRequestFilter {

    @Autowired
    private final JwtService jwtService;

    
  

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
               final String authHeader = request.getHeader("Authorization");

                if(authHeader != null && authHeader.startsWith("Bearer ")) {

                String jwtToken = authHeader.substring(7);
                Claims claims = jwtService.extractAllClaims(jwtToken);
                // todo check if token is valid
                String userEmail = String.valueOf(claims.get("email"));
                String authorites = String.valueOf(claims.get("authorites"));

                List<GrantedAuthority> authoritiesList = AuthorityUtils.commaSeparatedStringToAuthorityList(authorites);
                //todo check if security context is empty before updating
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userEmail,null,authoritiesList);
                SecurityContextHolder.getContext().setAuthentication(authToken);


               

        
                }
                filterChain.doFilter(request,response);


}


}
