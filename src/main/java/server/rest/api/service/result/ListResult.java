/**
 * 
 */
package server.rest.api.service.result;

import java.util.List;

import lombok.Data;

/**
 * @author ksh123
 * 응답데이터가 한건 이상인 응답을 처리할 Result객체
 */
@Data
public class ListResult<T> extends CommonResult{
	private List<T> list;
}
