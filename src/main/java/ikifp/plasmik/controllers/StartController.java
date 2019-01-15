package ikifp.plasmik.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StartController {
	
	@RequestMapping(value={"/", "/Start"}, method=RequestMethod.GET)
	public String displayStart() {
		return "start";
	}
	
	@RequestMapping(value= {"/Start"}, method=RequestMethod.POST)
	public String doLogin(@RequestParam(value="login") String login, Model model, HttpSession session) {
		session.setAttribute("userLogin", login);
		return "redirect:/Welcome";
	}
	
	@RequestMapping(value={"/Welcome"}, method=RequestMethod.GET)
	public String displayWelcome(Model model, HttpSession session) {
		model.addAttribute("message", "Witamy w systemie do zarządzania biblioteką plazmidów");
		return "welcome";
	}
	
	@RequestMapping(value={"/Logout"}, method=RequestMethod.GET)
	public String logout(Model model) {
//		model.addAttribute("message", "Dziękujemy za skorzystanie z plazmiIK");
		return "start";
	}
	
	@RequestMapping(value={"/Register"}, method=RequestMethod.GET)
	public String displayRegisterForm(Model model) {
//		model.addAttribute("message", "Dziękujemy za skorzystanie z plazmiIK");
		return "registerForm";
	}
}
