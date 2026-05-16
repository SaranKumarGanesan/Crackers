package com.example.Crackers.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.Crackers.Services.UserProfilesService;



@Controller
public class UserProfilesController {
	
	@Autowired
	private UserProfilesService userProfilesService;
	
	@GetMapping("/userProfiles")
	public String userProfilesPage(Model model) {
		model.addAttribute("userProfile", userProfilesService.getAllUsers());
		return "userProfiles";
		
	}
	

}
