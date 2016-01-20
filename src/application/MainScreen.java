package application;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MainScreen {

	@FXML
	private Button logOutButton;

	@FXML
	public Phone phone;

	@FXML
	private Label journeyTimeLabel;
	@FXML
	private Label speedLabel;
	@FXML
	private ProgressBar fuelLabel;
	@FXML
	private Label dateLabel;
	@FXML
	private Tab phonetab;
	@FXML
	private Tab radiotab;

	private SharedProperties sProperties;
	private Timer timer;

	private String userName;
	private int volume;
	private int fuel;

	public MainScreen() throws IOException, ParseException {
		sProperties = SharedProperties.getInstance();

		TimerTask timerTask = new TimerTask()
		{
			@Override
			public void run()
			{
				// TODO Auto-generated method stub
				try {
					Platform.runLater(new Runnable(){
						@Override
						public void run(){
							try {
								getUpdatedValues();
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					});
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		timer = new Timer();
		timer.schedule(timerTask, 250, 250);
	}

	private void getUpdatedValues()
	{
		journeyTimeLabel.setText(sProperties.getJourneyTime());
		//dateLabel.setText(sProperties.getDateAndTime());
		//userName = sProperties.getUserName();
		speedLabel.setText(sProperties.getSpeed());
		//volume = sProperties.getVolume();
		fuelLabel.setProgress(1.0 - (sProperties.getDistanceTraveled()/sProperties.getTotalMiles()));

		//System.out.println(sProperties.getDistanceTraveled());

		//set color of tabs if in use
		if(sProperties.getInCall()) {
			phonetab.setStyle("-fx-background-color: lightgreen;");

		} else {
			phonetab.setStyle("-fx-background-color: white;");

		}

		if(sProperties.getIsRadioOn()) {
			radiotab.setStyle("-fx-background-color: lightgreen;");
		} else {
			radiotab.setStyle("-fx-background-color: white;");
		}

		//fuel = sProperties.getFuel();
		setDateAndTime();
	}

	private void setDateAndTime()
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		dateLabel.setText(dateFormat.format(date));
	}

	//TODO: put this function in the exitScreen controller instead

}
