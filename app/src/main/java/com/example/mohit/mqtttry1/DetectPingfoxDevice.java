package com.example.mohit.mqtttry1;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;

public class DetectPingfoxDevice extends AppCompatActivity {

    private Boolean bPingFoxConnected;
    ProgressDialog progDailog ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detect_pingfox_device);

        bPingFoxConnected= checkPingFoxConnected();

    }

    private Boolean checkPingFoxConnected() {
        NetworkSniffTask task = new NetworkSniffTask(DetectPingfoxDevice.this);
        task.execute();
        return false;
    }

    class NetworkSniffTask extends AsyncTask<Void, Void, Boolean> {

        private static final String TAG = SyncStateContract.Constants.DATA + "nstask";
        private WeakReference<Context> mContextRef;
        private ArrayList<InetAddress> activeIPlist;
        Context noClueContext ;

        public NetworkSniffTask(Context context) {
            mContextRef = new WeakReference<Context>(context);
            noClueContext = context;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = ProgressDialog.show(DetectPingfoxDevice.this,"Scanning","please wait");
            progDailog.setMessage("Searching for pingfox Device on your network");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            Log.d(TAG, "Let's sniff the network");


            try {
                Context context = mContextRef.get();

                if (context != null) {

                    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                    WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

                    WifiInfo connectionInfo = wm.getConnectionInfo();
                    int ipAddress = connectionInfo.getIpAddress();
                    String ipString = Formatter.formatIpAddress(ipAddress);


                    Log.d(TAG, "activeNetwork: " + String.valueOf(activeNetwork));
                    Log.d(TAG, "ipString: " + String.valueOf(ipString));

                    String prefix = ipString.substring(0, ipString.lastIndexOf(".") + 1);
                    Log.d(TAG, "prefix: " + prefix);

                    for (int i = 1; i < 120; i++) {

                        String testIp = prefix + String.valueOf(i);
                        Log.d(TAG, "testip: " + testIp);
                        InetAddress address = InetAddress.getByName(testIp);
                        boolean reachable = address.isReachable(100);
                        //Log.d(TAG, "testip: " + testIp + "isreachable- "+reachable);
                        String hostName = address.getCanonicalHostName();
                        Log.d(TAG, "testip: " + testIp + "isreachable- " + reachable + ", host-" + hostName);

                        if (reachable){
                            Log.i(TAG, "Host: " + String.valueOf(hostName) + "(" + String.valueOf(testIp) + ") is reachable!");
                            //activeIPlist.add(address);1
                            //Boolean pingfoxIP = null;
                            Boolean pingfoxIP = checkIfpingFox(address, context);
                            Log.i("booleann",pingfoxIP.toString());
                            if (pingfoxIP == true){
                                Log.i("pingfoxIP",testIp);
                                return true;
                            }
                        }

                    }
                }
            } catch (Throwable t) {
                Log.e(TAG, "Well that's not good....", t);
                progDailog.dismiss();
                return true;


            }

            return null;
        }



        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progDailog.dismiss();
            if (aBoolean){
                Toast.makeText(DetectPingfoxDevice.this,"Found pingfox device on network",Toast.LENGTH_SHORT).show();
            }else {Toast.makeText(DetectPingfoxDevice.this,"No pingfox device found on network",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Boolean checkIfpingFox(InetAddress address, Context context) {
        Boolean pingfoxIp = false ;



        try {
            URL url = new URL("http:/" + address + "/cm?cmnd=power%20toggle");
            Log.i("URL", String.valueOf(url));
            //URL url = new URL("http://192.168.0.105/cm?cmnd=power%20toggle");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(100);
            conn.setRequestMethod("GET");
            int responseCode;

            conn.setDoOutput(true);

            //conn.setRequestProperty("Content-Type", "application/json");
            //int responseCode  = conn.getResponseCode();
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
            String body = new String();
            writer.write(body);
            //Sending the data to the server - This much is enough to send data to server
            //But to read the response of the server, you will have to implement the procedure below
            writer.flush();
            Log.i("custom_check", body);
            responseCode = conn.getResponseCode();

            Log.i("response_code", Integer.toString(responseCode));


            if (responseCode >= 200 && responseCode < 400) {
                // Create an InputStream in order to extract the response object
                //InputStream is = conn.getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                pingfoxIp = true;
                    /* userLocalStore.SetUserLoggedIn(true);
                    String line;
                    while ((line = reader.readLine()) != null) {
                        //Read till there is something available
                        sb.append(line + "\n");     //Reading and saving line by line - not all at once
                    }
                    line = sb.toString();           //Saving complete data received in string, you can do it differently
                    JSONObject jsonObject = new JSONObject(line);
                    //Just check to the values received in Logcat
                    Log.i("custom_check", "The values received in the store part are as follows:");
                    Log.i("rever", line);
                    Log.i("response_code", Integer.toString(responseCode));*/


            } else {
                pingfoxIp = false;
                //InputStream is = conn.getErrorStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    //Read till there is something available
                    sb.append(line + "\n");     //Reading and saving line by line - not all at once
                }
                line = sb.toString();
                //Saving complete data received in string, you can do it differently
                JSONObject jsonObject = new JSONObject(line);
                //jsonErrorArray = jsonObject.getJSONArray("errors");
                //JSONObject jsonErrorObject = jsonErrorArray.getJSONObject(0);
                //errorMessage = jsonObject.getString("message");
                //errorCodeString = jsonObject.getString("code");

                //Just check to the values received in Logcat
                //Toast.makeText(Register.this, "there is some error", Toast.LENGTH_SHORT).show();


                Log.i("custom_check", "The values received in the store part are as follows:");
                Log.i("custom_check", line);
                //Log.i("custom_check", errorMessage);
                Log.i("Response_Code", Integer.toString(responseCode));
                return false;

            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pingfoxIp;


    }


}
