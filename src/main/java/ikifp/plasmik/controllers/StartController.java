package ikifp.plasmik.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ikifp.plasmik.services.LoginService;
import ikifp.plasmik.services.UserService;

@Controller
public class StartController {
	
	@RequestMapping(value={"/", "/Start"}, method=RequestMethod.GET)
	public String displayStart() {
		return "start";
	}
	
	@RequestMapping(value= {"/Start"}, method=RequestMethod.POST)
	public String doLogin(@RequestParam(value="email") String email, @RequestParam(value="password") String password, Model model, HttpSession session) {
		LoginService loginService = new LoginService();
		UserService userService = new UserService();
		
		if (loginService.confirmUserEmailAndPassword(email, password)) {
			session.setAttribute("userDto", userService.findUserByEmail(email));
			return "redirect:/Welcome";
		}
		model.addAttribute("message", "Niepoprawne logowanie");
		return "start";
	}
	
	@RequestMapping(value={"/Welcome"}, method=RequestMethod.GET)
	public String displayWelcome(Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			model.addAttribute("message", "Witamy w systemie do zarządzania biblioteką plazmidów");
			return "welcome";
		}
		else {
			return "redirect:/Start";
		}

	}
	
	@RequestMapping(value={"/Logout"}, method=RequestMethod.GET)
	public String logout(Model model, HttpSession session) {
		//session.removeAttribute("userDto");
		session.invalidate();
		model.addAttribute("message", "Dziękujemy za skorzystanie z plazmiIK");
		return "start";
	}
	
	@RequestMapping(value="/RegisterForm", method=RequestMethod.GET)
	public String showRegistrationForm(Model model, HttpSession session) {
		return "registerForm";
	}
	
	@RequestMapping(value="/ForgotData", method=RequestMethod.GET)
	public String showForgotDataForm(Model model, HttpSession session) {
		return "forgotData";
	}
}
