package com.smart.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepositiory;
import com.smart.entites.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	
	
	@Autowired
	private UserRepositiory userRepositiory;

	@RequestMapping("/")
	public String home(Model model) {

		model.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
	}

	@RequestMapping("/about")
	public String About(Model model) {

		model.addAttribute("title", "About-Smart Contact Manager");
		return "about";
	}
	@RequestMapping("/footer")
	public String footer(Model model) {

		model.addAttribute("Footer", "About-Smart Contact Manager");
		return "footer";
	}

	@RequestMapping("/signup")
	public String signup(Model model,HttpSession session) {
		session.removeAttribute("message");
		model.addAttribute("title", "Register-Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}

	/* Register handler */
	@PostMapping("/do-register")
	public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult bindingResult,
			@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,
			HttpSession session) {
			session.removeAttribute("message");
		try {
			if (!agreement) {
				System.out.println("You have not agreed the terms and conditions !!!");
				throw new Exception("You have not agreed the terms and conditions !!!");
			}
			if(bindingResult.hasErrors())
			{
				System.out.println(bindingResult.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.jpg");
			user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
			

			System.out.println("Agreement" + agreement);
			System.out.println("User" + user);

			User result = this.userRepositiory.save(user);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Registered successfully","alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("Something went wrong"+e.getMessage(),"alert-danger"));
			return "signup";
		}
	}

	@GetMapping("/signin")
	public String customeLogin(Model model) {
		
		model.addAttribute("title", "Login Page");
		return "login";
	}
	
	@GetMapping("/forgot-password")
	public String adminLogin(Model model,HttpSession session) {
		session.removeAttribute("message");

		model.addAttribute("title", "Forgot Password");
		return "forgod_password";
	}
	
	
	@PostMapping("/forgot-password-proccess")
	public String forgotpass(@RequestParam("fakeemail") String email,@RequestParam("fakename") String name,@RequestParam("fakenewpassword") String newPassword,HttpSession session) {
		
	User user = this.userRepositiory.getUserByUserName(email);

	if(user == null)
	{
		session.setAttribute("message", new Message("The email you entered is incorrect. Try again....", "alert-danger"));
		return "forgod_password";
	}
	
	if((name.trim()).equals(user.getName().trim()))
	{
		user.setPassword(bCryptPasswordEncoder.encode(newPassword));
		this.userRepositiory.save(user);
		session.setAttribute("message", new Message("Your password has been updated....", "alert-success"));

		System.out.println(user.getPassword());
	}
	else
	{
		session.setAttribute("message", new Message("The name you entered is incorrect. Try again....", "alert-danger"));
	}
		
		return "forgod_password";
	}
}
