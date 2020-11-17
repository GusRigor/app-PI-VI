package com.example.piantxjava;

import java.util.List;

public class Trashes {
    private String id;
    private String name;
    private List<Trashe> trashes;

    public String getId() {
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public List<Trashe> getTrashes() {
        return trashes;
    }
    public void setTrashes(List<Trashe> trashes) { this.trashes = trashes;
    }
}

