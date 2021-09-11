
package com.arg.common.config;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.arg.common.exception.ErrorResponse;
import com.arg.common.utils.MapperUtil;

@Configuration
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {


	@Value("${service.endpoints.excluded}")
	private String[] excludedEndpoints;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		http.csrf().disable()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
		.exceptionHandling()
				.authenticationEntryPoint((request, response, authException) -> this.buildErrorResponse(response)).and()
				.authorizeRequests(
						authz -> authz.antMatchers(excludedEndpoints).permitAll().anyRequest().authenticated())
				.oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken).httpBasic().disable();
	}

	private void buildErrorResponse(HttpServletResponse response) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		ErrorResponse errorResponse = ErrorResponse.builder().errorCode(HttpStatus.UNAUTHORIZED.value())
				.errorDesc(HttpStatus.UNAUTHORIZED.getReasonPhrase())
				.userDesc(HttpStatus.UNAUTHORIZED.getReasonPhrase()).build();
		PrintWriter writer = response.getWriter();
		writer.print(MapperUtil.toJson(errorResponse));
		writer.flush();
	}

}
