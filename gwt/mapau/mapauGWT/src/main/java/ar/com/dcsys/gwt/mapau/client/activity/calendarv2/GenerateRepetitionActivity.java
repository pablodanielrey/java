package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.GenerateRepetitionEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.GenerateRepetitionEventHandler;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.HideViewEvent;
import ar.com.dcsys.gwt.mapau.client.activity.calendarv2.events.ShowViewEvent;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.DayOfWeek;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.RepeatType;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.GenerateRepetitionView.RepetitionData;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.repeat.GenerateRepetitionViewCss;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.appointment.repeat.GenerateRepetitionViewResources;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SingleSelectionModel;


public class GenerateRepetitionActivity extends AbstractActivity implements GenerateRepetitionView.Presenter {

	
	private final GenerateRepetitionView view;
	private final SingleSelectionModel<RepeatType> repeatTypeSelection;
	
	private ResettableEventBus eventBus;
	private Receiver<RepetitionData> receiver;
	
	private final DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
	
	
	public GenerateRepetitionActivity(GenerateRepetitionView view) {
		this.view = view;
		repeatTypeSelection = new SingleSelectionModel<RepeatType>();
	}
	
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = new ResettableEventBus(eventBus);
		
		this.eventBus.addHandler(GenerateRepetitionEvent.TYPE, new GenerateRepetitionEventHandler() {
			@Override
			public void onGenerateRepetition(GenerateRepetitionEvent event) {
		
				Date date = event.getDate(); 
				receiver = event.getData();
				if (date == null) {
					date = new Date();
				}
				view.setStartRepetition(date);
				view.setStartReadOnly(true);
				repeatTypeSelection.setSelected(RepeatType.DAILY, true);
				
				showView();
			}
		});

		view.setRepeatTypeSelectionModel(repeatTypeSelection);
		repeatTypeSelection.setSelected(RepeatType.DAILY, true);
		
		GenerateRepetitionViewCss style = GenerateRepetitionViewResources.INSTANCE.style(); 
		style.ensureInjected();	
		
		view.setPresenter(this);
		
		findRepeatType();
		
		panel.setWidget(view);		
	}
			
	@Override
	public void onStop() {
		eventBus.removeHandlers();
		repeatTypeSelection.clear();
		view.clear();
		view.setPresenter(null);
	}
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	private void showView() {
		eventBus.fireEvent(new ShowViewEvent(GenerateRepetitionView.class.getName()));
	}

	private void hideView() {
		eventBus.fireEvent(new HideViewEvent(GenerateRepetitionView.class.getName()));
	}
	
	private Date createEndDate(Date start, long everyMilis, int count) {
		Date end = new Date(start.getTime());
		
		for (int i = 1; i<= count; i++) {
			end.setTime(end.getTime() + everyMilis); 
		}
		
		return end;
	}
	
	
	
	/**
	 * Obtiene los datos de la vista diaria
	 * @return
	 */
	private void getDatesDaily(RepetitionData data) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Repetir diariamente");
		
		List<Date> dates = new ArrayList<Date>();				
		
		//fecha inicial
		Date start = view.getStartRepetition();
		
		sb.append(" desde ").append(dateFormat.format(start));
		
		//lapso de tiempo de la repeticion, cada 1 dia, 2, 3 etc
		int everyDate = view.getRepeatedEvery();
		
		sb.append(" cada ").append(everyDate).append(" días");
		
		long everyMilis = everyDate * 24l * 60l * 60l * 1000l;
		
		//finalizacion
		Date end = null;
		if (view.getEndRepetitionCount()) {
			int count = view.getEndRepetitionCountValue();
			end = createEndDate(start, everyMilis, count);
			
			sb.append(" ").append(count).append(" veces");
			
		} else {
			end = view.getEndRepetitionDateValue();
			
			sb.append(" hasta ").append(dateFormat.format(end));
		}
		
		if (end.getTime() < start.getTime()) {
			// el fin esta antes que el principio. asi que no hago nada.
			return;
		}
		
		Date actual = start;
		actual = new Date(actual.getTime() + everyMilis);
		while (actual.getTime() <= end.getTime()) {
			dates.add(actual);
			actual = new Date(actual.getTime() + everyMilis);
		}
		
		data.descripcion = sb.toString();
		data.dates = dates;
	}
	
	
	private int getNextDay(int actualDay, List<DayOfWeek> days) {
		int day = 0;
		DayOfWeek[] daysTypes = DayOfWeek.values();
		boolean include = false;
		while (!include) {
			actualDay++;
			if (actualDay >= 7) {
				actualDay = 0;
			}
			DayOfWeek dow = daysTypes[actualDay];
			for (DayOfWeek t : days) {
				if (dow.getDescription().equals(t.getDescription())) {
					day = actualDay;
				}
			}
				
		}
		
		return day;
	}
	

	private long getEndMilis(long initial, long jump, long daysOfRepetitions, StringBuilder description) {
		
		long finalMilis = 0l;
		
		if (view.getEndRepetitionCount()) {
			long count = view.getEndRepetitionCountValue();
			finalMilis = ((count / daysOfRepetitions) * jump) + initial;
			description.append(count).append(" veces");
		} else {
			Date finalDate = view.getEndRepetitionDateValue();
			finalMilis = finalDate.getTime();
			description.append(" hasta ").append(dateFormat.format(finalDate));
		}
		
		return finalMilis;
	}
	
	/**
	 * Obtiene la repeticion de tipo semanal.
	 * @return
	 */
	private void getDatesWeekly(RepetitionData data) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Repetir semanalmente");
		
		List<Date> dates = new ArrayList<Date>();	
		
		List<Date> repetitions = new ArrayList<Date>();
		List<DayOfWeek> days = view.getDaysOfWeek();
		
		if (days.size() <= 0) {
			return;
		}

		Date date =	view.getStartRepetition();
		
		sb.append(" desde ").append(dateFormat.format(date));
		
		int today = date.getDay();

		long dayMilis = 24l * 60l * 60l * 1000l;
		
		long[] deltas = new long[days.size()];
		
		sb.append(", los días: ");
		
		for (int i = 0; i < days.size(); i++) {
			DayOfWeek dow = days.get(i);				
			int index = dow.getIndex();
			deltas[i] = (index - today) * dayMilis;
			
			if (i > 0) {
				sb.append(", ");
			}
			sb.append(dow.getDescription());			
		}
		
		
		long weekMilis =  dayMilis * 7l;
		long every = view.getRepeatedEvery();
		long jumpInMilis = every * weekMilis;
		
		sb.append(" cada ").append(every).append(" semanas ");
		
		long index = date.getTime();
		long originalIndex = index;
		
		long finalMilis = getEndMilis(index, jumpInMilis, days.size(), sb);
		
		

		
		// genero las repeticiones de la primer semana.
		
		for (long delta : deltas) {
			long repDelta = delta + index;
			if (repDelta > originalIndex && repDelta <= finalMilis) {
				Date rep = new Date(delta + index);
				repetitions.add(rep);
			}
		}
		index = index + jumpInMilis;
		
		// genero las repeticiones de las semanas restantes.
		
		while (index <= finalMilis) {
			
			for (long delta : deltas) {
				long repDelta = delta + index;
				if (repDelta >= originalIndex && repDelta <= finalMilis) {
					Date rep = new Date(delta + index);
					repetitions.add(rep);
				}
			}
			index = index + jumpInMilis;
		}

		data.dates = repetitions;
		data.descripcion = sb.toString();
	}
	
	private Date getLastDayOfMonth(long date) {
		Date end = new Date(date);
		int day = (end.getDate() == 1)? 2 : end.getDate();		
		end.setDate(day);
		
		long dayMilis = 24l * 60l * 60l * 1000l; 
		
		while (day != 1) {
			end.setTime(end.getTime() + dayMilis);
			day = end.getDate();
		}
		
		end.setTime(end.getTime() - dayMilis);
		
		return end;
	}
	
	private Date nextMonth(long date, long every) {
		
		Date aux = new Date(date);
		
		//obtengo el mes
		long month = aux.getMonth();
		//le sumo every
		month += every;
		//si es mayor a 12 cambio de año y actualizo el mes
		if (month > 12) {
			int year = aux.getYear();
			year += month / 12;
			month = month % 12;
			aux.setYear(year);
		}
		
		aux.setMonth((int) month);
		
		return aux;
		
	}
	
	private long getEndMonthMilis(long every, long index, boolean sameDay, StringBuilder description) {
		
		long finalMilis = 0l;
		
		if (view.getEndRepetitionCount()) {
			
			long count = view.getEndRepetitionCountValue();
			description.append(count).append(" veces");
			Date aux = new Date(index);
			int day = aux.getDate();
			aux.setDate(1);
			
			while (count > 0) {
				aux = nextMonth(aux.getTime(), every);
				//si es el mismo dia del mes, verifico si el mes actual tiene dicho dia
				if ((sameDay) && (day > getLastDayOfMonth(aux.getTime()).getDate())) {
					continue;
				}	
				count--;
			}			
			
			finalMilis = aux.getTime();
			
		} else {
			
			Date finalDate = view.getEndRepetitionDateValue();			
			finalMilis = finalDate.getTime();			
			description.append(" hasta ").append(dateFormat.format(finalDate));
			
		}
		
		return finalMilis;
	}
	
	private long nextDateMonthly(long date, long every) {
		int day = new Date(date).getDate();		
		long index = (nextMonth(date,every)).getTime();
		
		while (day > getLastDayOfMonth(index).getDate()) {
			index = (nextMonth(index,every)).getTime();
		}	
		
		return index;
	}
	
	private void findWeekAndDay(Date date) {
		int dayOfMonth = date.getDate();
		dayOfWeek = date.getDay();
		
		Date firstDate = new Date(date.getTime());
		int firstDateOfMonth = 1;
		firstDate.setDate(firstDateOfMonth);		
		int firstDowOfMonth = firstDate.getDay();
		
		while (dayOfWeek != firstDowOfMonth) {
			firstDowOfMonth++;
			if (firstDowOfMonth == 7) {
				firstDowOfMonth = 0;
			}
			firstDateOfMonth++;
		}
		
		week = dayOfMonth / firstDateOfMonth;
		
	}
	
	private String weekToString(int n) {
		String[] weeks = {"nulo", "primer","segundo","tercer","cuarto","último","último"};		
		return weeks[n];
	}
	
	private String dayOfWeekToString(int n) {
		//The returned value (0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 = Thursday, 5 = Friday, 6 = Saturday)
		String[] days = {"domingo","lunes","martes","miércoles","jueves","sábado"};
		return days[n];
	}
	
	private long nextDateMonthly(long date, long every, int week, int dayOfWeek) {
	
		long index = (nextMonth(date,every)).getTime();
		
		//obtengo el primer dia del mes
		Date firstDate = new Date(index);
		int firstDateOfMonth = 1;
		firstDate.setDate(firstDateOfMonth);		
		int firstDowOfMonth = firstDate.getDay();
		
		while (dayOfWeek != firstDowOfMonth) {
			firstDowOfMonth++;
			if (firstDowOfMonth == 7) {
				firstDowOfMonth = 0;
			}
			firstDateOfMonth++;
		}
		
		//myDay = dia obtenido anteriormente + (week*7);
		int day = firstDateOfMonth + ((week-1) * 7);
		
		//obtengo el ultimo dia del mes
		int lastDay = getLastDayOfMonth(index).getDate();
		
		//mientras este fuera del rango del mes, resto 7
		while (day > lastDay) {
			day = day - 7;
		}
			
		//seteo el nuevo dia al index
		Date aux = new Date(index);
		aux.setDate(day);
		
		return aux.getTime();
	}	
	

	private int week = 0;
	private int dayOfWeek = 0;
	
	/**
	 * Obtiene la repetición del tipo mensual
	 * @param data
	 */
	
	private void getDatesMonthly(RepetitionData data) {
		
		StringBuilder sb = new StringBuilder();
		sb.append("Repetir mensualmente");
		
		List<Date> dates = new ArrayList<Date>();

		
		// fecha de inicio
		Date start =	view.getStartRepetition();
		
		sb.append(" desde ").append(dateFormat.format(start));
		
		// cada ...
		long every = view.getRepeatedEvery();
		sb.append(" cada ").append(every);
		if (every == 1) {
			sb.append(" mes ");
		} else {
			sb.append(" meses ");
		}
		
		
		boolean sameDay = view.isDayOfMonth();
		
		long index = start.getTime();
		long finalMilis = getEndMonthMilis(every,index,sameDay,sb);
			
		if (sameDay) {
			sb.append(" el día ").append(start.getDate()).append(" del mes ");
		
			//genero las fechas
			index = nextDateMonthly(start.getTime(),every);
			
			while (index <= finalMilis) {
				Date d = new Date(index);
				dates.add(d);
				
				index = nextDateMonthly(index,every);
			}
			
		} else {
			//el mismo dia de la semana
			//tengo que obtener el numero de la semana y el dia
			
			findWeekAndDay(start);
			
			String weekStr = weekToString(week);
			String dayStr = dayOfWeekToString(dayOfWeek);
			
			sb.append(" el ").append(weekStr).append(" ").append(dayStr);
			
			//genero las fechas
			index = nextDateMonthly(start.getTime(),every,week,dayOfWeek);
			
			while (index <= finalMilis) {
				Date d = new Date(index);
				dates.add(d);
				
				index = nextDateMonthly(index,every,week,dayOfWeek);
			}
			
		}		
		
		data.dates = dates;
		data.descripcion = sb.toString();
		
	}
	
	
	/**
	 * Obtiene los datos de la vista y devuelve un listado de fechas
	 * @return
	 */
	private RepetitionData getDates() {
		
		RepetitionData data = new RepetitionData();
		data.descripcion = "Ninguna repetición";
		data.dates = new ArrayList<Date>();
		
		if (this.repeatTypeSelection == null) {
			return data;
		} 
		
		RepeatType type = this.repeatTypeSelection.getSelectedObject();
		
		if (type == null) {
			return data;
		}
		
		if (type.equals(RepeatType.DAILY)) {
			getDatesDaily(data);
		} else if (type.equals(RepeatType.WEEKLY)) {
			getDatesWeekly(data);
		} else if (type.equals(RepeatType.MONTHLY)) {
			getDatesMonthly(data);
		}
		
		return data;
	}
	
	
	@Override
	public void commit() {

		
		receiver.onSuccess(getDates());

		view.clear();
		repeatTypeSelection.clear();
		receiver = null;
		hideView();
	}

	@Override
	public void cancel() {
		view.clear();
		repeatTypeSelection.clear();
		
		RepetitionData data = new RepetitionData();
		data.descripcion = "Ninguna repetición";
		data.dates = new ArrayList<Date>();
		
		receiver.onSuccess(data);
		receiver = null;

		hideView();
	}

	
	
	public void findRepeatType() {
		if (view == null) {
			return;
		}
		List<RepeatType> types = Arrays.asList(RepeatType.values());
		view.setRepeatTypes(types);
	}
	
	
}
