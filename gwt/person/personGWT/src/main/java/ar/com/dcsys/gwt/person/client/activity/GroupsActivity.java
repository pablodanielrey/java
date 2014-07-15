package ar.com.dcsys.gwt.person.client.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.group.GroupType;
import ar.com.dcsys.data.person.Mail;
import ar.com.dcsys.data.person.Person;
import ar.com.dcsys.data.person.PersonType;
import ar.com.dcsys.gwt.clientMessages.client.MessageDialogEvent;
import ar.com.dcsys.gwt.manager.shared.Receiver;
import ar.com.dcsys.gwt.person.client.manager.GroupsManager;
import ar.com.dcsys.gwt.person.client.manager.PersonsManager;
import ar.com.dcsys.gwt.person.client.place.GroupsPlace;
import ar.com.dcsys.gwt.person.client.ui.group.GroupsView;

import com.google.gwt.activity.shared.AbstractActivity;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent.Handler;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;


public class GroupsActivity extends AbstractActivity implements GroupsView.Presenter {

	private final GroupsView view;
	
	private final SingleSelectionModel<Group> groupsSelection;
	private final SingleSelectionModel<Person> personsInSelection;
	private final SingleSelectionModel<Person> personsOutSelection;
	private final SingleSelectionModel<GroupType> groupTypeSelection;
	
	private final List<Person> personsCache;
	private final List<Group> groupsCache;
	private final Map<Group,List<GroupType>> groupTypesCache;
	
	private final PersonsManager personsManager;
	private final GroupsManager groupsManager;
	
	private EventBus eventBus;
	
	@Inject
	public GroupsActivity(PersonsManager personsManager, GroupsManager groupsManager, GroupsView view, @Assisted GroupsPlace place) {
		this.personsManager = personsManager;
		this.groupsManager = groupsManager;
		this.view = view;
		
		personsCache = new ArrayList<Person>();
		groupsCache = new ArrayList<Group>();
		groupTypesCache = new HashMap<Group,List<GroupType>>();
		
		groupsSelection = new SingleSelectionModel<Group>();
		groupsSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				
				final Group group = groupsSelection.getSelectedObject();
				if (group == null) {
				
					clearPersonsSelection();
					GroupsActivity.this.view.clearGroupData();
					setGroupPersons(null);

				} else {
					
					GroupsActivity.this.view.setGroup(group);
					GroupsActivity.this.view.setSelectedGroupTypes(group.getTypes());
					String id = group.getId();
					updateGroupPersons(id);		
				
				}
			}
		});
		
		groupTypeSelection = new SingleSelectionModel<GroupType>();
		groupTypeSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				groupsSelection.clear();
				setFilteredGroups();
			}
		});	
		
		
		personsInSelection = new SingleSelectionModel<Person>();
		personsInSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				final Person person = personsInSelection.getSelectedObject();
				if (person == null) {
					return;
				}
				personsInSelection.clear();
				final Group group = groupsSelection.getSelectedObject();
				assert group != null;
				
				removePersonFromGroup(group, person);
				updateGroupPersons(group.getId());
			}
		});
		
		personsOutSelection = new SingleSelectionModel<Person>();
		personsOutSelection.addSelectionChangeHandler(new Handler() {
			@Override
			public void onSelectionChange(SelectionChangeEvent event) {
				final Person person = personsOutSelection.getSelectedObject();
				if (person == null) {
					return;
				}
				personsOutSelection.clear();
				final Group group = groupsSelection.getSelectedObject();
				assert group != null;
				
				addPersonToGroup(group, person);
				updateGroupPersons(group.getId());
			}
		});
		

	}
	
	private void addPersonToGroup(Group g, Person p) {
		String personId = p.getId();
		Person person = null;
		for (Person p2 : g.getPersons()) {
			if (personId.equals(p2.getId())) {
				person = p2;
			}
		}
		if (person == null) {
			g.getPersons().add(p);
		}		
	}
	
	private void removePersonFromGroup(Group g, Person p) {
		String personId = p.getId();
		Person personToRemove = null;
		for (Person p2 : g.getPersons()) {
			if (personId.equals(p2.getId())) {
				personToRemove = p2;
			}
		}
		if (personToRemove != null) {
			g.getPersons().remove(personToRemove);
		}		
	}
	
	
	/**
	 * Obtiene la lista de personas del grupo y las setea en las listas correctas.
	 * @param id
	 */
	private void updateGroupPersons(String id) {
		for (Group g : groupsCache) {
			if (id.equals(g.getId())) {
				setGroupPersons(g.getPersons());
				break;
			}
		}
	}
	
	/**
	 * ,setea en la vista las personas correspondientes a cada lista.
	 * @param persons
	 */
	private void setGroupPersons(List<Person> persons) {
		List<Person> out = new ArrayList<Person>();
		out.addAll(personsCache);
		
		// elimino las personas que pertenecen al grupo.
		if (persons != null) {

			for (Person p : persons) {
				Iterator<Person> it = out.iterator();
				while (it.hasNext()) {
					Person person = it.next();
					String p1Id = person.getId();
					String p2Id = p.getId();
					if (p1Id.equals(p2Id)) {
						it.remove();
						break;
					}
				}
			}
			
			view.setInPersons(persons);
		}
		
		view.setOutPersons(out);
	}
	
	@Override
	public void start(AcceptsOneWidget panel, EventBus eventBus) {
		this.eventBus = eventBus;
		
		view.setPresenter(this);
		view.setSelectionModel(groupsSelection);
		view.setInSelectionModel(personsInSelection);
		view.setOutSelectionModel(personsOutSelection);
		view.setGroupTypeSelectionModel(groupTypeSelection);
		view.clear();
		
		panel.setWidget(view);
		
		findGroups();
		findAllGroupTypes();
		findAllPersonTypes();
	}
	
	@Override
	public void onStop() {
		eventBus = null;
		
		view.clear();
		view.setPresenter(null);
		view.setSelectionModel(null);		
		
		super.onStop();
	}
	
	private void showMessage(String message) {
		eventBus.fireEvent(new MessageDialogEvent(message));
	}
	
	
	private void findAllGroupTypes() {
		groupsManager.findAllGroupTypes(new Receiver<List<GroupType>>() {
			@Override
			public void onSuccess(List<GroupType> types) {
				view.setAllGroupTypes(types);
			}
			public void onError(String error) {
				showMessage(error);
			};
		});
	}
	
	private void findAllPersonTypes() {
		personsManager.findAllTypes(new Receiver<List<PersonType>>() {
			@Override
			public void onSuccess(List<PersonType> types) {
				view.setAllPersonTypes(types);
			}
			public void onError(String error) {
				showMessage(error);
			};
		});
	}

	private void clearSelections() {
		groupsSelection.clear();
		clearPersonsSelection();
	}
	
	private void clearPersonsSelection() {
		personsInSelection.clear();
		personsOutSelection.clear();
	}

	
	/**
	 * Se obtienen todos los grupos existentens dentro del sistema.
	 */
	private void findGroups() {
		// se piden los grupos SIN personas
		groupsCache.clear();

		groupsManager.findAll(new Receiver<List<Group>>() {
			@Override
			public void onError(String error) {
				showMessage(error);
			}
			@Override
			public void onSuccess(List<Group> groups) {
				if (groups == null || view == null) {
					return;
				}
				clearSelections();
				groupsCache.addAll(groups);
				setFilteredGroups();
			}
		});
	}	
	

	/**
	 * Setea los grupos ya filtrados por tipo.
	 */
	private void setFilteredGroups() {
		GroupType selected = groupTypeSelection.getSelectedObject();
		if (selected == null) {
			// quiere decir que se debe mostar todos los grupos.
			GroupsActivity.this.view.setGroups(groupsCache);
			return;
		}
		// filtro los grupos del cache por los que tienen ese tipo y los seteo nuevamente en la vista.
		List<Group> selectedGroups = new ArrayList<Group>();
		for (Group g : groupsCache) {
			List<GroupType> groupTypes = groupTypesCache.get(g);
			if (groupTypes == null) {
				continue;
			}
			if (groupTypes.contains(selected)) {
				selectedGroups.add(g);
			}
		}
		GroupsActivity.this.view.setGroups(selectedGroups);		
	}
	
	
	/// cuando se selecciona un tipo de persona ////
	
	/**
	 * Busca las personas de los tipos seleccionados.
	 */
	@Override
	public void updatePersons() {
		personsCache.clear();
		List<PersonType> types = view.getSelectedPersonTypes();
		personsManager.findAll(types, new Receiver<List<Person>>() {
			@Override
			public void onSuccess(List<Person> persons) {
				assert persons != null;
				personsCache.addAll(persons);
				updatePersonGroups();
			}
			@Override
			public void onError(String error) {
				showMessage(error);
			}
		});
	}
	
	/**
	 * Chequea si un grupo esta seleccionado y setea correctamente las personas en las listas.
	 */
	private void updatePersonGroups() {
		Group selected = groupsSelection.getSelectedObject();
		if (selected == null) {
			setGroupPersons(null);
		} else {
			updateGroupPersons(selected.getId());
		}
	}
	
	/**
	 * Actualiza o crea un grupo.
	 */
	@Override
	public void createUpdate() {
		
		if (!checkPreconditions()) {
			return;
		}
		
		Group g = groupsSelection.getSelectedObject();
		if (g == null) {
			createNewGroup();
		} else {
			updateGroup(g);
		}
	}
	
	private boolean checkPreconditions() {
		String name = view.getName();
		if (name == null || name.trim().equals("")) {
			return false;
		}
		return true;
	}
	
	/**
	 * Crea un nuevo grupo
	 */
	private void createNewGroup() {
		Group g = new Group();
		createUpdateGroup(g);
	}

	/**
	 * Actualiza un grupo
	 * @param g
	 */
	private void updateGroup(Group g) {
		createUpdateGroup(g);
	}
	
	/**
	 * Crea o actualiza un grupo
	 */
	private void createUpdateGroup(final Group g) {
		String name = view.getName();
		g.setName(name);

		String mail = view.getMail();
		Mail m = new Mail();
		m.setMail(mail);
		m.setPrimary(true);
		g.setMails(Arrays.asList(m));

		
		List<GroupType> types = view.getSelectedGroupTypes();
		g.setTypes(types);
		
		showMessage("Grupo creado exitosamente");
		clearSelections();
		findGroups();
	}
	
	
	/*
	
	@Override
	public void persist() {
		view.closeWarning();
		
		Group group = pendingChanges;
		if (group == null) {
			return;
		}
		rf.groupRequest().persist(group).fire(new Receiver<Void>() {
			@Override
			public void onSuccess(Void v) {
				pendingChanges = null;
				Window.alert("Cambios guardados con éxito");
			}
			@Override
			public void onFailure(ServerFailure error) {
				super.onFailure(error);
				Window.alert(error.getMessage());
			}
		});
	}
	
	@Override
	public void cancel() {
		view.closeWarning();
		
		Group group = pendingChanges;
		if (group == null) {
			return;
		}
		String id = group.getId();
		updateGroupPersons(id);
		pendingChanges = null;
	}
	*/

}
