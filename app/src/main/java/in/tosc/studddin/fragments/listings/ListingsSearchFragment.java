package in.tosc.studddin.fragments.listings;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.internal.widget.TintCheckBox;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import in.tosc.studddin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ListingsSearchFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    //private RecyclerView.LayoutManager mLayoutManager;
    View rootView;

    private List<ParseObject> listings;
    private List<ListingInfo> listingInfos;


    public ListingsSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        SharedPreferences filterDetails = getActivity().getSharedPreferences("filterdetails", 0);
        new FetchListingsData().execute();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_listings, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.listing_recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.listing, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // handle item selection
        switch (item.getItemId()) {
            case R.id.listing_filter:
                FilterDialog dialog = new FilterDialog();
                dialog.show(getFragmentManager(),"filterdialog");

                return true;
            case R.id.listing_upload:
                Fragment fragment2 = new ListingsUploadFragment();
                FragmentManager fm = getFragmentManager();
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.replace(R.id.notes_pager, fragment2);
                transaction.commit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class ListingInfo {

        private String owner_name;
        private String listing_name;
        private String mobile;
        private String distance;
        private ParseFile image;


        public String getOwner_name() {
            return owner_name;
        }

        public void setOwner_name(String owner_name) {
            this.owner_name = owner_name;
        }

        public String getListing_name() {
            return listing_name;
        }

        public void setListing_name(String listing_name) {
            this.listing_name = listing_name;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public ParseFile getImage() {
            return image;
        }

        public void setImage(ParseFile image) {
            this.image = image;
        }
    }

    public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder>{

        private List<ListingInfo> mDataset;

        public ListingAdapter(List<ListingInfo> dataSet) {
            mDataset = dataSet;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            CardView v = (CardView) LayoutInflater.from(viewGroup.getContext())
                    .inflate(in.tosc.studddin.R.layout.listing_card_view, viewGroup, false);

            ViewHolder vh = new ViewHolder(v);
            return vh;
        }

        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.listing_name.setText(mDataset.get(i).getListing_name());
            viewHolder.owner_name.setText(mDataset.get(i).getOwner_name());
            viewHolder.mobile.setText(mDataset.get(i).getMobile());
            viewHolder.listing_image.setParseFile(mDataset.get(i).getImage());
            viewHolder.listing_image.loadInBackground(new GetDataCallback() {
                @Override
                public void done(byte[] bytes, ParseException e) {
                    // nothing to do
                    if (e != null)
                        e.printStackTrace();
                }
            });
            viewHolder.listing_distance.setText(mDataset.get(i).getDistance());
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView listing_name;
            TextView owner_name;
            TextView mobile;
            ParseImageView listing_image;
            TextView listing_distance;

            public ViewHolder(CardView v) {
                super(v);
                this.listing_name = (TextView) v.findViewById(R.id.listing_name);
                this.owner_name = (TextView) v.findViewById(R.id.owner_name);
                this.mobile = (TextView) v.findViewById(R.id.mobile);
                this.listing_distance = (TextView) v.findViewById(R.id.listing_distance);
                this.listing_image = (ParseImageView) v.findViewById(R.id.listing_image);
            }


        }
    }

    private class FetchListingsData extends AsyncTask<Void,Void,Void>
    {

        @Override
        protected Void doInBackground(Void... voids) {
            // Create the array
            listingInfos = new ArrayList<ListingInfo>();
            try {

                ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(
                        "Listings");
                query.orderByAscending("createdAt");
                listings = query.find();
                for (ParseObject listing : listings) {

                    ListingInfo listingInfo = new ListingInfo();
                    listingInfo.setListing_name((String) listing.get("listingName"));
                    listingInfo.setOwner_name((String) listing.get("ownerName"));
                    listingInfo.setMobile((String) listing.get("mobile"));
                    listingInfo.setDistance("temp");
                    listingInfo.setImage((ParseFile) listing.get("image"));
                    listingInfos.add(listingInfo);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {


            mAdapter = new ListingAdapter(listingInfos);
            mRecyclerView.setAdapter(mAdapter);

        }
    }


    public static class FilterDialog extends DialogFragment implements AdapterView.OnItemSelectedListener{

        private View v;
        private CheckBox books;
        private CheckBox apparatus;
        private CheckBox misc;
        SharedPreferences filterPrefs;
        SharedPreferences.Editor editor;

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            filterPrefs = getActivity().getSharedPreferences("filterdetails", 0);
            editor = filterPrefs.edit();
            List<String> spinnerList = new ArrayList<String>();
            spinnerList.add("Nearest");
            spinnerList.add("Recent");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,spinnerList);
            AlertDialog.Builder filterDialog = new AlertDialog.Builder(getActivity());
            LayoutInflater inflater = getActivity().getLayoutInflater();
            v = inflater.inflate(R.layout.listing_filter_dialog, null);
            books = (TintCheckBox) v.findViewById(R.id.cb_books);
            apparatus = (TintCheckBox) v.findViewById(R.id.cb_apparatus);
            misc = (TintCheckBox) v.findViewById(R.id.cb_misc);
            books.setChecked(filterPrefs.getBoolean("books",true));
            apparatus.setChecked(filterPrefs.getBoolean("apparatus",true));
            misc.setChecked(filterPrefs.getBoolean("misc",true));
            Spinner spinner = (Spinner) v.findViewById(R.id.spinner);
            spinner.setAdapter(dataAdapter);
            spinner.setOnItemSelectedListener(this);
            if(filterPrefs.getString("sortby","nearest").compareTo("nearest")==0)
                spinner.setSelection(0);
            else
                spinner.setSelection(1);
            filterDialog.setView(v);
            filterDialog.setTitle("Filter");
            filterDialog.setCancelable(false);
            filterDialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(books.isChecked())
                        editor.putBoolean("books",true);
                    else
                        editor.putBoolean("books",false);

                    if(apparatus.isChecked())
                        editor.putBoolean("apparatus",true);
                    else
                        editor.putBoolean("apparatus",false);

                    if(misc.isChecked())
                        editor.putBoolean("misc",true);
                    else
                        editor.putBoolean("books",false);

                    editor.commit();

                   //refresh listing
                }
            });
            return filterDialog.create();
        }

        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            if(i==0){
                editor.putString("sortby","nearest");
                editor.commit();
            }
            else if(i==1){
                editor.putString("sortby","recent");
                editor.commit();
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }


    }



}