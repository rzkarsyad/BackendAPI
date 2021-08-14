package com.vsga.kontakapp;

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

    public class GetJSON extends AsyncTask<Void, Void, String> {

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
            showContact();
        }

        private void showContact(){
            JSONObject jsonObject = null;
            ArrayList<HashMap<String, String>> list = new ArrayList<>();

            try {
                jsonObject = new JSONObject(JSON_STRING);
                JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
                for(int i = 0; i < result.length(); i++){
                    JSONObject jo = result.getJSONObject(i);
                    String id = jo.getString(config.TAG_ID);
                    String nama = jo.getString(config.TAG_NAMA);
                    String nomor = jo.getString(config.TAG_NOMOR);
                    HashMap<String, String> kontak = new HashMap<>();

                    kontak.put(config.TAG_ID, id);
                    kontak.put(config.TAG_NAMA, nama);
                    kontak.put(config.TAG_NOMOR, nomor);
                    list.add(kontak);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            SimpleAdapter simpleAdapter = new SimpleAdapter(MainActivity.this, list, R.layout.list_item, new String[] {config.TAG_ID, config.TAG_NAMA, config.TAG_NOMOR},
                    new int[] {R.id.id, R.id.name, R.id.phone});
            listView.setAdapter(simpleAdapter);
        }

    }


}