package com.example.apaodevo.basura_juan.Models;

/**
 * Created by apaodevo on 12/5/2017.
 */

public class BinModel {
    private String binName, binIpAddress, binId;


    public BinModel(String name, String address, String id) {
        this.binName = name;
        this.binIpAddress = address;
        this.binId = id;
    }
    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getBinIpAddress()
    {
        return binIpAddress;
    }

    public void setBinIpAdrress(String binIpAddress) {

        this.binIpAddress = binIpAddress;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }
}
