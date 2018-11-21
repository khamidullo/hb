package uz.hbs.beans;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.util.io.IClusterable;

public class Role implements IClusterable {
	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	private List<Action> actionslist;
	private Integer actions_count;
	private Integer users_count;

	public Role() {
		actionslist = new ArrayList<Action>();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Action> getActionslist() {
		return actionslist;
	}

	public void setActionslist(List<Action> actionslist) {
		this.actionslist = actionslist;
	}

	public Integer getActions_count() {
		return actions_count;
	}

	public void setActions_count(Integer actions_count) {
		this.actions_count = actions_count;
	}

	public Integer getUsers_count() {
		return users_count;
	}

	public void setUsers_count(Integer users_count) {
		this.users_count = users_count;
	}
}
