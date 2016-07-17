package twitchaid;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Steven
 */
public class MainPageController implements Initializable {

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
        
    }
    
    /**
     * Sets the current access token.
     * @param t 
     */
    public void setToken(String t){
        token = t;
    }
    
}
