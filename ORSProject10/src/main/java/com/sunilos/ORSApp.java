package com.sunilos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.sunilos.common.FrontCtl;

@SpringBootApplication
@EnableWebMvc
// @ComponentScan(basePackages = { "com.sunilos" })
// @EntityScan(basePackages = { "com.sunilos" })
public class ORSApp extends SpringBootServletInitializer {

	@Autowired
	private Environment env;

	@Autowired
	FrontCtl frontCtl;

	public static void main(String[] args) {
		SpringApplication.run(ORSApp.class, args);
	}

	/**
	 * Enables CORS to all urls
	 * 
	 * @return
	 */
	@Bean
	public WebMvcConfigurer corsConfigurer() {

		WebMvcConfigurer w = new WebMvcConfigurer() {

			/**
			 * Add CORS
			 */
			public void addCorsMappings(CorsRegistry registry) {
				CorsRegistration cors = registry.addMapping("/**");
				cors.allowedOrigins("http://localhost:4200");
				cors.allowedHeaders("*");
				cors.allowCredentials(true);
			}

			/**
			 * Add Interceptors
			 */
			@Override
			public void addInterceptors(InterceptorRegistry registry) {
				registry.addInterceptor(frontCtl).addPathPatterns("/**").excludePathPatterns("/Auth/**");
			}

			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/**").addResourceLocations("classpath:/public/");
			}

		};

		return w;
	}

}
