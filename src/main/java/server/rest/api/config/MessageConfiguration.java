/**
 * 
 */
package server.rest.api.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

/**
 * @author ksh123
 * Server의 response 메시지의 지역설정 별 변경을 위해 
 * MessageSource객체를 bean으로 등록
 */
@Configuration
public class MessageConfiguration implements WebMvcConfigurer{

	@Bean
	public LocaleResolver localResolver(){
		SessionLocaleResolver slr = new SessionLocaleResolver(); // sessionLocaleResolver로 등록했으니, 요청session에서 locale정보를 읽어오게 된다.
		slr.setDefaultLocale(Locale.KOREAN);
		return slr;
	}
	
	// 지역설정을 변경한다. 요청 시 파라미터에 lang정보를 지정하면 언어가 변경됨.
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor(){
		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
		lci.setParamName("lang");
		return lci;
	}
	
	// registry에 localeChangeInterceptor를 등록한다.
	@Override
	public void addInterceptors (InterceptorRegistry registry){
		registry.addInterceptor(localeChangeInterceptor());
	}
	
	 /**
	  * basename에 지정된 resource경로에 message.properties를 읽어온다.
	  * LocaleResolver를 bean으로 등록했으므로
	  *  session의 locale 정보를 읽어 와서 그에 맞는 properties파일을 읽게 된다.
	  **/
	@Bean
	public MessageSource messageSource(){
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("/messages/message");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
}
