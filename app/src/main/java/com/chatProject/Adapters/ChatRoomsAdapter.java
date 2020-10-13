package com.chatProject.Adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chatProject.Chat.FireBaseUtils.Model.Room;
import com.chatProject.Chat.R;

import java.util.List;


public class ChatRoomsAdapter  extends RecyclerView.Adapter<ChatRoomsAdapter.ViewHolder> {

    List<Room> roomsList ;
    OnItemClickListener onItemClickListener;

    public ChatRoomsAdapter(List<com.chatProject.Chat.FireBaseUtils.Model.Room> roomsList) {
        this.roomsList = roomsList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.chat_room_item_view,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int pos) {
        final com.chatProject.Chat.FireBaseUtils.Model.Room room = roomsList.get(pos);
        viewHolder.name.setText(room.getName());
        viewHolder.desc.setText(room.getDescription());
        if(onItemClickListener!=null)
            viewHolder.itemView
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onItemClickListener.onItemClick(pos,room);
                        }
                    });
    }

   public void changeData(List<com.chatProject.Chat.FireBaseUtils.Model.Room> newRooms){
        this.roomsList =newRooms;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if(roomsList==null)
            return 0;
        return roomsList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView name,desc;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name= itemView.findViewById(R.id.name);
            desc = itemView.findViewById(R.id.desc);
        }
    }
    public Room getRoomAtPos(int pos){
        return roomsList.get(pos);
    }

    public interface OnItemClickListener{
        void onItemClick(int pos, com.chatProject.Chat.FireBaseUtils.Model.Room room);
    }

}
