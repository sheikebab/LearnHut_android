package in.tosc.studddin.fragments.people;


        import android.content.Intent;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.support.v4.app.Fragment;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import in.tosc.studddin.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewPerson extends Fragment {

    TextView name , interests , qualifications , distance , institute;
    String sname , sinterests , squalifications , sdistance , sinstitute;
    ImageView pic ;
    byte[] data;


    public ViewPerson() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.view_person, container, false);

        Bundle i = getArguments();
        if (i  != null )
        {
            sname = i.getString("name");
            sinstitute = i.getString("institute");
            sinterests = i.getString("interests");
            squalifications = i.getString("qualifications");
            sdistance = i.getString("distance");
            data = i.getByteArray("pic");
            Log.e("pic" , String.valueOf(data));
        }

        pic = (ImageView) rootView.findViewById(R.id.person_image);
        name = (TextView)rootView.findViewById(R.id.person_name);
        institute = (TextView)rootView.findViewById(R.id.person_institute);
        interests = (TextView)rootView.findViewById(R.id.person_interests);
        qualifications = (TextView)rootView.findViewById(R.id.person_qualifications);
        distance = (TextView)rootView.findViewById(R.id.person_area);

        pic.setImageBitmap(BitmapFactory
                .decodeByteArray(
                        data, 0,
                        data.length));

        name.setText(" " + sname);
        interests.setText(" " + sinterests);
        institute.setText(" " + sinstitute);
        qualifications.setText(" " + squalifications);
        distance.setText(" " + sdistance);

        return rootView;

    }


}