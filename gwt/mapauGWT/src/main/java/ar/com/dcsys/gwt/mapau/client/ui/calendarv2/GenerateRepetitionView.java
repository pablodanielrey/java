package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import java.util.Date;
import java.util.List;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.SingleSelectionModel;

public interface GenerateRepetitionView extends IsWidget {

	public void setPresenter(Presenter p);
	
	public void clear();
	
	public void setRepeatTypeSelectionModel(SingleSelectionModel<RepeatType> selection);
	public void setRepeatTypes(List<RepeatType> types);
	
	public Integer getRepeatedEvery();
	
	public Date getStartRepetition();
	public void setStartRepetition(Date date);
	public void setStartReadOnly(boolean v);
	
	public boolean getEndRepetitionCount();
	public Integer getEndRepetitionCountValue();
	public boolean getEndRepetitionDate();
	public Date getEndRepetitionDateValue();
	
	public List<DayOfWeek> getDaysOfWeek();
	
	public boolean isDayOfMonth();
	
	
	public class RepetitionData {
		
		public String descripcion;
		public List<Date> dates;
		
	}
	
	
	public enum RepeatType {
		DAILY("Diaria"), WEEKLY("Semanal"), MONTHLY("Mensual");
		
		private final String description;
		
		private RepeatType(String description) {
			this.description = description;
		}
		
		public String getDescription() {
			return description;
		}
	} 
	
	public enum DayOfWeek {
		Monday("Lun",1), Tuesday("Mar",2), Wednesday("Mie",3), Thursday("Jue",4), Friday("Vie",5), Saturday("Sáb",6), Sunday("Dom",0);
		
		private final String description;
		private final Integer index;
		
		private DayOfWeek(String description, Integer index) {
			this.description = description;
			this.index = index;
		}
		
		public String getDescription() {
			return description;
		}

		public Integer getIndex() {
			return index;
		}
	}
	
	
	
	
	
	public interface Presenter {
		public void commit();
		public void cancel();
	}
}
