package ikifp.plasmik.controllers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import ikifp.plasmik.model.Construct;
import ikifp.plasmik.model.Project;
import ikifp.plasmik.model.User;
import ikifp.plasmik.services.ConstructService;
import ikifp.plasmik.services.ProjectService;
import ikifp.plasmik.services.UserService;

@Controller
public class ConstructController {
	@RequestMapping(value="/constructs", method=RequestMethod.GET)
	private String getAllConstructs(@RequestParam(value = "message", required=false) String message, Model model, HttpSession session) {
		ConstructService constructService = new ConstructService();
		Collection<Construct> constructsList = constructService.getAllConstructs();
		model.addAttribute("constructsList", constructsList);
		model.addAttribute("message", message);
		return "constructs";
	}
	
	@RequestMapping(value="/addConstructForm", method=RequestMethod.GET)
	private String showAddConstructForm(Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			Collection<User> usersList = userService.getAll();
			model.addAttribute("usersList", usersList);
			
			ProjectService projectService = new ProjectService();
			Collection<Project> projectList = projectService.getAllProjects();
			model.addAttribute("projectList", projectList);
			
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			Calendar today = Calendar.getInstance();
			Date todayDate = today.getTime();
			model.addAttribute("todayDate", df.format(todayDate).toString());
			
			return "addConstructForm";
		}else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value="/addConstructForm", method=RequestMethod.POST)
	private String addConstructForm(
			@RequestParam(value = "constructName", required=true) String constructName,
			@RequestParam(value = "projectId", required=false) long projectId,
			@RequestParam(value = "userId", required=false) long userId,
			@RequestParam(value = "date", required=false) String dateString,
			@RequestParam(value = "plazmidName", required=false) String plazmidName,
			@RequestParam(value = "insertSequence", required=true) String insertSequence,
			@RequestParam(value = "primers", required=false) String primers,
			@RequestParam(value = "comment", required=false) String comment,
			Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			Construct newConstruct = new Construct();
			ConstructService constructService = new ConstructService();
			ProjectService projectService = new ProjectService();
			UserService userService = new UserService();
			
			newConstruct.setConstructName(constructName);
			newConstruct.setProject(projectService.findProjectById(projectId));
			newConstruct.setUser(userService.findUserById(userId));
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			try {
				newConstruct.setDate((Date)format.parse(dateString));
			} catch (ParseException e) {
				 e.printStackTrace();
				System.out.println(e.toString());
			}
			newConstruct.setPlazmidName(plazmidName);
			newConstruct.setInsertSequence(insertSequence);
			newConstruct.setPrimers(primers);
			newConstruct.setComment(comment);
			constructService.addConstruct(newConstruct);
			return "redirect:/constructs";
		}else {
			return "redirect:/Start";
		}
	}
}
