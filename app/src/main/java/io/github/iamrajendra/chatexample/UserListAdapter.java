package io.github.iamrajendra.chatexample;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by rajendra on 28/1/18.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.User> {
    private List<UserModel> list;
    private Activity activity;

    public UserListAdapter(List<UserModel> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public UserListAdapter.User onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View view = inflater.inflate(R.layout.user_adapter, null);
        return new User(view);
    }

    @Override
    public void onBindViewHolder(UserListAdapter.User holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,ChatActivity.class);
                activity.startActivity(intent);
            }
        });
        holder.textView_name.setText(list.get(position).getName());
        holder.textView_online.setText(list.get(position).getStatus() ? "online" : "offline");

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void update(UserModel chatModel) {

        list.set(list.indexOf(chatModel), chatModel);
        notifyItemChanged(list.indexOf(chatModel));
    }

    public class User extends RecyclerView.ViewHolder {
        private TextView textView_name;
        private TextView textView_online;

        public User(View itemView) {
            super(itemView);
            textView_name = itemView.findViewById(R.id.name_tv);
            textView_online = itemView.findViewById(R.id.online_tv);
        }
    }
}
