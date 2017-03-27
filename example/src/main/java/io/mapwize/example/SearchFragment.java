package io.mapwize.example;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.arlib.floatingsearchview.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.mapwize.mapwize.MWZApi;
import io.mapwize.mapwize.MWZCallback;
import io.mapwize.mapwize.MWZPlace;
import io.mapwize.mapwize.MWZPlaceList;
import io.mapwize.mapwize.MWZSearchParams;
import io.mapwize.mapwize.MWZSearchable;
import io.mapwize.mapwize.MWZTranslation;
import io.mapwize.mapwize.MWZVenue;


public class SearchFragment extends Fragment {

    private String mLastQuery;
    private FloatingSearchView mSearchView;
    private SearchResultsListAdapter mSearchResultsAdapter;
    private SearchListener mSearchListener;
    private RecyclerView mSearchResultsList;
    private Stack<Handler> handlerStack = new Stack<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSearchView = (FloatingSearchView) view.findViewById(R.id.floating_search_view);
        setupFloatingSearch();
        setupResultsList();

    }

    public void attachNavigationDrawerToMenuButton(DrawerLayout drawer) {
        mSearchView.attachNavigationDrawerToMenuButton(drawer);
    }

    public void setSearchListener(SearchListener sl) {
        mSearchListener = sl;
    }

    private void setupResultsList() {
        mSearchResultsAdapter = new SearchResultsListAdapter();
    }

    private void setupFloatingSearch() {
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {
                //get suggestions based on newQuery
                if (!newQuery.equals("") && newQuery.length() > oldQuery.length()) {
                    mSearchView.showProgress();

                    final Handler handler = new Handler();
                    handlerStack.push(handler);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (handlerStack.size() > 0 && handlerStack.peek() == handler){
                                MWZSearchParams params = new MWZSearchParams();
                                params.setQuery(newQuery);

                                MWZApi.search(params, new MWZCallback<List<MWZSearchable>>() {
                                    @Override
                                    public void onSuccess(List<MWZSearchable> object) {
                                        List<SearchSuggestion> suggestions = new ArrayList<SearchSuggestion>();
                                        for (MWZSearchable o : object) {
                                            suggestions.add(new MWZSuggestionWrapper(o));
                                        }
                                        mSearchView.swapSuggestions(suggestions);
                                        mSearchView.hideProgress();
                                    }

                                    @Override
                                    public void onFailure(Throwable t) {

                                    }
                                });
                                handlerStack = new Stack<Handler>();
                            }
                        }
                    }, 500);


                }
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                MWZSuggestionWrapper searchObjectSuggestion = (MWZSuggestionWrapper) searchSuggestion;

                switch (searchObjectSuggestion.objectClass) {
                    case "MWZVenue":
                        if (mSearchListener != null) {
                            MWZVenue venue = (MWZVenue) searchObjectSuggestion.searchable;
                            mSearchListener.onVenueSuggestionClicked(venue);
                        }
                        break;
                    case "MWZPlace":
                        if (mSearchListener != null) {
                            MWZPlace place = (MWZPlace) searchObjectSuggestion.searchable;
                            mSearchListener.onPlaceSuggestionClicked(place);
                        }
                        break;
                    case "MWZPlaceList":
                        // TODO Whatever you want with place list
                        break;
                }

                mLastQuery = searchSuggestion.getBody();
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;
            }
        });



        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                Log.i("Focus", "OnFocus");
            }

            @Override
            public void onFocusCleared() {
                Log.i("Focus", "OnClear");
            }
        });


        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon,
                                         TextView textView, SearchSuggestion item, int itemPosition) {
                MWZSuggestionWrapper searchObjectSuggestion = (MWZSuggestionWrapper) item;

                leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_history_black_24dp, null));
                Util.setIconColor(leftIcon, Color.BLUE);
                leftIcon.setAlpha(.36f);

                int index = searchObjectSuggestion.getBody().toUpperCase().indexOf(mSearchView.getQuery().toUpperCase());

                if (index != -1) {
                    String oldSequence = searchObjectSuggestion.getBody().substring(index, index + mSearchView.getQuery().length());
                    String text = searchObjectSuggestion.getBody();
                    text = text.replaceFirst(oldSequence, "<font color=\"" + "#C51586" + "\">" + oldSequence + "</font>");

                    textView.setText(Html.fromHtml(text));
                }

            }

        });
    }
}


@SuppressLint("ParcelCreator")
class MWZSuggestionWrapper implements SearchSuggestion {

    String name;
    String alias;
    String objectId;
    String objectClass;
    List<MWZTranslation> translations;
    MWZSearchable searchable;
    String icon;

    public MWZSuggestionWrapper(MWZSearchable searchable) {
        name = searchable.getName();
        alias = searchable.getAlias();
        objectId = searchable.getIdentifier();
        if (searchable.getClass() == MWZPlace.class) {
            objectClass = "MWZPlace";
        }
        if (searchable.getClass() == MWZVenue.class) {
            objectClass = "MWZVenue";
        }
        if (searchable.getClass() == MWZPlaceList.class) {
            objectClass = "MWZPlaceList";
        }
        translations = searchable.getTranslations();
        this.searchable = searchable;
    }

    @Override
    public String getBody() {
        return translations.get(0).getTitle();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}