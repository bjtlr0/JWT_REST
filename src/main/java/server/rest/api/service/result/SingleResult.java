/**
 * 
 */
package server.rest.api.service.result;

import lombok.Data;

/**
 * @author ksh123
 * 응답 데이터가 한건인 경우 응답을 처리할 Result 객체 
 */
@Data
public class SingleResult<T> extends CommonResult {
	private T object;
}
