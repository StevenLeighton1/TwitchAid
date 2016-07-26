/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitchaid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import org.json.JSONObject;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class TwitchAid_LandController implements Initializable {

    
    private final String USER_AGENT = "Mozilla/5.0";
    private final String ACCEPT = "application/vnd.twitchtv.v3+json";
    private final String CLIENT_ID = "7rqjd7m4p60rwrbgy6j1kibaxmzmmen";
    
    public String token;
    public String username;
    
    @FXML
    private TextField title_input;
    @FXML
    private TextField playing_input;
    @FXML
    private Button update_button;
    @FXML
    private WebView web_view_stream;
    @FXML
    private WebView web_view_chat;
    @FXML
    private HBox land_root;
    @FXML
    private Label viewer_count_label;
    @FXML
    private Label total_view_count_label;
    @FXML
    private Label follower_count_label;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    public void start(){
	System.out.println("Opening landing page...");
        try {
            getUsername();
            getTitles();
            openStream();
            openStreamChat();
        } catch (Exception ex) {
            Logger.getLogger(TwitchAid_LandController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Sets the current access token.
     * @param t 
     */
    public void setToken(String t){
        token = t;
    }
    
    /**
     * Sets the username variable for later API calls.
     * @throws Exception 
     */
    public void getUsername() throws Exception {

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
//		System.out.println("\nSending request");
//		System.out.println("Response Code : " + responseCode);

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
                
                //get username saved
                username = objJson.getString("name");
    }
    
    private void getTitles() throws Exception {
                String url = "https://api.twitch.tv/kraken/channels/" + username;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request headers
		con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Client-ID", CLIENT_ID);
		con.setRequestProperty("Accept", ACCEPT);

		int responseCode = con.getResponseCode();
//		System.out.println("\nSending request");
//		System.out.println("Response Code : " + responseCode);

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
                
                //get title/game/viewer/follower updated
                title_input.setText(objJson.getString("status"));
                playing_input.setText(objJson.getString("game"));
                total_view_count_label.setText(Integer.toString(objJson.getInt("views")));
                follower_count_label.setText(Integer.toString(objJson.getInt("followers")));
                viewer_count_label.setText(Integer.toString(getViewers()));
                
    }

    private void openStream() {
        WebEngine webEngine = web_view_stream.getEngine();
        webEngine.load("https://player.twitch.tv/?channel=" + username);
    }

    private void openStreamChat() {
        WebEngine webEngine = web_view_chat.getEngine();
        webEngine.load("https://www.twitch.tv/" + username + "/chat");
    }
    
    private int getViewers() throws Exception{
                String url = "https://api.twitch.tv/kraken/streams/" + username;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		//add request headers
		con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Client-ID", CLIENT_ID);
		con.setRequestProperty("Accept", ACCEPT);

		int responseCode = con.getResponseCode();
//		System.out.println("\nSending request");
//		System.out.println("Response Code : " + responseCode);

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
		System.out.println("Viewers: " + objJson.get("stream"));
                
                if(objJson.isNull("stream")){
                    return 0;
                }
                else{
                    return objJson.getJSONObject("stream").getInt("viewers");
                }
    }
    
    @FXML
    private void update_title_playing(ActionEvent event) throws Exception {
                String url = "https://api.twitch.tv/kraken/channels/" + username;
		
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("PUT");
                con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

		//add request headers
		con.setRequestProperty("User-Agent", USER_AGENT);
                con.setRequestProperty("Client-ID", CLIENT_ID);
		con.setRequestProperty("Accept", ACCEPT);
                con.setRequestProperty("Authorization", "OAuth " + token);
                con.setDoOutput(true);

                String data = "channel[status]="+ title_input.getText() +"&channel[game]="+ playing_input.getText();
		OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
                out.write(data);
                out.flush();
                for (Entry<String, List<String>> header : con.getHeaderFields().entrySet()) {
                    System.out.println(header.getKey() + "=" + header.getValue());
                }
		
    }
}
