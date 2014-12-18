package ar.com.dcsys.model.device;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.websocket.CloseReason;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import ar.com.dcsys.assistance.server.AttLogSerializer;
import ar.com.dcsys.auth.server.FingerprintSerializer;
import ar.com.dcsys.data.device.Device;
import ar.com.dcsys.data.device.DeviceDAO;
import ar.com.dcsys.data.fingerprint.FingerprintDAO;
import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.AttLogException;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.FingerprintException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.device.GenericWebsocketClient.WebsocketClient;
import ar.com.dcsys.model.log.AttLogsManager;
import ar.com.dcsys.person.server.PersonSerializer;
import ar.com.dcsys.security.Fingerprint;

public class DevicesManagerBean implements DevicesManager {

	private static final Logger logger = Logger.getLogger(DevicesManagerBean.class.getName());
	
	private final FingerprintDAO fingerprintDAO;
	private final DeviceDAO deviceDAO;
	
	private final PersonsManager personsManager;
	
	private final AttLogsManager attLogsManager;
	private final AttLogSerializer attLogsSerializer;

	private static final String wsPort = "8025";
	private static final String wsUrl = "/websocket/cmd";
	
	
	private String getWsUrl(String ip) {
		return "ws://" + ip + ":" + wsPort + wsUrl;
	}
	
	private Device getDevices() throws DeviceException {
		
		// por ahora solo existe un reloj. asi que se usa el primero de la lista para enrolar
		List<String> devices = findAll();
		if (devices == null || devices.size() <= 0) {
			throw new DeviceException("No existe ningun device para enrolar");
		}
		
		String id = devices.get(0);
		Device device = findById(id);
		
		return device;
	}
	
	/**
	 * Retorna la uri de conexión para el websocket.
	 * @return
	 * @throws DeviceException
	 */
	private URI getConnectionUri() throws DeviceException {

		Device device = getDevices();
		
		String ip = device.getIp();
		String url = getWsUrl(ip);
		URI uri = URI.create(url);		
		
		return uri;
	}
	
	@Inject
	public DevicesManagerBean(PersonsManager personsManager, DeviceDAO deviceDAO, FingerprintDAO fingerprintDAO, AttLogsManager attLogsManager, AttLogSerializer attLogSerializer) {
		this.deviceDAO = deviceDAO;
		this.fingerprintDAO = fingerprintDAO;
		this.attLogsManager = attLogsManager;
		this.attLogsSerializer = attLogSerializer;
		this.personsManager = personsManager;
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
	
	
	/**
	 * se conecta el reloj y obteiene los logs.
	 * los guarda en la base de datos y responde los ids de los logs exitósamente almacenados.
	 */
	@Override
	public List<String> syncLogs() throws AttLogException, DeviceException {
	
		URI uri = getConnectionUri();
		final String cmd = "model;getAttLogs";
		final List<String> ids = new ArrayList<String>();
		final Semaphore sem = new Semaphore(0);
		
		GenericWebsocketClient gwc = new GenericWebsocketClient(new WebsocketClient() {
			
			private final List<AttLog> logs = new ArrayList<AttLog>();
			
			@Override
			public void onClose(Session session, CloseReason reason) {
				logger.fine("onClose " + String.valueOf(reason.getCloseCode().getCode()));
			}
			
			@Override
			public void onMessage(String m, Session session) {
				logger.fine("Mensaje recibido : " + m);
				
				if (m.contains("ok")) {
					
					try {
						String json = m.replace("ok ", "");
						AttLog log = attLogsSerializer.read(json);	
						Person personLog = log.getPerson();
						
						//busco por dni de la persona que posee el log
						Person person = personsManager.findByDni(personLog.getDni());
						if (person == null) {
							personsManager.persist(personLog);
						} else {
							if (!person.getId().equals(personLog.getId())) {
								//log.setPerson(person);
								changePersonId(person);
								return;
							}
						}
						
						logs.add(log);
						
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
					
				
				} else if (m.startsWith("OK ")) {
					
					try {
						session.close();

					} catch (IOException e) {
						e.printStackTrace();
					}					
					
					// almaceno los logs en el server.
					for (AttLog log : logs) {
						try {
							attLogsManager.persist(log);
							ids.add(log.getId());
							
						} catch (AttLogException e) {
							logger.log(Level.SEVERE,e.getMessage(),e);
						}
					}

					sem.release();
				}
				
			}
			@Override
			public void onOpen(Session s, EndpointConfig config) {
				try {
					s.getBasicRemote().sendText(cmd);
					
				} catch (IOException e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
				
		try {
			ContainerProvider.getWebSocketContainer().connectToServer(gwc, uri);
			
			// espero a que termine la conexion del websocket y sus resultados.
			sem.acquireUninterruptibly();
			
			return ids;
			
		} catch (DeploymentException e) {
			e.printStackTrace();
			throw new DeviceException(e);


		} catch (IOException e) {
			e.printStackTrace();
			throw new DeviceException(e);

		}
		
	}
	
	
	@Override
	public void cancel() throws DeviceException {
		logger.fine("cancel");

		URI uri = getConnectionUri();
		
		final String cmd = "reader;cancel";

		GenericWebsocketClient gwc = new GenericWebsocketClient(new WebsocketClient() {
			@Override
			public void onClose(Session session, CloseReason reason) {
				logger.fine("onClose " + String.valueOf(reason.getCloseCode().getCode()));
			}
			
			@Override
			public void onMessage(String m, Session session) {
				logger.fine("Mensaje recibido : " + m);
			}
			@Override
			public void onOpen(Session s, EndpointConfig config) {
				try {
					s.getBasicRemote().sendText(cmd);
					
				} catch (IOException e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
				
		try {
			ContainerProvider.getWebSocketContainer().connectToServer(gwc, uri);
			
		} catch (DeploymentException e) {
			e.printStackTrace();
			throw new DeviceException(e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new DeviceException(e);
			
		}		
	}
	
	
	
	
	@Override
	public void enroll(final String personId, final EnrollManager enrollManager) throws PersonException, DeviceException {
		
		logger.fine("enroll");
		
		URI uri = getConnectionUri();
		
		final String cmd = "reader;enroll;" + personId;
		
		GenericWebsocketClient gwc = new GenericWebsocketClient(new WebsocketClient() {
			@Override
			public void onClose(Session session, CloseReason reason) {
				logger.fine("onClose " + String.valueOf(reason.getCloseCode().getCode()));
			}
			
			@Override
			public void onMessage(String m, Session session) {
				logger.fine("Mensaje recibido : " + m);
				
				EnrollAction action = null;
				if (m.contains("primera")) {
					
					action = EnrollAction.NEED_FIRST_SWEEP;
					
				} else if (m.contains("segunda")) {
					
					action = EnrollAction.NEED_SECOND_SWEEP;
					
				} else if (m.contains("tercera")) {
					
					action = EnrollAction.NEED_THIRD_SWEEP;
					
				} else if (m.contains("timeout")) {
					
					action = EnrollAction.TIMEOUT;
					try {
						session.close();
					} catch (IOException e) {
						e.printStackTrace();
					}			
					
				} else if (m.contains("cancelado")) {
					
					action = EnrollAction.CANCELED;
					try {
						session.close();
					} catch (IOException e) {
						e.printStackTrace();
					}			
					
				} else if (m.contains("levantar")) {
					
					action = EnrollAction.RELEASE;
					
				} else if (m.contains("calidad")) {
					
					action = EnrollAction.BAD;			
					
				} else if (m.startsWith("OK ")) {
					
					String json = m.substring(3);
					FingerprintSerializer fps = new FingerprintSerializer();
					Fingerprint fp = fps.read(json);
					
					try {
						session.close();
					} catch (IOException e) {
						e.printStackTrace();
					}					
					
					// se tiene la huella. asi que se la almacena en la base.
					try {
						fingerprintDAO.persist(fp);
						enrollManager.onSuccess(fp);
						
					} catch (FingerprintException e1) {
						logger.log(Level.SEVERE,e1.getMessage(),e1);
						enrollManager.onMessage(EnrollAction.ERROR);
						
					}

					return;
				}
				
				enrollManager.onMessage(action);
				
			}
			@Override
			public void onOpen(Session s, EndpointConfig config) {
				try {
					s.getBasicRemote().sendText(cmd);
					
				} catch (IOException e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
					enrollManager.onMessage(EnrollAction.ERROR);
				}
			}
		});
				
		try {
			ContainerProvider.getWebSocketContainer().connectToServer(gwc, uri);
			
		} catch (DeploymentException e) {
			e.printStackTrace();
			throw new DeviceException(e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new DeviceException(e);
			
		}

	}
	
	@Override
	public void changePersonId(Person p) throws PersonException,DeviceException {
		if (p == null) {
			throw new PersonException("person == null");
		}
		
		if (p.getId() == null) {
			throw new PersonException("person.id == null");
		}
		
		URI uri = getConnectionUri();
		
		PersonSerializer ps = new PersonSerializer();
		String json = ps.toJson(p);
		final String cmd = "model;changePersonId;" + json;
		
		GenericWebsocketClient gwc = new GenericWebsocketClient(new WebsocketClient() {
			@Override
			public void onClose(Session s, CloseReason reason) {
				
			}
			
			@Override
			public void onMessage(String m, Session s) {
				if ("OK".equals(m)) {
					logger.fine("changePersonId - OK");

				} else if ("ERROR".equals(m)) {
					logger.log(Level.SEVERE,"Error enviado desde el reloj");
					
				} else {
					logger.log(Level.SEVERE,"changePersonId unknown response : " + m);

				}
			}
			
			@Override
			public void onOpen(Session s, EndpointConfig config) {
				try {
					s.getBasicRemote().sendText(cmd);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		try {
			ContainerProvider.getWebSocketContainer().connectToServer(gwc, uri);
			
		} catch (DeploymentException e) {
			e.printStackTrace();
			throw new DeviceException(e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new DeviceException(e);
			
		}		
	}
	
	@Override
	public void persist(Person p) throws PersonException, DeviceException {
		if (p.getId() == null) {
			throw new PersonException("person.id == null");
		}
		
		URI uri = getConnectionUri();
				
		PersonSerializer ps = new PersonSerializer();
		String json = ps.toJson(p);
		final String cmd = "model;persistPerson;" + json;
		
		GenericWebsocketClient gwc = new GenericWebsocketClient(new WebsocketClient() {
			@Override
			public void onClose(Session s, CloseReason reason) {
				
			}
			@Override
			public void onMessage(String m, Session s) {
				
				if ("OK".equals(m)) {
					logger.fine("persistPerson - OK");

				} else if ("ERROR".equals(m)) {
					logger.log(Level.SEVERE,"Error enviado desde el reloj");
					
				} else {
					logger.log(Level.SEVERE,"persistPerson unknown response : " + m);

				}
				
				try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
				
			}
			@Override
			public void onOpen(Session s, EndpointConfig config) {
				try {
					s.getBasicRemote().sendText(cmd);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
				
		try {
			ContainerProvider.getWebSocketContainer().connectToServer(gwc, uri);
			
		} catch (DeploymentException e) {
			e.printStackTrace();
			throw new DeviceException(e);


		} catch (IOException e) {
			e.printStackTrace();
			throw new DeviceException(e);

		}		
		
	}
	
	
	
	
	
	
	private interface TransferFingerprintsResult {
		
		public void setError(DeviceException e);
		public DeviceException getError();
		
		
	}
	
	private class DefaultTransferFingerprintsResult implements TransferFingerprintsResult {

		private DeviceException ex = null;
		
		@Override
		public void setError(DeviceException e) {
			this.ex = e;
		}

		@Override
		public DeviceException getError() {
			return ex;
		}
		
	}
	
	
	@Override
	public void transferFingerprints(String personId) throws PersonException, DeviceException {

		if (personId == null) {
			throw new PersonException("person.id == null");
		}
		
		final List<Fingerprint> fps = new ArrayList<Fingerprint>();
		try {
			List<Fingerprint> fpss = fingerprintDAO.findByPerson(personId);
			if (fpss != null) {
				fps.addAll(fpss);
			}
			
		} catch (FingerprintException e) {
			logger.log(Level.SEVERE,e.getMessage(),e);
			throw new PersonException(e);
		}
		
		if (fps.size() <= 0) {
			return;
		}
		
		final FingerprintSerializer fs = new FingerprintSerializer();
		String json = fs.toJson(fps.get(0));
		
		URI uri = getConnectionUri();
		final String cmd = "model;persistFingerprint;" + json; 
		
		// realizo la llamada asincrónica pero espero a que se termine. con un timeout.
		
		final TransferFingerprintsResult result = new DefaultTransferFingerprintsResult();
		final Semaphore sem = new Semaphore(0);
		
		
		GenericWebsocketClient gwc = new GenericWebsocketClient(new WebsocketClient() {
			
			private int index = 1;
			
			@Override
			public void onClose(Session session, CloseReason reason) {
				logger.fine("onClose " + String.valueOf(reason.getCloseCode().getCode()));
				sem.release();
			}
			
			@Override
			public void onMessage(String m, Session s) {
				if (m.startsWith("OK")) {
					
					if (index >= fps.size()) {
			
						sem.release();
			
					} else {
						
						Fingerprint fp = fps.get(index); 
						index = index + 1;
						String json = fs.toJson(fp);
						String cmd = "model;persistFingerprint;" + json;
						
						try {
							s.getBasicRemote().sendText(cmd);
							
						} catch (IOException e) {
							logger.log(Level.SEVERE,e.getMessage(),e);
							result.setError(new DeviceException(e));
							sem.release();
							
						}
						
					}
					
				} else {
					
					result.setError(new DeviceException("ERROR : " + m));
					sem.release();
				}
			}
			
			@Override
			public void onOpen(Session s, EndpointConfig config) {
				try {
					s.getBasicRemote().sendText(cmd);
					
				} catch (IOException e) {
					logger.log(Level.SEVERE,e.getMessage(),e);
				}
			}
		});
		
		try {
			ContainerProvider.getWebSocketContainer().connectToServer(gwc, uri);
			
		} catch (DeploymentException e) {
			e.printStackTrace();
			throw new DeviceException(e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new DeviceException(e);

		}		

		try {
			sem.tryAcquire(5, TimeUnit.MINUTES);
		} catch (InterruptedException e) {

		}

		if (result.getError() != null) {
			throw result.getError();
		}
	
	
	}
	
	
	

	
	
}
