/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package twitchaid;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 *
 * @author Steven
 */
public class TwitchAid extends Application {
    
    public String token; //access token
    final private String clientID = "7rqjd7m4p60rwrbgy6j1kibaxmzmmen";
    
    @Override
    public void start(Stage stage) throws Exception {
        
        VBox root = new VBox();
        Scene scene = new Scene(root); //use root for above parent
        stage.setTitle("TwitchAid");   //set title
        
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setContent(browser);
        
        /** authentication url: https://api.twitch.tv/kraken/oauth2/authorize
                                    ?response_type=token
                                    &client_id=[your client ID]
                                    &redirect_uri=[your registered redirect URI]
                                    &scope=[space separated list of scopes] 
        */
    
        webEngine.load("https://api.twitch.tv/kraken/oauth2/authorize"
                + "?response_type=token"
                + "&client_id=" + clientID
                + "&redirect_uri=http://127.0.0.1"
                + "&scope=user_read channel_editor");
        
        
        webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<Worker.State>() {
            @Override
            public void changed(ObservableValue<? extends Worker.State> observable, Worker.State oldValue, Worker.State newValue) {
                if (Worker.State.SUCCEEDED.equals(newValue)) {
                    String url = webEngine.getLocation(); //get current URL
                    System.out.println(url);
                    
                    //check for access_token in URL
                    if(url.contains("access_token")){
                        
                        //grab value of token which is found between the 
                        //first '=' and the first '&' of the string
                        token = url.substring(url.indexOf("=")+1, url.indexOf("&"));
                        if(token != null){
                            try {
                                //remove webview browser
                                root.getChildren().remove(browser);
                                
                                //create new loader/root for main page
                                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
                                VBox logRoot = fxmlLoader.load();
                                
                                //grab controller for main page and set access token
                                MainPageController mainController = (MainPageController) fxmlLoader.getController();
                                mainController.setToken(token);
                                
                                //set new root
                                scene.setRoot(logRoot);
                                stage.setWidth(400);
                                stage.setHeight(600);
                                
                                //start main page
                                mainController.start();
                                
                            } catch (IOException ex) {
                                Logger.getLogger(TwitchAid.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                    System.out.println("token = " + token);
                }
            }
        });

        root.getChildren().add(browser);

        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch(TwitchAid.class, (java.lang.String[])null);
    }
    
}
