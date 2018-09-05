package controllers.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import services.ConfigurationService;
import services.CustomerService;

@Controller
@RequestMapping("/jquery")
public class RestController {
	@Autowired
	private ConfigurationService configurationService;
	@Autowired
	private CustomerService customerService;

	
	@RequestMapping(value="/hostCheck")
	public @ResponseBody Boolean ajaxPasskeyCheck(HttpServletRequest req, HttpServletResponse res) {
		String passKey =this.configurationService.findPassKey();
		String companyKey = customerService.findOne(Integer.parseInt(req.getParameter("id"))).getPassKey();
		return passKey.equals(companyKey);
	}
}