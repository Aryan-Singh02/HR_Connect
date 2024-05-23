package Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.ChatWindow;
import com.example.connect.DashboardHR;
import com.example.connect.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import Fragments.ChatFragment;
import Models.EmployeeDetail;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.viewholder> {
    ChatFragment chatFragment;
    ArrayList<EmployeeDetail> employeeList;

    public UserAdapter(ArrayList<EmployeeDetail> employeeList) {
//        this.chatFragment = chatFragment;
        this.employeeList = employeeList;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item,parent,false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.viewholder holder, int position) {
        EmployeeDetail employeeDetail = employeeList.get(position);
        holder.username.setText(employeeDetail.getName());
        holder.userstatus.setText(employeeDetail.getGetStatus());
//        Picasso.get().load(employeeDetail.profilepic).into(holder.userImg);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatWindow.class);
                intent.putExtra("name",employeeDetail.getName());
                intent.putExtra("uid", employeeDetail.getUserID());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {

        CircleImageView userImg;
        TextView username;
        TextView userstatus;
        public viewholder(@NonNull View itemView) {
            super(itemView);

            userImg = itemView.findViewById(R.id.userImg);
            username = itemView.findViewById(R.id.username);
            userstatus = itemView.findViewById(R.id.userstatus);

        }
    }
}
