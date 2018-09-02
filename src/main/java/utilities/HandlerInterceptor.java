
package utilities;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public class HandlerInterceptor extends org.springframework.web.servlet.i18n.LocaleChangeInterceptor {

	@Autowired
	services.ConfigurationService	configurationService;


	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);

		modelAndView.addObject("enterpriseLogo", this.configurationService.findLogo());
		modelAndView.addObject("nameB", this.configurationService.findName());
	}

}
