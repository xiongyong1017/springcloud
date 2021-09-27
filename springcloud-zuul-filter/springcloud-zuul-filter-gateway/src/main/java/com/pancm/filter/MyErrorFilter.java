package com.pancm.filter;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.netflix.zuul.filters.post.SendErrorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import com.netflix.zuul.context.RequestContext;




/**
 * 
* @Title: MyErrorFilter
* @Description: 自定义异常过滤器实现类
* @Version:1.0.0  
* @Since: jdk1.8
* @author pancm
* @date 2019年5月7日
 */
@Component
public class MyErrorFilter extends SendErrorFilter{

	@Override
	public Object run() {
		String msg="请求失败！";
		//重写 run方法		
		try{
			RequestContext ctx = RequestContext.getCurrentContext();
			//直接复用异常处理类
			ExceptionHolder exception = findZuulException(ctx.getThrowable());
			System.out.println("错误信息:"+exception.getErrorCause());
			msg+="error:"+exception.getErrorCause();
			System.out.println("msg:"+msg);

			HttpServletResponse response = ctx.getResponse();
			//设置编码
			response.setCharacterEncoding("UTF-8");
			response.getOutputStream().write(msg.getBytes());           	 
		}catch (Exception ex) {
			ex.printStackTrace();
			ReflectionUtils.rethrowRuntimeException(ex);
		}
		return msg;
	}
	
	/**
	 *  自定义异常过滤器
	 */
	@Bean
	//@ConditionalOnProperty(name="zuul.SendErrorFilter.error.disable")
	public MyErrorFilter errorFilter() {
	    return new MyErrorFilter();
	}
	

}
