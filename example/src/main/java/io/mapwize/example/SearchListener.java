package io.mapwize.example;

import io.mapwize.mapwize.MWZPlace;
import io.mapwize.mapwize.MWZVenue;

public interface SearchListener {

    public void onVenueSuggestionClicked(final MWZVenue venue);

    public void onPlaceSuggestionClicked(final MWZPlace place);

}
