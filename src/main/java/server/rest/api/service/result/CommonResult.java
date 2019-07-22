/**
 * 
 */
package server.rest.api.service.result;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author ksh123
 * controller에 대한 응답 결과의 기본값
 */
@Data
public class CommonResult {
	@ApiModelProperty(value="응답 성공여부 : True / False")
	private boolean success;
	
	@ApiModelProperty(value="응답 코드 : 0보다 작은경우 비정상, 0과 같거나 큰 경우 정상")
	private int code;
	
	@ApiModelProperty(value="응답 메시지")
	private String msg;
}
