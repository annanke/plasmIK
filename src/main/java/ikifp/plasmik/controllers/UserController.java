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

import ikifp.plasmik.model.User;
import ikifp.plasmik.persistence.dto.UserDto;
import ikifp.plasmik.services.UserService;

@Controller
public class UserController {

	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public String getAll(Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			Collection<User> usersList = userService.getAll();
			ArrayList<UserDto> usersDtoList = new ArrayList();
			for (User user : usersList) {
				UserDto userDto = new UserDto(user);
				usersDtoList.add(userDto);
			}
			model.addAttribute("usersDtoList", usersDtoList);

			return "users";
		}
		else {
			return "redirect:/Start";
		}
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public String addUser(@RequestBody User user, Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			UserService userService = new UserService();
			userService.addUser(user);
			model.addAttribute("message", "user created");
			return "users";
		}
		else {
			return "redirect:/Start";
		}

	}

	@RequestMapping(value = "/showAddUserForm", method = RequestMethod.GET)
	public String showAddUserForm(Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			return "addUserForm";
		}
		else {
			return "redirect:/Start";
		}

	}

	@RequestMapping(value = "/addUserForm", method = RequestMethod.POST)
	public String addUser(
			@RequestParam(value = "name", required=true) String name, 
			@RequestParam(value = "login", required=true) String login,
			@RequestParam(value = "password", required=true) String password, 
			@RequestParam(value = "email", required=true) String email, 
			Model model, HttpSession session) {
		if (session.getAttribute("userDto")!=null) {
			User newUser = new User();
			newUser.setName(name);
			newUser.setLogin(login);
			newUser.setEmail(email);
			newUser.setPassword(password);
			
			UserService userService = new UserService();
			userService.addUser(newUser);
			model.addAttribute("message", "user created"); 
			//TODO : spr czy user istnieje w bazie i validacje 
			return "redirect:/users";
		}
		else {
			return "redirect:/Start";
		}
		
	}
}
