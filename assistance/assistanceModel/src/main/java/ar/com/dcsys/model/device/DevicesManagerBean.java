package ar.com.dcsys.model.device;

import java.io.IOException;
import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;

import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.device.DeviceDAO;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;

public class DevicesManagerBean implements DevicesManager {

	private final DeviceDAO deviceDAO;
	
	@Inject
	public DevicesManagerBean(DeviceDAO deviceDAO) {
		this.deviceDAO = deviceDAO;
	}
	
	@Override
	public List<String> findAll() throws DeviceException {
		return deviceDAO.findAll();
	}

	@Override
	public Device findById(String id) throws DeviceException {
		return deviceDAO.findById(id);
	}

	@Override
	public void persist(Device d) throws DeviceException {
		deviceDAO.persist(d);
	}
	
	@Override
	public void enroll(String personId, final EnrollManager em) throws PersonException, DeviceException {
		
		// por ahora solo existe un reloj. asi que se usa el primero de la lista para enrolar
		List<String> devices = findAll();
		if (devices == null || devices.size() <= 0) {
			throw new DeviceException("No existe ningun device para enrolar");
		}
		
		String id = devices.get(0);
		Device device = findById(id);
		
		if (!device.getEnabled()) {
			throw new DeviceException("Dispositivo no habilitado");
		}
		
		String ip = device.getIp();
		String url = "ws://" + ip + ":8025/websocket/cmd";
		URI uri = URI.create(url);
		
		
		EnrollWebsocketClient ewc = new EnrollWebsocketClient(personId, em);
				
		try {
			ContainerProvider.getWebSocketContainer().connectToServer(ewc, uri);
			
		} catch (DeploymentException e) {
			e.printStackTrace();
			throw new DeviceException(e);


		} catch (IOException e) {
			e.printStackTrace();
			throw new DeviceException(e);

		}
		
		
	}
	
	
}
