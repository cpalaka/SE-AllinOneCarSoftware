package application;

//import com.sun.org.apache.xml.internal.security.Init;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.AnchorPane;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Chaitanya on 11/22/2015.
 */
public class Phone extends AnchorPane implements Initializable{

    //main phone screen
    @FXML
    private TextArea numberBar;
    @FXML
    private GridPane numberPad;
    @FXML
    private Button pb1;
    @FXML
    private Button pb2;
    @FXML
    private Button pb3;
    @FXML
    private Button pb4;
    @FXML
    private Button pb5;
    @FXML
    private Button pb6;
    @FXML
    private Button pb7;
    @FXML
    private Button pb8;
    @FXML
    private Button pb9;
    @FXML
    private Button pb0;
    @FXML
    private Button pbback;
    @FXML
    private Button pbclear;
    @FXML
    private ComboBox<String> contactsbox;
    @FXML
    private Button addContactButton;
    @FXML
    private Button deleteContactButton;
    @FXML
    private Button callButton;
    @FXML
    private Button endButton;

    //add contact screen
    @FXML
    private AnchorPane addContactScreen;
    @FXML
    private TextField addContactNameField;
    @FXML
    private TextField addContactNumberField;
    @FXML
    private Button addContact;
    @FXML
    private Button backButton;
    @FXML
    private Label contactOutput;

    private int callDuration = 0;//in seconds
    private Timer callTimer;
    private boolean usedContactBox = false;//keeps track of whether the number called is from contacts or typed in

    private String timeOfCall;
    //this map is used to link the contact name to the number
    public Map<String, String> contactsmap = new HashMap<String, String>();

    //This list is used to initialize the combo box if there are saved contacts in the contacts.json
    public ObservableList<String> savedContactsList =
                FXCollections.observableArrayList(

                );

    private String currentText = "";

    private SharedProperties sProperties;
    private Statistics stats;
    private int connectedAfter = 5; // length of time in seconds after which call will automatically connect

    public Phone() {
        //contactsbox.getItems().setAll("Contact1","Contact2","Contact3");

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PhoneModule.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        sProperties = SharedProperties.getInstance();
        stats = Statistics.getInstance();
        getContactsFromJson();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        contactsbox.setItems(savedContactsList);
    }

    private void getContactsFromJson() {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("data/"+sProperties.getUserName()+"/contacts.json"));
            JSONObject contactObj = (JSONObject) obj;
            if(!contactObj.isEmpty()) {
                Set<String> contactnames = contactObj.keySet();

                for(String name:contactnames) {
                    String number = (String) contactObj.get(name);

                    //add name and number to contacts map, and the name into savedContactsList
                    contactsmap.put(name, number);
                    savedContactsList.add(name);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public StringProperty textProperty() {
        return numberBar.textProperty();
    }

    @FXML
    protected void onNumberPadButtonClicked(ActionEvent event) {
        //System.out.println(currentText);
        usedContactBox = false;
        Object obj = event.getSource();
        if(obj.equals(pb1)) {
            currentText = currentText.concat("1");
            textProperty().set(currentText);
        }
        if(obj.equals(pb2)) {
            currentText = currentText.concat("2");
            textProperty().set(currentText);
        }
        if(obj.equals(pb3)) {
            currentText = currentText.concat("3");
            textProperty().set(currentText);
        }
        if(obj.equals(pb4)) {
            currentText = currentText.concat("4");
            textProperty().set(currentText);
        }
        if(obj.equals(pb5)) {
            currentText = currentText.concat("5");
            textProperty().set(currentText);
        }
        if(obj.equals(pb6)) {
            currentText = currentText.concat("6");
            textProperty().set(currentText);
        }
        if(obj.equals(pb7)) {
            currentText = currentText.concat("7");
            textProperty().set(currentText);
        }
        if(obj.equals(pb8)) {
            currentText = currentText.concat("8");
            textProperty().set(currentText);
        }
        if(obj.equals(pb9)) {
            currentText = currentText.concat("9");
            textProperty().set(currentText);
        }
        if(obj.equals(pb0)) {
            currentText = currentText.concat("0");
            textProperty().set(currentText);
        }
        if(obj.equals(pbclear)) {
            currentText = "";
            textProperty().set(currentText);
        }
        if(obj.equals(pbback)) {
            if(!currentText.isEmpty()) {
                currentText = currentText.substring(0, currentText.length() - 1);
                textProperty().set(currentText);
            }
        }
    }

    @FXML
    private void addContactToList() throws IOException, ParseException {
        String name = addContactNameField.getText();
        String number = addContactNumberField.getText();

        //add contact to the contacts.json file
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("data/"+sProperties.getUserName()+"/contacts.json"));
        JSONObject contactsObject = (JSONObject) obj;
        contactsObject.put(name, number);

        FileWriter file = new FileWriter("data/"+sProperties.getUserName()+"/contacts.json");
        file.write(contactsObject.toJSONString());
        file.flush();
        file.close();

        if(!contactsmap.containsKey(name)) {
            contactOutput.setVisible(true);
            contactsmap.put(name, number);
            //options.add(name);
            contactsbox.getItems().add(name);
            contactOutput.textProperty().set("Contact Added!");
        } else {
            contactOutput.textProperty().set("Contact name already exists in list");
            contactOutput.setVisible(true);
        }
    }

    @FXML
    private void deleteContactFromList() throws IOException, ParseException {
        String name = contactsbox.getSelectionModel().selectedItemProperty().getValue();
        contactsmap.remove(name);
        contactsbox.getItems().remove(name);
        contactsbox.setPromptText("Contacts");

        //delete the contact from the contacts.json file
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("data/"+sProperties.getUserName()+"/contacts.json"));
        JSONObject contactsObject = (JSONObject) obj;
        contactsObject.remove(name);

        FileWriter file = new FileWriter("data/"+sProperties.getUserName()+"/contacts.json");
        file.write(contactsObject.toJSONString());
        file.flush();
        file.close();
    }

    @FXML
    private void onAddContact() {
        pbback.setVisible(false);
        pbclear.setVisible(false);
        numberBar.setVisible(false);
        numberPad.setVisible(false);
        contactsbox.setVisible(false);
        addContactButton.setVisible(false);
        deleteContactButton.setVisible(false);
        callButton.setVisible(false);
        endButton.setVisible(false);

        pbback.setDisable(true);
        pbclear.setDisable(true);
        numberBar.setDisable(true);
        numberPad.setDisable(true);
        contactsbox.setDisable(true);
        addContactButton.setDisable(true);
        deleteContactButton.setDisable(true);
        callButton.setDisable(true);
        endButton.setDisable(true);

        addContactScreen.setVisible(true);
        addContactScreen.setDisable(false);
    }

    @FXML
    private void contactsBoxAction() {
        usedContactBox = true;
        //get the chosen contact
        String chosen = contactsbox.getSelectionModel().selectedItemProperty().getValue();

        String chosennumber = contactsmap.get(chosen);
        textProperty().set(chosennumber);
        currentText = chosennumber;
    }

    @FXML
    private void onBackPressed() {
        addContactScreen.setVisible(false);
        addContactScreen.setDisable(true);

        addContactNameField.clear();
        addContactNumberField.clear();

        pbback.setVisible(true);
        pbclear.setVisible(true);
        numberBar.setVisible(true);
        numberPad.setVisible(true);
        contactsbox.setVisible(true);
        addContactButton.setVisible(true);
        deleteContactButton.setVisible(true);
        callButton.setVisible(true);
        endButton.setVisible(true);
        contactOutput.setVisible(false);

        pbback.setDisable(false);
        pbclear.setDisable(false);
        numberBar.setDisable(false);
        numberPad.setDisable(false);
        contactsbox.setDisable(false);
        addContactButton.setDisable(false);
        deleteContactButton.setDisable(false);
        callButton.setDisable(false);
        endButton.setDisable(false);
    }

    @FXML
    private void onCall() {
        numberBar.setStyle("-fx-background-color: green");
        textProperty().set("Calling "+ currentText);

        //After 5 seconds waiting, automatically connect to call
        final Timer t = new Timer();
        class RemindTask extends TimerTask {
            public void run() {
                //System.out.format("Time's up!%n");
                textProperty().set("Connected to " + currentText);
                sProperties.setInCall(true);
                t.cancel(); //Terminate the timer thread
                startCallTimer();
            }
        }
        t.schedule(new RemindTask(), connectedAfter*1000);

        numberBar.setDisable(true);

        callButton.setDisable(true);
        numberPad.setDisable(true);
    }

    private void startCallTimer() {
        endButton.setDisable(false);
        contactsbox.setPromptText("Contacts");

        //set the time of call
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        timeOfCall = dateFormat.format(date);

        callTimer = new Timer();
        class CountTime extends TimerTask {
            public void run() {
                //keep track of how long the call has been going on
                callDuration++;
            }
        }
        callTimer.schedule(new CountTime(), 1000, 1000);
    }

    @FXML
    private void onEnd() {

            String numberCalled = currentText;
            String contactName;
            numberBar.setStyle("-fx-background-color: white");
            numberBar.clear();

            if(usedContactBox) {
                contactName = contactsbox.getSelectionModel().selectedItemProperty().getValue();
            } else {
                contactName = "none";
            }

            currentText = "";
            //textProperty().set(number);

            callTimer.cancel();
            //record the number called and duration
            stats.addNewCall(contactName, numberCalled, callDuration, timeOfCall);

            //reset call duration
            timeOfCall = "";
            callDuration = 0;
            numberBar.setDisable(false);
            endButton.setDisable(true);
            callButton.setDisable(false);
            numberPad.setDisable(false);
            sProperties.setInCall(false);
    }
}
