/**
 * 
 */
package server.rest.api.config.security;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;

/**
 * @author ksh123
 * 포인트 : 암호화 알고리즘, 비밀키, claim
 * Provider 객체를 component로 선언했으니,
 * JwtToken을 사용하는 객체에 jwtToken을 제공하기 위한 클래스
 * 사용자 정보 확인을 위해 UserDetailsService를 포함하고 있다.
 */
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
	@Value("spring.jwt.secret")
	private String secretKey;
	
	private long tokenValidMilisecond = 1000L * 60 * 60; // 1시간
	
	private final UserDetailsService userDetailsService;
	
	/**
	 * 
	 * @작성일 : 2019. 7. 10.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : 객체 초기화
	 * @Description : 객체 초기화 시암호키를 인코딩한다.
	 * 암호화가 아니라 인코딩임.
	 * PostConstruct : 현재 객체에서 의존하는 객체를 설정한 이후 초기화를 수행할 메서드를 지정
	 * 유사한 어노테이션 PreDestroy : 컨테이너 객체를 제거하기 전에 호출 될 메서드를 지정
	 */
	@PostConstruct
	protected void init(){
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}
	
	/**
	 * @param userPk
	 * @param roles
	 * @return
	 * @작성일 : 2019. 7. 10.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : 토큰 생성 함수
	 * @Description : Claims객체는 io.jsonwebtoken:jjwt 플러그인 적용 시 사용 가능
	 * 사용자의 일반정보를 토큰에 추가하기 위해 사용한다.
	 * secretKey 문자열에 암호화 알고리즘을 적용해서 token에 적재한다.
	 */
	public String createToken(String userPk, List<String> roles){
		Claims claims = Jwts.claims().setSubject(userPk);
		claims.put("roles", roles);
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(now)
				.setExpiration(new Date(now.getTime() + tokenValidMilisecond))
				.signWith(SignatureAlgorithm.HS256, secretKey)
				.compact();
	}
	
	/**
	 * @param token
	 * @return
	 * @작성일 : 2019. 7. 10.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : 암호화 된 토큰에서 사용자 고유값 가져오기
	 * @Description : 서버에 저장된 비밀키를 이용해서 사용자 Token을 복호화해서 사용자 정보를 알아낸다.
	 */
	public String getUserPk(String token){
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}
	
	/**
	 * @param req
	 * @return
	 * @작성일 : 2019. 7. 10.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : HttpServlet request에 x-auth-token 파싱
	 * @Description : JwtToken은 X-AUTH-TOKEN이라는 헤더에 저장되는 값으로 httpServletResolver를 등록하여
	 * 해당 헤더값을 가져온다.
	 */
	public String resolveToken(HttpServletRequest req){
		return req.getHeader("X-AUTH-TOKEN");
	}
	
	/**
	 * @param token
	 * @return
	 * @작성일 : 2019. 7. 10.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : 토큰으로 인증정보 가져오기
	 * @Description : 토큰을 이용해서 사용자 인증정보 조회한다.
	 */
	public Authentication getAuthentication(String token){
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
		// UsernamePasswordAuthenticationToken의 두번째 인자는 credentials, 일반 string값으로 무시해도 상관없음
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}
	
	/**
	 * @param jwtToken
	 * @return
	 * @작성일 : 2019. 7. 10.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : JWT Token 의 유효성 검증
	 * @Description : expired여부와 secretKey적용 여부 확인
	 */
	public boolean validateToken(String jwtToken){
		try{
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		}catch(Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
	}
}
