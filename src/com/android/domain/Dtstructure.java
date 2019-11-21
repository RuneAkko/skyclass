package com.android.domain;

public class Dtstructure {
	String id, courseid, name;

	public Dtstructure() {

	}
     public Dtstructure(String id,String courseid,String name){
    	 this.id=id;
    	 this.courseid=courseid;
    	 this.name=name;
     }

	public void setid(String id) {
		this.id = id;
	}

	public String getid() {
		return id;
	}
	public void setcourseid(String courseid) {
		this.courseid = courseid;
	}

	public String getcourseid() {
		return courseid;
	}
	
	

	public void setname(String name) {
		this.name=name;
	}

	public String getname() {
		return name;
	}
	
	@Override  
    public String toString() {  
        return "dtstructure [id=" + id + ",courseid="+courseid+", name=" + name  + "]";  
    }  
}
