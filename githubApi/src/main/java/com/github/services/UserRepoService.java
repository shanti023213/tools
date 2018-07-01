package com.github.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.viewModel.UserRepoInfo;

@Service
public class UserRepoService {
	
	@Value( "${github.url}" )
	private String githubBaseUrl;
	


	private final RestTemplate restTemplate;
	

	public List<UserRepoInfo> fetchUserRepoInfo(String userName){
		
		String url = buildUrl(userName, 0);
		
		ResponseEntity<UserRepoInfo[]> response = restTemplate.getForEntity(url, UserRepoInfo[].class);
		return null;
		
		
	}

	
	private String buildUrl(String userName, int pageSize){
		
		return githubBaseUrl+"/users/"+userName+"/repos?per_page="+pageSize;
	}
	
	@Autowired
	public UserRepoService(RestTemplateBuilder restTemplateBuilder, @Value("${github.userName}") String username ,@Value("${github.password}") String password ) {
		this.restTemplate = restTemplateBuilder.basicAuthorization(username, password).build();
	}

}
