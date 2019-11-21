package com.android.domain;

public class Dtcourse {
	String id, cno, name;

	public Dtcourse() {
	}

	public Dtcourse(String id, String cno, String name) {
		this.id = id;
		this.cno = cno;
		this.name = name;
	}

	public void setid(String id) {
		this.id = id;
	}

	public String getid() {
		return id;
	}

	public void setcno(String cno) {
		this.cno = cno;
	}

	public String getcno() {
		return cno;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getname() {
		return name;
	}

	@Override
	public String toString() {
		return "dtcourse [id=" + id + ", name=" + name + ",cno=" + cno + "]";
	}
}
