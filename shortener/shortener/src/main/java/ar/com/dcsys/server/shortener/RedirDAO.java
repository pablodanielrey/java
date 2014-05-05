package ar.com.dcsys.server.shortener;

import java.io.IOException;
import java.util.List;

public interface RedirDAO {

	public String persist(Redir r) throws IOException;
	public Redir findByUrl(String r) throws IOException;
	public List<Redir> findAll() throws IOException;
	
	
}
