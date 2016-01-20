package application;

import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Chaitanya on 11/22/2015.
 */
public class Radio extends AnchorPane{


    @FXML
    private RadioButton AMradioButton;
    @FXML
    private RadioButton FMradioButton;

    @FXML
    private Label radioFrequency;
    @FXML
    private Label stationName;
    @FXML
    private Button plus;
    @FXML
    private Button minus;
    @FXML
    private ListView<String> radioList;

    public boolean radioState;//true = FM, false = AM

    public Map<String, String> AMstations = new HashMap<String, String>();//maps station name to frequency
    public Map<String, String> FMstations = new HashMap<String, String>();//maps station name to frequency

    private SharedProperties sProperties;
    private Statistics stats;
    private Timer radioTimer;
    private int radioDuration;//in seconds
    private String timeOfUse;

    public Radio() {

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("RadioModule.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        sProperties = SharedProperties.getInstance();
        stats = Statistics.getInstance();
        radioState = false;
        radioFrequency.textProperty().set("00.0");
        stationName.textProperty().set("-");

        AMstations.put("Classics","1510");
        AMstations.put("KVNA AM", "600");
        AMstations.put("KAA AM", "1230");

        FMstations.put("KISS FM","102.7");
        FMstations.put("HOT 99.5","99.5");
        FMstations.put("NOW FM", "92.3");
        FMstations.put("HOT FM","97.5");

        //we always start in area 1, so we populate the listview with area 1 AM stations(it is on AM by default
        ObservableList<String> items = FXCollections.observableArrayList (
                );
        radioList.setItems(items);
    }

    @FXML
    private void onAMPress() {
        radioState = false;
        if(FMradioButton.isSelected()) {
            FMradioButton.setSelected(false);
        }
        if(sProperties.getLocation() == 1) {
            ObservableList<String> items = FXCollections.observableArrayList (
                    "Classics");
            radioList.setItems(items);
        } else {
            ObservableList<String> items = FXCollections.observableArrayList (
                    "KVNA AM","KAA AM");
            radioList.setItems(items);
        }
    }

    @FXML
    private void onFMPress() {
        radioState = true;
        if(AMradioButton.isSelected()) {
            AMradioButton.setSelected(false);
        }
        if(sProperties.getLocation()==1) {
            ObservableList<String> items = FXCollections.observableArrayList (
                    "KISS FM", "HOT 99.5");
            radioList.setItems(items);
        } else {
            ObservableList<String> items = FXCollections.observableArrayList (
                    "Now FM","Hot FM");
            radioList.setItems(items);
        }
    }

    @FXML
    private void onOffRadioPress() {
        sProperties.setIsRadioOn(false);
        radioTimer.cancel();

        //add radio use statistics
        stats.addNewRadioUse(stationName.getText(),radioDuration, timeOfUse);
        radioDuration = 0;
    }

    @FXML
    private void onRadioListClicked() {
        String selected = radioList.getFocusModel().getFocusedItem();
        stationName.textProperty().set(selected);
        sProperties.setIsRadioOn(true);

        if(radioState) {//if its FM
            radioFrequency.textProperty().set(FMstations.get(selected));
        } else {        //if its AM
            radioFrequency.textProperty().set(AMstations.get(selected));
        }

        //set the time of call
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        timeOfUse = dateFormat.format(date);

        radioTimer = new Timer();
        class CountTime extends TimerTask {
            public void run() {
                //keep track of how long the call has been going on
                radioDuration++;
            }
        }
        radioTimer.schedule(new CountTime(), 1000, 1000);
    }
}
