package com.revworkforce.security;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.revworkforce.util.JwtUtil;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;

	public JwtAuthFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String path = request.getServletPath();

		if (path.equals("/auth/login") || path.equals("/login") || path.startsWith("/css/") || path.startsWith("/js/")
				|| path.startsWith("/images/")) {

			filterChain.doFilter(request, response);
			return;
		}

		final String header = request.getHeader("Authorization");

		String token = null;
		String email = null;

		if (header != null && header.startsWith("Bearer ")) {

			token = header.substring(7);

			try {
				email = jwtUtil.extractUsername(token);
			} catch (ExpiredJwtException e) {
				System.out.println("JWT Token expired. Please login again.");
			} catch (Exception e) {
				System.out.println("Invalid JWT Token.");
			}
		}

		if (email != null && SecurityContextHolder.getContext().getAuthentication() == null
				&& jwtUtil.validateToken(token)) {

			UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());

			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

			SecurityContextHolder.getContext().setAuthentication(authentication);

			System.out.println("Authenticated user: " + email);
			System.out.println("Authorities: " + userDetails.getAuthorities());
		}

		filterChain.doFilter(request, response);
	}
}