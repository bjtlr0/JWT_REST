/**
 * 
 */
package server.rest.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import server.rest.api.service.result.CommonResult;
import server.rest.api.service.result.ListResult;
import server.rest.api.service.result.SingleResult;

/**
 * @author ksh123
 * RestController에 대한 응답 비지니스 로직을 작성할 객체
 * Database를 호출해서 데이터를 가공한다.
 */
@Service
public class ResponseService{
	public enum CommonResponse{
		SUCCESS(0, "성공"),
		FAIL(-1, "실패");
		
		int code;
		String msg;
		
		CommonResponse(int code, String msg){
			this.code = code;
			this.msg = msg;
		}
		
		public int getCode(){
			return code;
		}
		public String getMsg(){
			return msg;
		}
	}
	
	private void setSuccessResponse(CommonResult result){
		result.setSuccess(true);
		result.setCode(CommonResponse.SUCCESS.getCode());
		result.setMsg(CommonResponse.SUCCESS.getMsg());
	}
	
	// Object 없이 결과:성공 만 리턴
	public CommonResult getSuccessResult(){
		CommonResult result = new CommonResult();
		setSuccessResponse(result);
		return result;
	}
	
	// Object 없이 결과:실패 만 리턴
	public CommonResult getFailResult(int code, String msg){
		CommonResult result = new CommonResult();
		result.setSuccess(false);
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}
	
	// 단일 Object 결과물에 대한 성공결과
	public <T> SingleResult<T> getSingleResult(T data){
		SingleResult<T> result = new SingleResult<T>();
		result.setObject(data);
		setSuccessResponse(result);
		return result;
	}
	
	// 다중 Object 결과물에 대한 성공결과
	public <T> ListResult<T> getListResult(List<T> list){
		ListResult<T> resultList = new ListResult<>();
		resultList.setList(list);
		setSuccessResponse(resultList);
		return resultList;
	}
}
