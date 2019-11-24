package com.test.myapplication1123.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.test.myapplication1123.Models.AllMethods;
import com.test.myapplication1123.Models.Message;
import com.test.myapplication1123.R;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder>
{

    Context context;
    List<Message> messages;
    DatabaseReference messageDb;

    public MessageAdapter(Context context, List<Message> messages, DatabaseReference messageDb) {
        this.context = context;
        this.messages = messages;
        this.messageDb = messageDb;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message,parent,false);
        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {

        Message message = messages.get(position);

        if(message.getName().equals(AllMethods.name)){
            holder.tvTitle.setText("You"+message.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.l1.setBackgroundColor(Color.parseColor("#EF9E73"));
        }
        else
        {
            holder.tvTitle.setText(message.getName()+ ":" +message.getMessage());
            holder.tvTitle.setGravity(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder
    {
        TextView tvTitle;
        ImageButton ibDelete;
            LinearLayout l1;

        public MessageAdapterViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tvTitle=(TextView)itemView.findViewById(R.id.tvTitle);
            ibDelete=(ImageButton)itemView.findViewById(R.id.ibDelete);
            l1=(LinearLayout)itemView.findViewById(R.id.l1Message);

            ibDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    messageDb.child(messages.get(getAdapterPosition()).getKey()).removeValue();
                }
            });
        }
    }
}
