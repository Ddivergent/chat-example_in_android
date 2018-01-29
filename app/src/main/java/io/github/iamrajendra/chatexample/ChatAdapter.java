package io.github.iamrajendra.chatexample;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rajendra on 29/1/18.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatHolder> {
    private static final int CONSTANT_LEFT = 0;
    private static final int CONSTANT_RIGHT = 1;
    private String TAG = ChatAdapter.class.getSimpleName();
    private Activity activity;
    private List<ChatModel> list;
    private String myId;

    public ChatAdapter(Activity activity) {
        this.activity = activity;
        list = new ArrayList<>();
        Application application = (Application) activity.getApplication();
        myId = application.getId();
    }

    @Override
    public ChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(activity);
        View view = null;
        switch (viewType) {
            case CONSTANT_LEFT:
                Log.i(TAG, "onCreateViewHolder: left");


                view = layoutInflater.inflate(R.layout.chat_left, null);
                break;
            case CONSTANT_RIGHT:
                Log.i(TAG, "onCreateViewHolder: left");

                view = layoutInflater.inflate(R.layout.chat_right, null);
                break;
        }
        return new ChatHolder(view);
    }


    @Override
    public void onBindViewHolder(ChatHolder holder, int position) {
        holder.textView_chatMsg.setText(list.get(position).getMsg());

    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).getId().equals(myId)) {

            return CONSTANT_LEFT;
        } else {
            return CONSTANT_RIGHT;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    void addItem(ChatModel chatModel) {
        list.add(chatModel);
        notifyDataSetChanged();

    }


    public class ChatHolder extends RecyclerView.ViewHolder {
        private TextView textView_chatMsg;

        public ChatHolder(View itemView) {
            super(itemView);
            textView_chatMsg = itemView.findViewById(R.id.chat_text_tv);

        }
    }
}
