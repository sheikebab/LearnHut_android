package in.tosc.studddin.fragments.notes;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.luminous.pick.Action;
import com.parse.ParseFile;
import com.parse.ParseUser;

import java.util.ArrayList;

import in.tosc.studddin.R;
import in.tosc.studddin.ui.ProgressBarCircular;

/**
 * NotesUploadFragment
 */
public class NotesUploadFragment extends Fragment {

    static String[] imagePaths = new String[0];

    private ProgressBarCircular uploadingNotesProgressBar;
    private Button uploadButton;
    private EditText topicNameEdTxt, branchNameEdTxt, subjectNameEdTxt;
    private ArrayList<ParseFile> parseFileList;
    private String topicNameString = "", branchNameString = "", subjectNameString = "";
    private ImageView notes_attach_imageview;

    public NotesUploadFragment() {

        // Required empty public constructor

    }

    public void setImagePaths(String[] paths, Boolean isSelected) {
        if (isSelected)
            Toast.makeText(getActivity(), getString(R.string.notes_files_selected), Toast.LENGTH_SHORT)
                    .show();

        else
            Toast.makeText(getActivity(), getString(R.string.notes_files_not_selected), Toast.LENGTH_SHORT)
                    .show();

        imagePaths = paths;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_notes_upload, container, false);

        notes_attach_imageview = (ImageView) rootView.findViewById(R.id.notes_selected_image);

        topicNameEdTxt = (EditText) rootView.findViewById(R.id.notes_topic);
        branchNameEdTxt = (EditText) rootView.findViewById(R.id.notes_branch);
        subjectNameEdTxt = (EditText) rootView.findViewById(R.id.notes_subject);
        uploadingNotesProgressBar = (ProgressBarCircular) rootView.findViewById(R.id.notes_upload_progress);

        parseFileList = new ArrayList<>();

        notes_attach_imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 5);
            }
        });

        uploadButton = (Button) rootView.findViewById(R.id.notes_upload);

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                topicNameString = topicNameEdTxt.getText().toString();
                branchNameString = branchNameEdTxt.getText().toString();
                subjectNameString = subjectNameEdTxt.getText().toString();

                if (topicNameString.length() < 1) {
                    Toast.makeText(getActivity(), getString(R.string.enter_topic_name),
                            Toast.LENGTH_SHORT).show();
                } else if (branchNameString.length() < 1) {
                    Toast.makeText(getActivity(), getString(R.string.enter_subject_name),
                            Toast.LENGTH_SHORT).show();
                } else if (subjectNameString.length() < 1) {
                    Toast.makeText(getActivity(), getString(R.string.enter_branch_name),
                            Toast.LENGTH_SHORT).show();

                } else {

                    if (imagePaths.length != 0) {


                        Intent serviceIntent = new Intent(getActivity(), NotesUploadService.class);
                        serviceIntent.putExtra("imagePaths", imagePaths);
                        serviceIntent.putExtra("userName", ParseUser.getCurrentUser().getString("NAME"));
                        serviceIntent.putExtra("subjectName", subjectNameString);
                        serviceIntent.putExtra("topicName", topicNameString);
                        serviceIntent.putExtra("branchName", branchNameString);

                        getActivity().startService(serviceIntent);
//                        final ProgressDialog notesUploadProgress = new ProgressDialog(getActivity());
//                        notesUploadProgress.setMessage(getString(R.string.notes_uploading));
//                        notesUploadProgress.setCancelable(false);
//                        notesUploadProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//                        notesUploadProgress.show();
//                        uploadingNotesProgressBar.setVisibility(View.VISIBLE);

//                        for (int i = 0; i < imagePaths.length; i++) {
//                            byte[] imageToBeUploaded = convertToByteArray(imagePaths[i]);
//                            if (imageToBeUploaded == null) {
//                                Toast.makeText(getActivity(), "File size beyond limit", Toast.LENGTH_SHORT)
//                                        .show();
//                            }
//
//                            ParseFile parseFile = new ParseFile("notes_images", imageToBeUploaded);
//
//                            try {
//                                parseFile.save();
//                            } catch (ParseException e) {
//                                Log.d("Raghav", "Error in parsefile");
//                            }
//                            parseFileList.add(parseFile);
//                        }
//
//                        ParseObject uploadNotes = new ParseObject("Notes");
//
//                        uploadNotes.addAll("notesImages", parseFileList);
//                        uploadNotes.put("userName", ParseUser.getCurrentUser().getString("NAME"));
//                        uploadNotes.put("subjectName", subjectNameString);
//                        uploadNotes.put("topicName", topicNameString);
//                        uploadNotes.put("branchName", branchNameString);
//                        uploadNotes.put("collegeName", "DTU");

//                        uploadNotes.saveInBackground(new SaveCallback() {
//                            @Override
//                            public void done(ParseException e) {
//                                // uploading.setVisibility(View.GONE);
//                                Log.d("Raghav", "File Uploaded");
////                                notesUploadProgress.dismiss();
//                                uploadingNotesProgressBar.setVisibility(View.GONE);
//                                Toast.makeText(getActivity(), getString(R.string.upload_complete),
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });

                    } else {
                        Toast.makeText(getActivity(), "Please select an Image to upload", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return rootView;
    }


}
