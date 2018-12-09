
package utilities;

import domain.Actor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.servlet.ModelAndView;
import services.ActorService;
import services.ConfigurationService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HandlerInterceptor extends org.springframework.web.servlet.i18n.LocaleChangeInterceptor {

	@Autowired
	ConfigurationService	configurationService;
	@Autowired
	ActorService	actorService;


	@Override
	public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		super.postHandle(request, response, handler, modelAndView);
if(modelAndView!=null) {
	modelAndView.addObject("enterpriseLogo", this.configurationService.findLogo());
	try {
		Actor actor = actorService.findByPrincipal();
		Assert.notNull(actor, "msg.not.loged.block");	
		modelAndView.addObject("enterpriseLogo", actor.getCustomer().getLogo());		
	} catch (final Throwable oops) {
		modelAndView.addObject("enterpriseLogo", this.configurationService.findLogo());		
	}
	modelAndView.addObject("nameB", this.configurationService.findName());	
}
	}

}
