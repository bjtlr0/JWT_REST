/**
 * 
 */
package server.rest.api.controller;

import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import server.rest.api.advice.exception.CUserNotFoundException;
import server.rest.api.database.mapper.MemberMapperDao;
import server.rest.api.database.vo.MemberVO;
import server.rest.api.service.ResponseService;
import server.rest.api.service.result.CommonResult;
import server.rest.api.service.result.ListResult;
import server.rest.api.service.result.SingleResult;

/**
 * @author ksh123
 * SWAGGER를 적용한 RestController 문서 작성
 * ResponseService 객체를 통해서 요청에 대한 응답을 처리
 *  
 * 리소스 사용목적에 따른 HTTP Method 구분
 * GET : 서버에 주어진 리소스정보를 요청(읽기)
 * POST : 서버에 리소스를 전달(쓰기)
 * PUT : 서버에 리소스를 전달(수정)
 * DELETE : 서버에 리소스를 삭제 요청(삭제)
 */

@Api(tags={"1. Members API "}) // 여기 '#'을 넣으면 expand기능 동작 안함
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class MemberController {
	@Autowired
	private final ResponseService responseService;
	
	@Autowired
	private final MemberMapperDao memberMapperDao;
	
	/**
	 * @param userName
	 * @return
	 * @작성일 : 2019. 7. 8.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : get메소드 단일 회원 조회
	 * @Description : 회원 ID로 단일회원을 조회한다.
	 */
	@ApiOperation(value="회원 조회", notes="회원 ID(username)을 통해서 멤버를 조회한다.")
	@GetMapping(path="/members/{username}")
	public SingleResult<MemberVO> getMembers(@ApiParam(value="회원 ID", required=true) @PathVariable("username") String userName,
			@ApiParam(value="언어설정",defaultValue="ko") @RequestParam String lang){
		return responseService.getSingleResult(
				// Optional을 통해서 객체의 null여부를 확인하고 null인 경우 Throw할 수 있도록 한다. java 1.8부터 사용 가능
				Optional
					.ofNullable(memberMapperDao.selectMemberByUserId(userName))
					.orElseThrow(CUserNotFoundException::new));
	}
	
	/**
	 * @return
	 * @작성일 : 2019. 7. 8.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : get메소드 전체 회원 조회
	 * @Description : 파라미터 없이 전체 목록을 조회한 값을 리턴한다.
	 */
	@ApiOperation(value="회원 목록 조회", notes="모든 회원 목록을 조회한다.")
	@GetMapping(path="/members/list")
	public ListResult<MemberVO> getMembersList(){
		return responseService.getListResult(memberMapperDao.getMemberList());
	}
	
	/**
	 * @param userName
	 * @param userPassword
	 * @return
	 * @작성일 : 2019. 7. 8.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : Post메소드로 회원 가입 
	 * @Description : Post메소드로 userName,userPassword 파라미터를 받는다.
	 */
	@ApiOperation(value="회원 가입", notes="UserName과 Userpassword로 신규 회원을 추가한다.")
	@PostMapping(value="/members")
	public CommonResult registMembers(@ApiParam(value="회원 아이디", required=true) @RequestParam String userName,
			@ApiParam(value="회원 패스워드", required=true) @RequestParam String userPassword){
		MemberVO member = new MemberVO();
		member.setUserName(userName);
		member.setUserPassword(userPassword);
		memberMapperDao.insertMember(member);
		return responseService.getSuccessResult();	
	}
	
	/**
	 * 작성을 하다보니, 사용자 패스워드 변경에 대해 의문이 생겼다.
	 * 회원 정보를 수정하기 위해서는 put을 사용하도록 하는 규칙인데,
	 * put으로 패스워드 전달 시 변경 패스워드가 노출될 위험이 있다.
	 * 
	 * */
	
	@ApiOperation(value="회원 정보 수정", notes="MemberVO Json 객체를 입력받아서 회원 정보를 수정한다.")
	@PutMapping(value="/members/{}")
	public CommonResult updateMemers(@ApiParam(value="회원 Index", required=true) @RequestParam int index, 
			@ApiParam(value="회원 아이디", required=true) @RequestParam String userName){
		// id, username, modifytime
		// without password, createTime, roles
		MemberVO member = new MemberVO();
		member.setId(index);
		member.setUserName(userName);
		Date d = new Date(System.currentTimeMillis());
		member.setModifyTime(d);
		memberMapperDao.updateMember(member);
		return responseService.getSuccessResult();	
	}
	
	
	/**
	 * @param idx
	 * @return
	 * @작성일 : 2019. 7. 8.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : delete메소드로 회원 탈퇴
	 * @Description : Member엔티티의 고유값인 Index정보로 회원을 탈퇴한다.
	 */
	@ApiOperation(value="회원 탈퇴", notes="회원 INDEX값으로 탈퇴한다.")
	@DeleteMapping(value="/members/{idx}")
	public CommonResult deleteMembers (@ApiParam(value="회원 Index", required=true) @PathVariable("idx") int idx){
		memberMapperDao.deleteMember(idx);
		return responseService.getSuccessResult();	
	}
}
