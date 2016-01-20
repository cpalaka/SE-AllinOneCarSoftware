package application;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.Stage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

/**
 * Created by Chaitanya on 11/26/2015.
 */
public class exitScreenController {

    private SharedProperties sProperties;
    private Statistics stats;

    private int currentDriveNo;
    @FXML
    private TreeView<String> treeView;

    @FXML
    private TreeView<String> prevTreeView;

    public exitScreenController() throws IOException, ParseException {
        sProperties = SharedProperties.getInstance();
        stats = Statistics.getInstance();
    }

    public void initialize() throws IOException, ParseException {
        incrementDriveNo();
        stats.compileAllStatsAsJson();
        createCurrentStatsTree();
        createPreviousStatsTree();
    }

    private void incrementDriveNo() throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("data/"+sProperties.getUserName()+"/driveNo.json"));

        JSONObject driveNoObject = (JSONObject) obj;
        Integer i = Integer.parseInt(driveNoObject.get("Drive Number").toString());


        driveNoObject.put("Drive Number", ++i);
        currentDriveNo = i;

        FileWriter file = new FileWriter("data/"+sProperties.getUserName()+"/driveNo.json");
        file.write(driveNoObject.toJSONString());
        file.flush();
        file.close();
    }

    private void createPreviousStatsTree() throws IOException, ParseException {
        TreeItem<String> root = new TreeItem<String>("Previous Drives");
        root.setExpanded(true);

        for(int i = currentDriveNo - 1; i > 0; --i ) {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader("data/"+sProperties.getUserName()+"/driveStats" +Integer.toString(i)+".json"));
            JSONArray driveStatsArray = (JSONArray) obj;

            //get all jsonobjects from the chosen drivestats file
            JSONObject callinfo = (JSONObject) driveStatsArray.get(1);
            JSONObject radioinfo = (JSONObject) driveStatsArray.get(2);
            JSONObject driveduration = (JSONObject) driveStatsArray.get(3);
            JSONObject drivedate = (JSONObject) driveStatsArray.get(4);
            JSONObject speedinfo = (JSONObject) driveStatsArray.get(5);
            JSONObject fuelinfo = (JSONObject) driveStatsArray.get(6);

            TreeItem<String> driveroot = new TreeItem<String>("Drive "+Integer.toString(i)+" Statistics");

            //drivedate
            TreeItem<String> treedrivedate = new TreeItem<String>("Drive Date: " + drivedate.get("Drive Date"));
            //driveduration
            TreeItem<String> treedriveduration = new TreeItem<String>("Drive Duration: " + driveduration.get("Drive Duration")+"seconds");
            //speedinfo
            TreeItem<String> treespeedinfo = new TreeItem<String>("Speed Info");
            JSONArray speedinfoArr = (JSONArray ) speedinfo.get("Speed Info");
            JSONObject speedinfoObj = (JSONObject) speedinfoArr.get(0);
            treespeedinfo.getChildren().addAll(
                    new TreeItem<String>("Maximum Speed: "+speedinfoObj.get("Maximum Speed")+" mph"),
                    new TreeItem<String>("Average Speed: "+speedinfoObj.get("Average Speed")+" mph")
            );
            //fuelinfo
            TreeItem<String> treefuelinfo = new TreeItem<String>("Fuel Info");
            JSONArray fuelinfoArr = (JSONArray ) fuelinfo.get("Fuel Info");
            JSONObject fuelinfoObj = (JSONObject) fuelinfoArr.get(0);
            treefuelinfo.getChildren().addAll(
                    new TreeItem<String>("Fuel Consumed: "+fuelinfoObj.get("Fuel Consumed")+" gallons"),
                    new TreeItem<String>("Fuel Remaining: "+fuelinfoObj.get("Fuel Remaining")+" gallons")
            );
            //callinfo
            TreeItem<String> treecallinfo = new TreeItem<String>("Call Info");
            JSONArray callArr = (JSONArray) callinfo.get("Call List");

            for(Object _obj: callArr) {
                JSONObject o = (JSONObject)_obj;
                TreeItem callitem = new TreeItem<String>("Time: "+(String) o.get("Time"));

                callitem.getChildren().addAll(
                        new TreeItem<String>("Contact Name: "+(String) o.get("Contact Name")),
                        new TreeItem<String>("Contact Number: "+(String) o.get("Contact Number")),
                        new TreeItem<String>("Call Duration: "+ String.valueOf((Long) o.get("Duration")))
                );
                treecallinfo.getChildren().add(callitem);
            }
            //radioinfo
            TreeItem<String> treeradioinfo = new TreeItem<String>("Radio Info");
            JSONArray radioArr = (JSONArray) radioinfo.get("Radio List");
            for(Object _obj: radioArr) {
                JSONObject o = (JSONObject)_obj;
                TreeItem radioitem = new TreeItem<String>((String) o.get("Time"));
                radioitem.getChildren().addAll(
                        new TreeItem<String>("Station Name: "+(String) o.get("Station Name")),
                        new TreeItem<String>("Listen Duration: "+String.valueOf((Long) o.get("Duration")))
                );
                treeradioinfo.getChildren().add(radioitem);
            }
            driveroot.getChildren().addAll(
                    treedrivedate,
                    treedriveduration,
                    treespeedinfo,
                    treefuelinfo,
                    treecallinfo,
                    treeradioinfo
            );

            root.getChildren().add(driveroot);
        }

        prevTreeView.setRoot(root);
    }

    private void createCurrentStatsTree() {
        //specify root node as drive number statistics
        TreeItem<String> root = new TreeItem<String>("Drive "+currentDriveNo+" Statistics");
        root.setExpanded(true);

        //create drivedate tree item
        TreeItem<String> drivedate = new TreeItem<String>("Drive Date: " + stats.getDriveDate().get("Drive Date"));
        //create drive duration tree item
        TreeItem<String> driveduration = new TreeItem<String>("Drive Duration: " + stats.getDriveDuration().get("Drive Duration")+"seconds");
        //create speed info tree item
        TreeItem<String> speedinfo = new TreeItem<String>("Speed Info");
        JSONArray speedinfoArr = (JSONArray ) stats.getSpeedInfo().get("Speed Info");
        JSONObject speedinfoObj = (JSONObject) speedinfoArr.get(0);
        speedinfo.getChildren().addAll(
                new TreeItem<String>("Maximum Speed: "+ speedinfoObj.get("Maximum Speed")+" mph"),
                new TreeItem<String>("Average Speed: "+ speedinfoObj.get("Average Speed")+" mph")
        );
        //create fuel info tree item
        TreeItem<String> fuelinfo = new TreeItem<String>("Fuel Info");
        JSONArray fuelinfoArr = (JSONArray ) stats.getFuelInfo().get("Fuel Info");
        JSONObject fuelinfoObj = (JSONObject) fuelinfoArr.get(0);
        fuelinfo.getChildren().addAll(
                new TreeItem<String>("Fuel Consumed: "+(String) fuelinfoObj.get("Fuel Consumed")+" gallons"),
                new TreeItem<String>("Fuel Remaining: "+(String) fuelinfoObj.get("Fuel Remaining")+" gallons")
        );

        //create call info tree item
        TreeItem<String> callinfo = new TreeItem<String>("Call Info");
        JSONArray callArr = (JSONArray) stats.getCallInfo().get("Call List");

        for(Object obj: callArr) {
            JSONObject o = (JSONObject)obj;
            TreeItem callitem = new TreeItem<String>("Time: "+(String) o.get("Time"));

            callitem.getChildren().addAll(
                    new TreeItem<String>("Contact Name: "+(String) o.get("Contact Name")),
                    new TreeItem<String>("Contact Number: "+(String) o.get("Contact Number")),
                    new TreeItem<String>("Call Duration: "+ Integer.toString((Integer) o.get("Duration")))
            );
            callinfo.getChildren().add(callitem);
        }

        //create radio info tree item
        TreeItem<String> radioinfo = new TreeItem<String>("Radio Info");
        JSONArray radioArr = (JSONArray) stats.getRadioInfo().get("Radio List");
        for(Object obj: radioArr) {
            JSONObject o = (JSONObject)obj;
            TreeItem radioitem = new TreeItem<String>((String) o.get("Time"));
            radioitem.getChildren().addAll(
                    new TreeItem<String>("Station Name: "+(String) o.get("Station Name")),
                    new TreeItem<String>("Listen Duration: "+Integer.toString((Integer) o.get("Duration")))
            );
            radioinfo.getChildren().add(radioitem);
        }

        //add all children tree items to the root node
        root.getChildren().addAll(
                drivedate,
                driveduration,
                speedinfo,
                fuelinfo,
                callinfo,
                radioinfo
        );

        //set the root node of the tree view
        treeView.setRoot(root);
    }

    @FXML
    private void onQuit() throws IOException {
        Platform.exit();
    }
}
