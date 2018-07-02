package com.github.viewModel;

public class UserRepoInfo {
		private String url;
		private String name;
		private int NoOfOpenPullReq;
	    
	   public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getNoOfOpenPullReq() {
			return NoOfOpenPullReq;
		}
		public void setNoOfOpenPullReq(int noOfOpenPullReq) {
			NoOfOpenPullReq = noOfOpenPullReq;
		}


}
