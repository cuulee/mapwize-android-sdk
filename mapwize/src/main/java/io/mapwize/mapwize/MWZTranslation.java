package io.mapwize.mapwize;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MWZTranslation {

    private String title;
    private String subtitle;
    private String details;
    private String language;

    public MWZTranslation() {
        super();
    }

    public MWZTranslation(String title, String subtitle, String details, String language) {
        super();
        this.title = title;
        this.subtitle = subtitle;
        this.details = details;
        this.language = language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String toString() {
        return "Title="+title+" Subtitle="+subtitle+" Details="+details+" Language="+language;
    }
}
