package application;
	
import java.io.*;
import java.util.Set;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			/*
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,600,400);
			scene.setRoot((Parent) FXMLLoader.load(getClass().getResource("loginScreen.fxml")));

			primaryStage.setScene(scene);
			primaryStage.setTitle("All in One Car Software");
			primaryStage.show();
			///// */

			createUserDirectories();
			primaryStage.setTitle("All In One Car Software");

			FXMLLoader myLoader = new FXMLLoader(getClass().getResource("loginScreen.fxml"));

			Pane myPane = (Pane)myLoader.load();

			LoginScreen controller = (LoginScreen) myLoader.getController();

			controller.setPrevStage(primaryStage);

			Scene myScene = new Scene(myPane,700,400);
			primaryStage.setScene(myScene);
			primaryStage.show();


		} catch(Exception e) {
			e.printStackTrace();
		}
		
//		Parent root = FXMLLoader.load(getClass().getResource("loginScreen.fxml"));
//		primaryStage.setTitle("All in one Car Software");
//		primaryStage.setScene(new Scene(root, 800, 500));
//		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	//make sure that for each known user, there exists a separate directory
	//in the data folder in which we can store all related data.
	//if it doesn't exist, create a new directory
	private void createUserDirectories() throws IOException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("data/login/users.json"));
		JSONObject jsonObject = (JSONObject) obj;

		Set<String> arr = jsonObject.keySet();

		for(String s:arr) {
			File theDir = new File("data/"+s);
			theDir.mkdirs();

			//every directory must have a contacts json file and driveNo json file, so create them here
			File contactFile = new File("data/"+s+"/contacts.json");
			File driveNoFile = new File("data/"+s+"/driveNo.json");

			if(!contactFile.exists()) {
				JSONObject contactsobj = new JSONObject();
				FileWriter file = new FileWriter("data/" + s + "/contacts.json");
				file.write(contactsobj.toJSONString());
				file.flush();
				file.close();
			}
			if(!driveNoFile.exists()) {
				JSONObject numObj = new JSONObject();
				numObj.put("Drive Number", new Integer(0));
				FileWriter file = new FileWriter("data/" + s + "/driveNo.json");
				file.write(numObj.toJSONString());
				file.flush();
				file.close();
			}
		}
	}
}
