package ikifp.plasmik.controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ikifp.plasmik.model.User;
import ikifp.plasmik.persistence.dto.UserDto;
import ikifp.plasmik.services.UserService;

@Controller
public class UserController {

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String getAll(@RequestParam(value = "message", required=false) String message, Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			Collection<User> usersList = userService.getAll();
			ArrayList<UserDto> usersDtoList = new ArrayList();
			for (User user : usersList) {
				UserDto userDto = new UserDto(user);
				usersDtoList.add(userDto);
			}
			model.addAttribute("usersDtoList", usersDtoList);
			model.addAttribute("message", message);
			return "users";
		}
		else {
			return "redirect:/Start";
		}
	}
	
/*	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public String addUser(@RequestBody User user, Model model, HttpSession session) {
			UserService userService = new UserService();
			userService.addUser(user);
			model.addAttribute("message", "user created");
			return "users";
	}
*/
	
	@RequestMapping(value = "/deleteUser", method = RequestMethod.POST)
	public String deleteUser(@RequestParam(value="userId") long id, RedirectAttributes redirectAttributes, Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			String deletionMessage = userService.deleteUser(id);
			redirectAttributes.addAttribute("message", deletionMessage);
			return "redirect:/users";
		}
		else {
			return "redirect:/Start";
		}
	}

	@RequestMapping(value = "/showAddUserForm", method = RequestMethod.GET)
	public String showAddUserForm(@RequestParam(value = "message", required=false) String message, Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			model.addAttribute("message", message);
			return "addUserForm";
		}
		else {
			return "redirect:/Start";
		}
	}

	@RequestMapping(value = "/addUserForm", method = RequestMethod.POST)
	public String addUser(
			@RequestParam(value = "name", required=true) String name, 
//			@RequestParam(value = "login", required=true) String login,
			@RequestParam(value = "password", required=true) String password, 
			@RequestParam(value = "email", required=true) String email,
			RedirectAttributes redirectAttributes,
			Model model, HttpSession session) {
		
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			if (userService.findUserByEmail(email)==null) {
				User newUser = new User();
				newUser.setName(name);
				//newUser.setLogin(login);
				newUser.setEmail(email);
				newUser.setPassword(password);
								
				userService.addUser(newUser);
				redirectAttributes.addAttribute("message", "user created");
				return "redirect:/users";
			}else {
				redirectAttributes.addAttribute("message", "user with the given address: '"+email+"' already exists");
				return "redirect:/showAddUserForm";
			}
		}
		else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/showEditUserForm", method = RequestMethod.GET)
	public String showEditUserForm(
			@RequestParam(value="userId") long id, 
			@RequestParam(value = "message", required=false) String message, Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			model.addAttribute("userDto", userService.findUserDtoById(id));
			model.addAttribute("message", message);
			return "editUserForm";
		}
		else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/editUser", method = RequestMethod.POST)
	public String editUser(
			@RequestParam(value="userId") long id, 
			@RequestParam(value="userName") String name,
			@RequestParam(value="userEmail") String email,
			@RequestParam(value="userRole", required=false) boolean role,
			RedirectAttributes redirectAttributes, Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			String editionResult = userService.editUserData(id, name, email, role);
			redirectAttributes.addAttribute("message", editionResult);
			return "redirect:/users";
		}
		else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/changePass", method = RequestMethod.POST)
	public String changePassword(
		HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			
			return "redirect:/users";
		}
		else {
			return "redirect:/Start";
		} 
	}
	
	@RequestMapping(value = "/RegistrationForm", method = RequestMethod.POST)
	public String registerUser(
			@RequestParam(value = "name", required=true) String name, 
//			@RequestParam(value = "login", required=true) String login,
			@RequestParam(value = "password", required=true) String password, 
			@RequestParam(value = "email", required=true) String email,
			RedirectAttributes redirectAttributes,
			Model model, HttpSession session) {
		
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			if (userService.findUserByEmail(email)==null) {
				User newUser = new User();
				newUser.setName(name);
				//newUser.setLogin(login);
				newUser.setEmail(email);
				newUser.setPassword(password);
								
				userService.addUser(newUser);
				redirectAttributes.addAttribute("message", "user created");
				return "redirect:/users";
			}else {
				redirectAttributes.addAttribute("message", "user with the given address: '"+email+"' already exists");
				return "redirect:/showAddUserForm";
			}
		}
		else {
			return "redirect:/Start";
		}
	}
}
