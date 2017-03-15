package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZVenue implements MWZSearchable {

    private String identifier;
    private String name;
    private String alias;
    private Map<String, Object> data;
    private List<MWZTranslation> translations;
    private MWZGeometry geometry;

    public MWZVenue(){
        super();
    }

    public String getIdentifier() {
        return identifier;
    }

    @JsonSetter("_id")
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

    public List<MWZTranslation> getTranslations() {
        return translations;
    }

    public void setTranslations(List<MWZTranslation> translations) {
        this.translations = translations;
    }

    public MWZGeometry getGeometry() {
        return geometry;
    }

    public void setGeometry(MWZGeometry geometry) {
        this.geometry = geometry;
    }
}
