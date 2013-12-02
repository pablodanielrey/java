package ar.com.dcsys.data.reserve;

import java.util.List;

import ar.com.dcsys.data.group.Group;
import ar.com.dcsys.data.reserve.GroupVisible.Params;
import ar.com.dcsys.exceptions.MapauException;
import ar.com.dcsys.exceptions.PersonException;

public class GroupVisibleBean extends VisibleBean {

	private static final long serialVersionUID = 1L;

	private List<Group> groups;

	private Params params;
	
	public GroupVisibleBean(Params params) {
		this.params = params;
	}
	
	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	@Override
	public Boolean isVisible() throws MapauException {
		try {
			List<Group> groups = params.getLoggedUserGroups();
			if (groups == null || groups.size() <= 0) {
				return false;
			}
			for (Group g : groups) {
				String personGroup = g.getId();
				for (Group g2 : this.groups) {
					if (personGroup.equals(g2.getId())) {
						return true;
					}
				}
			}
			return false;
		} catch (PersonException e) {
			throw new MapauException(e);
		}
	}

}