package com.chatProject.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chatProject.Chat.FireBaseUtils.Model.Message;
import com.chatProject.Chat.FireBaseUtils.Model.Room;
import com.chatProject.Chat.R;

import java.util.ArrayList;
import java.util.List;


public class ChatThreadAdapter extends RecyclerView.Adapter<ChatThreadAdapter.ViewHolder> {

    List<Message> messages ;
    OnItemClickListener onItemClickListener;
    String userId;

    public ChatThreadAdapter(List<Message> messages,String userId) {
        this.messages = messages;
        this.userId =userId;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    final int outgoing =1;
    final int incoming=2;
    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSenderId().equals(userId)){
            return outgoing;
        }
        return incoming;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View view =null;
        if(viewType==incoming) {
           view= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item_message_incoming, viewGroup, false);
           return new IncomingViewHolder(view);
        }else if(viewType==outgoing){
            view= LayoutInflater.from(viewGroup.getContext())
                    .inflate(R.layout.card_item_message_outgoing, viewGroup, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int pos) {

        int viewType = getItemViewType(pos);
        Message message = messages.get(pos);
        viewHolder.message.setText(message.getContent());
        viewHolder.date.setText(message.getSentAt());

        if(viewType==incoming){
            ((IncomingViewHolder) viewHolder)
                    .senderName.setText(message.getSenderName());

        }

    }

   public void changeData(List<Message> messages){
        this.messages =messages;
        notifyDataSetChanged();
    }
    public void inserNewItem(Message message){
        if(messages==null) messages = new ArrayList<>();

        messages.add(message);
        notifyItemInserted(messages.size()-1);
    }

    @Override
    public int getItemCount() {
        if(messages==null)
            return 0;
        return messages.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView message,date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            message= itemView.findViewById(R.id.message);
            date = itemView.findViewById(R.id.date);
        }
    }

    class IncomingViewHolder extends ViewHolder{
        TextView senderName;
        public IncomingViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName=itemView.findViewById(R.id.sender_name);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int pos, Room room);
    }

}
