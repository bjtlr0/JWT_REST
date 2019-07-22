/**
 * 
 */
package server.rest.api.advice.exception;

/**
 * @author ksh123
 * ControllerAdvice에서 Exception.class를 핸들링 하고 있으나
 * 이와 같이 별도의 exception 클래스를 커스텀하여 메시지를 조정할 수 있다.
 */
public class CUserNotFoundException extends RuntimeException{
	public CUserNotFoundException (String msg, Throwable t){
		super(msg, t);
	}
	public CUserNotFoundException (String msg){
		super(msg);
	}
	public CUserNotFoundException (){
		super();
	}

	
}
