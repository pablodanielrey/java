package ar.com.dcsys.gwt.mapau.client.activity.calendarv2;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.classroom.Characteristic;
import ar.com.dcsys.data.classroom.CharacteristicQuantity;
import ar.com.dcsys.data.classroom.ClassRoom;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.mapau.client.manager.ClassRoomsManager;
import ar.com.dcsys.gwt.mapau.client.manager.MapauManager;
import ar.com.dcsys.gwt.mapau.client.ui.calendarv2.classroom.ClassRoomAdminView;
import ar.com.dcsys.gwt.mapau.shared.ClassRoomsFactory;
import ar.com.dcsys.utils.CharacteristicSort;
import ar.com.dcsys.utils.ClassRoomSort;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.ResettableEventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;

public class ClassRoomAdminActivity extends AbstractActivity implements ClassRoomAdminView.Presenter {

	private final ClassRoomAdminView classRoomAdminView;
	private final MapauManager rf;
	private final ClassRoomsFactory cf;
	private final ClassRoomsManager classRoomsManager;
	
	private final SingleSelectionModel<ClassRoom> classRoomSelectionModel;
	private ResettableEventBus eventBus;
		

	private final Handler handler = new Handler() {
		@Override
		public void onSelectionChange(SelectionChangeEvent event) {
			ClassRoom classRoomSelected = classRoomSelectionModel.getSelectedObject();
			if (classRoomAdminView == null) {
				return;
			}

			clearCharacteristicQuantityForm();
			
			if (classRoomSelected == null ) {			
				clearClassRoomForm();
				classRoomAdminView.setCharacteristicQuantityFormVisible(false);
				classRoomAdminView.setCharacteristicQuantitys(new ArrayList<CharacteristicQuantity>());
				return;				
			} 
			
			classRoomAdminView.setName(classRoomSelected.getName());
			
			List<CharacteristicQuantity> characteristicQuantitys = (classRoomSelected.getCharacteristicQuantity() == null)? new ArrayList<CharacteristicQuantity>() : classRoomSelected.getCharacteristicQuantity();
			classRoomAdminView.setCharacteristicQuantitys(characteristicQuantitys);
			classRoomAdminView.setCharacteristicQuantityFormVisible(true);
						
		}
	};
	
	
	@Inject
	public ClassRoomAdminActivity(MapauManager rf, 
								  ClassRoomsFactory cf, 
								  ClassRoomsManager classRoomsManager, 
								  ClassRoomAdminView classRoomAdminView/*, @Assisted ClassRoomAdminPlace place*/){
		this.rf = rf;
		this.cf = cf;
		this.classRoomsManager = classRoomsManager;
		this.classRoomAdminView = classRoomAdminView;		
		
		classRoomSelectionModel = new SingleSelectionModel<ClassRoom>();
		classRoomSelectionModel.addSelectionChangeHandler(handler);
	}

	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = new ResettableEventBus(eventBus);
		
		classRoomAdminView.setPresenter(this);
		
		//TODO:falta implementar
		//ClassRoomAdminViewCss style = ClassRoomAdminViewResources.INSTANCE.style(); 
		//style.ensureInjected();	
		
		panel.setWidget(classRoomAdminView);
		
		setCharacteristics();
		setClassRooms();
		
		classRoomAdminView.setClassRoomsSelectionModel(classRoomSelectionModel);

		clearClassRoomForm();
		clearCharacteristicQuantityForm();	
	}
	
	@Override
	public void onStop() {
		eventBus.removeHandlers();
	}

	private void clearClassRoomForm(){
		classRoomAdminView.setName("");
	}
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	
	private void setClassRooms(){
		
		classRoomsManager.findAllClassRooms(new Receiver<List<ClassRoom>>() {
			
			@Override
			public void onSuccess(List<ClassRoom> classRooms) {
				ClassRoomSort.sort(classRooms);
				classRoomAdminView.setClassRooms(classRooms);
			}
			
			@Override
			public void onFailure(Throwable error) {
				showMessage("Error buscando las aulas: "+ error.getMessage());
			}
		});
	}
	
	private void clearCharacteristicQuantityForm() {
		classRoomsManager.findAllCharacteristics(new Receiver<List<Characteristic>>() {
			@Override
			public void onSuccess(List<Characteristic> characteristics) {
				if(characteristics == null || characteristics.size() <= 0) {
					classRoomAdminView.setCharacteristic(null);	
				} else {
					classRoomAdminView.setCharacteristic(characteristics.get(0));
				}

				classRoomAdminView.setQuantity(0L);				
			}
			@Override
			public void onFailure(Throwable error) {
				showMessage("Error al buscar todas las características: " + error.getMessage());
			}
		});
	}
	
	/**
	 * Definir characteristics
	 */
	private void setCharacteristics() {
		classRoomsManager.findAllCharacteristics(new Receiver<List<Characteristic>>() {
			@Override
			public void onSuccess(List<Characteristic> characteristics) {
				CharacteristicSort.sort(characteristics);
				classRoomAdminView.setCharacteristics(characteristics);				
			}
			
			@Override
			public void onFailure(Throwable error) {
				showMessage("Error al buscar todas las características: " + error.getMessage());
			}
		});

	}
	

	@Override
	/**
	 * Agregar characteristicQuantity de un classRoom a la base de datos
	 */
	public void persistCharacteristicQuantity() {
		
		if(classRoomSelectionModel.getSelectedObject() == null) {
			return;
		}
				
		//TODO verificar que los datos de la CharacteristicQuantity sean correctos
		
		//definir characteristicQuantity
		
		CharacteristicQuantity cq = cf.characteristicQuantity().as();		
		cq.setQuantity(classRoomAdminView.getQuantity());
		cq.setCharacteristic(classRoomAdminView.getCharacteristic());
		
	/*	req.persist(object)
		
		ClassRoom classRoom = classRoomSelectionModel.getSelectedObject();
		List<CharacteristicQuantity> characteristicQuantitys = testManager.findBy(classRoom);
		characteristicQuantitys.add(characteristicQuantity);
	
		classRoom.setCharacteristicQuantity(characteristicQuantitys);
		
		testManager.persist(classRoom);
		
		setClassRooms();
		
		classRoomAdminView.setCharacteristicQuantitys(characteristicQuantitys);*/
	}
	
	@Override
	/**
	 * Eliminar characteristicQuantity de un classRoom selected
	 */
	public void removeCharacteristicQuantity(CharacteristicQuantity characteristicQuantity) {
		
		if (characteristicQuantity == null) {
			return;
		}
		
		ClassRoom classRoom = classRoomSelectionModel.getSelectedObject();
		
		if (classRoom == null){
			return;
		}
		
		List<CharacteristicQuantity> characteristicQuantitys = new ArrayList<CharacteristicQuantity>();
		
		/*characteristicQuantitys = testManager.findBy(classRoom);
		
		characteristicQuantitys.remove(characteristicQuantity);
		
		classRoom.setCharacteristicQuantity(characteristicQuantitys);
		
		testManager.persist(classRoom);
			
		classRoomAdminView.setCharacteristicQuantitys(characteristicQuantitys);*/
	}
	
	
	@Override
	public void persistClassRoom() {
		//TODO verificar datos ingresados por el usuario antes de persist ClassRoom
		
	/*	ClassRoom classRoom = classRoomSelectionModel.getSelectedObject();
		
		if (classRoom == null) { 
			classRoom = new ClassRoom();
		}
		
		classRoom.setName(classRoomAdminView.getName());
		testManager.persist(classRoom);
		
		setClassRooms();
		classRoomSelectionModel.setSelected(classRoom, true);
		clearCharacteristicQuantityForm();*/
	}
	
	@Override
	public void removeClassRoom(ClassRoom classRoom) {
		if(classRoom != null) {
	/*		testManager.remove(classRoom);
			classRoomSelectionModel.setSelected(classRoomSelectionModel.getSelectedObject(), false);
			
			setClassRooms();
			clearClassRoomForm();
			clearCharacteristicQuantityForm();*/
		}
	}
}