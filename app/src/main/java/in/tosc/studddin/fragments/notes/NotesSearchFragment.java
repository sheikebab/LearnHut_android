package in.tosc.studddin.fragments.notes;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import in.tosc.studddin.ApplicationWrapper;
import in.tosc.studddin.R;
import in.tosc.studddin.fragments.NotesFragment;
import in.tosc.studddin.ui.FloatingActionButton;
import in.tosc.studddin.utils.Utilities;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesSearchFragment extends Fragment {

    public static final String TAG = "NotesSearch";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    FloatingActionButton addNotesButton;
    EditText searchEdTxt;
    // TODO: Rename and change types of parameters
    private String mParam1;

    private String mParam2;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton searchButton;
    private RecyclerView mRecyclerView;
    private GridLayoutManager gridLayoutManager;
    private ArrayList<ArrayList<ParseFile>> notesFirstImage;

    private NotesCustomGridViewAdapter notesCustomGridViewAdapter;
    private ArrayList<String> notesCollegeName, notesBranchName, notesTopicName, notesSubjectName, uploadedBy;

    private boolean onRefresh = false;

    public NotesSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotesSearchFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static NotesSearchFragment newInstance(String param1, String param2) {
        NotesSearchFragment fragment = new NotesSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_notes_search, container, false);

        //notesGridView = (GridView) rootView.findViewById(R.id.notes_gridview);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.notes_gridview);
        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        notesCollegeName = new ArrayList<String>();
        notesBranchName = new ArrayList<String>();
        notesSubjectName = new ArrayList<String>();
        notesTopicName = new ArrayList<String>();
        uploadedBy = new ArrayList<String>();
        notesFirstImage = new ArrayList<ArrayList<ParseFile>>();

        addNotesButton = (FloatingActionButton) rootView.findViewById(R.id.notes_button_add);
        searchEdTxt = (EditText) rootView.findViewById(R.id.notes_search);
        searchEdTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                    performSearch(v.getText().toString());
                    ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow((v).getWindowToken(),0);
                    return true;
                }

                return false;
            }
        });
        searchButton = (ImageButton) rootView.findViewById(R.id.searchblahblah);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchEdTxt.setFocusableInTouchMode(true);
                searchEdTxt.requestFocus();
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            }
        });
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utilities.isNetworkAvailable(getActivity())) {
                    getNotes();
                    swipeRefreshLayout.setRefreshing(false);

                } else {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "Please connect to the Internet", Toast.LENGTH_SHORT).show();
                }
            }

        });
        getNotes();


        searchEdTxt = (EditText) rootView.findViewById(R.id.notes_search);


        addNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUploadFragment();

            }
        });

        return rootView;

    }


    public void goToUploadFragment() {
        if (ApplicationWrapper.LOG_DEBUG) Log.d(TAG, "goToUploadFragment called");
        NotesFragment notesFragment = (NotesFragment) getParentFragment();
        if (notesFragment != null) {
            notesFragment.goToOtherFragment(1);
        }
    }

    public void getNotes() {
        clear_lists();
        ParseQuery<ParseObject> query = new ParseQuery<>(
                "Notes");

        query.orderByDescending("createdAt");
        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {
                for (ParseObject notes : parseObjects) {

                    notesBranchName.add((String) notes.get("branchName"));
                    notesSubjectName.add((String) notes.get("subjectName"));
                    notesCollegeName.add((String) notes.get("collegeName"));
                    notesTopicName.add((String) notes.get("topicName"));
                    uploadedBy.add((String) notes.get("userName"));
                    notesFirstImage.add((ArrayList<ParseFile>) notes.get("notesImages"));


                }
                notesCustomGridViewAdapter = new NotesCustomGridViewAdapter(getActivity(), notesCollegeName,
                        notesBranchName, notesTopicName, notesSubjectName, notesFirstImage, uploadedBy);

                mRecyclerView.setAdapter(notesCustomGridViewAdapter);
            }

        });
    }

    public void performSearch(final String s) {

        if(s.equals("")) {
            Toast.makeText(getActivity(),"Please Enter Something",Toast.LENGTH_SHORT).show();
            return;
        }
        ParseQuery<ParseObject> query = new ParseQuery<>(
                "Notes");

        clear_lists();
        query.whereContains("topicName",s);
        query.orderByDescending("createdAt");

        query.setCachePolicy(ParseQuery.CachePolicy.NETWORK_ELSE_CACHE);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, ParseException e) {

//                Log.d("Search notes","result size : "+String.valueOf( parseObjects.size())+" "+s);

                for (ParseObject notes : parseObjects) {

                    notesBranchName.add((String) notes.get("branchName"));
                    notesSubjectName.add((String) notes.get("subjectName"));
                    notesCollegeName.add((String) notes.get("collegeName"));
                    notesTopicName.add((String) notes.get("topicName"));
                    uploadedBy.add((String) notes.get("userName"));
                    notesFirstImage.add((ArrayList<ParseFile>) notes.get("notesImages"));


                }
                notesCustomGridViewAdapter = new NotesCustomGridViewAdapter(getActivity(), notesCollegeName,
                        notesBranchName, notesTopicName, notesSubjectName, notesFirstImage, uploadedBy);

                mRecyclerView.setAdapter(notesCustomGridViewAdapter);
//                notesCustomGridViewAdapter.notifyDataSetChanged();
            }

        });
    }

    private void clear_lists() {
        notesBranchName.clear();
        notesSubjectName.clear();
        notesCollegeName.clear();
        notesTopicName.clear();
        uploadedBy.clear();
        notesFirstImage.clear();
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.notes_search, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.notes_search_upload:
                goToUploadFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}


