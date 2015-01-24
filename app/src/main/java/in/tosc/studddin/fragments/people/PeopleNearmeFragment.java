package in.tosc.studddin.fragments.people;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import in.tosc.studddin.R;


public class PeopleNearmeFragment extends Fragment {


    ArrayList<EachRow3> list3 = new ArrayList<PeopleNearmeFragment.EachRow3>();
    EachRow3 each;
    MyAdapter3 q ;
    ListView lv ;

    ArrayList<String> namelist = new ArrayList<String>();
    ArrayList<String> institutelist = new ArrayList<String>();
    ArrayList<String> qualificationlist = new ArrayList<String>();
    ArrayList<String> arealist = new ArrayList<String>();
    ArrayList<String> distancelist = new ArrayList<String>();

    public PeopleNearmeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people_nearme, container, false);



        lv = (ListView)view.findViewById(R.id.listviewpeople);

        q = new MyAdapter3(getActivity(), 0, list3);
        q.setNotifyOnChange(true);

        loaddata();


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


            }
        });

        return  view;
    }



    class MyAdapter3 extends ArrayAdapter<EachRow3> {
        LayoutInflater inflat;
        ViewHolder holder;

        public MyAdapter3(Context context, int textViewResourceId,
                          ArrayList<EachRow3> objects) {
            super(context, textViewResourceId, objects);
            // TODO Auto-generated constructor stub
            inflat = LayoutInflater.from(context);
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            final int pos=position;

            if (convertView == null) {
                convertView = inflat.inflate(R.layout.listview_people, null);
                holder = new ViewHolder();
                holder.textname = (TextView) convertView.findViewById(R.id.people_name);
                holder.textarea = (TextView) convertView.findViewById(R.id.people_area);
//                holder.textdate = (TextView) convertView.findViewById(R.id.date);
                holder.textinstituition = (TextView) convertView.findViewById(R.id.people_institute);
                holder.textdistance = (TextView) convertView.findViewById(R.id.people_distance);
                holder.textqualification = (TextView) convertView.findViewById(R.id.people_qualification);

                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            EachRow3 row = getItem(position);

            holder.textname.setText(row.cname);
            holder.textarea.setText(row.carea);
            holder.textinstituition.setText(row.cinstituition);
            holder.textdistance.setText(row.cdistance);
            holder.textqualification.setText(row.cqualification);

            return convertView;
        }




        private class ViewHolder {

            TextView textname;
            TextView textarea;
            TextView textdistance;
            TextView textinstituition;
            TextView textqualification;

        }


        @Override
        public EachRow3 getItem(int position) {
            // TODO Auto-generated method stub
            return list3.get(position);
        }

    }

    private class EachRow3
    {
        String cname;
        String carea ;
        String cdistance ;
        String cqualification ;
        String cinstituition ;

    }


    private void loaddata()
    {

        for(int i  =0 ; i<list3.size(); i++)
        {
            list3.remove(each);
        }


        for(int  i = 0 ; i<5; i++)
        {
            each = new EachRow3();
            each.cname = "Laavanye";
            each.carea  = "Rohini"  ;
            each.cqualification  = "B tech"  ;
            each.cinstituition  = "DTU"  ;
            each.cdistance = "5 km"  ;

            list3.add(each);
        }

        lv.setAdapter(q);


    }



}
