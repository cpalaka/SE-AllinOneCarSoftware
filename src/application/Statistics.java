package application;

/**
 * Created by Chaitanya on 11/25/2015.
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class Statistics {
    //holds all the call details in a list
    private JSONObject callInfo = new JSONObject();

    //holds all the radio details in a list
    private JSONObject radioInfo = new JSONObject();

    //holds duration of drive
    private JSONObject driveDuration = new JSONObject();

    //holds date of drive
    private JSONObject driveDate = new JSONObject();

    //holds information about the speed
    private JSONObject speedInfo = new JSONObject();

    //holds information about the fuel
    private JSONObject fuelInfo = new JSONObject();

    SharedProperties sProperties;

    private static final Statistics statistics = new Statistics();

    private Statistics() {
        sProperties = SharedProperties.getInstance();
        //set up callInfo
        JSONArray callList = new JSONArray();
        callInfo.put("Call List", callList);

        //set up radioInfo
        JSONArray radioList = new JSONArray();
        radioInfo.put("Radio List", radioList);

        //set up speedInfo
        JSONArray spdArr = new JSONArray();
        speedInfo.put("Speed Info", spdArr);

        //set up fuelInfo
        JSONArray fuelArr = new JSONArray();
        fuelInfo.put("Fuel Info", fuelArr);

    }

    public static Statistics getInstance() { return statistics; }

    //function which updates callInfo by adding new call
    public void addNewCall(String name, String number, int duration, String timeOfCall) {
        JSONObject call = new JSONObject();
        call.put("Contact Name", name);
        call.put("Contact Number", number);
        call.put("Duration", duration);
        call.put("Time", timeOfCall);

        ((JSONArray) callInfo.get("Call List")).add(call);
        //System.out.println(callInfo.toJSONString());
    }

    //function which updates radioInfo by adding an instance of radio use
    public void addNewRadioUse(String stationName, int duration, String timeOfUse) {
        JSONObject radioUse = new JSONObject();
        radioUse.put("Station Name", stationName);
        radioUse.put("Time", timeOfUse);
        radioUse.put("Duration", duration);

        ((JSONArray) radioInfo.get("Radio List")).add(radioUse);
        //System.out.println(radioInfo.toJSONString());
    }

    //function which sets the total duration of the drive
    public void setDriveDuration(int driveDuration) {
        this.driveDuration.put("Drive Duration", String.valueOf(driveDuration));
        //System.out.println(this.driveDuration.toJSONString());
    }

    //function which sets the date of the drive
    public void setDriveDate(String driveDate) {
        this.driveDate.put("Drive Date", driveDate);
        //System.out.println(this.driveDate.toJSONString());
    }

    //function which sets the speed information (max speed, average speed)
    public void setSpeedInfo(int maxspeed, double avgspeed) {
        JSONObject speedinfo = new JSONObject();
        speedinfo.put("Maximum Speed", String.valueOf(maxspeed));
        speedinfo.put("Average Speed", String.valueOf(avgspeed));
        ((JSONArray) speedInfo.get("Speed Info")).add(speedinfo);
        //System.out.println(this.speedInfo.toJSONString());
    }

    //function which sets the fuel information (fuel consumed, fuel left)
    public void setFuelInfo(double consumed, double remaining) {
        JSONObject fuelinfo = new JSONObject();
        fuelinfo.put("Fuel Consumed", String.valueOf(consumed));
        fuelinfo.put("Fuel Remaining", String.valueOf(remaining));
        ((JSONArray) fuelInfo.get("Fuel Info")).add(fuelinfo);
        //System.out.println(this.fuelInfo.toJSONString());
    }

    public void compileAllStatsAsJson() throws IOException, ParseException {
        //this Json array holds all the information in this class and will be written to a json file
        JSONArray completeDriveProfile = new JSONArray();

        //get drive number for driveNo.json in folder
        JSONParser parser = new JSONParser();
        Object obj = parser.parse(new FileReader("data/"+sProperties.getUserName()+"/driveNo.json"));
        JSONObject driveNoObject = (JSONObject) obj;

        JSONObject driveNo = new JSONObject();
        driveNo.put("Drive Number", driveNoObject.get("Drive Number"));

        //add all info to completedriveprofile
        completeDriveProfile.add(driveNo);
        completeDriveProfile.add(callInfo);
        completeDriveProfile.add(radioInfo);
        completeDriveProfile.add(driveDuration);
        completeDriveProfile.add(driveDate);
        completeDriveProfile.add(speedInfo);
        completeDriveProfile.add(fuelInfo);

        //write to data/username/driveStats[drive number].json
        FileWriter file = new FileWriter("data/" + sProperties.getUserName() + "/driveStats" +driveNoObject.get("Drive Number")+ ".json");

        file.write(completeDriveProfile.toJSONString());
        file.flush();
        file.close();

    }

    //GETTERS
    //
    public JSONObject getCallInfo() {
        return callInfo;
    }

    public JSONObject getRadioInfo() {
        return radioInfo;
    }

    public JSONObject getDriveDuration() {
        return driveDuration;
    }

    public JSONObject getDriveDate() {
        return driveDate;
    }

    public JSONObject getSpeedInfo() {
        return speedInfo;
    }

    public JSONObject getFuelInfo() {
        return fuelInfo;
    }
}
