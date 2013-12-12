package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.data.reserve.Reserve;
import ar.com.dcsys.data.reserve.ReserveAttemptDate;
import ar.com.dcsys.data.reserve.ReserveAttemptDateType;
import ar.com.dcsys.data.silabouse.Area;
import ar.com.dcsys.data.silabouse.Course;
import ar.com.dcsys.gwt.autoBeans.shared.AutoBeanUtils;
import ar.com.dcsys.gwt.manager.shared.ManagerUtils;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauEncoderDecoder;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.MapauMethods;
import ar.com.dcsys.gwt.mapau.shared.SilegEncoderDecoder;
import ar.com.dcsys.gwt.message.shared.Message;
import ar.com.dcsys.gwt.message.shared.MessageException;
import ar.com.dcsys.gwt.message.shared.MessageType;
import ar.com.dcsys.gwt.message.shared.MessageUtils;
import ar.com.dcsys.gwt.utils.client.EncoderDecoder;
import ar.com.dcsys.gwt.ws.client.WebSocket;
import ar.com.dcsys.gwt.ws.client.WebSocketReceiver;

public class MapauManagerBean implements MapauManager {

	private static final Logger logger = Logger.getLogger(MapauManagerBean.class.getName());
	
	private final ManagerUtils managerUtils;
	private final MapauFactory mapauFactory;
	private final MapauEncoderDecoder encoderDecoder;
	private final SilegEncoderDecoder silegEncoderDecoder;
	private final ClassRoomsEncoderDecoder classRoomEncoderDecoder;
	private final MessageUtils messageUtils;
	private final WebSocket socket;
	
	
	@Inject
	public MapauManagerBean(ManagerUtils managerUtils, 
							MapauFactory mapauFactory, 
							MapauEncoderDecoder encoderDecoder, 
							SilegEncoderDecoder silegEncoderDecoder,
							ClassRoomsEncoderDecoder classRoomEncoderDecoder,
							MessageUtils messageUtils, 
							WebSocket ws) {
		this.socket = ws;
		this.encoderDecoder = encoderDecoder;
		this.silegEncoderDecoder = silegEncoderDecoder;
		this.classRoomEncoderDecoder = classRoomEncoderDecoder;
		this.messageUtils = messageUtils;
		this.mapauFactory = mapauFactory;
		this.managerUtils = managerUtils;
	}
	
	private boolean handleError(Message response, Receiver<?> receiver) {
		if (MessageType.ERROR.equals(response.getType())) {
			String error = response.getPayload();
			receiver.onFailure(new MessageException(error));
			return true;
		}		
		return false;
	}
	
	
	@Override
	public void findAllReserveAttemptType(final Receiver<List<ReserveAttemptDateType>> rec) {
		try {
			Message msg = messageUtils.method(MapauMethods.findAllReserveAttemptTypes);
			
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<ReserveAttemptDateType> types = null;
					try {
						String list = response.getPayload();
						types = encoderDecoder.decodeReserveAttemptDateTypeList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(types);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	@Override
	public void getCoursesToCreateReserve(final Receiver<List<Course>> rec) {
		try {
			Message msg = messageUtils.method(MapauMethods.findCoursesToCreateReserve);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<Course> courses = null;
					try {
						String list = response.getPayload();
						courses = silegEncoderDecoder.decodeCourseList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(courses);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	@Override
	public void findAllArea(final Receiver<List<Area>> rec) {
		try {
			Message msg = messageUtils.method(MapauMethods.findAllAreas);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<Area> areas = null;
					try {
						String list = response.getPayload();
						areas = encoderDecoder.decodeAreaList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(areas);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	@Override
	public void createReserves(List<ReserveAttemptDate> rads, List<ClassRoom> classRooms, String description, final Receiver<Void> rec) {
		try {
			String[] params = new String[3];
			params[0] = encoderDecoder.encodeReserveAttemptDateList(rads);
			params[1] = classRoomEncoderDecoder.encodeClassRoomList(classRooms);
			params[3] = description;
			String sparams = ManagerUtils.encodeParams(params);

			Message msg = messageUtils.method(MapauMethods.createReserves, sparams);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					try {
						rec.onSuccess(null);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	@Override
	public void findAllClassRoomsAvailableIn(List<ReserveAttemptDate> rads,	Boolean checkCapacity, final Receiver<List<ClassRoom>> rec) {
		try {
			String[] params = new String[2];
			params[0] = encoderDecoder.encodeReserveAttemptDateList(rads);
			params[1] = managerUtils.encodeBoolean(checkCapacity);
			String sparams = ManagerUtils.encodeParams(params);	
			
			Message msg = messageUtils.method(MapauMethods.findAllClassRoomsAvailableInRads, sparams);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<ClassRoom> classRooms = null;
					try {
						String list = response.getPayload();
						classRooms = classRoomEncoderDecoder.decodeClassRoomList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(classRooms);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}
	
	@Override
	public void findReservesBy(ReserveAttemptDate rad, final Receiver<List<Reserve>> rec) {
		try {
			String param = ManagerUtils.encode(mapauFactory, ReserveAttemptDate.class, rad);
			Message msg = messageUtils.method(MapauMethods.findReservesByRad, param);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<Reserve> reserves = null;
					try {
						String list = response.getPayload();
						reserves = encoderDecoder.decodeReserveList(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(reserves);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}				
	}
	
	
	
	@Override
	public void createNewAppointments(List<AppointmentV2> apps,	final Receiver<Void> rec) {
		try {
			String sapps = encoderDecoder.encodeAppointmentV2List(apps);
			Message msg = messageUtils.method(MapauMethods.createNewAppointments, sapps);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					try {
						rec.onSuccess(null);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}					
	}
	
	/**
	 * Utilitario para llamar a un método que recibe un appointmentV2 como parámetro y retorna void.
	 * @param method
	 * @param app
	 * @param rec
	 */
	private void callWithAppointment(String method, AppointmentV2 app, final Receiver<Void> rec) {
		try {
			String sapp = ManagerUtils.encode(mapauFactory, AppointmentV2.class, app);
			Message msg = messageUtils.method(method, sapp);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					try {
						rec.onSuccess(null);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}		
	}

	@Override
	public void deleteAppointment(AppointmentV2 app, final Receiver<Void> rec) {
		callWithAppointment(MapauMethods.deleteAppointment, app, rec);
	}

	@Override
	public void modifyAppointment(AppointmentV2 app, Receiver<Void> rec) {
		callWithAppointment(MapauMethods.modifyAppointment, app, rec);		
	}

	
	@Override
	public void findAllAppointmentsBy(AppointmentV2 appv2, List<Date> dates, boolean checkHour, final Receiver<List<AppointmentV2>> rec) {
		try {
			String[] params = new String[3];
			params[0] = ManagerUtils.encode(mapauFactory, AppointmentV2.class, appv2);
			params[1] = managerUtils.encodeDateList(dates);
			params[2] = managerUtils.encodeBoolean(checkHour);
			String sparams = ManagerUtils.encodeParams(params);

			Message msg = messageUtils.method(MapauMethods.findAllAppointmentsBy, sparams);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<AppointmentV2> l = null;
					try {
						String list = response.getPayload();
						l = encoderDecoder.decodeAppointmentV2List(list);
					} catch (Exception e) {
						rec.onFailure(e);
						return;
					}
					
					try {
						rec.onSuccess(l);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}			
	}

	@Override
	public void findAllFilters(final Receiver<List<TransferFilterType>> rec) {
		try {
			Message msg = messageUtils.method(MapauMethods.findAllFilters);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<TransferFilterType> l = null;
					try {
						String list = response.getPayload();
						l = encoderDecoder.decodeTransferFilterTypeList(list);
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(l);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}			
	}

	@Override
	public void findAppointmentsV2By(List<TransferFilter> filters, final Receiver<List<AppointmentV2>> rec) {
		try {
			String sfilters = encoderDecoder.encodeTransferFilterList(filters);
			Message msg = messageUtils.method(MapauMethods.findAppointmentsV2By, sfilters);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<AppointmentV2> l = null;
					try {
						String list = response.getPayload();
						l = encoderDecoder.decodeAppointmentV2List(list);
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(l);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}			
	}

	@Override
	public void findAppointmentsBy(List<TransferFilter> filters, final Receiver<List<Appointment>> rec) {
		try {
			String sfilters = encoderDecoder.encodeTransferFilterList(filters);
			Message msg = messageUtils.method(MapauMethods.findAppointmentsBy, sfilters);
			
			// envío el mensaje al servidor.
			socket.open();
			socket.send(msg, new WebSocketReceiver() {
				@Override
				public void onSuccess(Message response) {
					
					if (handleError(response, rec)) {
						return;
					}
					
					List<Appointment> l = null;
					try {
						String list = response.getPayload();
						l = encoderDecoder.decodeAppointmentList(list);
					} catch (Exception e) {
						rec.onFailure(e);
					}
					
					try {
						rec.onSuccess(l);
					} catch (Exception e) {
						logger.log(Level.SEVERE,e.getMessage(),e);
					}
				}
				@Override
				public void onFailure(Throwable t) {
					rec.onFailure(t);
				}
			});
		} catch (Exception e) {
			rec.onFailure(e);
		}			
	}
	
}
