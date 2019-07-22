/**
 * 
 */
package server.rest.api.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import server.rest.api.advice.exception.CUserNotFoundException;
import server.rest.api.database.mapper.MemberMapperDao;

/**
 * @author ksh123
 *
 */
@RequiredArgsConstructor
@Service
public class CUserDetailsService implements UserDetailsService{
	private final MemberMapperDao memberDao;
	
	@Override
	public UserDetails loadUserByUsername(String userPk) throws UsernameNotFoundException {
		return Optional.ofNullable(memberDao.selectMember(Integer.parseInt(userPk))).orElseThrow(CUserNotFoundException::new);
	}
}
