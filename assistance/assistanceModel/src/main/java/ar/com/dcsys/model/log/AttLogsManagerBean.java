package ar.com.dcsys.model.log;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.log.AttLog;
import ar.com.dcsys.data.log.AttLogDAO;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.AttLogException;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;

/**
 * 
 * @author pablo
 *
 */

@Singleton
public class AttLogsManagerBean implements AttLogsManager {

	private final AttLogDAO attLogManager;
	
//	@EJB MailService mailService;
//	@EJB Configuration config;
//	@EJB DevicesManager devicesManager;
	private final PersonsManager personsManager;
	
	@Inject
	public AttLogsManagerBean(AttLogDAO attLogDAO, PersonsManager personsManager) {
		this.attLogManager = attLogDAO;
		this.personsManager = personsManager;
	}

	
	@Override
	public AttLog findById(String id) throws AttLogException, PersonException {
		return attLogManager.findById(id);
	}
	
	@Override
	public List<AttLog> findAll(Date start, Date end) throws AttLogException, PersonException {
		return attLogManager.findAll(start, end);
	}

	@Override
	public List<AttLog> findAll(Person person, Date start, Date end) throws AttLogException {
		return attLogManager.findAll(person, start, end);
	}
	
	
	private String fullName(Person p) {
		String name = (p.getName() != null) ? p.getName() : "";
		String lastName = (p.getLastName() != null) ? p.getLastName() : "";
		return lastName + " " + name;
	}
	
	
	/*
	@Override
	public synchronized void synchronizeLogs(Device d) throws AttLogException {
		if (!(d instanceof ZkSoftwareDevice)) {
			throw new AttLogException("No es un dispositivo de zksoftware");
		}

		if (d.getEnabled() == null || (!d.getEnabled()))  {
			throw new AttLogException("No se puede sincronizar de un dispositivo deshabilitado");
		}
		
		try {
			ZkSoftwareDevice device = (ZkSoftwareDevice)d;
			List<zksoftware.soap.AttLog> logs = device.getAttLogs();
			
			if (logs == null || logs.size() <= 0) {
				// si no existen logs entonces doy por finalizado correctamente la sincronizacion.
				return;
			}
			
			StringBuilder mailToSend = new StringBuilder().append("Logs sincronizados al sistema : \n");
			
			List<String> dnisNotFound = new ArrayList<String>();
			
			List<AttLog> newLogs = new ArrayList<AttLog>(logs.size());
			for (zksoftware.soap.AttLog l : logs) {
				String dni = l.getPin();
				Person person = personsManager.findByDni(dni);
				if (person == null) {
					dnisNotFound.add(dni);
					mailToSend.append("\n").append("Dni no existente : ").append(dni);
					continue;
				}

				Date date = l.getDate();
				Long inOutMode = 0l;
				Long verifyMode = Long.parseLong(l.getVerifiedMode());
				
				AttLog newLog = new AttLog();
				newLog.setDevice(device);
				newLog.setDate(date);
				newLog.setInOutMode(inOutMode);
				newLog.setPerson(person);
				newLog.setVerifyMode(verifyMode);
				
				newLogs.add(newLog);
				
				mailToSend.append("\n").append(dni).append(" ").append(fullName(person)).append(" ").append(date).append(" ioMode: ").append(inOutMode).append(" verifyMode: ").append(verifyMode);
			}
			
			persist(newLogs);
			
			if (dnisNotFound.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (String dni : dnisNotFound) {
					sb.append(dni + "\n");
				}
				throw new AttLogException("Logs con personas que no se pueden encontrar : " + sb.toString());
			}

			String[] account = config.getMailData().assistanceAdministrativeAccount();
			if (account != null) {
				try {
					for (String a : account) {
						mailService.sendMail("Sincronización de logs", a, mailToSend.toString());
					}
				} catch (MailException e) {
					throw new AttLogException(e.getMessage());
				}
			}
			
			// elimino los logs del dispositivo ya que se actualizo la base correctamente.
			device.deleteAttLogs();
			
		} catch (DeviceException e) {
			throw new AttLogException(e);
		} catch (PersonException e) {
			throw new AttLogException(e);
		}
	}
	
	
	private void persist(List<AttLog> logs) throws AttLogException {
		for (AttLog l : logs) {
			attLogManager.persist(l);
		}
	}
	
	
	
	@Override
	public void persist(RawAttLogValue log) throws AttLogException, PersonException, DeviceException {
		
		String dni = log.getDni();
		Person person = personsManager.findByDni(dni);
		if (person == null) {
			throw new AttLogException("No se puede encontrar la persona con dni : " + dni);
		}
		
		String id = log.getDeviceId();
		Device device = devicesManager.findById(id);
		if (device == null) {
			throw new AttLogException("No se puede encontrar el dispositivo con el id : " + id);
		}
		
		AttLog newLog = new AttLog();
		newLog.setDevice(device);
		newLog.setDate(log.getDate());
		newLog.setInOutMode(0l);
		newLog.setPerson(person);
		newLog.setVerifyMode(Long.parseLong(log.getMethod()));
		
		attLogManager.persist(newLog);
	}
	
	*/
	
	/**
	 * Busca los logs que deberían sincronizarse. 
	 * son los logs que están en el dispositivo y NO estan en el sistema.
	 */
	/*
	@Override
	public List<RawAttLogValue> findLogsToSynchronize(Device d)	throws AttLogException, DeviceException {
		
		List<RawAttLogValue> logs = devicesManager.findLogsToSynchronize(d);
		
		// elimino los que teóricamente ya existen.
		Iterator<RawAttLogValue> it = logs.iterator();
		while (it.hasNext()) {
			RawAttLogValue rlog = it.next();
			
			String dni = rlog.getDni();
			Date date = rlog.getDate();
			try {
				Person person = personsManager.findByDni(dni);
				if (person == null) {
					continue;
				}
				Boolean b = findByExactDate(person, date);
				if (b != null && b) {
					it.remove();
				}
			} catch (PersonException e) {
				// no se puede determinar si existe o no asi que no elimino el log.
			}
		}
		
		return logs;
	}
	*/
	
	@Override
	public Boolean findByExactDate(Person person, Date date) throws AttLogException, PersonException, DeviceException {
		return attLogManager.findByExactDate(person, date);
	}
	
}
