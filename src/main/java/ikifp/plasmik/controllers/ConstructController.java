package ikifp.plasmik.controllers;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ikifp.plasmik.model.Construct;
import ikifp.plasmik.services.ConstructService;

@Controller
public class ConstructController {
	@RequestMapping(value="/constructs", method=RequestMethod.GET)
	private String getAllConstructs(Model model, HttpSession session) {
		ConstructService constructService = new ConstructService();
		Collection<Construct> constructst = constructService.getAllConstructs();
		model.addAttribute("constructst", constructst);
		
		return "constructs";
	}
}
