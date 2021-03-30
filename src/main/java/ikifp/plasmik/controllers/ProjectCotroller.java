package ikifp.plasmik.controllers;

import java.util.Collection;

import javax.servlet.http.HttpSession;
import javax.swing.SortOrder;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ikifp.plasmik.model.Project;
import ikifp.plasmik.model.User;
import ikifp.plasmik.services.ProjectService;
import ikifp.plasmik.services.UserService;

@Controller
public class ProjectCotroller {
	
	@RequestMapping(value="/projects", method=RequestMethod.GET)
	private String getAllProjects(@RequestParam(value = "message", required=false) String message, 
			@RequestParam(value= "sortByProperty", required=false, defaultValue="projectName") String sortByProperty,
			@RequestParam(value= "selectedOrder", required=false, defaultValue="ASCENDING") SortOrder selectedOrder,
			Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			
			model.addAttribute("nextSortOrder", selectedOrder==selectedOrder.ASCENDING?selectedOrder.DESCENDING:selectedOrder.ASCENDING);
			ProjectService projectService = new ProjectService();
			Collection<Project> projectsList = projectService.getAllProjects(sortByProperty, selectedOrder);
			model.addAttribute("projectsList", projectsList);
			model.addAttribute("message", message);
			
			return "projects";
		}
		else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/addProjectForm", method = RequestMethod.GET)
	public String showAddUserForm(@RequestParam(value = "message", required=false) String message, Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			Collection<User> usersList = userService.getAll();
			model.addAttribute("usersList", usersList);
			model.addAttribute("message", message);
			return "addProjectForm";
		}else {
			return "redirect:/Start";
		}
	}

	@RequestMapping(value = "/addProjectForm", method = RequestMethod.POST)
	public String addUser(
			@RequestParam(value = "projectName", required=true) String projectName, 
			@RequestParam(value = "ownerId", required=false) long ownerId,
			RedirectAttributes redirectAttributes,
			Model model, HttpSession session) {
		
		if (session.getAttribute("userDto")!=null) {
			ProjectService projectService = new ProjectService();
			if (projectService.findProjectByName(projectName)==null) {
				Project newProject = new Project();
				UserService userService = new UserService();
				newProject.setProjectName(projectName);
				newProject.setUser(userService.findUserById(ownerId));
				projectService.addProject(newProject);
				redirectAttributes.addAttribute("message", "project created");
				return "redirect:/projects";
			}else {
				redirectAttributes.addAttribute("message", "project with the given name: '"+projectName+"' already exists");
				return "redirect:/addProjectForm";
			}
		}
		else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/editProjectForm", method = RequestMethod.GET)
	public String showEditProjectForm(
			@RequestParam(value = "projectId", required=true) long projectId,
			//@RequestParam(value = "message", required=false) String message, 
			Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			ProjectService projectService = new ProjectService();

			Collection<User> usersList = userService.getAll();
			model.addAttribute("usersList", usersList);
			model.addAttribute("project", projectService.findProjectById(projectId));
			//model.addAttribute("message", message);
			return "editProjectForm";
		}
		else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/editProjectForm", method = RequestMethod.POST)
	public String updateProject(
			@RequestParam(value = "projectId", required=true) long projectId,
			@RequestParam(value = "projectName", required=true) String projectName,
			@RequestParam(value = "ownerId", required=false) long ownerId,
			RedirectAttributes redirectAttributes, 
			Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			//UserService userService = new UserService();
			ProjectService projectService = new ProjectService();
			String editionResult = projectService.editProjectData(projectId, projectName, ownerId);
			model.addAttribute("message", editionResult);
			return "redirect:/projects";
		}
		else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/deleteProject", method = RequestMethod.POST)
	public String deleteProject(
			@RequestParam(value = "projectId", required=true) long projectId,
			RedirectAttributes redirectAttributes,
			Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			ProjectService projectService = new ProjectService();
			projectService.deleteProject(projectId);
			redirectAttributes.addAttribute("message", "project deleted");
			return "redirect:/projects";
		}
		else {
			return "redirect:/Start";
		}
	}
}
