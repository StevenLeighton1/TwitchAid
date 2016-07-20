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
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
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
    public String username;
    
    @FXML
    private Label welcomeLabel;
    @FXML
    private ListView<?> followList;
    @FXML
    private Button previewStreamButton;
    @FXML
    private Button openChatButton;
    @FXML
    private Button openEditInfoButton;

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
                con.setRequestProperty("Client-ID", CLIENT_ID);
		con.setRequestProperty("Accept", ACCEPT);
                con.setRequestProperty("Authorization", "OAuth " + token);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending request");
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
                
                //set welcome label to user's display name
                welcomeLabel.setText("Welcome " + objJson.getString("display_name") + "!");
                
                //get username saved
                username = objJson.getString("name");
	}

    @FXML
    private void openStream(ActionEvent event) {
        
        Stage stage = new Stage();
        VBox root = new VBox();
        Scene scene = new Scene(root); //use root for above parent
        stage.setTitle(username + " Preview");   //set title
        
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
    
        // option 1 (stream+chat): 
        webEngine.load("https://twitch.tv/" + username);
        
        // option 2 (just stream): webEngine.load("https://player.twitch.tv/?channel=" + username);
        root.getChildren().add(browser);

        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openChat(ActionEvent event) {
        
        Stage stage = new Stage();
        VBox root = new VBox();
        Scene scene = new Scene(root); //use root for above parent
        stage.setTitle(username + " Chat");   //set title
        
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
   
        webEngine.load("https://www.twitch.tv/" + username + "/chat");
        
        root.getChildren().add(browser);
        
        scene.setRoot(root);
        stage.setWidth(400);
        stage.setHeight(600);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void openEditInfo(ActionEvent event) {
        
        Stage stage = new Stage();
        VBox root = new VBox();
        Scene scene = new Scene(root); //use root for above parent
        stage.setTitle(username + " Info");   //set title
        
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
   
        webEngine.load("https://www.twitch.tv/" + username + "/dashboard");
        
        root.getChildren().add(browser);
        
        scene.setRoot(root);
        stage.setWidth(650);
        stage.setHeight(570);
        stage.setScene(scene);
        stage.show();
        
    }
    
}
