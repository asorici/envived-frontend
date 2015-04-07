package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class Environment extends Model{
	@Id
	String id;
	
	String name ;
	String tags;
	String description;
	String width;
	String height;
	String img_thumbnail_url;
	
	
	public String getWidth() {
		return width;
	}
	public void setWidth(String width) {
		this.width = width;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getImg_thumbnail_url() {
		return img_thumbnail_url;
	}
	public void setImg_thumbnail_url(String img_thumbnail_url) {
		this.img_thumbnail_url = img_thumbnail_url;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
