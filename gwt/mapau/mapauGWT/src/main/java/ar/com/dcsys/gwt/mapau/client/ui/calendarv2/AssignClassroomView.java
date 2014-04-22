package ar.com.dcsys.gwt.mapau.client.ui.calendarv2;

import java.util.List;

import ar.com.dcsys.data.classroom.ClassRoom;

import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.MultiSelectionModel;

public interface AssignClassroomView extends IsWidget {

	
	public void setClassRoomsSelectionModel(MultiSelectionModel<ClassRoom> selection);
	public void setClassRooms(List<ClassRoom> classRooms);
	
	public boolean getSameHours();
	public void setRepetitionSummary(String summary);
	
	public boolean getCheckCapacity();
	public void setCheckCapacity(boolean checkCapacity);
	
	public void setPresenter(Presenter p);
	public void clear();
	
	public void showValidationMessage();
	public void hideValidationMessage();
	
	public AcceptsOneWidget getUserValidationContainer();
	
	public interface Presenter {
		public void generateRepetition();
		public void findClassroom();
		public void commit();
		public void cancel();
	}
	
}
