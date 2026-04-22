package com.collectivities.binome.entity;

import java.util.List;

public class Federation {
    private String id;
    private List<FederationMandate> leaders;

    public Federation() {}

    public Federation(String id) {
        this.id = id;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public List<FederationMandate> getLeaders() { return leaders; }
    public void setLeaders(List<FederationMandate> leaders) { this.leaders = leaders; }
}
