package in.tosc.studddin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.ParseFacebookUtils;

import in.tosc.studddin.fragments.signon.SignOnFragment;

public class SignOnActivity extends ActionBarActivity {
    public static final String TAG = "SignOnActivity";
    public static final boolean DEBUG = ApplicationWrapper.LOG_DEBUG;
    public static final boolean INFO = ApplicationWrapper.LOG_INFO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_on);

        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fragmentManager.beginTransaction();

        SignOnFragment newFragment = new SignOnFragment();
        transaction.replace(R.id.signon_container, newFragment,"SignOnFragment").commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sign_on, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Handle action bar item clicks here. The action bar will

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (DEBUG) Log.d(TAG, "onActivityResult called");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag("SignOnFragment");
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode,data);
        }
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }
}
