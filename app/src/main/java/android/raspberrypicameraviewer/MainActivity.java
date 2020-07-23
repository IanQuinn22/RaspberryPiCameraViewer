package android.raspberrypicameraviewer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;


import java.io.ObjectOutputStream;
import android.content.Context;
import java.net.InetAddress;
import java.net.Socket;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Toast;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.annotation.TargetApi;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;

public class MainActivity extends AppCompatActivity {

    private WebView video;
    private Button connect;
    private Handler handler;
    private ImageButton up;
    private ImageButton down;
    private ImageButton left;
    private ImageButton right;
    private EditText message;
    private Button send;
    private ObjectOutputStream out;
    private Socket socket;
    private String raspi_ip = "192.168.1.109";
    private int raspi_portnum = 12345;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Context context = this;
        super.onCreate(savedInstanceState);
        handler = new Handler();
        setContentView(R.layout.activity_main);

        video = findViewById(R.id.raspivid);
        connect = findViewById(R.id.connect_button);
        up = findViewById(R.id.uparrow);
        down = findViewById(R.id.downarrow);
        left = findViewById(R.id.leftarrow);
        right = findViewById(R.id.rightarrow);
        message = findViewById(R.id.message_text);
        send = findViewById(R.id.send_button);

        try{
            client = new Client(raspi_ip,raspi_portnum);
            client.start();

        } catch (Exception e){
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("Error! ").setMessage("Couldn't connect to server.").setNeutralButton("OK", null).create().show();
        }

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (connect.getText().toString().equalsIgnoreCase("Connect Video")) {
                    try{
                        video.getSettings().setJavaScriptEnabled(true);
                        video.setWebViewClient(new WebViewClient() {
                            @SuppressWarnings("deprecation")
                            @Override
                            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                                Toast.makeText(getApplicationContext(),description,Toast.LENGTH_SHORT).show();
                            }
                            @TargetApi(android.os.Build.VERSION_CODES.M)
                            @Override
                            public void onReceivedError(WebView view, WebResourceRequest req, WebResourceError rerr) {
                                onReceivedError(view, rerr.getErrorCode(), rerr.getDescription().toString(), req.getUrl().toString());
                            }
                        });
                        video.loadUrl("http://" + raspi_ip + ":8000");
                    } catch (Exception e){
                        Log.e("Error found here: ",e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try{
                            client.sendMessage();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                };
                thread.start();
            }
        });
    }

    private void closeConnection(){
        try{
            out.close();
            socket.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop(){
        super.onStop();
        closeConnection();
    }

    private class Client extends Thread {
        private String ip_address;
        private int port_number;

        public Client(String ipaddress,int portnum){
            this.ip_address = ipaddress;
            this.port_number = portnum;
        }

        @Override
        public void run() {
            super.run();
            connectToServer(ip_address,port_number);
        }

        public void connectToServer(String ipaddress, int portnum){
            try{
                socket = new Socket(InetAddress.getByName(ipaddress),portnum);
                out = new ObjectOutputStream(socket.getOutputStream());
                out.flush();
            }catch (Exception e){
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Error! ").setMessage("Couldn't connect to server.").setNeutralButton("OK", null).create().show();
            }
        }

        public void sendMessage(){
            String message_text = message.getText().toString();
            try{
                out.writeObject(message_text);
                out.flush();
            } catch (Exception e) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Error! ").setMessage("IO Exception.").setNeutralButton("OK", null).create().show();
            }
        }
    }
}
