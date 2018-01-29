package io.github.iamrajendra.chatexample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class UserList extends AppCompatActivity {

    private static final String TAG = UserList.class.getSimpleName();
    private RecyclerView recyclerView_list;
    private UserListAdapter userListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        init();
        resisterSocketReceivers();
    }

    private void resisterSocketReceivers() {
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i(TAG, "userlist: " + ((ArrayList<UserModel>) intent.getSerializableExtra("userlist")).size());

                List<UserModel> list = ((ArrayList<UserModel>) intent.getSerializableExtra("userlist"));

                userListAdapter = new UserListAdapter(list, UserList.this);
                recyclerView_list.setAdapter(userListAdapter);
            }
        }, new IntentFilter(ChatSocket.INTENT_ACTION_JOIN));


        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (userListAdapter!=null)
                userListAdapter.update((UserModel) intent.getParcelableExtra("user"));
            }
        }, new IntentFilter(ChatSocket.INTENT_ACTION_STATUS));
    }

    private void init() {
        recyclerView_list = findViewById(R.id.user_list_rv);
        recyclerView_list.setLayoutManager(new LinearLayoutManager(this));
    }

}
