/**
 * 
 */
package server.rest.api.config.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

/**
 * @author ksh123
 * JwtTokenProvider를 적용하기 위한 GenericFilter 구현체
 * Annotation을 등록하지 않는다.
 */
public class JwtAuthenticationFilter extends GenericFilterBean{
	private JwtTokenProvider jwtTokenProvider;
	
	/**
	 * @param jwtTokenProvider
	 * Custom 작성한 JwtTokenProvider를 Filter에 적용한다. 
	 */
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider){
		this.jwtTokenProvider = jwtTokenProvider;
	}
	
	/* (non-Javadoc)
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 * 
	 * req/res 필터에 JwtToken을 검증하는 로직을 추가한다.
	 * provider를 통해 헤더의 token을 추출 및 검증한다.
	 * 검증된 토큰은 SecurityContextHolder에 등록한다.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		if(token != null && jwtTokenProvider.validateToken(token)){
			Authentication auth = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		chain.doFilter(request, response);
	}

}
