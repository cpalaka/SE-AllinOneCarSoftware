package application;

import javafx.scene.control.Label;

//Singleton class which all other classes use to share information
public class SharedProperties {

    private static final SharedProperties sharedProperties = new SharedProperties();

    private String speedLabel = "0.0 mph";
    private String journeyTimeLabel = "0 sec";
    private int totalJourneyTime = 0;
    private String dateLabel;
    private String userName;
    private int volume;
    private double startingfuel = 12.0;//the amount of fuel the car starts with is 12 gallons
    private double totalmiles = 24.0;//supposed to be 283.2 (assuming a mpg of 23.6) here we have set mpg as 2, and total amount of fuel as 12 gallons
    private double distanceTraveled = 0;
    private double distanceFromStart = 0;
    private int location = 1;//only 2 possible locations: 1 or 2
    private boolean inCall = false;
    private boolean isRadioOn = false;
    //drive statistics
    //location

    private SharedProperties()
    {
        //System.out.println("Inside sharedproperties constructor");
    }

    public static SharedProperties getInstance()
    {
        return sharedProperties;
    }

    public void setTotalJourneyTime(int totalJourneyTime) { this.totalJourneyTime += totalJourneyTime; }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setLocation(int location)
    {
        this.location = location;
    }

    public void setSpeed(String speed)
    {
        this.speedLabel = speed;
    }

    public void setJourneyTime(String time)
    {
        this.journeyTimeLabel = time;
    }

    public void setDateAndTime(String dateAndTime)
    {
        this.dateLabel = dateAndTime;
    }

    public void setVolume(int volume)
    {
        this.volume = volume;
    }

    public void setIsRadioOn(boolean isRadioOn){ this.isRadioOn = isRadioOn;}

    public void setInCall(boolean inCall) { this.inCall = inCall;}

    public void setDistanceTraveled(double distanceTraveled)
    {
        this.distanceTraveled = distanceTraveled;
    }

    public void setDistanceFromStart(double distanceFromStart)
    {
        this.distanceFromStart = distanceFromStart;
    }

    public int getTotalJourneyTime() { return totalJourneyTime; }

    public String getUserName()
    {
        return userName;
    }

    public double getTotalMiles() { return totalmiles; }

    public String getSpeed()
    {
        return speedLabel;
    }

    public String getJourneyTime()
    {
        return journeyTimeLabel;
    }

    public String getDateAndTime()
    {
        return dateLabel;
    }

    public int getVolume()
    {
        return volume;
    }

    public double getDistanceTraveled()
    {
        return distanceTraveled;
    }

    public double getDistanceFromStart()
    {
        return distanceFromStart;
    }

    public double getFuel()
    {
        return startingfuel;
    }

    public int getLocation()
    {
        return location;
    }

    public boolean getIsRadioOn() { return isRadioOn; }

    public boolean getInCall() { return inCall; }

}
