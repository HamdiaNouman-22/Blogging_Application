package com.blogapp.bloggingapplication.security;

import com.blogapp.bloggingapplication.security.JwtTokenHelper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
public class JWTAuthenticationFIlter extends OncePerRequestFilter {
//    private final UserDetailsService userDetailsService;
    private final CustomerDetailsService customerDetailsService;
    private final JwtTokenHelper jwtTokenHelper;
    public JWTAuthenticationFIlter(CustomerDetailsService customerDetailsService, JwtTokenHelper jwtTokenHelper) {
        this.customerDetailsService = customerDetailsService;
        this.jwtTokenHelper=jwtTokenHelper;
//            super.setAuthenticationManager(authenticationManager);
    }
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)throws IOException,ServletException{
        String token=request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username=jwtTokenHelper.extractUsername(token);
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
               UserDetails userDetails=customerDetailsService.loadUserByUsername(username);
               if (jwtTokenHelper.validateToken(token,userDetails)) {
                   UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                           userDetails, null, userDetails.getAuthorities());
                   auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                   SecurityContextHolder.getContext().setAuthentication(auth);
               }
               }
            }
        chain.doFilter(request,response);

    }
}
//@Component
//public class JWTAuthenticationFIlter extends OncePerRequestFilter {
//@Autowired
//    private JwtTokenHelper jwtTokenHelper;
//@Autowired
//    private UserDetailsService userDetailsService;
//@Override
//protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//        throws ServletException, IOException {
//    String requestToken = request.getHeader("Authorization");
//    String username = null;
//    String token = null;
//
//    if (requestToken != null && requestToken.startsWith("Bearer")) {
//        token = requestToken.substring(7);
//        try {
//            username = jwtTokenHelper.getUsernameFromToken(token);
//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
//            if (username != null && jwtTokenHelper.validateToken(token, userDetails)) {
//                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
//                        userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } catch (IllegalArgumentException e) {
//            System.out.println("Unable to get JWT token");
//        } catch (ExpiredJwtException e) {
//            System.out.println("Expired JWT token");
//        } catch (MalformedJwtException e) {
//            System.out.println("Invalid JWT token");
//        }
//    }
//
//    chain.doFilter(request, response);
//}
//}
//
