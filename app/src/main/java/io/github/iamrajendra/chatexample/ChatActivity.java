package io.github.iamrajendra.chatexample;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class ChatActivity extends AppCompatActivity {
    private ChatSocket socket;
    private TextView textView_msg;
    private EditText editText_send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView_msg  = findViewById(R.id.chat_text_tv);
        editText_send  = findViewById(R.id.chat_text_et);

        socket  = new ChatSocket(this);
        socket.setUrl("http://192.168.1.6:3000/").forceNewConnection(false).
                reConnection(false).secure(false).init();
        socket.setCallback(new ChatSocket.Callback() {
            @Override
            public void onMessage(String msg) {
                textView_msg.append(msg+"\n");
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socket.sendMessage(editText_send.getText().toString());
                editText_send.setText("");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chat, menu);
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
