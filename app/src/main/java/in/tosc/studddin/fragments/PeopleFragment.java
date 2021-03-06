package in.tosc.studddin.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.tosc.studddin.ApplicationWrapper;
import in.tosc.studddin.R;
import in.tosc.studddin.fragments.people.PeopleNearmeFragment;
import in.tosc.studddin.fragments.people.PeopleSameInstituteFragment;
import in.tosc.studddin.fragments.people.PeopleSameInterestsFragment;
import in.tosc.studddin.ui.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class PeopleFragment extends Fragment {


    ViewPager peoplePager;
    FragmentPagerAdapter fragmentPagerAdapter;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;

    public PeopleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people, container, false);
        int p = getActivity().getResources().getColor(R.color.colorPrimary);
        int s = getActivity().getResources().getColor(R.color.colorPrimaryDark);
        ApplicationWrapper.setCustomTheme((ActionBarActivity) getActivity(), p, s);

/*        fragmentPagerAdapter = new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return (new PeopleNearmeFragment());
                    case 1:
                        return (new PeopleSameInstituteFragment());
                    case 2:
                        return (new PeopleSameInterestsFragment());

                }
                return new NotesSearchFragment();
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Near Me";
                    case 1:
                        return "Same Institute";
                    case 2:
                        return "Same Interests";
                }

                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        };*/

        peoplePager = (ViewPager) view.findViewById(R.id.people_pager);
        adapter  =  new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        peoplePager.setAdapter(adapter);
        peoplePager.setOffscreenPageLimit(3);
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabstripColor);
            }
        });
        tabs.setViewPager(peoplePager);

        return view;
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence TAB_TITLES[]={"Near Me","In Institute", "Same Interest"};
        int NUM_TAB =3;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {


            switch (position) {
                case 0:
                    return (new PeopleNearmeFragment());
                case 1:
                    return (new PeopleSameInstituteFragment());
                case 2:
                    return (new PeopleSameInterestsFragment());
            }
            return (new PeopleNearmeFragment());

        }

        @Override
        public CharSequence getPageTitle(int position) {
            return TAB_TITLES[position];
        }

        @Override
        public int getCount() {
            return NUM_TAB;
        }
    }

}