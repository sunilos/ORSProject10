package com.sunilos.common;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * Front controller verifies if user id logged in
 * 
 * @author SunilOS
 * @version 1.0
 * @Copyright (c) SunilOS *
 */
@Component
public class FrontCtl extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String path = request.getServletPath();
		System.out.println("Front Ctl Called " + path);

		if (!path.startsWith("/Auth/")) {
			HttpSession session = request.getSession();
			if (session.getAttribute("userContext") == null) {
				response.setContentType("application/json");
				response.setStatus(HttpServletResponse.SC_OK);
				PrintWriter out = response.getWriter();
				out.print("{\"success\":\"false\",\"error\":\"OOPS! Your session has been expired\"}");
				out.close();
				return false;
			}
		}
		return true;
	}

}
