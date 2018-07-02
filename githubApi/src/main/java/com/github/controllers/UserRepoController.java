package com.github.controllers;


import java.awt.print.Book;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.github.services.UserRepoService;
import com.github.viewModel.UserRepoInfo;


@RestController
public class UserRepoController {
	
	private static int currentPage = 1;
    private static int pageSize = 5;
    
    
    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = {5, 10, 20};
	
	@Autowired
	private UserRepoService userRepoService;

		@GetMapping("/{userName}")
	    public ModelAndView greeting(@PathVariable("userName") String  userName, Model model,
	    					   @RequestParam("page") Optional<Integer> page, 
	    					   @RequestParam("size") Optional<Integer> size) {
	        page.ifPresent(p -> currentPage = p);
	        size.ifPresent(s -> pageSize = s);
	        
	        ModelAndView modelAndView = new ModelAndView("listBooks");
		
			List<UserRepoInfo> repoInfoList = userRepoService.buildUserRepos(userName);
			model.addAttribute("list", repoInfoList);
			


	        return modelAndView;
	    }
	

	    
	    /*@GetMapping("/user/{userName}")
	    public ModelAndView listBooks(@PathVariable("userName") String  userName,@RequestParam("pageSize") Optional<Integer> pageSize,
	            @RequestParam("page") Optional<Integer> page) {
	    	
	    	 ModelAndView modelAndView = new ModelAndView("listBooks1");
	    	 // Evaluate page size. If requested parameter is null, return initial
	        // page size
	        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
	        // Evaluate page. If requested parameter is null or less than 0 (to
	        // prevent exception), return initial size. Otherwise, return value of
	        // param. decreased by 1.
	        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
	        Page<UserRepoInfo> repoInfoList = userRepoService.buildUserRepos(userName, PageRequest.of(evalPage, evalPageSize));

	        Pager pager = new Pager(repoInfoList.getTotalPages(), repoInfoList.getNumber(), BUTTONS_TO_SHOW);

	        modelAndView.addObject("lists", repoInfoList);
	        modelAndView.addObject("user", userName);
	        modelAndView.addObject("selectedPageSize", evalPageSize);
	        modelAndView.addObject("pageSizes", PAGE_SIZES);
	        modelAndView.addObject("pager", pager);
	        return modelAndView;
	      
	   
	    }
*/
}
