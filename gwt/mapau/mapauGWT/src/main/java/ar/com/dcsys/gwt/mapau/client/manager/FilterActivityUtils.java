package ar.com.dcsys.gwt.mapau.client.manager;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.appointment.Appointment;
import ar.com.dcsys.data.appointment.AppointmentV2;
import ar.com.dcsys.data.filter.TransferFilter;
import ar.com.dcsys.data.filter.TransferFilterType;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment;
import ar.com.dcsys.gwt.mapau.client.activity.calendar.adapter.MapauAppointment.State;
import ar.com.dcsys.gwt.mapau.client.filter.FilterConverter;
import ar.com.dcsys.gwt.mapau.shared.MapauFactory;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterType;
import ar.com.dcsys.gwt.mapau.shared.data.filter.FilterValue;
import ar.com.dcsys.gwt.mapau.shared.data.filter.transfer.TransferableFilter.TransferFilterProvider;

import com.google.inject.Inject;


public class FilterActivityUtils {

	private final MapauFactory mapauFactory;
	private final MapauManager mapauManager;
	private final FilterConverter filterConverter;
	
	@Inject
	public FilterActivityUtils(MapauFactory mapauFactory, MapauManager mapauManager, FilterConverter filterConverter) {
		this.mapauFactory = mapauFactory;
		this.mapauManager = mapauManager;
		this.filterConverter = filterConverter;
	}
	
	
	/*
	 * Busco los filtros permitidos por el modelo.
	 */
	public void findFilters(final Receiver<List<FilterType<?>>> rec) {
		mapauManager.findAllFilters(new Receiver<List<TransferFilterType>>(){
			@Override
			public void onSuccess(List<TransferFilterType> types) {
				List<FilterType<?>> filters = new ArrayList<FilterType<?>>(); 
				if (types != null) {
					filters.addAll(filterConverter.enumToFilterTypes(types));
				}
				rec.onSuccess(filters);
			}
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});	
	}	
	
	
	/**
	 * Busco los appointments indicados por los filtros.
	 * @param rf
	 * @param filters
	 * @param rec
	 */
	public void findAppointmentsV2(List<FilterValue<?>> filters, final Receiver<List<AppointmentV2>> rec) {
		
		// convierto los filtros del modelo del cliente para ser transferidos por RequestFactory
		TransferFilterProvider tp = new TransferFilterProvider() {
			@Override
			public TransferFilter newTransferFilter() {
				return mapauFactory.transferFilter().as();
			}
		};
		
		List<TransferFilter> filtersValues = filterConverter.convertFilters(tp,filters);		

		// busco los appointments que indican los filtros.
		mapauManager.findAppointmentsV2By(filtersValues, new Receiver<List<AppointmentV2>>() {
			@Override
			public void onSuccess(List<AppointmentV2> apps) {
				rec.onSuccess(apps);
			}
			
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
	}
	
	
	/**
	 * Busco los appointments indicados por los filtros.
	 * @param rf
	 * @param filters
	 * @param rec
	 */
	public void findAppointments(List<FilterValue<?>> filters, final Receiver<List<Appointment>> rec) {
		
		// convierto los filtros del modelo del cliente para ser transferidos por RequestFactory
		TransferFilterProvider tp = new TransferFilterProvider() {
			@Override
			public TransferFilter newTransferFilter() {
				return mapauFactory.transferFilter().as();
			}
		};
		
		List<TransferFilter> filtersValues = filterConverter.convertFilters(tp,filters);		

		// busco los appointments que indican los filtros.
		mapauManager.findAppointmentsBy(filtersValues, new Receiver<List<Appointment>>() {
			@Override
			public void onSuccess(List<Appointment> apps) {
				rec.onSuccess(apps);
			}
			
			@Override
			public void onFailure(Throwable error) {
				rec.onFailure(error);
			}
		});
	}	
		
	/**
	 * Encapsulo los appointments en MapauAppointmnets para poder ser mostrados en el control del calendario usado.
	 * @param apps
	 * @return
	 */
	public static List<MapauAppointment> wrapAppointments(List<AppointmentV2> apps) {
		List<MapauAppointment> mapauApps = new ArrayList<MapauAppointment>();
		for (AppointmentV2 a : apps) {
			MapauAppointment app = new MapauAppointment(a);
			app.setState(State.UNTOUCHED);
			mapauApps.add(app);
		}
		return mapauApps;
	}



}
