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

import ar.com.dcsys.data.PostgresSqlConnectionProvider;
import ar.com.dcsys.exceptions.DeviceException;

public class DevicePostgreSqlDAO implements DeviceDAO {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = Logger.getLogger(DevicePostgreSqlDAO.class.getName());
	private final PostgresSqlConnectionProvider cp;

	@Inject
	public DevicePostgreSqlDAO(PostgresSqlConnectionProvider cp) {
		this.cp = cp;
	}

	@PostConstruct
	void init() {
		createTables();
	}
	
	private void createTables() {
		try {
			Connection con = cp.getConnection();
			try {
				PreparedStatement st = con.prepareStatement("create table if not exists devices (" +
																	"id varchar not null primary key, " +
																	"name varchar not null," +
																	"description varchar," +
																	"ip varchar," +
																	"netmask varchar," +
																	"gateway varchar," +
																	"mac varchar," +
																	"enabled boolean not null)");
				try {
					st.execute();
					
				} finally {
					st.close();
				}
			} finally {
				cp.closeConnection(con);
			}
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}	
	

	private Device getDevice(ResultSet rs) throws SQLException {
		Device device = new DeviceBean();
		
		device.setId(rs.getString("id"));
		device.setName(rs.getString("name"));
		device.setDescription(rs.getString("description"));
		device.setIp(rs.getString("ip"));
		device.setMac(rs.getString("mac"));
		device.setGateway(rs.getString("gateway"));
		device.setNetmask(rs.getString("netmask"));
		device.setEnabled(rs.getBoolean("enabled"));
		
		return device;
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
							Device d = getDevice(rs);
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
							Device d = getDevice(rs);
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
