/**
 * 
 */
package server.rest.api.advice;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.omg.CORBA.portable.UnknownException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;
import server.rest.api.advice.exception.CUserNotFoundException;
import server.rest.api.service.ResponseService;
import server.rest.api.service.result.CommonResult;

/**
 * @author ksh123
 * ControllerAdvice : 특정 패키지나 어노테이션을 지정하면 해당 Controller들에 전역으로
 * 적용되는 코드를 작성할 수 있도록 해주는 어노테이션
 */
@RequiredArgsConstructor
@RestControllerAdvice("server.rest.api.controller")  // REST를 사용하는 경우 예외발생 시 JSON형태로 결과를 반환한다.
public class ExceptionAdvice {
	private final ResponseService responseService;
	private final MessageSource messageSource;
	
	@ExceptionHandler(Exception.class) // Exception.class가 예외처리의 최상위 클래스이므로 발생하는 예외 모두 이 exceptionHandler에 걸리게 된다.
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // Exception 발생 시 전달할 httpCode는 500이다.
	protected CommonResult defaultException(HttpServletRequest req, Exception ex){
		/**
		 * 이런식으로 최상위 Exception에서 instanceof를 통해 구분한다면
		 * 각 exception별 ResponseStatus를 정의할 수 없는 불편함이 있다. 
		 * */
		CommonResult result = new CommonResult();
		if(ex instanceof UsernameNotFoundException){
			result = responseService.getFailResult(Integer.parseInt(""), "");
		}else if(ex instanceof NullPointerException){
			result = responseService.getFailResult(Integer.parseInt(""), "");
		}else if(ex instanceof IOException){
			result = responseService.getFailResult(Integer.parseInt(""), "");
		}else if(ex instanceof UnknownException){
			result = responseService.getFailResult(Integer.parseInt(""), "");
		}
		return result;
	}
	
	
	/**
	 * @param req
	 * @param ex
	 * @return
	 * @작성일 : 2019. 7. 9.
	 * @작성자 : ksh123
	 * @변경이력 :
	 * @MethodUsage : UserNotFoundException Custom Handler
	 * @Description : ExceptionClass를 지정하여 Custom Message를 전달할 수 있다.
	 * 근데 굳이 500error를 주지 않아도 될 것 같은뎅?
	 */
	@ExceptionHandler(CUserNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest req, CUserNotFoundException ex){
		return responseService.getFailResult(Integer.valueOf(getMessage("userNotFound.code")), getMessage("userNotFound.msg"));
	}
	
	@ExceptionHandler(NullPointerException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult nullPointerException(HttpServletRequest req, NullPointerException ex){
		return responseService.getFailResult(Integer.valueOf(getMessage("nullPointer.code")), getMessage("nullPointer.msg"));
	}
	
	private String getMessage(String code){
		return getMessage(code, null);
	}
	
	private String getMessage(String code, Object[] args){
		return messageSource.getMessage(code,  args, LocaleContextHolder.getLocale());
	}
}
