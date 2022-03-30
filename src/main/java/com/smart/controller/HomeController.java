package com.smart.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;


@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public String home(Model m) {
		m.addAttribute("title", "Home - Smart Contact Manager");
		return "home";
	}

	@GetMapping("/about")
	public String about(Model m) {
		m.addAttribute("title", "about - Smart Contact Manager");
		return "about";
	}

	@GetMapping("/signup")
	public String signup(Model m) {
		m.addAttribute("title", "signup - Smart Contact Manager");
		m.addAttribute("user", new User());
		return "signup";
	}

	
//	@PostMapping("/process")
//	public String processForm( @Valid @ModelAttribute("loginData") LoginData loginData, BindingResult result) {
//		System.out.println(loginData);
//		
//		if(result.hasErrors()) {
//			
//			System.out.println(result);
//			return "form";
//		}
//		
//		//data process
//		return "success";
//	}
	
	
	// this handler for registering user
	@PostMapping("/do_register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult bResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {

		try {
			if (!agreement) {
				System.out.println("user not aggred with the term and conditions");
				throw new Exception("user not aggred with the term and conditions");
			}
			
			if(bResult.hasErrors()) {
				System.out.println("ERROR" + bResult.toString());
				model.addAttribute("user",user);
				
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnable(true);
			user.setImage("deafult.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			User result = this.userRepository.save(user);

			System.out.println("Agreement: " + agreement);
			System.out.println("User: " + result);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Succesfully Registered", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went Wrong!!" + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}

	//this is custom login
	@GetMapping("/signin")
	public String customLogin(Model model) {
		model.addAttribute("title", "Home - custom login");
		return "login";
	}
}

