package models;

import javax.persistence.Entity;


@Entity
public class Area {
	
	String areaName ;
	String tags;
	
	
	public String getAreaName() {
		return areaName;
	}
	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}

}