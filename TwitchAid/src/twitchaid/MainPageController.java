package twitchaid;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

//imports for accessing API
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.*;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class MainPageController implements Initializable {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String ACCEPT = "application/vnd.twitchtv.v3+json";
    private final String CLIENT_ID = "7rqjd7m4p60rwrbgy6j1kibaxmzmmen";
    
    public String token;
    
    @FXML
    private Label welcomeLabel;
    @FXML
    private ListView<?> followList;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }  
    
    public void start(){
	System.out.println("Testing 1 - Send Http GET request");
        try {
            sendGet();
        } catch (Exception ex) {
            Logger.getLogger(MainPageController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sets the current access token.
     * @param t 
     */
    public void setToken(String t){
        token = t;
    }
    
    // HTTP GET request
    public void sendGet() throws Exception {

                //API link: https://github.com/justintv/Twitch-API/blob/master/v3_resources/users.md#get-user
                //returns data about user with just the auth token
		String url = "https://api.twitch.tv/kraken/user";
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request headers
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept", ACCEPT);
                con.setRequestProperty("Authorization", "OAuth " + token);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		
		String jsonString = response.toString();
		
		JSONObject objJson = new JSONObject(jsonString);

		//print result
		System.out.println(jsonString);
	}
    
}
