package com.example.apaodevo.basura_juan.Models;

/**
 * Created by apaodevo on 12/5/2017.
 */

public class BinModel {
    private String binName, binIpAdrress, binId;


    public BinModel(String name, String address, String id) {
        this.binName = name;
        this.binIpAdrress = address;
        this.binId = id;
    }
    public String getBinName() {
        return binName;
    }

    public void setBinName(String binName) {
        this.binName = binName;
    }

    public String getBinIpAdrress() {
        return binIpAdrress;
    }

    public void setBinIpAdrress(String binIpAdrress) {
        this.binIpAdrress = binIpAdrress;
    }

    public String getBinId() {
        return binId;
    }

    public void setBinId(String binId) {
        this.binId = binId;
    }
}
