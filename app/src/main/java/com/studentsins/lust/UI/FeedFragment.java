package com.studentsins.lust.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.studentsins.lust.Adapters.FeedCardAdapter;
import com.studentsins.lust.R;
import com.studentsins.lust.Utils.Constants;
import com.studentsins.lust.Utils.ListenerCollection;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by koemdzhiev on 07/02/16.
 */
public class FeedFragment extends Fragment implements Callback {
    //media type object that is required by the okHTTP library to send json data
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final String ARG_PAGE = "ARG_PAGE";
    private static final String TAG = FeedFragment.class.getSimpleName();
    private int mPage;
    private RecyclerView mRecyclerView;
    private Context mActivity;
    //json object to hold the json data to be send to the server
    private JSONObject feedJson = new JSONObject();
    private String userToken;
    //url of the feed api
    private final String FEED_URL = "https://api2.studentsins.com/feed";

    public static FeedFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        FeedFragment fragment = new FeedFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        mActivity = getActivity();
//TODO  This code bellow will change. It is onCreate just to establish a successful connection to the server. Once that is done, it will be changed.
        //get the saved user token from the shared preferences (it must be saved when the user logs into the app)
        userToken = MainActivity.sharedPreferences.getString(Constants.USER_TOKEN, "");
        if (!userToken.equals("")) {
            //if there is userToken stored...
            Log.d("FeedFragment. ", "UserToken: " + userToken);
            // Set up the jason...
            try {
                feedJson.put("cursor", 0);
                feedJson.put("user_id", 0);
                feedJson.put("venue_id", 0);
                feedJson.put("token", userToken.trim());
                // Post the data to the server
                post(FEED_URL, feedJson.toString());


            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }

        Log.d("FeedFragment", "onCreate" + mPage);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_layout, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.feedCardViewList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.addOnScrollListener(ListenerCollection.showHideCollapseFAB);
//      Test data entries...
        ArrayList<String> users = new ArrayList<>();
        users.add("Andrew Garden");
        users.add("Ben Richnond");
        users.add("Liviu Sima");
        users.add("Georgi Koemdzhiev");
        users.add("Daniel Tait");
        users.add("Prevain Davelaar");
        users.add("Andrius Kucinskas");
        users.add("Alexander Lunar");
        users.add("Awesome Jhon");

        // Instantiate the Feed adapter
        FeedCardAdapter adapter = new FeedCardAdapter(users, mActivity);

        mRecyclerView.setAdapter(adapter);
        Log.d("FeedFragment", "onCreateView" + mPage);
        return view;
    }

    /**
     * Callback from the Post method.
     * This Method is called when a connection cannot be established to the
     * server at all
     *
     * @param call
     * @param e
     */
    @Override
    public void onFailure(Call call, IOException e) {
        Log.d("FeedFragment", "onFailure" + e.toString());
    }

    /**
     * Callback from the Post method.
     * This method is called when the server respond back
     *
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(Call call, Response response) throws IOException {
        Log.d("FeedFragment", "onResponse" + response.toString());

        if (response.isSuccessful()) {
            try {
                JSONObject responseJson = new JSONObject(response.body().string());

                Log.d(TAG, "ResponseJSON: " + responseJson.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Method that will post the json data to the server. The data is in the following format:
     * "cursor": 0,
     * "user_id": 0,
     * "venue_id": 0,
     * "token": "O@)D...ILhc"
     *
     * @param url  the url to the server - https://api2.studentsins.com/feed/
     * @param json - the data in a json object that needs to be send to the server
     * @throws IOException
     */
    private void post(String url, String json) throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(this);
    }
}
