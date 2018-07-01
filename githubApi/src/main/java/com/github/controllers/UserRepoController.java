package com.github.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.services.UserRepoService;
import com.github.viewModel.UserRepoInfo;

@Controller
public class UserRepoController {
	
	
	@Autowired
	private UserRepoService userRepoService;

		@GetMapping("/{userName}")
	    public String greeting(@PathVariable("userName") String  userName, Model model) {
			
			System.out.println("******");
			List<UserRepoInfo> repoInfoList = userRepoService.fetchUserRepoInfo(userName);
			
			model.addAttribute("name", userName);
	        return "greeting";
	    }

	

}
