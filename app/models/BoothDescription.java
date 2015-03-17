package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.ebean.Model;

@Entity
public class BoothDescription extends Model {
	
	@Id
	String product_id;
	String product_name;
	String product_description;
	
	public BoothDescription(String product_id, String product_name, String product_description) {
		 
	    this.product_id = product_id;
	    this.product_name = product_name;
	    this.product_description = product_description;    
	  }
	
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_description() {
		return product_description;
	}
	public void setProduct_description(String product_description) {
		this.product_description = product_description;
	}
	
	
}
