package com.sunilos.common;

import java.io.PrintWriter;

import javax.management.RuntimeErrorException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
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

		HttpSession session = request.getSession();
		String path = request.getServletPath();

		System.out.println(" Front Ctl Called " + path);
		System.out.println(" Session ID " + session.getId());
		System.out.println("Usercontext " + session.getAttribute("userContext"));

		if (!path.startsWith("/Auth/")) {
			if (session.getAttribute("userContext") == null) {
				// throw new RuntimeException("OOPS! Your session has been
				// expired");

				/*
				 * response.setContentType("application/json");
				 * response.setStatus(HttpServletResponse.SC_OK);
				 * 
				 * //response.setHeader("Access-Control-Allow-Origin", "*");
				 * //response.setHeader("Access-Control-Allow-Credentials",
				 * "true");
				 * 
				 * response.setHeader("Access-Control-Allow-Origin", "*");
				 * response.setHeader("Access-Control-Allow-Credentials",
				 * "true"); response.setHeader("Access-Control-Allow-Methods",
				 * "GET,HEAD,OPTIONS,POST,PUT");
				 * response.setHeader("Access-Control-Allow-Headers",
				 * "set-cookie,Access-Control-Allow-Headers, Origin,Accept, X-Requested-With, Content-Type, Access-Control-Request-Method, Access-Control-Request-Headers"
				 * );
				 * 
				 * PrintWriter out = response.getWriter(); out.
				 * print("{\"success\":\"false\",\"error\":\"OOPS! Your session has been expired\"}"
				 * ); out.close();
				 */
				// return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		response.setHeader("Access-Control-Allow-Origin", "SunilOS");
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "GET,HEAD,OPTIONS,POST,PUT");
	}

}
