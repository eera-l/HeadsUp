/**
* GraphActivity.java
* Author: Federica Comuni
* Function.: Gets sensor readings through JSONRequestHandler.java
* and plots them in a graph.
*/

package cloud.headsup.controller;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LabelFormatter;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import cloud.headsup.R;
import cloud.headsup.model.MyVolley;

public class GraphActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        final GraphView graphView = findViewById(R.id.graph);


        final ArrayList<String> results = new ArrayList<>();

        RequestQueue queue = MyVolley.getRequestQueue();
        JsonArrayRequest myReq = new JsonArrayRequest(Request.Method.GET,
                "http://headsupapi.azurewebsites.net/sensor/getsensordata",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                results.add(response.get(i).toString());
                            }
                            Log.d("Array result ", Arrays.toString(results.toArray()));
                            ArrayList<String> dateStrings = new ArrayList<>();
                            ArrayList<Integer> distances = new ArrayList<>();

                            for (String s : results) {
                                //for ISO date format
                                dateStrings.add(s.substring(13, 32).replace('T', ' '));
                                distances.add(Integer.parseInt(s.substring(45, 46)));
                            }
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                            ArrayList<Date> dates = new ArrayList<>();

                            try {
                                for (String s : dateStrings) {
                                    dates.add(format.parse(s));
                                }
                            } catch (ParseException pe) {
                                pe.printStackTrace();
                            }

                            DataPoint[] dataPoints = new DataPoint[dates.size()];

                            for (int i = 0; i < dataPoints.length; i++) {
                                dataPoints[i] = new DataPoint(dates.get(i), distances.get(i));
                            }

                            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(dataPoints);

                            graphView.addSeries(series);
                            final DateFormat dateFormat = new SimpleDateFormat("mm:ss");

                            //graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(GraphActivity.this));
                            graphView.getGridLabelRenderer().setLabelFormatter(new LabelFormatter() {
                                @Override
                                public String formatLabel(double value, boolean isValueX) {
                                    // TODO Auto-generated method stub
                                    if (isValueX) {
                                        Date d = new Date((long) (value));
                                        return (dateFormat.format(d));
                                    } else {
                                        return String.format(Locale.ENGLISH,"%.2f", value);
                                    }
                                }

                                @Override
                                public void setViewport(Viewport viewport) {

                                }
                            });
                            graphView.getGridLabelRenderer().setNumHorizontalLabels(5); 

                            
                            graphView.getViewport().setMinX(dates.get(0).getTime());
                            graphView.getViewport().setMaxX(dates.get(dates.size() - 1).getTime());
                            graphView.getViewport().setXAxisBoundsManual(true);

                          

                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(myReq);


    }
}
