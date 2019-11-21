package com.android.domain;

import java.util.List;

public class TableData {

	private String Name;

	private List Rows;

	public String getName() {
		return Name;
	}

	public void setName(String Name) {
		this.Name = Name;
	}

	public List getRows() {
		return Rows;
	}

	public void setTableData(List Rows) {
		this.Rows = Rows;
	}
}
