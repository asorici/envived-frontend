package controllers;

public class Url {
	
	 static String host = "http://localhost";
	 static String port = "8080";
	 
	 static String register = "/envived/client/v2/actions/register/";
	 static String login    = "/envived/client/v2/actions/login/";
	 
	 static String parent = "/envived/client/v2/resources/environment/";
	 static String area   = "/envived/client/v2/resources/area/";
	 static String owner  = "/envived/client/v2/resources/user/";
	
	 static String description     = "/envived/client/v2/resources/features/description/";
	 static String boo_description = "/envived/client/v2/resources/features/booth_description/";
	 static String program         = "/envived/client/v2/resources/features/program/";
	 static String order           = "/envived/client/v2/resources/features/order/";
	 static String socialmedia     = "/envived/client/v2/resources/features/social_media/";
	 static String conference_role = "/envived/client/v2/resources/features/conference_role/";
	 
	 static String temperature     = "/envived/client/v2/resources/things/temperature/";
	 static String humidity     = "/envived/client/v2/resources/things/humidity/";
	 static String luminosity     = "/envived/client/v2/resources/things/luminosity/";
	 
	 static String crTvTfj = "?clientrequest=true&virtual=true&format=json/";
	 static String vFcrTfj = "?virtual=false&clientrequest=true&format=json/";        
	 static String vTcrTfJ = "?virtual=true&clientrequest=true&format=json";
	 
	 static String crTvTfjO = "?clientrequest=true&virtual=true&format=json"+"&owner=";

	 static String www     = "application/x-www-form-urlencoded";
	 static String appjson = "application/json";

}
