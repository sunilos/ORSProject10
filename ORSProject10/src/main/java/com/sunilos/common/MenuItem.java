package com.sunilos.common;

import java.util.LinkedHashSet;

public class MenuItem {

	String label = null;
	String url = null;
	LinkedHashSet<MenuItem> submenu = new LinkedHashSet<MenuItem>();

	public MenuItem(String label, String url) {
		this.label = label;
		this.url = url;
	}

	public void addSubmenu(String label, String url) {
		submenu.add(new MenuItem(label, url));

	}

	public void addSubmenu(MenuItem menuItem) {
		submenu.add(menuItem);

	}

	public void clear() {
		submenu = new LinkedHashSet<MenuItem>();
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LinkedHashSet<MenuItem> getSubmenu() {
		return submenu;
	}

	public void setSubmenu(LinkedHashSet<MenuItem> submenu) {
		this.submenu = submenu;
	}

}
