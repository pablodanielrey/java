package ar.com.dcsys.model.utils;


public class AreasUtilsBean {
	
	/**
	 * Obtiene los subjectGroups de la persona que se encuentra logueada en el sistema.
	 * @return
	 * @throws AuthException
	 * @throws PersonException
	 * @throws MapauException
	 */
	/*
	 * TODO: tengo que ver bien los parametros y el tema del principal
	 */
	/*public static List<Area> getAreasFromLoggedUser(AuthsManager authsManager, PersonsManager personsManager, GroupsManager groupsManager, AreasManager aManager) throws AuthException, PersonException, MapauException {
		DCSysPrincipal principal = authsManager.getUserPrincipal();
		Person person = personsManager.findByPrincipal(principal);
		List<Group> groups = groupsManager.findByPerson(person);
		List<Area> areas = aManager.findBy(groups);
		return areas;
	}	
	*/
}
