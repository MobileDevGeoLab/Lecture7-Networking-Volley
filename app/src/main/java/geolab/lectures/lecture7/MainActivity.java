package geolab.lectures.lecture7;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private ArrayList<String> list;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *  Chveni JSON-istvis satesto Kodi / Data
         */
        list = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            list.add("Object " + i);
        }

        final Context ctx = this;

        Button click = (Button) findViewById(R.id.click);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                String url = "http://api.openweathermap.org/data/2.5/forecast/daily";

                StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://www.google.com",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String s) {
                                TextView container = (TextView) findViewById(R.id.container);
                                container.setText(s);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                TextView container = (TextView) findViewById(R.id.container);
                                container.setText(volleyError.getMessage());
                            }
                        });

                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET,
                        url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject jsonObject) {
                                try {


                                    /**
                                     *  Chveni JSON-is satesto Kodi
                                     */
                                    JSONArray jsonArray = new JSONArray();
                                    for(int i = 0; i < list.size(); i++){
                                        JSONObject chveniJsonObject = new JSONObject();
                                        chveniJsonObject.put("Key", list.get(i));
                                        jsonArray.put(chveniJsonObject);
                                    }

                                    String cod = jsonObject.getString("cod");
                                    String message = jsonObject.getString("message");

                                    TextView container = (TextView) findViewById(R.id.container);
                                    container.setText(jsonObject.toString());
                                    progress.dismiss();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError volleyError) {
                                progress.dismiss();
                            }
                        });

                progress = ProgressDialog.show(ctx, "დაიცადეთ", "გთხოვთ დაიცადოთ");
                queue.add(jsonRequest);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
