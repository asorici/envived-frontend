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
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import models.Environment;
import models.UserProfile;

import com.fasterxml.jackson.core.util.BufferRecycler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.net.ssl.HttpsURLConnection;

import play.*;
import play.data.DynamicForm;
import play.data.Form;
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
    
    
    /**
     * @FIX THIS:
     * 
	  views.py:270: DeprecationWarning: The use of AUTH_PROFILE_MODULE to define user profiles has been deprecated.
	  user_res_uri = UserResource().get_resource_uri(user.get_profile())

     * */
    
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
    	
  	 // ObjectNode jsonParams = Json.newObject();

  	System.out.println("rez is: "+ response.getStatus()+response.getBody());

	 boolean successYes;
	 JSONObject out;
	 JSONObject allData;
	 String success = "success";
	 String resources	= "resource_uri";
	 String inputs = response.getBody(); //message received
	
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
    		  
    		  System.out.println("User Name : " + session("userid"));
    		  
    	  }
    	  
    	  session(response.getCookie("sessionid").getName(), response.getCookie("sessionid").getValue());	  
	  
	 } catch (JSONException e) {
			
			e.printStackTrace();
			System.out.println("Unul dintre campuri nu este gasit....");
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
    	
    	return ok (Login.render("Login"));
    }
    
    public static Result PostLogin(){
    	
    	  String idReturn = "http://localhost:8080/envived/client/v2/actions/login/";
    	  
    	  DynamicForm myLoginForm = Form.form().bindFromRequest();
    	  String loginParam = "email="+myLoginForm.get("email")+"&password="+myLoginForm.get("password");
    	  
          String email = myLoginForm.get("email");
          String password = myLoginForm.get("password");
          
          System.out.println("The user is: "+email+" "+"password: "+password);
          
          WSRequestHolder holder = WS.url(idReturn);
          holder.setContentType("application/x-www-form-urlencoded");
          WSResponse response = holder.post(loginParam).get(20000);
          
          System.out.println("rez este: "+ response.getStatus()+response.getBody());
          
          if(response.getStatus()<400){
        	  
        	  JsonNode responseJson = response.asJson();
        	  List<String> userid = responseJson.findValuesAsText("resource_uri");
        	  
        	  String id = userid.get(0).split("/")[6];
        	  session("userid",id);
        	  
        	  System.out.println("The id is: "+id+" :)");
        	  System.out.println(response.getCookie("sessionid").getName()+response.getCookie("sessionid").getValue());
        	  
        	  session(response.getCookie("sessionid").getName(), response.getCookie("sessionid").getValue());
        	
        	  //return ok(Profile.render());
        	  return redirect(routes.Application.welcome());
        	  
          }
        	  return ok(Login.render("wrong credentials"));    	  
    }
    
    /**
     * Profile
     * */
       
    public static Result EditProfile(){
    	
    	return TODO;
    }
    
    /**
     * Add Environment
     * */
    
    public static Result AddEnvironment(){
    	
    	String val = session("userid");
    	System.out.println("Add Environment : "+ val);
    	
    	return ok(NewEnvironment.render());
    }
    
    public static Result PostNewEnvironment(){
    	
   	
    	  Form<Environment> formData = Form.form(Environment.class);
       // DynamicForm envData = Form.form().bindFromRequest();
    	  Form <Environment> form = formData.bindFromRequest();
          Environment envData = form.get();
               
        String idReturn ="http://localhost:8080/envived/client/v2/resources/environment/?clientrequest=true&virtual=true&format=json/";
       
      /** String envParam = "name"+"="+envData.get("name")
     		   +"&"+"owner"+"="+ "/envived/client/v2/resources/user/"+session("userid")+"/"
     		   +"&"+"tags"+"="+envData.get("tags");*/
       
        ObjectNode jsonParams = Json.newObject();
        jsonParams.put("name", envData.getName() );
        jsonParams.put("owner", "/envived/client/v2/resources/user/"+session("userid")+"/");
        jsonParams.put("tags", envData.getTags());
        
        String name = envData.getName();
        System.out.println("New environment : " + name);
    
        WSRequestHolder holder=WS.url(idReturn);
        holder.setContentType("application/json");
        holder.setHeader("Cookie","sessionid="+session("sessionid"));
        
        //JSONObject json = JSONSerializer.toJSON(myJsonString);
        
        //must make it a json object ! -> stupid !
        WSResponse postResult = holder.post(jsonParams).get(20000);
        
       // WSResponse postResult = holder.post(jsonParams).get(20000);
        
        System.out.println("rez este:" + postResult.getStatus());    
        System.out.println("Bello ! ^_^ i am in post new env, and the session id is : "+session("sessionid"));
        
        
        return redirect(routes.Application.welcome());
 		   
    }
    
    public static Result viewEnvironments(String id){
    	/**
    	String url = "http://localhost:8080/envived/client/v2/resources/environment/?clientrequest=true&virtual=true&format=json/";
    		
    	String param = "owner"+"="+session("userid")+"/";
    	WSResponse response;
    	
    	HashMap <Integer, String> hash = new HashMap<Integer, String>();
    	
    	URL obj;
    	int responseCode;
    	StringBuffer buffResp = new StringBuffer();
    	HttpURLConnection con;
    	OutputStreamWriter writer;
    	
	    	try{
	    		obj = new URL(url);
	    		con = (HttpURLConnection) obj.openConnection();
	    		
	    		con.setRequestMethod("GET");
	    		con.setRequestProperty ("Cookie","sessionid=" + param );
				
	    		System.out.println("session: " + param);
	    		
	    		responseCode = con.getResponseCode();
	    		System.out.println("Response code : "+responseCode);
	    		
	    		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	    		String inputLine;
	    		
	    		buffResp = new StringBuffer();
	    		while ((inputLine = in.readLine()) != null) {
	    			buffResp.append(inputLine);
				}
				in.close();
	    	  	
	    } catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
	    
	      String url2 = url+param;
	      
	  	  WSRequestHolder holder2 = WS.url(url2);
	  	  holder2.setContentType("application/x-www-form-urlencoded");
	  	  
	  	  holder2.setHeader("Cookie","sessionid="+session("sessionid"));
	  	  response = holder2.get().get(20000);
	    	
    	
    	HashMap<Integer,String> id_name_env = HttpRequests.
    			obtainHashMapFromResponseGet(url + parameters, session(ConstantsPreferences.sessionId));
    			
    			response = HttpRequests.executeActionWithGET(url, parameters);
    			
    			
    			id_name_env = ResponseProcessor.obtainDataAboutEnvironments(response);

    					
    			re
    	*/
	 /** 	 
    	 String getEnvUrl = "http://192.168.8.110:8080/envived/client/v2/resources/environment/"+id+"/?virtual=true";
    	 WSRequestHolder holder=WS.url(getEnvUrl);
    	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	 holder.setContentType("application/json");
    	 
    	 WSResponse envListResponse = holder.get().get(200000);
    	 System.out.println(envListResponse.getStatus());
    	 
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
		 return ok(ViewEnvironment.render(nume.get(0),tags));*/
    	return TODO;
    }
    
    public static Result editEnv(String id){
    	
    /**
    	String getEnvUrl = "http://192.168.8.110:8080/envived/client/v2/resources/environment/?virtual=true";
    	WSRequestHolder holder=WS.url(getEnvUrl);
    	holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	
    	holder.setContentType("application/json");
    	WSResponse envListResponse = holder.get().get(200000);
    	
    	System.out.println(envListResponse.getStatus());
    	JsonNode content = envListResponse.asJson();
    	
    	List<String> listId = content.findValuesAsText("id");
    	List<String> listName = content.findValuesAsText("name");
    	  
    	 HashMap<String, String> hash =new HashMap<String, String>();
    	
    	 for(int i=0;i<listId.size();i++){
    		 hash.put(listId.get(i),listName.get(i));
         }
    	 
    	  System.out.println("lista: "+listName);
    	  
    	return ok(ViewEnvironment.render(hash));*/
    	
    	//TODO!!!!
    	/**
    	 String getEnvUrl = "http://192.168.8.110:8080/envived/client/v2/resources/environment/"+id+"/?virtual=true";
    	 WSRequestHolder holder=WS.url(getEnvUrl);
    	 
    	 holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	 holder.setContentType("application/json");
    	 
    	 WSResponse envListResponse = holder.get().get(200000);
    	 System.out.println(envListResponse.getStatus());
    	 
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
		 return ok(EditEnvironment.render(nume.get(0),tags));
    	 */
    	 
    	 return TODO;
    }
    
    public static Result saveEditEnv(){
    	/**
    	DynamicForm mySaveForm;
    	mySaveForm = Form.form().bindFromRequest();
    	
    	String date = mySaveForm.get("name")+mySaveForm.get("tags");
    	System.out.println("Datele sunt: "+date);
    	
    	switch (mySaveForm.get("update")){
    	case "save":
    		String saveUrl = "http://192.168.8.110:8080/envived/client/v2/resources/environment/"+ session("editid")+"/?clientrequest=true&virtual=true&format=json";
    		ObjectNode jsonParams = Json.newObject();
    		
    		jsonParams.put("name", mySaveForm.get("name"));
    		jsonParams.put("owner", "/envived/client/v2/resources/user/"+session("userid")+"/");
    		jsonParams.put("tags", mySaveForm.get("tags"));
    		System.out.println("userid:" + session("userid") + " sessionid:" + session("sessionid") + "rezultat: " + jsonParams);
    		
    		WSRequestHolder holder=WS.url(saveUrl);
    		holder.setContentType("application/json");
    	    holder.setHeader("Cookie","sessionid="+session("sessionid"));
    	    WSResponse rezultatPut = holder.put(jsonParams).get(20000);
    		break;
    		
    	case "delete":
    		String deleteUrl = "http://192.168.8.110:8080/envived/client/v2/resources/environment/"+ session("editid")+"/?clientrequest=true&virtual=true&format=json";
    		 WSRequestHolder delHolder=WS.url(deleteUrl);
    		 delHolder.setContentType("application/json");
    		 delHolder.setHeader("Cookie","sessionid="+session("sessionid"));
    		 WSResponse rezultatDelete = delHolder.delete().get(20000);
    		 System.out.println("rezultata delete: "+rezultatDelete.getBody()+"statusul: "+rezultatDelete.getStatus());
    		 
    		break;
    		default: break;
    	}*/
    	
    	
    	return TODO;
    }
    
    public static Result Profile(){
    	return TODO;
    }
       
    
    /**
     * Add Area
     * */
    

    
    public static Result PostNewArea(){
    	
    	
    	return TODO;
    }
    
    /**
     * Edit Area
     * */
}
