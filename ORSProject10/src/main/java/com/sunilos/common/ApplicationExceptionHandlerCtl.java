package com.sunilos.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Handles application propagated exceptions
 *
 * @author DELL
 *
 */
@ControllerAdvice
public class ApplicationExceptionHandlerCtl {

	private static final Logger log = LoggerFactory.getLogger(ApplicationExceptionHandlerCtl.class);

	@ExceptionHandler(value = RuntimeException.class)
	public ORSResponse handleRuntimeException(RuntimeException e){
		log.error("Unhandled RuntimeException in controller", e);
		ORSResponse res = new ORSResponse(false);
		res.addMessage(e.getMessage());
		return res;
	}

}
