package ikifp.plasmik.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ikifp.plasmik.model.Construct;
import ikifp.plasmik.model.Project;
import ikifp.plasmik.model.User;
import ikifp.plasmik.services.ConstructService;
import ikifp.plasmik.services.ProjectService;
import ikifp.plasmik.services.UserService;

@Controller
public class ConstructController {
	
	String pathInProject = "src/main/resources/static/files/";
	
	@RequestMapping(value="/constructs", method=RequestMethod.GET)
	private String getAllConstructs(@RequestParam(value = "message", required=false) String message, Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			ConstructService constructService = new ConstructService();
			Collection<Construct> constructsList = constructService.getAllConstructs();
			model.addAttribute("constructsList", constructsList);
			model.addAttribute("message", message);
			return "constructs";
		}else {
			return "redirect:/Start";
		}
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
			@RequestParam(value = "originDNA", required=false) String originDNA,
			@RequestParam(value = "primers", required=false) String primers,
			@RequestParam(value = "comment", required=false) String comment,
			@RequestParam(value="mapFile", required=false) MultipartFile mapFile,
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
			newConstruct.setOriginDNA(originDNA);
			newConstruct.setPrimers(primers);
			newConstruct.setComment(comment);
			constructService.addConstruct(newConstruct);
			
			try {
				byte[] mapFileBytes;
				mapFileBytes = mapFile.getBytes();
				Path path = Paths.get(pathInProject+((Long)newConstruct.getId()).toString()+"_map_"+constructName+".pdf");
				Files.write(path, mapFileBytes);
			} catch (IOException e) {
				e.printStackTrace();
			}
	        
			return "redirect:/constructs";
		}else {
			return "redirect:/Start";
		}
	}
		
	@RequestMapping(value="/deleteConstruct", method=RequestMethod.POST)
	private String deleteConstruct(
			@RequestParam(value = "constructId", required=true) long constructId,
			RedirectAttributes redirectAttributes,
			Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			ConstructService constructService = new ConstructService();
			constructService.deleteConstruct(constructId);
			redirectAttributes.addAttribute("message", "construct deleted");
			return "redirect:/constructs";
		}else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value="/constructDetails", method=RequestMethod.GET)
	private String showConstructDetailsm(
			@RequestParam(value = "constructId", required=true) long constructId,
			Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			ConstructService constructService = new ConstructService();
			model.addAttribute("construct", constructService.findConstructById(constructId));
			
			return "constructDetails";
		}else {
			return "redirect:/Start";
		}
	}
	@RequestMapping(value = "/constructDetails/{constructId}/pdf/{constructId}_map_{constructName}.pdf", method = RequestMethod.GET)
	public ResponseEntity<byte[]> showPdf(Model model, HttpSession session,
			@PathVariable(value = "constructId") String constructId,
			@PathVariable(value = "constructName") String constructName) throws IOException {

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.parseMediaType("application/pdf"));
		String filename = constructId +"_map_"+constructName+ ".pdf";

		headers.add("content-disposition", "inline;filename=" + filename);
		Path path = Paths.get(pathInProject + constructId +"_map_"+constructName+ ".pdf".toString());

		byte[] pdfAsBytes = Files.readAllBytes(path);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(pdfAsBytes, headers, HttpStatus.OK);
		return response;
	}
}
