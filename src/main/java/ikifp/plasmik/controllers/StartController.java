package ikifp.plasmik.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ikifp.plasmik.services.LoginService;

@Controller
public class StartController {
	
	@RequestMapping(value={"/", "/Start"}, method=RequestMethod.GET)
	public String displayStart() {
		return "start";
	}
	
	@RequestMapping(value= {"/Start"}, method=RequestMethod.POST)
	public String doLogin(@RequestParam(value="login") String login, @RequestParam(value="password") String password, Model model, HttpSession session) {
		session.setAttribute("userLogin", login);
		session.setAttribute("userPassword", password);
		return "redirect:/Welcome";
	}
	
	@RequestMapping(value={"/Welcome"}, method=RequestMethod.GET)
	public String displayWelcome(Model model, HttpSession session) {
//		if ((session.getAttribute("userLogin")!="" && session.getAttribute("userPassword")!="")) {
		LoginService loginService =new LoginService();
		String userLogin = session.getAttribute("userLogin").toString();
		
		System.out.println("-------------------------------------");
		System.out.println(userLogin);
		System.out.println(session.getAttribute("userPassword").toString());
		System.out.println(loginService.confirmLogin(userLogin));
		System.out.println(loginService.confirmUserPassword(userLogin, session.getAttribute("userPassword").toString()));

		System.out.println("-------------------------------------");
		
		if (loginService.confirmLogin(userLogin) && loginService.confirmUserPassword(userLogin, session.getAttribute("userPassword").toString())) {

			model.addAttribute("message", "Witamy w systemie do zarządzania biblioteką plazmidów");
			return "welcome";
		}else {
			model.addAttribute("message", "Niepoprawne logowanie");
			return "start";
		}
	}
	
	@RequestMapping(value={"/Logout"}, method=RequestMethod.GET)
	public String logout(Model model) {
//		model.addAttribute("message", "Dziękujemy za skorzystanie z plazmiIK");
		return "redirect:/Start";
	}
	
	@RequestMapping(value={"/Register"}, method=RequestMethod.GET)
	public String displayRegisterForm(Model model) {
		return "registerForm";
	}
}
