package io.mapwize.mapwize;

import java.util.Map;

public class MWZVenue {

    private String identifier;
    private String name;
    private String alias;
    private Map<String, Object> data;

    public MWZVenue(){
        super();
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public String toString() {
        return "Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias;
    }
}
