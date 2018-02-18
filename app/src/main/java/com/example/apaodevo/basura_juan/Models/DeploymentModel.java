package com.example.apaodevo.basura_juan.Models;

/**
 * Created by Brylle on 1/31/2018.
 */

public class DeploymentModel {
    private String binName;
    private String dateStart;
    private String dateEnd;
    private String timeStart;
    private String timeEnd;
    private String location;



    private String stopDeployment;
    public static DeploymentModel deploymentModelInstance;
    public static DeploymentModel getInstance()
    {
        if(deploymentModelInstance == null)
        {
            return deploymentModelInstance = new DeploymentModel();
        }
        return deploymentModelInstance;
    }

    public String getStopDeployment() {
        return stopDeployment;
    }

    public void setStopDeployment(String stopDeployment) {
        this.stopDeployment = stopDeployment;
    }
    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }


    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }




}
