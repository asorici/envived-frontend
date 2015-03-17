package models;

import javax.persistence.Entity;

import play.db.ebean.Model;


@Entity
public class Area extends Model{
	
	String name ;
	String tags;
	
	
	public String getName() {
		return name;
	}
	public void setAreaName(String name) {
		this.name = name;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}

}