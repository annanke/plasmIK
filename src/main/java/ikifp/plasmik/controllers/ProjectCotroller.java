package ikifp.plasmik.controllers;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ikifp.plasmik.model.Project;
import ikifp.plasmik.services.ProjectService;

@Controller
public class ProjectCotroller {
	
	@RequestMapping(value="/projects", method=RequestMethod.GET)
	private String getAllProjects(Model model, HttpSession session) {
		ProjectService projectService = new ProjectService();
		Collection<Project> projectsList = projectService.getAllProjects();
		model.addAttribute("projectsList", projectsList);
		
		return "projects";
	}
}
