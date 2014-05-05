package ar.com.dcsys.gwt.assistance.client.activity.justification;

import java.util.ArrayList;
import java.util.List;

import ar.com.dcsys.data.justification.Justification;
import ar.com.dcsys.gwt.assistance.client.manager.JustificationsManager;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEvent;
import ar.com.dcsys.gwt.assistance.client.manager.events.JustificationModifiedEventHandler;
import ar.com.dcsys.gwt.assistance.client.ui.justification.manage.ManageJustificationsView;
import ar.com.dcsys.gwt.assistance.shared.AssistanceFactory;
import ar.com.dcsys.gwt.manager.shared.Receiver;

import com.google.inject.Inject;
import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;

import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;

public class ManageJustificationsActivity extends AbstractActivity implements ManageJustificationsView.Presenter{

	private final ManageJustificationsView view;
	private final AssistanceFactory assistanceFactory;
	private final JustificationsManager justificationsManager;
	private EventBus eventBus = null;
	
	private SingleSelectionModel<Justification> selection;
	private final List<Justification> justifications = new ArrayList<Justification>();

	private static final String CREATE = "Crear Nueva";
	private static final String MODIFY = "Modificar";
	
	private HandlerRegistration hr = null;
	
	private final JustificationModifiedEventHandler handler = new JustificationModifiedEventHandler() {
		@Override
		public void onJustificationModifiedEvent(JustificationModifiedEvent event) {
			if (view == null) {
				return;
			}
			view.clearData();
			update();
		}
	};
	
	
	@Inject
	public ManageJustificationsActivity(JustificationsManager justificationsManager, AssistanceFactory assistanceFactory, ManageJustificationsView view) {
		this.view = view;
		this.assistanceFactory = assistanceFactory;
		this.justificationsManager = justificationsManager;
		
		selection = new SingleSelectionModel<Justification>();
		selection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				Justification justification = selection.getSelectedObject();
				ManageJustificationsActivity.this.view.clearData();
				ManageJustificationsActivity.this.view.setActionText(CREATE);
				
				if (justification != null) {
					ManageJustificationsActivity.this.view.setJustification(justification);
					ManageJustificationsActivity.this.view.setActionText(MODIFY);
				}
			}
			
		});
	}
	
	private void setJustifications(List<Justification> justifications) {
		this.justifications.clear();
		this.justifications.addAll(justifications);
		view.setJustifications(justifications);
	}
	
	private void update() {
		this.justificationsManager.getJustifications(new Receiver<List<Justification>>() {

			@Override
			public void onSuccess(List<Justification> justifications) {
				if (view == null || justifications == null || justifications.size() < 1) {
					return;
				}
				setJustifications(justifications);
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
			}
			
		});
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		view.setPresenter(this);
				
		view.clear();
		view.setSelectionJustification(selection);
		view.setActionText(CREATE);
		
		panel.setWidget(view);
		
		update();
		
		hr = eventBus.addHandler(JustificationModifiedEvent.TYPE, handler);
	}
	
	@Override
	public void onStop() {
		if (hr != null) {
			hr.removeHandler();
		}
		view.clear();
		view.setPresenter(null);
	}

	@Override
	public void remove() {
		Justification justification = selection.getSelectedObject();
		if (justification == null) {
			return;
		}
		this.justificationsManager.remove(justification, new Receiver<Void>() {

			@Override
			public void onSuccess(Void t) {
				selection.clear();
				view.clear();
				update();
				showMessage("Justificación eliminada correctamente");
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage("No se permite eliminar esta justificación");
			}
			
		});
	}

	@Override
	public void cancel() {
		selection.clear();
		view.clearData();
	}
	
	private boolean existCode(String code, List<Justification> justifications) {
		boolean exist = false;
		String codeNew = code.replaceAll(" ", "");
		codeNew = codeNew.toLowerCase();
		
		for (Justification justification : justifications) {
			String codeJustification = justification.getCode();
			if (codeJustification == null) {
				continue;
			}
			codeJustification = codeJustification.replaceAll(" ", "");
			codeJustification = codeJustification.toLowerCase();
			if (codeJustification.equals(codeNew)) {
				exist = true;
				break;
			}
		}
		return exist;
	}

	@Override
	public void commit() {
		
		Justification justification = selection.getSelectedObject();
		List<Justification> justificationsAux = new ArrayList<Justification>(this.justifications); 
		
		final String msg;
		
		if (justification == null) {
			justification = assistanceFactory.justification().as();
			msg = "Se insertado correctamente la justificación";
		} else {			
			justificationsAux.remove(justification);
			msg = "Se ha actualizado correctamente la justificación";
		}
		
		if (existCode(view.getCode(),justifications)) {
			showMessage("El código ingresado ya existe");
			return;
		}
		
		justification.setCode(view.getCode());
		justification.setDescription(view.getDescription());
		
		justificationsManager.persist(justification, new Receiver<Void>() {

			@Override
			public void onSuccess(Void t) {
				showMessage(msg);
				selection.clear();
				view.clearData();
				update();
			}

			@Override
			public void onFailure(Throwable t) {
				showMessage(t.getMessage());
				view.clearData();
			}
			
		});
	}
		
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(true, message));
	}

}
