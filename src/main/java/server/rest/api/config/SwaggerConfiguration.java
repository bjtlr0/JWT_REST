/**
 * 
 */
package server.rest.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author ksh123
 *
 */

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
	@Bean
	public Docket swaggerApi(){
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(swaggerInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("server.rest.api.controller"))
				.paths(PathSelectors.any())
				.build()
				.useDefaultResponseMessages(false); // 기본 세팅되는 httpStatus 메시지를 표시하지 않는다.
	}
	
	private ApiInfo swaggerInfo(){
		return new ApiInfoBuilder().title("SpringBoot RESTful API Documentation")
				.description("RESTful API에 대한 설명문서입니다.")
				.license("bjtlr0").licenseUrl("").version("1").build();
	}
}
