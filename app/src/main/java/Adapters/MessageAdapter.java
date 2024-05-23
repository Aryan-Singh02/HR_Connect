package Adapters;

import static com.example.connect.ChatWindow.receiverIImg;
import static com.example.connect.ChatWindow.senderImg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import Models.MessageClass;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter {

    Context context;
    ArrayList<MessageClass> messagesAdapterArrayList;
    int ITEM_SEND = 1;
    int ITEM_RECEIVE = 2;

    public MessageAdapter(Context context, ArrayList<MessageClass> messagesAdapterArrayList) {
        this.context = context;
        this.messagesAdapterArrayList = messagesAdapterArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == ITEM_SEND){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sender_layout, parent,false);
            return new senderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reciver_layout,parent,false);
            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MessageClass messages = messagesAdapterArrayList.get(position);
        if(holder.getClass() == senderViewHolder.class){
            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
//            Picasso.get().load(senderImg).into(viewHolder.circleImageView);
        }
        else {
            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.msgtxt.setText(messages.getMessage());
//            Picasso.get().load(receiverIImg).into(viewHolder.circleImageView);
        }
    }

    @Override
    public int getItemCount() {
        return messagesAdapterArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
        MessageClass messages = messagesAdapterArrayList.get(position);
        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(messages.getSenderId())) {
            return ITEM_SEND;
        } else {
            return ITEM_RECEIVE;
        }
    }
    class senderViewHolder extends RecyclerView.ViewHolder {

        CircleImageView circleImageView;
        TextView msgtxt;
        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.profilerggg);
            msgtxt = itemView.findViewById(R.id.msgsendertyp);
        }
    }

    class receiverViewHolder extends RecyclerView.ViewHolder{
        CircleImageView circleImageView;
        TextView msgtxt;
        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);

            circleImageView = itemView.findViewById(R.id.pro);
            msgtxt = itemView.findViewById(R.id.recivertextset);
        }
    }
}

