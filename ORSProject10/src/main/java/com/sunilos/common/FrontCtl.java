package com.sunilos.common;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.sunilos.util.JwtUtil;

/**
 * Front controller. Validates a JWT Bearer token on every protected REST
 * request.
 *
 * Which paths are protected is decided declaratively by
 * {@code ORSApp.corsConfigurer()} (addPathPatterns/excludePathPatterns), not
 * here. Requests that aren't dispatched to an {@code @RestController} method
 * (e.g. the static HTML/JS/CSS frontend served from classpath:/public/) are
 * always let through, since only actual API calls need a token.
 *
 * On success the resolved loginId is stored in request attribute "loginId"
 * so {@link BaseCtl#setUserContext} can rebuild the full UserContext.
 *
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS
 */
@Component
public class FrontCtl implements HandlerInterceptor {

	@Autowired
	private JwtUtil jwtUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// Only guard actual controller methods; static resources (the
		// HTML/JS/CSS frontend) are handled by a different HandlerMapping.
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
			return true;
		}

		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || !authHeader.startsWith("Bearer ")) {
			sendUnauthorized(response, "Missing Authorization header");
			return false;
		}

		String token = authHeader.substring(7);
		if (!jwtUtil.validateToken(token)) {
			sendUnauthorized(response, "Invalid or expired token");
			return false;
		}

		request.setAttribute("loginId", jwtUtil.getLoginId(token));
		return true;
	}

	/**
	 * CORS headers are handled centrally by
	 * {@code ORSApp.corsConfigurer().addCorsMappings(...)}, which applies to
	 * every response (including ones this interceptor rejects with 401)
	 * since it runs at the handler-mapping level, before preHandle. Setting
	 * headers here too would be redundant and risks conflicting values.
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) throws Exception {
	}

	private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");
		response.getWriter().write("{\"success\":false,\"result\":{\"message\":\"" + message + "\"}}");
	}

}
