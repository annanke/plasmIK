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
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	private String getAll(Model model, HttpSession session) {
		UserService userService = new UserService();
		Collection<User> usersList = userService.getAll();
		ArrayList<UserDto> usersDtoList = new ArrayList();
		for (User user : usersList) {
			UserDto userDto = new UserDto();
			userDto.setId(user.getId());
			userDto.setEmail(user.getEmail());
			userDto.setName(user.getName());
			userDto.setAdmin(user.isAdmin());
			usersDtoList.add(userDto);
		}
		model.addAttribute("usersDtoList", usersDtoList);
		
		return "users";
	}
	
	@RequestMapping(value="/users", method=RequestMethod.POST)
	private String addUser(@RequestBody User user, Model model, HttpSession session) {
		UserService userService = new UserService();
		userService.addUser(user);
		model.addAttribute("message", "user created");
		return "users";
	}
	
	@RequestMapping(value="/RegisterForm", method=RequestMethod.GET)
	private String showRegistrationForm(Model model, HttpSession session) {
		return "registerForm";
	}
}
