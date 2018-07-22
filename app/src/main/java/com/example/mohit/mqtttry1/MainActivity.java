package com.example.mohit.mqtttry1;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    String clientId = MqttClient.generateClientId();
    MqttAndroidClient client ;
    String SdeviceStatus;
    Boolean BdeviceOn ;
    ImageView deviceStatusImage ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        deviceStatusImage =(ImageView) findViewById(R.id.DeviceStatusImage);

        client = new MqttAndroidClient(MainActivity.this.getApplicationContext(),"tcp://m11.cloudmqtt.com:15121",
                clientId);
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName("rdqlgagy");
        options.setPassword("V4-dlT_EKFEe".toCharArray());

        try {
            IMqttToken token = client.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.d("TAG", "onSuccess");
                    try {
                        subscribe(client,"stat/sonoff1/POWER",1);
                    } catch (MqttException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems

                    Log.d("TAG", "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }


        BdeviceOn = getDeviceStatus();

    }


        public void subscribe(@NonNull MqttAndroidClient client,
        @NonNull final String topic, int qos) throws MqttException {
            IMqttToken token = client.subscribe(topic, qos);
            token.setActionCallback(new IMqttActionListener() {

                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    Log.d("TAG", "Subscribe Successfully " + topic);
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    Log.e("TAG", "Subscribe Failed " + topic);
                }
            });
        }



    private Boolean getDeviceStatus() {
       return null;
    }


    public void buttonClick(View view) {

            client = new MqttAndroidClient(MainActivity.this.getApplicationContext(),"tcp://m11.cloudmqtt.com:15121",
                            clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("rdqlgagy");
            options.setPassword("V4-dlT_EKFEe".toCharArray());

            try {
                IMqttToken token = client.connect(options);
                token.setActionCallback(new IMqttActionListener() {
                    @Override
                    public void onSuccess(IMqttToken asyncActionToken) {
                        // We are connected
                        Log.d("TAG", "onSuccess");
                    }

                    @Override
                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                        // Something went wrong e.g. connection timeout or firewall problems

                        Log.d("TAG", "onFailure");

                    }
                });
            } catch (MqttException e) {
                e.printStackTrace();
            }
        }

    public void turnOn(View view) {

        String topic = "cmnd/sonoff1/power";
        String payload = "toggle";
        Log.d("TAG", "toggle pressed");
        byte[] encodedPayload = new byte[0];
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }

        client.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                
            }

            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                Log.d("msg", new String(message.getPayload()));
                Toast.makeText(MainActivity.this, new String(message.getPayload()), Toast.LENGTH_SHORT).show();
                SdeviceStatus= new String(message.getPayload());
                if (SdeviceStatus.equals("ON")){
                    BdeviceOn = true;
                    deviceStatusImage.setImageResource(R.drawable.bulb_on);
                }
                if (SdeviceStatus.equals("OFF")){
                    BdeviceOn = false;
                    deviceStatusImage.setImageResource(R.drawable.bulb_off);
                }

            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }
        });

    }

    public void login(View view) {

        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
}
