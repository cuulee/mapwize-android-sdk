package io.mapwize.mapwize;

import java.util.HashMap;

public class MWZPlace {

    private String identifier;
    private String name;
    private String alias;
    private String venueId;
    private MWZTranslation[] translations;

    public MWZPlace(){
        super();
    }

    public MWZPlace(HashMap<String, String> map) {
        super();
        this.identifier = map.get("_id");
        this.name = map.get("name");
        this.alias = map.get("alias");
        this.venueId = map.get("venueId");
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

    public String getVenueId() {
        return venueId;
    }

    public void setVenueId(String venueId) {
        this.venueId = venueId;
    }

    public MWZTranslation[] getTranslations() {
        return translations;
    }

    public void setTranslations(MWZTranslation[] translations) {
        this.translations = translations;
    }

    public String toString() {
        return "Identifier="+this.identifier+" Name="+this.name+" Alias="+this.alias+" VenueId="+this.venueId;
    }
}
