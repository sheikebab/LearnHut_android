package in.tosc.studddin.externalapi;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import com.parse.ParseTwitterUtils;
import com.parse.twitter.Twitter;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import in.tosc.studddin.utils.Utilities;

/**
 * Created by championswimmer on 26/1/15.
 */
public class TwitterApi {
    private static final String TAG = "TwitterApi";

    private static final String infoGetUrl = "https://api.twitter.com/1.1/users/show.json?screen_name=%s";

    public static void getUserInfo(final TwitterInfoCallback callback) {
        new AsyncTask<Void, Void, JSONObject>() {
            Bitmap bitmap = null;

            @Override
            protected JSONObject doInBackground(Void... params) {
                Twitter twitter = ParseTwitterUtils.getTwitter();
                HttpClient client = new DefaultHttpClient();
                HttpGet verifyGet = new HttpGet(String.format(infoGetUrl, twitter.getScreenName()));
                twitter.signRequest(verifyGet);
                try {
                    HttpResponse response = client.execute(verifyGet);
                    JSONObject object = new JSONObject(EntityUtils.toString(response.getEntity()));
                    bitmap = Utilities.downloadBitmap(object.getString("profile_image_url").replace("_normal", ""));
                    Log.d(TAG, "twitter profile url = " + object.getString("profile_image_url").replace("_normal", ""));
                    return object;
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(JSONObject object) {
                try {
                    callback.gotInfo(object, bitmap);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    public interface TwitterInfoCallback {
        public void gotInfo(JSONObject object, Bitmap bitmap) throws JSONException;
    }
}
