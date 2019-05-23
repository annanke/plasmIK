package ikifp.plasmik.controllers;

import java.io.File;
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

import org.springframework.core.io.ByteArrayResource;
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
	
	@RequestMapping(value = "/constructForm", method = RequestMethod.GET)
	public String showEditConstruct(Model model, HttpSession session,
			@RequestParam(value = "message", required=false) String message,
			@RequestParam(value = "constructId", required=false) Long constructId,
			RedirectAttributes redirectAttributes){
		if (session.getAttribute("userDto")!=null) {
			
			model.addAttribute("message", message);
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			
			if ( constructId != null) {
				ConstructService constructService = new ConstructService();
				Construct construct = constructService.findConstructById(constructId);
				model.addAttribute("construct", construct);
				model.addAttribute("creationDate", df.format(construct.getDate()).toString());
				redirectAttributes.addAttribute("constructId", constructId);
				
			} else {
				model.addAttribute("construct", null);
				redirectAttributes.addAttribute("constructId", null);
				
				Calendar today = Calendar.getInstance();
				Date todayDate = today.getTime();
				model.addAttribute("creationDate", df.format(todayDate).toString());
			}
			
			UserService userService = new UserService();
			Collection<User> usersList = userService.getAll();
			model.addAttribute("usersList", usersList);
			
			ProjectService projectService = new ProjectService();
			Collection<Project> projectList = projectService.getAllProjects();
			model.addAttribute("projectList", projectList);
					
		return "addOrEditConstruct";
		} else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value="/constructForm", method=RequestMethod.POST)
	private String addConstruct(
			@RequestParam(value = "constructId", required=false) Long constructId,
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
			@RequestParam(value="sequenceFile", required=false) MultipartFile sequenceFile,
			RedirectAttributes redirectAttributes,
			Model model, HttpSession session) {
		
		if (session.getAttribute("userDto")!=null) {
			
			Construct construct = new Construct();
			ConstructService constructService = new ConstructService();
			ProjectService projectService = new ProjectService();
			UserService userService = new UserService();
			
			construct.setConstructName(constructName);
			construct.setProject(projectService.findProjectById(projectId));
			construct.setUser(userService.findUserById(userId));		
			construct.setPlazmidName(plazmidName);
			construct.setInsertSequence(insertSequence);
			construct.setOriginDNA(originDNA);
			construct.setPrimers(primers);
			construct.setComment(comment);
			
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar today = Calendar.getInstance();
			Date todayDate = today.getTime();
			
			boolean isValid = true;
			String errorMessage = "";
			
			if ( dateString==null || dateString.isEmpty()) {
				errorMessage = "please provide the date";
				isValid = false ;
				//dateString = format.format(todayDate);
			} else {
				try {
					Date constructCreationDate = (Date)format.parse(dateString);
					if ( !constructCreationDate.after(todayDate) ) {
						construct.setDate(constructCreationDate);
						isValid = true ;
					
					} else {
						errorMessage = "you tried to create a construct from the future!!! try again with correct date";
						isValid = false ;
					}
					
				} catch (ParseException e) {
					 e.printStackTrace();
					System.out.println("problem with date of construct");
				}
			}
			
			if (isValid == false) {
				Collection<User> usersList = userService.getAll();
				model.addAttribute("usersList", usersList);
				Collection<Project> projectList = projectService.getAllProjects();
				model.addAttribute("projectList", projectList);
				model.addAttribute("message", errorMessage);
				model.addAttribute("construct", construct);
				model.addAttribute("creationDate", dateString);
				return "addOrEditConstruct";
			}
					
			if (constructId == null) {
				constructService.addConstruct(construct);
			} else {
				construct.setId(constructId);
				constructService.updateConstruct(construct);
			}
						
			// adding gene map in pdf file 
			savingMapFile(mapFile, construct);
			
			// save on disk file with sequence
			try {
				if (sequenceFile.getBytes().length > 0) {
					savingFile(sequenceFile, construct);
					constructService.updateConstruct(construct);
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("problem with saving seq file");
			}
			 
			return "redirect:/constructs";
		}else {
			return "redirect:/Start";
		}
	}

	private void savingMapFile(MultipartFile mapFile, Construct construct) {
		try {
			//TODO check if it is pdf file!
			byte[] mapFileBytes = mapFile.getBytes();
			if(mapFileBytes.length>0) {
				Path path = Paths.get(pathInProject+((Long)construct.getId()).toString()+"_map_"+construct.getConstructName()+".pdf");
				Files.write(path, mapFileBytes);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("problem with saving map file");
		}
	}

	private void savingFile(MultipartFile file, Construct construct) {
		try {
			byte[] sequenceFileBytes = file.getBytes();
			if(sequenceFileBytes.length>0) {
				String sequenceFile = pathInProject+((Long)construct.getId()).toString()+"_seq_"+construct.getConstructName()+"_"+file.getOriginalFilename();
				construct.setSequenceFileName(sequenceFile);
				Path path = Paths.get(sequenceFile);
				Files.write(path, sequenceFileBytes);			
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("problem with saving sequence file");
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
			Construct construct = constructService.findConstructById(constructId);
			model.addAttribute("construct", construct);
			
			//check if map exist - if yes, it will be displayed below construct data
			File mapFile = new File(pathInProject + constructId +"_map_"+construct.getConstructName()+ ".pdf".toString());
			model.addAttribute("mapFileExist", mapFile.exists());
			
			//check if sequence file exist - if yes, it will be displayed below construct data
			//File dir = new File(directory);
			//File[] files = dir.listFiles((dir1, name) -> name.startsWith("temp") && name.endsWith(".txt"));
			File dir = new File(pathInProject);
			File[] seqFileList = dir.listFiles((dir1, name) -> name.startsWith(((Long)construct.getId()).toString()+"_seq_"+construct.getConstructName()));
			
			model.addAttribute("seqFileExist", seqFileList.length>0);
			
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
	
	@RequestMapping(value = "/addSequence", method = RequestMethod.POST)
	public String addSequenceFile(
			@RequestParam(value = "sequenceFile", required=false) MultipartFile sequenceFile,
			@RequestParam(value = "constructId", required=true) long constructId,
			Model model, HttpSession session,
			RedirectAttributes redirectAttributes){
		if (session.getAttribute("userDto")!=null) {
			ConstructService constructService = new ConstructService();
			Construct construct = constructService.findConstructById(constructId);
			try {
				if (sequenceFile.getBytes().length > 0) {
					savingFile(sequenceFile, construct);
					constructService.updateConstruct(construct);
					redirectAttributes.addAttribute("message", "sequence file added");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
				System.out.println("problem with saving seq file");
				redirectAttributes.addAttribute("message", "problem with saving seq file");
			}
			redirectAttributes.addAttribute("constructId", constructId);
			return "redirect:/constructDetails";
		}else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/downloadSequence", method = RequestMethod.GET)
	public ResponseEntity<ByteArrayResource> downloadSequenceFile(Model model, HttpSession session,
			@RequestParam(value = "constructId", required=true) long constructId) throws IOException {
		
		ConstructService constructService = new ConstructService();
		Construct construct = constructService.findConstructById(constructId);
		
		Path path = Paths.get(construct.getSequenceFileName());
		byte[] sequenceBytes = Files.readAllBytes(path);
		ByteArrayResource sequenceByteArrayResource = new ByteArrayResource(sequenceBytes);
		
		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path.getFileName().toString())
				.contentLength(sequenceBytes.length).body(sequenceByteArrayResource);
/*		if (session.getAttribute("userDto")!=null) {
			ConstructService constructService = new ConstructService();
			Construct construct = constructService.findConstructById(constructId);
			//TODO implement file downloading
			return "constructDetails";
		}else {
			return "redirect:/Start";
		}*/
	}
	
	@RequestMapping(value = "/addMapFile", method = RequestMethod.POST)
	public String addMapFile(
			@RequestParam(value = "mapFile", required=false) MultipartFile mapFile,
			@RequestParam(value = "constructId", required=true) long constructId,
			Model model, HttpSession session,
			RedirectAttributes redirectAttributes){
		if (session.getAttribute("userDto")!=null) {
			ConstructService constructService = new ConstructService();
			Construct construct = constructService.findConstructById(constructId);
			
			savingMapFile(mapFile, construct);
			redirectAttributes.addAttribute("constructId", constructId);
			return "redirect:/constructDetails";
		}else {
			return "redirect:/Start";
		}
	}
	

}
