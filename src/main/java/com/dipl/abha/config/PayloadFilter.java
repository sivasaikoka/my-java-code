package com.dipl.abha.config;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class PayloadFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			if ("POST".equals(httpRequest.getMethod())) {
				CapturingRequestWrapper requestWrapper = new CapturingRequestWrapper(httpRequest);
				String payload = requestWrapper.getCapturedPayload();
				requestWrapper.setAttribute("Payload", payload);
				chain.doFilter(requestWrapper, response);
			} else {
				chain.doFilter(request, response);
			}
		}
	}

	@Override
	public void destroy() {
	}

	private static class CapturingRequestWrapper extends HttpServletRequestWrapper {
		private final String capturedPayload;

		public CapturingRequestWrapper(HttpServletRequest request) throws IOException {
			super(request);
			InputStream inputStream = request.getInputStream();
			capturedPayload = new BufferedReader(new InputStreamReader(inputStream)).lines()
					.collect(Collectors.joining(System.lineSeparator()));
		}

		public String getCapturedPayload() {
			return capturedPayload;
		}

		@Override
		public ServletInputStream getInputStream() throws IOException {
			return new ServletInputStreamWrapper(capturedPayload.getBytes());
		}
	}

	private static class ServletInputStreamWrapper extends ServletInputStream {
		private final InputStream sourceStream;

		public ServletInputStreamWrapper(byte[] content) {
			this.sourceStream = new ByteArrayInputStream(content);
		}

		@Override
		public int read() throws IOException {
			return sourceStream.read();
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener listener) {
		}
	}
}
