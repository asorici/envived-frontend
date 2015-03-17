package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.Area;
import models.BoothDescription;
import models.Environment;
import models.UserProfile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.ssl.HttpsURLConnection;

import play.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Akka;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.mvc.*;
import views.html.*;
import views.html.helper.form;

public class Application extends Controller {
	
	final static Form<Environment> EnvNew = Form.form(Environment.class);
	
    public static Result index() {
    	
        return ok(Index.render("Welcome to Envived"));
    }
    
	public static Result welcome()
	{	
	    return ok(Profile.render(session("first_name")));
		
	}
       
    /**
     * Register:
     * */
    
    public static Result Register(){
    	
    	return ok(Register.render("Register"));
    }
    
    public static Result AddUser(){
    	
      Form<UserProfile> formData = Form.form(UserProfile.class);
      DynamicForm myRegisterForm = Form.form().bindFromRequest();
    	
      String idReturn = "http://localhost:8080/envived/client/v2/actions/register/";

      String registerParam = "email"+"="+myRegisterForm.get("email")
			   +"&"+"first_name"+"="+myRegisterForm.get("first_name")
			   +"&"+"last_name"+"="+myRegisterForm.get("last_name")
			   +"&"+"password1"+"="+myRegisterForm.get("password1")
		       +"&"+"password2"+"="+myRegisterForm.get("password2");
    	  
      String email = myRegisterForm.get("email");
      String password1 = myRegisterForm.get("password1");
      String password2 = myRegisterForm.get("password2");
      
      System.out.println("New user: "+email+" "+" password1: "+password1+" password2: "+password2);
      
  	  WSRequestHolder holder = WS.url(idReturn);
  	  holder.setContentType("application/x-www-form-urlencoded");
  	  
  	  holder.setHeader("Cookie","sessionid="+session("sessionid"));
  	  WSResponse response = holder.post(registerParam).get(20000);

  	System.out.println("rez is: "+ response.getStatus()+response.getBody());

	 boolean successYes;
	 JSONObject out;
	 JSONObject allData;
	 String success = "success";
	 String resources	= "resource_uri";
	 String inputs = response.getBody(); 
	
 try{
	 		 
	 out = new JSONObject(inputs.toString());
	 successYes = out.getBoolean(success);
	 allData = out.getJSONObject("data");
	 
  if(successYes == false){
		  //TODO:validari
		  return ok(Register.render("wrong credentials...."));
	  }
  	   
    	  if(allData.has(resources)){
    		  
    	  String userId = allData.getString(resources).split("/")[6];
    	  session("userid",userId);
    	  System.out.println("id user: " + userId);
    	  
    	  }
    	   	  
    	  if(allData.has("first_name")){
    		  
    		  String user_name;
    		  user_name = allData.getString("first_name");
    		  session("first_name", user_name);
    		  
    		  System.out.println("\nUser Name : " + session("userid"));
    		  
    	  }
    	  
    	  session(response.getCookie("sessionid").getName(), response.getCookie("sessionid").getValue());	  
	  
	 } catch (JSONException e) {
			
			e.printStackTrace();
			System.out.println("something is missing....");
	 }
	 		
 	return ok(Index.render("Now you can log in ..."));
      	  
}   
    
    /**
     * Forgot Password
     * */
    
    public static Result ForgotPass(){
    	
    	return TODO;
    }
    
    public static Result PostForgotPass(){
    	
    	return TODO;
    }
    
    /**
     * Login
     * */
    
    public static Result LoginPage(){
    	
    	return ok (Login.render(""));
    }
    
    public static Result PostLogin(){
    	
    	  String idReturn = "http://localhost:8080/envived/client/v2/actions/login/";
    	  
    	  DynamicForm myLoginForm = Form.form().bindFromRequest();
    	  String loginParam = "email="+myLoginForm.get("email")+"&password="+myLoginForm.get("password");
    	  
          String email = myLoginForm.get("email");
          String password = myLoginForm.get("password");
          
          System.out.println("The user is: "+email+" "+"\npassword: "+password);
          
          WSRequestHolder holder = WS.url(idReturn);
          holder.setContentType("application/x-www-form-urlencoded");
          WSResponse response = holder.post(loginParam).get(20000);
          
          System.out.println("response:  "+ response.getStatus()+response.getBody());
          
          if(response.getStatus()<400){
        	  
        	  JsonNode responseJson = response.asJson();
        	  List<String> userid = responseJson.findValuesAsText("resource_uri");
        	  
        	  String id = userid.get(0).split("/")[6];
        	  session("userid",id);
        	  
        	  System.out.println("The id is: "+id+" :)");
        	  System.out.println(response.getCookie("sessionid").getName()+response.getCookie("sessionid").getValue());
        	  
        	  session(response.getCookie("sessionid").getName(), response.getCookie("sessionid").getValue());
        	
        	  return redirect(routes.Application.welcome());
        	  
          }
        	  return ok(Login.render("wrong credentials"));    	  
    }
    
    /**
     * Profile
     * */
       
    public static Result EditProfile(){
    	
    	return ok(EditProfile.render(new Form<UserProfile>(UserProfile.class)));
    }
    
    public static Result AddEditedProfile(){
    	return TODO;
    }
    
    /**
     * Add Environment
     * */
    
    //add a new environment
    public static Result AddEnvironment(){
    	
    	String val = session("userid");
    	System.out.println("Add Environment : "+ val);
    	
    	return ok(NewEnvironment.render());
    }
    
    //post the new environment
    public static Result PostNewEnvironment(){
    	
   	
        Form<Environment> formData = Form.form(Environment.class);
        Form <Environment> form = formData.bindFromRequest();
        Environment envData = form.get();
                
        String idReturn ="http://localhost:8080/envived/client/v2/resources/environment/?clientrequest=true&virtual=true&format=json/";
  
        ObjectNode jsonParams = Json.newObject();
        jsonParams.put("name", envData.getName() );
        jsonParams.put("owner", "/envived/client/v2/resources/user/"+session("userid")+"/");
        jsonParams.put("tags", envData.getTags());
        
        String name = envData.getName();
        System.out.println("New environment : " + name);
    
        WSRequestHolder holder=WS.url(idReturn);
        holder.setContentType("application/json");
        holder.setHeader("Cookie","sessionid="+session("sessionid"));
  
        WSResponse postResult = holder.post(jsonParams).get(20000);
         
        System.out.println("status result: " + postResult.getStatus());    
        System.out.println("Bello !\n ^_^ i am in 'post new env', and the session id is :\n "+session("sessionid")+ " \nJson result: " + jsonParams);
        
         idReturn = "http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json/";
 		 jsonParams.put("category", "description");
 		 jsonParams.put("description", envData.getDescription());  		 
 		 
 		 holder=WS.url(idReturn);
 		 holder.setContentType("application/json");
 		 holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	
 		 postResult = holder.post(jsonParams).get(20000);
 		
 		 System.out.println("description (environment) status result:" + postResult.getStatus());		 
        
        return redirect(routes.Application.welcome());
 		   
    }
    
    //view all environments
    public static Result viewEnvironments(){
 
     String getEnvUrl = "http://localhost:8080/envived/client/v2/resources/environment/?clientrequest=true&virtual=true&format=json&owner="+session("userid");
     
   	 WSRequestHolder holder = WS.url(getEnvUrl);
   	 
   	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
   	 holder.setContentType("application/json");
   	 WSResponse envListResponse = holder.get().get(200000);
   	 
   	 System.out.println("\nStatus response: "+envListResponse.getStatus());
   	 
   	 JsonNode content = envListResponse.asJson();
   	 
   	 List<String> listaIduri = content.findValuesAsText("id");
  	 List<String> listaNume = content.findValuesAsText("name");
  	     
  	     HashMap<String, String>hash=new HashMap<String, String>();
		   	    
		   	 for(int i=0;i<listaIduri.size();i++){
		   		hash.put(listaIduri.get(i),listaNume.get(i));
		   	 	}
		   	 
		   	System.out.println("Environments:\n "+listaNume);
		   	System.out.println("");
		   	
		  return ok(ViewEnvironment.render(hash));
  		
    }
    
    //edit environment
    public static Result editEnv(String id){
    	
    	
    	 String getEnvUrl = "http://localhost:8080/envived/client/v2/resources/environment/"+id+"/?virtual=true";
    	 WSRequestHolder holder=WS.url(getEnvUrl);
    	 
    	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	 holder.setContentType("application/json");
    	 
    	 WSResponse envListResponse = holder.get().get(200000);
    	 System.out.println("Edit environment status response : "+envListResponse.getStatus());
    	 
    	 JsonNode content = envListResponse.asJson();
    	 List<String> nume = content.findValuesAsText("name");
    	 String tags = "";
    	 
    	 Iterator<JsonNode> it ;
    	 
    	 it=content.findPath("tags").iterator();
		 while(it.hasNext()){
			 JsonNode content_keyword=it.next();
			 tags+=content_keyword.asText()+" ";
		 }
		 
		 session("editid",id);
	 return ok(EditEnvironment.render(nume.get(0),tags,id));
    	 
    }
    
    //save edited environment
    public static Result saveEnv(String id){
    	
    	DynamicForm mySaveForm;
    	mySaveForm = Form.form().bindFromRequest();
    	
    	String date = mySaveForm.get("name")+mySaveForm.get("tags");
    	System.out.println("(save/delete edited environment) Datele sunt: "+date);
    	
    	switch (mySaveForm.get("update")){
    	case "save":
    		String saveUrl = "http://localhost:8080/envived/client/v2/resources/environment/"+ session("editid")+"/?clientrequest=true&virtual=true&format=json/";
    		ObjectNode jsonParams = Json.newObject();
    		
    		jsonParams.put("name", mySaveForm.get("name"));
    		jsonParams.put("owner", "/envived/client/v2/resources/user/"+session("userid")+"/");
    		jsonParams.put("tags", mySaveForm.get("tags"));
    		
    		System.out.println("Save edited environment: ");
    		System.out.println("userid:" + session("userid") + " sessionid:" + session("sessionid") + "\njson result: " + jsonParams);
    		
    		WSRequestHolder holder=WS.url(saveUrl);
    		holder.setContentType("application/json");
    	    holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	    WSResponse rezultatPut = holder.put(jsonParams).get(20000);
    		break;
    		
    	case "delete":
    		 String deleteUrl = "http://localhost:8080/envived/client/v2/resources/environment/"+id+"/?clientrequest=true&virtual=true&format=json";
    		 WSRequestHolder delHolder=WS.url(deleteUrl);
    		 delHolder.setContentType("application/json");
    		 delHolder.setHeader("Cookie","sessionid="+session("sessionid"));
    		 WSResponse rezultatDelete = delHolder.delete().get(20000);
    		 System.out.println("*delete (env) result: "+rezultatDelete.getBody()+"status: "+rezultatDelete.getStatus());
    		 
//    		break;
    		 return redirect(routes.Application.welcome());
    		default: break;	
    	
    	}
    	return redirect(routes.Application.viewEnv(id));
    	
    }
    
    //view an environment :
    public static Result viewEnv(String id) throws JsonParseException, JsonMappingException, IOException{
    	
    	 String getEnvUrl = "http://localhost:8080/envived/client/v2/resources/environment/"+id+"/?virtual=true";
    	 WSRequestHolder holder=WS.url(getEnvUrl);
    	 
    	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	 holder.setContentType("application/json");
    	 
    	 WSResponse envListResponse = holder.get().get(200000);
    	 System.out.println("Edit environment status response : "+envListResponse.getStatus());
    JsonNode content = envListResponse.asJson();
	 
	 List<String> nume = content.findValuesAsText("name");
	 String tags = "";
	 
	 Iterator<JsonNode> it ;
	 
	 it=content.findPath("tags").iterator();
	 while(it.hasNext()){
		 JsonNode content_keyword=it.next();
		 tags+=content_keyword.asText()+" ";
	 }
	 
	 session("editid",id);
    
	 return ok(viewEnv.render(nume.get(0),tags,id));
		 
    	
    }
    
      
    /**
     * Environment Description
     * */

//if i have description return edit page if not return add description page
public static Result envDescription(String id){
	
	
	 String descrUrl ="http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json&environment="+id;
	
	 WSRequestHolder holder=WS.url(descrUrl);
	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
	 holder.setContentType("application/json");
	 
	 WSResponse response = holder.get().get(20000);
	 System.out.println("i'm in view environment description");
	 System.out.println("view description env response status:"+response.getStatus());
	
	 JsonNode content = response.asJson();
	 List<String> description = content.findValuesAsText("description");

try{
   
	 if(description.get(0) != null){ 		 
		 System.out.println("you have a description ...");
	    return ok(editEnvDesc.render(description.get(0),id));
	 	}
	}catch(InputMismatchException ex){
		 System.out.println("This is your problem: " +  ex.getMessage()   
		          + "\nHere is where it happened:\n");
		       ex.printStackTrace();
	   }catch (IndexOutOfBoundsException ex )
    {  
       System.out.println("This is your problem: " +  ex.getMessage()   
          + "\nHere is where it happened:\n");
       ex.printStackTrace(); 
     } 
	
	return ok(EnvDescription.render(id));
	
}

//Post the new environment descrption
public static Result PostEnvDescription(String id){
	
	DynamicForm envData = Form.form().bindFromRequest();
	
	String url ="http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json"; 	
	
	ObjectNode jsonParams = Json.newObject();
	jsonParams.put("category", "description");
	jsonParams.put("description", envData.get("textarea"));
	jsonParams.put("environment", "/envived/client/v2/resources/environment/"+id+"/");
	
	 WSRequestHolder holder=WS.url(url);
		 holder.setContentType("application/json");
		 holder.setHeader("Cookie","sessionid="+session("sessionid"));
	
		 WSResponse postResult = holder.post(jsonParams).get(20000);
		 
		 System.out.println("add environment description status result:" + postResult.getStatus());
		 System.out.println("userid:" + session("userid") + "\nsessionid:" + session("sessionid") + " \nJson result: " + jsonParams);
		
		
		return redirect(routes.Application.viewEnv(id));
}

//view area description if it has one, if not redirect to add one :)
public static Result viewDescriptionEnv(String id){
	
	 String descrUrl ="http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json&environment="+id;
	
	 WSRequestHolder holder=WS.url(descrUrl);
	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
	 holder.setContentType("application/json");
	 
	 WSResponse response = holder.get().get(20000);
	 System.out.println("i'm in view env description");
	 System.out.println("view description env response status:"+response.getStatus());
	
	 JsonNode content = response.asJson();
	 List<String> description = content.findValuesAsText("description");

try{
	 if(description.get(0) != null){ 		 
		 System.out.println("you have a description ...: "+description.get(0));
	    return ok(viewEnvDesc.render(description.get(0),id));
	 	}
	}catch(InputMismatchException ex){
		 System.out.println("This is your problem: " +  ex.getMessage()   
		          + "\nHere is where it happened:\n");
		       ex.printStackTrace();
	   }catch (IndexOutOfBoundsException ex )
    {  
       System.out.println("This is your problem: " +  ex.getMessage()   
          + "\nHere is where it happened:\n");
       ex.printStackTrace(); 
     } 
	

return redirect(routes.Application.envDescription(id));

}


//Edit Description Environment : save or delete the environment description
public static Result saveDeleteDescEnv(String id){
	DynamicForm mySaveForm;
	mySaveForm = Form.form().bindFromRequest();
	
	String deleteUrl = "http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json&environment="+id;
	
	
	String date = mySaveForm.get("name")+mySaveForm.get("tags");
	System.out.println("(save/delete edited env) Datele sunt: "+date);
	
	switch (mySaveForm.get("update")){
	case "save":
		String saveUrl = "http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json&environment="+session("editid");
		
		//delete old description
		 WSRequestHolder delHolder=WS.url(deleteUrl);
		 delHolder.setContentType("application/json");
		 delHolder.setHeader("Cookie","sessionid="+session("sessionid"));
		 WSResponse rezultatDelete = delHolder.delete().get(20000);
		 System.out.println("*delete (old description env, to add the new one) result: "+rezultatDelete.getBody()+"status: "+rezultatDelete.getStatus());
		 
		 
		ObjectNode jsonParams = Json.newObject();
		
		jsonParams.put("category", "description");
    	jsonParams.put("description", mySaveForm.get("textarea"));
    	jsonParams.put("environment", "/envived/client/v2/resources/environment/"+id+"/");
		
		System.out.println(":) Save edited description environment: ");
		System.out.println("userid:" + session("userid") + " sessionid:" + session("sessionid") + "\njson result: " + jsonParams);
		
		WSRequestHolder holder=WS.url(saveUrl);
		holder.setContentType("application/json");
	    holder.setHeader("Cookie","sessionid="+session("sessionid"));
	    WSResponse postResult = holder.post(jsonParams).get(20000);
	    
	    System.out.println("status result save new description: "+postResult.getStatus());
		break;
		
		
	case "delete":
		  WSRequestHolder delHolder2=WS.url(deleteUrl);
		 delHolder2.setContentType("application/json");
		 delHolder2.setHeader("Cookie","sessionid="+session("sessionid"));
		 WSResponse rezultatDelete2 = delHolder2.delete().get(20000);
		 System.out.println("*delete (description environment) result: "+rezultatDelete2.getBody()+"status: "+rezultatDelete2.getStatus());
		 
		break;
		default: break;	
	
	}
	return redirect(routes.Application.viewEnv(id));

}


    /**
     * Add Area
     * */
      
    //add a new area
    public static Result AddArea(String id){
    	   
 		return ok(newarea.render(id));
    }
    
    //post the new area
    public static Result PostNewArea(String id){
    	//env id = String id
    	 DynamicForm areaData = Form.form().bindFromRequest(); 
     	 
    	 //tags and name
     	 String idReturn = "http://localhost:8080/envived/client/v2/resources/area/?clientrequest=true&virtual=true&format=json/";
     	 String user_id = session("userid");
  		 
  		 ObjectNode jsonParams = Json.newObject();
  		  
  		 jsonParams.put("parent","/envived/client/v2/resources/environment/" + id + "/");
  		 jsonParams.put("admin","/envived/client/v2/resources/user/" +user_id+ "/");
  		 jsonParams.put("name",areaData.get("name")); 
  		 jsonParams.put("tags",areaData.get("tags"));
  		 jsonParams.put("areaType", "interest"); // TODO : interest // non-interest
  			 
  		 WSRequestHolder holder=WS.url(idReturn);
  		 holder.setContentType("application/json");
  		 holder.setHeader("Cookie","sessionid="+session("sessionid"));
     	
   		 WSResponse postResult = holder.post(jsonParams).get(20000);
		
  		 System.out.println("*~* area id: ");
  		 System.out.println("Environment id : "+id);
  		 System.out.println("status result:" + postResult.getStatus());
  		 System.out.println("userid:" + session("userid") + "\nsessionid:" + session("sessionid") + " \nJson result: " + jsonParams);
 		
  		
  		return redirect(routes.Application.viewAreas(id));
    }
    
    //view one area
    public static Result viewArea(String id, String idenv){
    	//String id = id area ; 
   	 String getUrl = "http://localhost:8080/envived/client/v2/resources/area/"+id+"/?virtual=true&clientrequest=true&format=json";
   	 WSRequestHolder holder=WS.url(getUrl);
   	 
   	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
   	 holder.setContentType("application/json");
   	 
   	 WSResponse ListResponse = holder.get().get(200000);
   	 System.out.println("view area response status: "+ListResponse.getStatus());
   	 
   	 JsonNode content = ListResponse.asJson();
   	 List<String> nume = content.findValuesAsText("name");
   	 String tags = "";
   	 
   	 Iterator<JsonNode> it ;
   	 
   	 it=content.findPath("tags").iterator();
		 while(it.hasNext()){
			 JsonNode content_keyword=it.next();
			 tags+=content_keyword.asText()+" ";
		 }
	 System.out.println("area id : "+id);
	 
		 
	 return ok(viewarea.render(nume.get(0),tags,id,idenv));
    	  	
    }
    
    //view all areas
    public static Result viewAreas(String id){
    	//String id = id env
    	String url = "http://localhost:8080/envived/client/v2/resources/area/?virtual=true&clientrequest=true&format=json";
    	String parameters = "&parent="+id;
    	String idReturn = url+parameters;
         //this is the parent
    	String envid = "http://localhost:8080/envived/client/v2/resources/environment/"+id+"/?clientrequest=true&virtual=true&format=json";
    	
    	 WSRequestHolder holder = WS.url(idReturn);
       	 
       	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
       	 holder.setContentType("application/json");
       	
       	 WSResponse areaListResponse = holder.get().get(200000);
       	 
       	 System.out.println("Environment (parent) id : "+id);
       	 System.out.println("Status response: "+areaListResponse.getStatus());
       	 
       	 JsonNode content = areaListResponse.asJson();
    	 List<String> listaIduri = content.findValuesAsText("id");
      	 List<String> listaNume = content.findValuesAsText("name");
    		  
      	 HashMap<String, String>hash=new HashMap<String, String>();
	   	    
	   	 for(int i=0;i<listaIduri.size();i++){
	   		hash.put(listaIduri.get(i),listaNume.get(i));
	   	 	}
	   	 
	   	System.out.println("Areas:\n "+listaNume);
	   	System.out.println("^_^");   	
       	 
    	return ok(ViewAreas.render(hash,id));
       	 
      		
    }
    
    public static Result viewAreasEnv(String id){
    	//String id = id env
    	String url = "http://localhost:8080/envived/client/v2/resources/area/?virtual=true&clientrequest=true&format=json";
    	String parameters = "&parent="+id;
    	String idReturn = url+parameters;
         //this is the parent
    	String envid = "http://localhost:8080/envived/client/v2/resources/environment/"+id+"/?clientrequest=true&virtual=true&format=json";
    	
    	 WSRequestHolder holder = WS.url(idReturn);
       	 
       	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
       	 holder.setContentType("application/json");
       	
       	 WSResponse areaListResponse = holder.get().get(200000);
       	 
       	 System.out.println("Environment (parent) id : "+id);
       	 System.out.println("Status response: "+areaListResponse.getStatus());
       	 
       	 JsonNode content = areaListResponse.asJson();
    	 List<String> listaIduri = content.findValuesAsText("id");
      	 List<String> listaNume = content.findValuesAsText("name");
    		  
      	 HashMap<String, String>hash=new HashMap<String, String>();
	   	    
	   	 for(int i=0;i<listaIduri.size();i++){
	   		hash.put(listaIduri.get(i),listaNume.get(i));
	   	 	}
	   	 
	   	System.out.println("Areas:\n "+listaNume);
	   	System.out.println("^_^");   	
       	 
    	return ok(ViewAreasEnv.render(hash,id));
    
    }
    
    /**
     * Edit Area
     * */
    
    //edit area
    public static Result editArea(String id, String idenv){
    	
   	 String getUrl = "http://localhost:8080/envived/client/v2/resources/area/"+id+"/?virtual=true&clientrequest=true&format=json";
   	 WSRequestHolder holder=WS.url(getUrl);
   	 
   	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
   	 holder.setContentType("application/json");
   	 
   	 WSResponse ListResponse = holder.get().get(200000);
   	 System.out.println("Edit Area response status: "+ListResponse.getStatus());
   	 
   	 JsonNode content = ListResponse.asJson();
   	 List<String> nume = content.findValuesAsText("name");
   	 String tags = "";
   	 
   	 Iterator<JsonNode> it ;
   	 
   	 it=content.findPath("tags").iterator();
		 while(it.hasNext()){
			 JsonNode content_keyword=it.next();
			 tags+=content_keyword.asText()+" ";
		 }
	
		 
		 session("editid",id);
	 return ok(editArea.render(nume.get(0),tags,id, idenv));
    	
    }
    
    
    //save edited area or delete it
    public static Result saveArea(String id,String idenv){
    	DynamicForm mySaveForm;
    	mySaveForm = Form.form().bindFromRequest();
    	
    	String date = mySaveForm.get("name")+mySaveForm.get("tags");
    	System.out.println("(save/delete edited area) Datele sunt: "+date);
    	
    	switch (mySaveForm.get("update")){
    	case "save":
    		String saveUrl = "http://localhost:8080/envived/client/v2/resources/area/"+ session("editid")+"/?clientrequest=true&virtual=true&format=json/";
    		ObjectNode jsonParams = Json.newObject();
    		
    		 jsonParams.put("name",mySaveForm.get("name")); 
      		 jsonParams.put("tags",mySaveForm.get("tags"));
      		 jsonParams.put("areaType", "interest");
    		
    		System.out.println(":) Save edited area: ");
    		System.out.println("userid:" + session("userid") + " sessionid:" + session("sessionid") + "\njson result: " + jsonParams);
    		
    		WSRequestHolder holder=WS.url(saveUrl);
    		holder.setContentType("application/json");
    	    holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	    WSResponse rezultatPut = holder.put(jsonParams).get(20000);
    	    System.out.println("status result : "+ rezultatPut.getStatus());
    		break;
    		
    	case "delete":
    		 String deleteUrl = "http://localhost:8080/envived/client/v2/resources/area/"+ session("editid")+"/?clientrequest=true&virtual=true&format=json";
    		 WSRequestHolder delHolder=WS.url(deleteUrl);
    		 delHolder.setContentType("application/json");
    		 delHolder.setHeader("Cookie","sessionid="+session("sessionid"));
    		 WSResponse rezultatDelete = delHolder.delete().get(20000);
    		 System.out.println("*delete (aria) result: "+rezultatDelete.getBody()+"status: "+rezultatDelete.getStatus());
    		 
//    		break;
    		 return redirect(routes.Application.viewEnv(idenv));
    		 
    		default: break;	
    	
    	}
    	return redirect(routes.Application.viewArea(id,idenv));
    }
        /**
         * Area Description
         * */
   
    //if i have description return edit page if not return add description page
    public static Result areaDescription(String id, String idenv){
    	   	
   	 String descrUrl ="http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json&area="+id;
 	
   	 WSRequestHolder holder=WS.url(descrUrl);
   	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
   	 holder.setContentType("application/json");
   	 
   	 WSResponse response = holder.get().get(20000);
   	 System.out.println("i'm in area description");
   	 System.out.println("area description status response: "+response.getStatus());
   	
   	 JsonNode content = response.asJson();
   	 List<String> description = content.findValuesAsText("description");

   try{
	   
   	 if(description.get(0) != null){ 		 
   		 System.out.println("you have a description ...");
   	    return ok(editAreaDesc.render(description.get(0),id,idenv));
   	 	}
   	}catch(InputMismatchException ex){
   		 System.out.println("This is your problem: " +  ex.getMessage()   
   		          + "\nHere is where it happened:\n");
   		       ex.printStackTrace();
		   }catch (IndexOutOfBoundsException ex )
	    {  
	       System.out.println("This is your problem: " +  ex.getMessage()   
	          + "\nHere is where it happened:\n");
	       ex.printStackTrace(); 
	     } 
    	
    	return ok(AreaDescription.render(id,idenv));
    	
    }

    //Edit Description Area : save or delete the area description
    public static Result saveDeleteDescArea(String id, String idenv){
    	DynamicForm mySaveForm;
    	mySaveForm = Form.form().bindFromRequest();
    	String saveUrl = "http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json";
    	String deleteUrl = "http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json&area="+id;
     	
    	
    	switch (mySaveForm.get("update")){
    	case "save":
    		
    		System.out.println(":) i'm in Save edited description area ");
    		//System.out.println("userid:" + session("userid") + " sessionid:" + session("sessionid") + "\njson result: " + jsonParams);
    		
    		//delete old description
    		 WSRequestHolder delHolder=WS.url(deleteUrl);
    		 delHolder.setContentType("application/json");
    		 delHolder.setHeader("Cookie","sessionid="+session("sessionid"));
    		 WSResponse rezultatDelete = delHolder.delete().get(20000);
    		 System.out.println("*delete (old description area, to add the new one) result: "+rezultatDelete.getBody()+"status: "+rezultatDelete.getStatus());
    		 
    		//add the new one
    		 ObjectNode jsonParams = Json.newObject();
    	     jsonParams.put("category", "description");
    	     jsonParams.put("description", mySaveForm.get("textarea"));
    	     jsonParams.put("area", "/envived/client/v2/resources/area/"+id+"/");
    	    	
    	    WSRequestHolder holder=WS.url(saveUrl);
    	  	holder.setContentType("application/json");
    	  	holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	     	
    	  	WSResponse postResult = holder.post(jsonParams).get(20000);
    	  		    
    	    System.out.println("status result save new description: "+postResult.getStatus());
    		break;
    		
    	case "delete":
    		
    		 WSRequestHolder delHolder2=WS.url(deleteUrl);
    		 delHolder2.setContentType("application/json");
    		 delHolder2.setHeader("Cookie","sessionid="+session("sessionid"));
    		 WSResponse rezultatDelete2 = delHolder2.delete().get(20000);
    		 System.out.println("*delete (description area) result: "+rezultatDelete2.getBody()+"status: "+rezultatDelete2.getStatus());
    		 
    		break;
    		default: break;	
    	
    	}
    	return redirect(routes.Application.viewArea(id,idenv));
    
    }
    
    //Post the new area descrption
    public static Result PostAreaDescription(String id, String idenv){
    	
    	DynamicForm areaData = Form.form().bindFromRequest();
    	
    	String url ="http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json"; 	
    	
    	ObjectNode jsonParams = Json.newObject();
    	jsonParams.put("category", "description");
    	jsonParams.put("description", areaData.get("textarea"));
    	jsonParams.put("area", "/envived/client/v2/resources/area/"+id+"/");
    	
    	 WSRequestHolder holder=WS.url(url);
  		 holder.setContentType("application/json");
  		 holder.setHeader("Cookie","sessionid="+session("sessionid"));
     	
  		 WSResponse postResult = holder.post(jsonParams).get(20000);
  		 
  		 System.out.println("add area description status result:" + postResult.getStatus());
  		 System.out.println("userid:" + session("userid") + "\nsessionid:" + session("sessionid") + " \nJson result: " + jsonParams);
 		
  		
  		return redirect(routes.Application.viewArea(id,idenv));
    }
    
    //view area description if it has one, if not redirect to add one :)
    public static Result viewDescription(String id,String idenv){
    	
    	 String descrUrl ="http://localhost:8080/envived/client/v2/resources/features/description/?virtual=false&clientrequest=true&format=json&area="+id;
    	
    	 WSRequestHolder holder=WS.url(descrUrl);
    	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	 holder.setContentType("application/json");
    	 
    	 WSResponse response = holder.get().get(20000);
    	 System.out.println("i'm in view area description");
    	 System.out.println("view description area response status:"+response.getStatus());
    	
    	 JsonNode content = response.asJson();
    	 List<String> description = content.findValuesAsText("description");

    try{
    	 if(description.get(0) != null){ 		 
    		 System.out.println("you have a description ...: "+description.get(0));
    	    return ok(viewAreaDesc.render(description.get(0),id,idenv));
    	 	}
    	}catch(InputMismatchException ex){
    		 System.out.println("This is your problem: " +  ex.getMessage()   
    		          + "\nHere is where it happened:\n");
    		       ex.printStackTrace();
		   }catch (IndexOutOfBoundsException ex )
	    {  
	       System.out.println("This is your problem: " +  ex.getMessage()   
	          + "\nHere is where it happened:\n");
	       ex.printStackTrace(); 
	     } 
    	
    
    return redirect(routes.Application.areaDescription(id,idenv));
    
    }
    
   
    
    /**
     * Area Booth Description
     * */
    
    public static Result areaBothDescription(String id, String idenv){
    	
    	 String descrUrl ="http://localhost:8080/envived/client/v2/resources/features/booth_description/?virtual=false&clientrequest=true&format=json&area="+id;
    	 	
       	 WSRequestHolder holder=WS.url(descrUrl);
       	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
       	 holder.setContentType("application/json");
       	 
       	 WSResponse response = holder.get().get(20000);
       	 System.out.println("i'm in area both description");
       	 System.out.println("area both descripion response status:"+response.getStatus());
       	
       	 JsonNode content = response.asJson();
       	 List<String> description = content.findValuesAsText("TODO");

       try{
    	   
       	 if(description.get(0) != null){ 		 
       		 System.out.println("you have a description ...");
       	   // return ok(editAreaBoothDesc.render(description.get(0),id,idenv));
       		 return TODO;
       	 	}
       	}catch(InputMismatchException ex){
//       		 System.out.println("This is your problem (you don't have booth descriptiions so you have to add it): " +  ex.getMessage()   
//       		          + "\nHere is where it happened:\n");
       		System.out.println("I am in ;InputMismatchException; \n(you don't have booth descriptiions so you have to add it)");
       		       ex.printStackTrace();
    		   }catch (IndexOutOfBoundsException ex )
    	    {  
//    	       System.out.println("This is your problem: " +  ex.getMessage()   
//    	          + "\nHere is where it happened:\n");
    		  System.out.println("I am in ;IndexOutOfBoundsException; \n(you don't have booth descriptiions so you have to add it)");
    	       ex.printStackTrace(); 
    	     } 
        	
        	return ok(areaBoothDesc.render(id,idenv));
        	
    }
    //post the new booth description
    public static Result PostBoothAreaDesc(String id, String idenv){
    	
    	DynamicForm areaData = Form.form().bindFromRequest();
    	
    	String url ="http://localhost:8080/envived/client/v2/resources/features/booth_description/?virtual=false&clientrequest=true&format=json"; 	
    	
    	ObjectNode jsonParams = Json.newObject();
    	jsonParams.put("category", "booth_description");  	
    	jsonParams.put("image_url", areaData.get("image_url"));
    	jsonParams.put("contact_email", areaData.get("contact_email"));
    	jsonParams.put("contact_website", areaData.get("contact_website"));
    	jsonParams.put("description", areaData.get("description"));
    	


    	List<BoothDescription> product = new ArrayList<BoothDescription>();
    	JSONArray array = new JSONArray();
    	for (int i = 0; i < 10; ++i) {
    	    product.add(new BoothDescription(areaData.get("product_id"), 
    	    								 areaData.get("product_name"), 
    	    								 areaData.get("product_description")
    	    								 ));
    	    array.put(product.get(i));
    	}
    	JSONObject obj = new JSONObject();
    	try {
    	    obj.put("products", array);
    	} catch (JSONException e) {
    	 // TODO Auto-generated catch block
    	e.printStackTrace();
    	}
    	System.out.println("Area booth description \n ; json product: "+array);
    	
    	jsonParams.put("area", "/envived/client/v2/resources/area/"+id+"/");
    	
    	 WSRequestHolder holder=WS.url(url);
  		 holder.setContentType("application/json");
  		 holder.setHeader("Cookie","sessionid="+session("sessionid"));
     	
  		 WSResponse postResult = holder.post(jsonParams).get(20000);
  		 
  		 System.out.println("Add area booth description status result:" + postResult.getStatus());
  		 System.out.println("userid:" + session("userid") + "\nsessionid:" + session("sessionid") + " \nJson result: " + jsonParams);
 		
  		
  		return redirect(routes.Application.viewArea(id,idenv));
   
    }
       
    /**
     * Area conference role
     * */
    public static Result areaConferenceRole(String id, String idenv){
    	
    	String descrUrl ="http://localhost:8080/envived/client/v2/resources/features/conference_role/?virtual=false&clientrequest=true&format=json&area="+id;
	 	
      	 WSRequestHolder holder=WS.url(descrUrl);
      	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
      	 holder.setContentType("application/json");
      	 
      	 WSResponse response = holder.get().get(20000);
      	 System.out.println("i'm in area conference role");
      	 System.out.println("area conference role response status:"+response.getStatus());
       	
       	return ok(areaConferenceRole.render(id,idenv));
       	
  
    }
    
    public static Result areaPostConferenceRole(String id, String idenv){
    
	DynamicForm areaData = Form.form().bindFromRequest();
    	
    	String url ="http://localhost:8080/envived/client/v2/resources/features/conference_role/?virtual=false&clientrequest=true&format=json"; 	
    	
    	ObjectNode jsonParams = Json.newObject();
    	jsonParams.put("category", "conference_role");
    	jsonParams.put("first_name", areaData.get("first_name"));
    	jsonParams.put("last_name", areaData.get("last_name"));
    	jsonParams.put("role", areaData.get("role"));
    	jsonParams.put("area", "/envived/client/v2/resources/area/"+id+"/");
    	
    	 WSRequestHolder holder=WS.url(url);
  		 holder.setContentType("application/json");
  		 holder.setHeader("Cookie","sessionid="+session("sessionid"));
     	
  		 WSResponse postResult = holder.post(jsonParams).get(20000);
  		 
  		 System.out.println("add area conference-role status result:" + postResult.getStatus());
  		 System.out.println("userid:" + session("userid") + "\nsessionid:" + session("sessionid") + " \nJson result: " + jsonParams);
 		
  		
  		return redirect(routes.Application.viewArea(id,idenv));
   
    }
    
    
    
   
}
