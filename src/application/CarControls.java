package application;

import java.io.IOException;
import java.math.BigDecimal;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class CarControls extends AnchorPane{

    @FXML
    private Button logOutButton;
    private double startTime;

    @FXML
    private Label startLabel;
    @FXML
    private Label endLabel;
    @FXML
    private Label userLabel;

    @FXML
    private Circle startCircle;
    @FXML
    private Circle endCircle;

    @FXML
    private Polygon accelerateButton;
    @FXML
    private Polygon deccelerateButton;
    @FXML
    private Rectangle brakeButton;

    @FXML
    private Label journeyTimeLabel;
    @FXML
    private Label speedLabel;
    @FXML
    private Label accelerationLabel;
    @FXML
    private Label fuelLabel;
    @FXML
    private Label brakeLabel;

    private SharedProperties sProperties;
    private Statistics stats;

    private Paint grayColor = Paint.valueOf("#858c86");
    private Paint blueColor = Paint.valueOf("#1e90ff");
    private Timer timer;
    private boolean run;
    private double speed;
    private double maxspeed = 0;
    private double prevSpeed = 0;
    //private double mpg = 23.6;//miles per gallon
    private double acceleration;
    //private double fuelCapacity;
    private String speedUnits = "mph";
    private String accelerationUnits = "mphsquare";
    private String fuelUnits = "%";
    //private MainScreen mainScreen;
    private double tempTimer;//used to keep track of the distance traveled
    private double distanceTraveled = 0;
    private double distanceFromStart = 0;
    private int timeFromStart = 0;

    public CarControls() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CarControl.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        sProperties = SharedProperties.getInstance();
        stats = Statistics.getInstance();
        //System.out.println(sProperties.getUserName());
    }

    @FXML
    private void onStartButtonClicked() throws Exception
    {
        if(!isCarRunning())
        {
            setCarState(true);
            startTime = System.currentTimeMillis();
            tempTimer = System.currentTimeMillis();
            System.out.println("Start Time: " + startTime);
            onStart();

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
                                    updateValues();
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
            timer.schedule(timerTask, 1000, 1000);

        }
    }

    private void setCarState(boolean state)
    {
        run = state;
    }

    private boolean isCarRunning()
    {
        if(startTime > 0 && run == false)
        {
            return false;
        }
        else if(startTime > 0 && run == true)
            return true;
        else if(startTime <= 0)
        {
            return false;
        }
        return false;
    }

    private void onStart()
    {
        startCircle.setFill(grayColor);
        endCircle.setFill(blueColor);
        accelerateButton.setFill(blueColor);
        deccelerateButton.setFill(blueColor);
        brakeButton.setFill(blueColor);
    }

    @FXML
    private void onEndButtonClicked()
    {
        speed = 0;
        setCarState(false);
        sProperties.setTotalJourneyTime(timeFromStart);
        sProperties.setJourneyTime("Stopped");
        sProperties.setSpeed(speed + " " + speedUnits);
        onEnd();
        timer.cancel();
    }

    private void onEnd()
    {
        startCircle.setFill(blueColor);
        endCircle.setFill(grayColor);
        accelerateButton.setFill(grayColor);
        deccelerateButton.setFill(grayColor);
        brakeButton.setFill(grayColor);
    }

    public void updateValues() throws Exception
    {
        setTimeLabel();

        setAcceleration();
        sProperties.setSpeed(speed + " " + speedUnits);
        updateDistanceTraveled();

        //keep track of the maximum speed
        if(speed > maxspeed) {
            maxspeed = speed;
        }
        //System.out.println(distanceTraveled + ", " + distanceFromStart);
//		speedLabel.setText(speed + " " + speedUnits);
//		accelerationLabel.setText(acceleration + " " + accelerationUnits);
//		fuelLabel.setText(fuelCapacity + " " + fuelUnits);
    }

    private void setAcceleration()
    {
        acceleration = (speed - prevSpeed);
        prevSpeed = speed;
    }

    public void setTimeLabel()
    {
        double cTime = System.currentTimeMillis();
        double totalTime = (cTime-startTime)/1000;
        timeFromStart = (int) totalTime;

        //System.out.println("Time since start: " + totalTime);
        String time = null;
        if(totalTime<=60)
            time = (int)totalTime + " sec";
        if(totalTime>60)
            time = (int)(totalTime/60) + " min";
        if(totalTime>3600)
            time = (int)(totalTime/3600) + " min";
        sProperties.setJourneyTime(time);

        //journeyTimeLabel.setText((time) + " since start");
    }

    private void updateDistanceTraveled() {
        if(speed > 0) {//going forward
            //System.out.println("Speed>0");
            distanceTraveled += (speed / 3600);
            distanceFromStart += (speed/3600);
            sProperties.setDistanceTraveled(distanceTraveled);
            sProperties.setDistanceFromStart(distanceFromStart);
        } else {//going backward
            //System.out.println("Speed<0");
            distanceTraveled -= (speed / 3600);
            sProperties.setDistanceTraveled(distanceTraveled);
            distanceFromStart += (speed/3600);
            sProperties.setDistanceFromStart(distanceFromStart);
        }
    }

    @FXML
    private void accelerate()
    {
        speed += 1;
    }

    @FXML
    private void deccelerate()
    {
        speed -= 1;
    }

    @FXML
    private void brake()
    {
        speed = 0;
    }

    @FXML
    public void onLoggingOut()
    {
        sProperties.setTotalJourneyTime(timeFromStart);
        //save stats for total duration of drive and date
        stats.setDriveDuration(sProperties.getTotalJourneyTime());

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        String _date = dateFormat.format(date);
        stats.setDriveDate(_date);

        //save stats for average and max speed
        stats.setSpeedInfo((int)maxspeed, sProperties.getDistanceTraveled()/(sProperties.getTotalJourneyTime()*3600));

        //save stats for fuel info
        double consumed = (sProperties.getDistanceTraveled()/sProperties.getTotalMiles())*sProperties.getFuel();
        double remaining = sProperties.getFuel() - consumed;
        Double truncatedConsumed=new BigDecimal(consumed).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        Double truncatedRemaining=new BigDecimal(remaining).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        stats.setFuelInfo(truncatedConsumed,truncatedRemaining);

        Parent exitScreen;
        try {
            exitScreen = FXMLLoader.load(getClass().getResource("exitScreen.fxml"));
            Scene exitScene = new Scene(exitScreen,700,400);
            Stage stage = (Stage) logOutButton.getParent().getScene().getWindow();
            stage.setScene(exitScene);
            stage.show();
            //System.out.println("Logging out");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
