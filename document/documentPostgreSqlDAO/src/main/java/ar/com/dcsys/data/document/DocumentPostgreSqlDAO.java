package ar.com.dcsys.data.document;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import ar.com.dcsys.exceptions.DocumentException;
import ar.com.dcsys.exceptions.DocumentNotFoundException;
import ar.com.dcsys.persistence.JdbcConnectionProvider;

public class DocumentPostgreSqlDAO implements DocumentDAO {

	private static final Logger logger = Logger.getLogger(DocumentPostgreSqlDAO.class.getName());
	private final JdbcConnectionProvider cp;
	
	@Inject
	public DocumentPostgreSqlDAO(JdbcConnectionProvider cp) {
		this.cp = cp;
	}
	
	@PostConstruct
	void init() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists documents ("
						+ "id varchar not null primary key,"
						+ "name varchar not null,"
						+ "description varchar,"
						+ "mimetype varchar,"
						+ "encoding varchar,"
						+ "content bytea not null,"
						+ "created timestamp not null default now())");
				try {
					st.execute();
					
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
		}
	}
	
	
	@Override
	public void persist(Document d) throws DocumentException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "";
				String id = d.getId();
				
				if (id == null) {
					id = UUID.randomUUID().toString();					
					query = "insert into documents (name,description,created,mimetype,encoding,content,id) values (?,?,?,?,?,?,?)";
				} else {
					query = "update documents set name = ?, description = ?, created = ?, mimetype = ?, encoding = ?, content = ?) where id = ?";
				}
				
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, d.getName());
					st.setString(2, d.getDescription());
					st.setTimestamp(3, new Timestamp(d.getCreated().getTime()));
					st.setString(4, d.getMimeType());
					st.setString(5, d.getEncoding());
					st.setBytes(6, d.getContent());
					st.setString(7, id);
					st.executeUpdate();
					
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new DocumentException(e);
		}
	}

	@Override
	public List<String> findAll() throws DocumentException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select id from documents";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<String> ids = new ArrayList<>();
						while (rs.next()) {
							String id = rs.getString("id");
							ids.add(id);
						}
						return ids;
						
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
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new DocumentException(e);
		}
	}
	
	/**
	 * Obtiene un documento desde un resultSet.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private Document getDocument(ResultSet rs) throws SQLException {
		DocumentBean d = new DocumentBean();
		d.setId(rs.getString("id"));
		d.setCreated(new Date(rs.getTimestamp("created").getTime()));
		d.setName(rs.getString("name"));
		d.setDescription(rs.getString("description"));
		d.setEncoding(rs.getString("encoding"));
		d.setMimeType(rs.getString("mimetype"));
		d.setContent(rs.getBytes("content"));
		return d;
	}

	@Override
	public Document findById(String id) throws DocumentException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from documents where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next() == false) {
							throw new DocumentNotFoundException(id);
						}
						Document d = getDocument(rs);
						return d;

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
			logger.log(Level.SEVERE, e.getMessage(), e);
			throw new DocumentException(e);
		}
	}

	
}
