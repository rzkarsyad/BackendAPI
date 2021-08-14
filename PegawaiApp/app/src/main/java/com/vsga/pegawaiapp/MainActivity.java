package com.vsga.pegawaiapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private String JSON_STRING = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = findViewById(R.id.listView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    public class GetJSON extends AsyncTask<Void, Void, String>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(MainActivity.this, "Mengambil data", "Mohon tunggu", false, false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            RequestHandler rh = new RequestHandler();
            String s = rh.sendGetRequest(config.URL_GET_ALL);

            return s;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            JSON_STRING = s;
            showEmployee();
        }
    }

    private void showEmployee(){
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<>();

        try {
            jsonObject = new JSONObject(JSON_STRING);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            for(int i = 0; i < result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String id = jo.getString(config.TAG_ID);
                String name = jo.getString(config.TAG_NAME);
                HashMap<String, String> employees = new HashMap<>();

                employees.put(config.TAG_ID, id);
                employees.put(config.TAG_NAME, name);
                list.add(employees);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, list, R.layout.list_item, new String[] {config.TAG_ID, config.TAG_NAME},
                new int[] {R.id.id, R.id.name});
        listView.setAdapter(simpleAdapter);
    }
}