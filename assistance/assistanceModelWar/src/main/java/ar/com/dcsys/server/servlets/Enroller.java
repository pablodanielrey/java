package ar.com.dcsys.server.servlets;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

import javax.inject.Inject;
import javax.inject.Singleton;

import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.exceptions.DeviceException;
import ar.com.dcsys.exceptions.PersonException;
import ar.com.dcsys.model.PersonsManager;
import ar.com.dcsys.model.device.DevicesManager;
import ar.com.dcsys.model.device.EnrollAction;
import ar.com.dcsys.model.device.EnrollManager;
import ar.com.dcsys.security.Fingerprint;

@Singleton
public class Enroller {

	private final ExecutorService executor = Executors.newFixedThreadPool(1);
	private final DevicesManager devicesManager;
	private final PersonsManager personsManager;

	private void showMessage(String msg) {
		messageLock.lock();
		message = msg;
		messageLock.unlock();
	}
	
	
	private void transferPerson(String personId) throws PersonException, DeviceException {
		showMessage("Transfiriendo persona al dispositivo");
		Person p = personsManager.findById(personId);
		devicesManager.persist(p);
		
		showMessage("Transfiriendo las huellas al dispositivo");
		devicesManager.transferFingerprints(personId);
		
		showMessage("Transferencia Exitosa");
	}
	
	private final Runnable r = new Runnable() {
		public void run() {
			try {
				running = true;
				
				if (personId == null) {
					return;
				}

				devicesManager.enroll(personId, new EnrollManager() {
					@Override
					public void onSuccess(Fingerprint fingerprint) {
						try {
							transferPerson(personId);
							showMessage("Usuario correctamente enrolado");
						} catch (PersonException | DeviceException e) {
							showMessage("Error enrolando usuario en el dispositivo");
						}
						
					}
					@Override
					public void onMessage(EnrollAction action) {
						showMessage(action.toString());
					}
				});
			} catch (Exception e) {
				showMessage(e.getMessage());

			} finally {
				running = false;
			}
		};
	};
	
	private final ReentrantLock messageLock = new ReentrantLock();
	private volatile String message;
	
	private volatile boolean running = false;
	 
	private String personId;
	
	
	@Inject
	public Enroller(DevicesManager devicesManager, PersonsManager personsManager) {
		this.devicesManager = devicesManager;
		this.personsManager = personsManager;
	}
	
	
	public synchronized void enroll(String personId) {
		if (running) {
			return;
		}
		showMessage("iniciando enrolamiento");
		this.personId = personId;
		executor.execute(r);
	}
	
	public synchronized boolean isRunning() {
		return running;
	}
	
	public synchronized String getStatus() {
		messageLock.lock();
		try {
			return message;
		} finally {
			messageLock.unlock();
		}
	}
	
}
