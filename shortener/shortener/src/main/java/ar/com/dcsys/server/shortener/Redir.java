package ar.com.dcsys.server.shortener;

public class Redir {

	private String url;
	private String redir;

	public Redir(String url, String redir) {
		this.url = url;
		this.redir = redir;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getRedir() {
		return redir;
	}
	
	public void setRedir(String redir) {
		this.redir = redir;
	}
	
	
}
