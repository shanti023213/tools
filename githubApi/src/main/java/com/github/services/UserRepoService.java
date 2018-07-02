package com.github.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.github.viewModel.UserRepoInfo;

@Service
public class UserRepoService {
	
	@Value( "${github.url}" )
	private String githubBaseUrl;
	private final RestTemplate restTemplate;
	public static final String  nextPagecriteria="rel=\"next\"";
	public static final String lastPageCriteria="rel=\"last\"";
	public static final String LINK_HEADER="Link";
	public static final String USERS="/users";
	public static final String PER_PAGE="?per_page=";
	public static final String REPOS="/repos";
	public static final String SLASH="/";
	public static final String PULLS ="/pulls";
	public List<UserRepoInfo> list ;
	
	@Autowired
	public UserRepoService(RestTemplateBuilder restTemplateBuilder, @Value("${github.userName}") String username ,@Value("${github.password}") String password ) {
		this.restTemplate = restTemplateBuilder.basicAuthorization(username, password).build();
	}
	
	
	public List<UserRepoInfo> buildUserRepos(String userName){
		
		//if(list== null || list.isEmpty()){
			list = fetchUserRepoInfo(userName);
		//}
		
			return list;
		//return findPaginated(pageable,list);
		
	}

	public List<UserRepoInfo> fetchUserRepoInfo(String userName){
		
		//Increase the per page and see if that increases performance
		String url = buildUrl(userName, 0);
		ArrayList<UserRepoInfo> list = new ArrayList<UserRepoInfo>();
		populateRepositoryInfo(url, list);
		
		System.out.println("Totoal Repositories"+list.size());
		for (UserRepoInfo userRepo : list) {
			String pullUrl = buildUrlOpenPullRequests(userName, userRepo.getName(), 1);
			ResponseEntity<UserRepoInfo[]> response = restTemplate.getForEntity(pullUrl, UserRepoInfo[].class);
			String lastUrl = fetchNextUrl(response.getHeaders().get(LINK_HEADER), lastPageCriteria);
			int openPullRequests = 0;
			if (lastUrl == null) {
				 openPullRequests = 0;
				System.out.println("Open pull requests for " + userRepo.getName() + "=" + 0);
			} else {
				int pageNumber = Integer.valueOf(pageNumber(lastUrl));
				 openPullRequests = pageNumber;
				System.out.println("Open pull requests for " + userRepo.getName() + "=" + openPullRequests);

				System.out.println("Open pull requests for " + userRepo.getName() + "=" + pageNumber);
			}
			userRepo.setNoOfOpenPullReq(openPullRequests);
		}
		
		Collections.sort(list, new SortByOpenPulls());
		
		
		return list;
		
		
	}
	
	private String buildUrl(String userName, int pageSize){
		
		return githubBaseUrl+USERS+SLASH+userName+REPOS+PER_PAGE+pageSize;
	}
	
	private String buildUrlOpenPullRequests(String userName,String repoName, int pageSize){
		
		return githubBaseUrl+REPOS+SLASH+userName+SLASH+repoName+PULLS+PER_PAGE+pageSize;
	}
	
	private void populateRepositoryInfo(String url, ArrayList<UserRepoInfo> list){
		ResponseEntity<UserRepoInfo[]> response = getResponseEntity(url);
		ArrayList<UserRepoInfo> pageList = new ArrayList<UserRepoInfo>(Arrays.asList(response.getBody()));
		list.addAll(pageList);
		
		url = fetchNextUrl(response.getHeaders().get(LINK_HEADER),nextPagecriteria );
		if(url!=null){
			populateRepositoryInfo(url,list);
		}
	}
	  
	private String fetchNextUrl(List<String> link, String criteria) {
		if (link == null)
			return null;

		String token = link.get(0);
		String url = null;

		for (String str : token.split(", ")) {
			if (str.endsWith(criteria)) {
				int idx = str.indexOf('>');
				url = str.substring(1, idx);
				return url;
			}
		}
		return url;
	}
        
        
        public String pageNumber(String url){
            
                String[] params = url.split("&");  
                Map<String, String> map = new HashMap<String, String>();  
                for (String param : params)  
                { 
                	String[] keyValue = param.split("=", 2);
                	
                	String key = keyValue[0];
                	String value = keyValue[1];
                	if (!key.isEmpty()) {
                		map.put(key, value);
                    }
               }  
               
                return map.get("page");  
            } 
        
        
        public ResponseEntity<UserRepoInfo[]>getResponseEntity(String url){
        	return restTemplate.getForEntity(url, UserRepoInfo[].class);
        }
        
        
        public Page<UserRepoInfo> findPaginated(Pageable pageable, List<UserRepoInfo> userList) {
            int pageSize = pageable.getPageSize();
            int currentPage = pageable.getPageNumber();
            int startItem = currentPage * pageSize;
            List<UserRepoInfo> list;
     
            if (userList.size() < startItem) {
                list = Collections.emptyList();
            } else {
                int toIndex = Math.min(startItem + pageSize, userList.size());
                list = userList.subList(startItem, toIndex);
            }
     
            Page<UserRepoInfo> bookPage
              = new PageImpl<UserRepoInfo>(list, PageRequest.of(currentPage, pageSize), userList.size());
     
            return bookPage;
        }
        }
        
        
	
	


