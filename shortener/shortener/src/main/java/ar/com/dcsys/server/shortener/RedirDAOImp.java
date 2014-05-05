package ar.com.dcsys.server.shortener;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.persistence.JdbcConnectionProvider;

public class RedirDAOImp implements RedirDAO {

	private static final Logger logger = Logger.getLogger(RedirDAOImp.class.getName());
	private final JdbcConnectionProvider cp;
	
	@Inject
	public RedirDAOImp(JdbcConnectionProvider cp) {
		this.cp = cp;
	}

	
	@PostConstruct
	private void createTables() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists shortener_redirs ("
																+ "url varchar not null primary key,"
																+ "redir varchar not null"
																+ ")");
				try {
					st.executeUpdate();
					
				} finally {
					st.close();
				}
				
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	@Override
	public String persist(Redir r) throws IOException {
		
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("insert into shortener_redirs (url,redir) values (?,?)");
				try {
					st.setString(1,r.getUrl());
					st.setString(2,r.getRedir());
					st.executeUpdate();
		
					return r.getUrl();
					
				} finally {
					st.close();
				}
				
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			logger.log(Level.WARNING,e.getMessage(),e);
			throw new IOException(e);
		}
		
	}

	
	private Redir getRedir(ResultSet rs) throws SQLException {
		String url = rs.getString("url");
		String r = rs.getString("redir");
		Redir redir = new Redir(url,r);
		return redir;
	}
	
	@Override
	public Redir findByUrl(String r) throws IOException {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select * from shortener_redirs where url = ?");
				try {
					st.setString(1,r);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							return getRedir(rs);
						} else {
							throw new  IOException("Url Not found");
						}
						
					} finally {
						rs.close();
					}
					
				} finally {
					st.close();
				}
				
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			logger.log(Level.WARNING,e.getMessage(),e);
			throw new IOException(e);
		}
	}

	@Override
	public List<Redir> findAll() throws IOException {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("select * from shortener_redirs");
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Redir> r = new ArrayList<Redir>();
						while (rs.next()) {
							r.add(getRedir(rs));
						};
						return r;
						
					} finally {
						rs.close();
					}
					
				} finally {
					st.close();
				}
				
			} finally {
				cp.closeConnection(con);
			}
			
		} catch (SQLException e) {
			logger.log(Level.WARNING,e.getMessage(),e);
			throw new IOException(e);
		}
	}
	
}
