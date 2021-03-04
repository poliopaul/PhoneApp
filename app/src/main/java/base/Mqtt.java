package base;

import android.content.Context;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class Mqtt implements MqttCallback, IMqttActionListener {
    public static final String BROKER_ADDRESS = "tcp://192.168.1.100:1883";

    private String clientId;
    private MqttAndroidClient client;
    private IMqttToken token;
    private Context context;
    private Toast toast;
    private HomeEvents event;

    private List<String> subscriptions = new ArrayList<String>();

    public Mqtt(Context context, HomeEvents event) {
        this.context = context.getApplicationContext();
        this.clientId = MqttClient.generateClientId();
        this.client = new MqttAndroidClient(this.context, BROKER_ADDRESS, clientId);
        this.toast = new Toast(this.context);
        this.event = event;
    }

    public void initialize() {
        client.setCallback(this); // callback needs to be set before connecting to broker

        try {
            token = client.connect();
            token.setActionCallback(this);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {

    }

    @Override
    public void connectionLost(Throwable cause) {
        String err = "Lost connection with MQTT broker :(\n";
        System.out.printf(err);
        this.toast.makeText(context, err, Toast.LENGTH_LONG);
    }

    @Override
    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
        String err = "Failed to connect to MQTT broker :(\n";
        System.out.printf(err);
        this.toast.makeText(context, err, Toast.LENGTH_LONG);
    }

    @Override
    public void onSuccess(IMqttToken asyncActionToken) {
        System.out.printf("Connected to MQTT broker on address: %s!\n", BROKER_ADDRESS);

        String cfgSubscription = "home/general/config/output";
        System.out.printf("Subscribing to: [%s]\n", cfgSubscription);
        subscribe(cfgSubscription);
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        String msg = new String(message.getPayload());
        System.out.printf("%s >> %s\n", topic, msg);

        String[] tmp = topic.split("/");
        String location = tmp[1];
        String device = tmp[2];
        String variable = tmp[3];

        if(device.equals("config")) event.configInit(msg);
        else if(variable.equals("pwm")) event.pwmTrigger(location, device, msg);
        else if(device.equals("mode")) event.modeTrigger(location, msg);
        else if(variable.equals("temperature")) event.temperatureUpdate(device, msg);
        else if(variable.equals("brightness")) event.brightnessUpdate(device, msg);
    }

    public void publish(String topic, String payload) {
        publish(topic, payload, false);
    }

    public void publish(String topic, String payload, boolean retained) {
        byte[] encodedPayload = new byte[0];

        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            message.setRetained(retained);
            client.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String subscription) {

        if(subscriptions.contains(subscription)) {
            return;
        }

        subscriptions.add(subscription);

        int qos = 1;
        try {
            IMqttToken subToken = client.subscribe(subscription, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The message was published
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void stopSubscription(String subscription) {

        if(!subscriptions.contains(subscription)) {
            return;
        }

        try {
            IMqttToken unsubToken = client.unsubscribe(subscription);
            unsubToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // The subscription could successfully be removed from the client
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    // some error occurred, this is very unlikely as even if the client
                    // did not had a subscription to the topic the unsubscribe action
                    // will be successfully
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void stopAllSubscriptions() {
        for(String subscription : subscriptions) {
            stopSubscription(subscription);
        }
    }

    public void disconnect() {
        try {
            token = client.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

}