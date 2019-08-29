package com.anie.dara.trackmonbus.model;

public class Perihal {

    String perihal_id, perihal;

    public Perihal(String perihal_id, String perihal) {

        this.perihal = perihal;
        this.perihal_id = perihal_id;
    }

    public String getPerihal_id() {
        return perihal_id;
    }

    public void setPerihal_id(String perihal_id) {
        this.perihal_id = perihal_id;
    }

    public String getPerihal() {
        return perihal;
    }

    public void setPerihal(String perihal) {
        this.perihal = perihal;
    }
}
