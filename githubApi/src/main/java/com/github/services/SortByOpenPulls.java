package com.github.services;

import java.util.Comparator;

import com.github.viewModel.UserRepoInfo;

public class SortByOpenPulls implements Comparator<UserRepoInfo>{
	
	    public int compare(UserRepoInfo a, UserRepoInfo b)
	    {
	        return b.getNoOfOpenPullReq()-a.getNoOfOpenPullReq();
	    }
	

}
