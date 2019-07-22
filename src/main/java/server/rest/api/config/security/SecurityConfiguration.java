/**
 * 
 */
package server.rest.api.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

/**
 * @author ksh123
 *
 */
@RequiredArgsConstructor
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	private final JwtTokenProvider jwtTokenProvider;
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception{
		return super.authenticationManagerBean();
	}
	
	/* (non-Javadoc)
	 * Test용 Plugin인 swagger를 SpringSecurity에서 제외시킨다.
	 */
	@Override
	public void configure(WebSecurity web){
		web.ignoring().antMatchers("/swagger-ui.html", "/swagger-resources/**", "/swagger/**");
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{
		http
			.httpBasic().disable() //disable안할 경우 로그인폼 화면이 자동으로 표시된다.
			.csrf().disable() // restAPI 특성상 csrf에 대한 보안이 필요 없음
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // token기반으로 세션사용 안함
			.and()
				.authorizeRequests() // 인증을 확인할 요청을 작성
					.antMatchers("/*/singIn", "/*/singUp").permitAll() // 로그인, 회원가입은 모두 접근인가
					.antMatchers(HttpMethod.GET, "helloWorld/**").permitAll() // helloWorld 하위의 모든 링크에 get으로 접근하는 경우는 모두 접근인가
					.anyRequest().hasRole("USER") // 나머지 모든 링크는 ROLE_USER를 확인하여 접근인가
			.and()
				.addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class); // jwtToken필터를 userpassword확인필터 전에 넣는다.
	}
}
