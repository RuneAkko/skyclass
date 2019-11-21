package com.android.domain;

public class Dtplaycourse {
	String id, name, svpath,svpath_abs, cno;
	//String id, name, svpath, cno;

	public Dtplaycourse() {
	}

	//public Dtplaycourse( String id, String name,String showname, String svpath) {
	//public Dtplaycourse(String id, String name, String svpath, String cno) {
	//public Dtplaycourse(String cid, String id, String name, String svpath, String structureid) {
	public Dtplaycourse(String cno, String id, String name, String svpath, String svpath_abs) {
		this.cno = cno;
		this.id = id;
		this.name = name;
        //this.showname = showname;
		this.svpath = svpath;
		this.svpath_abs = svpath_abs;
		//this.structureid = structureid;
	}


	public void setcno(String cno) {
		this.cno = cno;
	}

	public String getcno() {
		return cno;
	}

	public void setid(String id) {
		this.id = id;
	}

	public String getid() {
		return id;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getname() {
		return name;
	}

	public void setsvpath(String svpath) {
		this.svpath = svpath;
	}

	public String getsvpath() {
		return svpath;
	}
	public void setsvpath_abs (String svpath_abs ) {
		this.svpath_abs  = svpath_abs ;
	}

	public String getsvpath_abs () {
		return svpath_abs ;
	}

	/*public void setstructureid(String structureid) {
		this.structureid = structureid;
	};


	public String getstructureid() {
		return structureid;
	}*/


	@Override
	public String toString() {
		//return "dtplaycourse [id="+id+", name=" + name + ",svpath=" + svpath + "]";
		//return "dtplaycourse [cid=" + cid + ",id="+id+", name=" + name + ",svpath=" + svpath + ",structureid=" + structureid + "]";
		//return "dtplaycourse [cno=" + cno + ",id="+id+", name=" + name + ",svpath=" + svpath + " ]";
		return "dtplaycourse [cno=" + cno + ",id="+id+", name=" + name + ",svpath=" + svpath + ",svpath_abs =" + svpath_abs + " ]";
		//return "dtplaycourse [cno=" + cno + ",id="+id+", name=" + name + ",svpath=" + svpath_apple + " ]";
	}
}
