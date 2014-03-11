package ar.com.dcsys.data.device;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import ar.com.dcsys.data.HsqlConnectionProvider;
import ar.com.dcsys.exceptions.DeviceException;

@Named
public class DeviceHsqlDAO implements DeviceDAO {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(DeviceHsqlDAO.class.getName());
	private final HsqlConnectionProvider cp;

	@Inject
	public DeviceHsqlDAO(HsqlConnectionProvider cp) {
		this.cp = cp;
	}

	@PostConstruct
	void init() {
		try {
			createTables();
		} catch (SQLException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
		}
	}
	
	private void createTables() throws SQLException {
		Connection con = cp.getConnection();
		try {
			PreparedStatement st = con.prepareStatement("create table if not exists devices (" +
																"id longvarchar not null primary key, " +
																"name longvarchar not null," +
																"description longvarchar," +
																"ip longvarchar," +
																"netmask longvarchar," +
																"gateway longvarchar," +
																"mac longvarchar," +
																"enabled boolean not null)");
			try {
				st.executeUpdate();
				
			} finally {
				st.close();
			}
		} finally {
			con.close();
		}		
	}	
	

	private void getDevice(Device d, ResultSet rs) throws SQLException {
		d.setId(rs.getString("id"));
		d.setName(rs.getString("name"));
		d.setDescription(rs.getString("description"));
		d.setIp(rs.getString("ip"));
		d.setMac(rs.getString("mac"));
		d.setGateway(rs.getString("gateway"));
		d.setNetmask(rs.getString("netmask"));
		d.setEnabled(rs.getBoolean("enabled"));
	}
	
	@Override
	public Device findById(String id) throws DeviceException {
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from device where id = ?";
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1,id);
					ResultSet rs = st.executeQuery();
					try {
						if (rs.next()) {
							Device d = new DeviceBean();
							getDevice(d,rs);
							return d;
						} else {
							return null;
						}
						
					} finally {
						rs.close();
					}
				
				} finally {
					st.close();
				}
				
			} finally {
				con.close();
			}
			
		} catch (SQLException e) {
			throw new DeviceException(e);
		}
	}

	@Override
	public List<Device> findAll() throws DeviceException {
		
		try {
			Connection con = cp.getConnection();
			try {
				String query = "select * from device";
				PreparedStatement st = con.prepareStatement(query);
				try {
					ResultSet rs = st.executeQuery();
					try {
						List<Device> devices = new ArrayList<Device>();
						while (rs.next()) {
							Device d = new DeviceBean();
							getDevice(d,rs);
							devices.add(d);
						}
						return devices;
						
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
			throw new DeviceException(e);
		}
	}

	@Override
	public String persist(Device d) throws DeviceException {
		
		try {
			Connection con = cp.getConnection();
			try {
				String query;
				if (d.getId() == null) {
					String id = UUID.randomUUID().toString();
					d.setId(id);
					query = "insert into device (name, description, ip, netmask, gateway, mac, enabled, id) values (?,?,?,?,?,?,?,?)";
				} else {
					query = "update device set name = ?, description = ?, ip = ?, netmask = ?, gateway = ?, mac = ?, enabled = ? where id = ?";
				}
				PreparedStatement st = con.prepareStatement(query);
				try {
					st.setString(1, d.getName());
					st.setString(2, d.getDescription());
					st.setString(3, d.getIp());
					st.setString(4, d.getNetmask());
					st.setString(5, d.getGateway());
					st.setString(6, d.getMac());
					st.setBoolean(7, d.getEnabled());
					st.setString(8, d.getId());
					st.executeUpdate();
					
					return d.getId();
					
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (SQLException e) {
			throw new DeviceException(e);
		}
	}

	@Override
	public void remove(Device d) throws DeviceException {
		throw new DeviceException("Not Implemented");
	}
	
}
