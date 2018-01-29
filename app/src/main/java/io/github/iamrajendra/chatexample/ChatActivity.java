package io.github.iamrajendra.chatexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerViewChat;
    private EditText editText_send;
    private Application application;

    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        application = (Application) getApplication();
        init();
        registerReceivers();

    }

    private void registerReceivers() {
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                ChatModel chatModel = new ChatModel();
                chatModel.setMsg(intent.getStringExtra("message"));
                chatModel.setId(intent.getStringExtra("id"));
                chatAdapter.addItem(chatModel);

//                textView_msg.append(intent.getStringExtra("message") + "\n" + intent.getStringExtra("id") + "\n");
            }
        }, new IntentFilter(ChatSocket.INTENT_ACTION_MESSAGE));
    }

    private void init() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerViewChat = findViewById(R.id.chat_rv);
        chatAdapter = new ChatAdapter(this);
        recyclerViewChat.setAdapter(chatAdapter);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this));
        editText_send = findViewById(R.id.chat_text_et);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.send_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.sendMessage(editText_send.getText().toString());
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
