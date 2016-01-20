package application;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.util.*;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class LoginScreen {

	private ArrayList<String> userNames;
	private ArrayList<String> passwords;
	private String userName;
	private String password;
	private int index;
	private int trials;
	
	@FXML
	private TextField userTextField;
	@FXML
	private PasswordField passwordField;
	@FXML 
	private Button loginButton;
	@FXML 
	private Circle verificationCircle;
	@FXML
	private Label outputText;

	Stage prevStage;
	private SharedProperties sProperties;

	public LoginScreen() throws IOException, ParseException {
		userNames = new ArrayList<String>();
		passwords = new ArrayList<String>();

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("data/login/users.json"));
		JSONObject jsonObject = (JSONObject) obj;

		Set<String> arr = jsonObject.keySet();
		for(String s:arr) {
			userNames.add(s);
			passwords.add((String) jsonObject.get(s));
		}

		trials = 0;
		sProperties = SharedProperties.getInstance();
	}

	@FXML
	private void checkIfEmpty() {
		setUserName(userTextField.getText());
		if(this.userName.isEmpty()) {
			verificationCircle.setVisible(false);
		}
	}

	@FXML
	private void isUserNameValid()
	{
		setUserName(userTextField.getText());
		setPassword(passwordField.getText());
		if(!this.userName.isEmpty()) {
			for (String user : userNames) {
				if (userName.compareTo(user) == 0) {
					//setting the visibility of verification circle to true
					verificationCircle.setVisible(true);
					outputText.setText("");
					break;
				} else {
					verificationCircle.setVisible(false);
					outputText.setText("Wrong UserName");
				}
			}
		} else {
			verificationCircle.setVisible(false);
		}
	}

	@FXML
	private void onKeyPressed(KeyEvent event) {
		if(event.getCode() == KeyCode.ENTER) {
			onLoginButtonClicked();
			//System.out.println("enter pressed");
		}
	}

	@FXML
	private void onLoginButtonClicked()
	{
	//System.out.println(trials);
		setUserName(userTextField.getText());
		setPassword(passwordField.getText());
		if(trials < 3)
		{
			if(isValidUser(userName, password))
			{
				// navigate to main Screen
				//System.out.println("Logged In");
				outputText.setText("");
				sProperties.setUserName(userName);
				try {
					Parent mainScreen = FXMLLoader.load(getClass().getResource("mainScreen2.fxml"));
					Scene mainScreenScene = new Scene(mainScreen,700,400);
					Stage stage = (Stage) userTextField.getParent().getScene().getWindow();
					stage.setScene(mainScreenScene);

					stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
						@Override
						public void handle(WindowEvent t) {
							Platform.exit();
							System.exit(0);
						}
					});

					prevStage.close();

					stage.show();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				outputText.setText("Wrong Credentials");
			}
			trials++;
		}
		else
		{
			onLoginFailure();
			trials = 0;
		}
	}

	public void setPrevStage(Stage stage){
		this.prevStage = stage;
	}

	private boolean isValidUser(String userName, String password) {
		// returns true if the entered credentials are valid
		for(String user: userNames)
		{
			if(userName.compareTo(user) == 0)
			{
				int index = userNames.indexOf(user);
				if(password.compareTo(passwords.get(index)) == 0 )
				{
					setUserName(userTextField.getText());
					setIndex(userNames.indexOf(userName));
					setPassword(passwordField.getText());
					return true;
				}
				else
					return false;					
			}				
		}
		return false;
		// TODO Auto-generated method stub
		
	}

	private void onLoginFailure()
	{
		// timeout for 30 seconds
		outputText.setText("Wait for 10 seconds before trying again");
		userTextField.clear();
		passwordField.clear();
		disableView();

		final Timer t = new Timer();
		class RemindTask extends TimerTask {
            public void run() {
                //System.out.format("Time's up!%n");
                outputText.setText("You can try now");
                evableView();
                t.cancel(); //Terminate the timer thread
            }
        }
		t.schedule(new RemindTask(), 10000);


		//outputText.setText("You can try now");
		//evableView();
	}
	
	private void evableView() {
		// TODO Auto-generated method stub
		userTextField.setEditable(true);
		passwordField.setEditable(true);
		loginButton.setDisable(false);
		
	}

	private void disableView()
	{
		//disable the view till the 30 seconds are over
		userTextField.setEditable(false);
		passwordField.setEditable(false);
		loginButton.setDisable(true);
	}
	
	private void setUserName(String name)
	{
		this.userName = name;
	}
	
	private void setPassword(String password)
	{
		this.password = password;
	}
	
	private void setIndex(int index)
	{
		this.index = index;
	}
	
}
