package com.prm;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.prm.service.PrmServiceImpl;

@Component
public class PrmFilter implements Filter {
	private static Logger logger = LoggerFactory
			.getLogger(PrmServiceImpl.class);

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		long startTime = 0l;
		if (logger.isDebugEnabled()) {
			startTime = Calendar.getInstance().getTimeInMillis();
		}
		chain.doFilter(req, res);
		setCorsHeaders(res);
		
		if (logger.isDebugEnabled()) {
			HttpServletRequest httpReq = (HttpServletRequest) req;
			
			StringBuffer reqSB = httpReq.getRequestURL();
			String strQuery = httpReq.getQueryString();
			String method = httpReq.getMethod();
			if (strQuery != null) {
				reqSB.append("?").append(strQuery);
			}
			reqSB.append(" ").append(method);
			logger.debug("Time costs "
					+ (Calendar.getInstance().getTimeInMillis() - startTime)
					+ " for " + reqSB.toString());
		}
	}
	
	private void setCorsHeaders(ServletResponse res) {
		HttpServletResponse response = (HttpServletResponse) res;
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods",
				"POST, GET, PUT, PATCH, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers",
				"x-requested-with, accept, content-type");
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}

}
