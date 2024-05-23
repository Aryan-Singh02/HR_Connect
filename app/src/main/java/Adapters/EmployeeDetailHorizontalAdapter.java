package Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.R;

import java.util.List;

import Models.EmployeeDetail;

public class EmployeeDetailHorizontalAdapter extends RecyclerView.Adapter<EmployeeDetailHorizontalAdapter.ViewHolder> {

    private List<EmployeeDetail> itemList;

    public EmployeeDetailHorizontalAdapter(List<EmployeeDetail> itemList){
        this.itemList = itemList;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_detail_card,parent,false);
         ViewHolder viewHolder = new ViewHolder(view);
         return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EmployeeDetail employeeDetail = itemList.get(position);
        holder.name.setText(employeeDetail.getName());
        holder.jobPosition.setText(employeeDetail.getJobPosition());
        holder.department.setText(employeeDetail.getDepartment());
        holder.contactDetail.setText(employeeDetail.getContactDetail());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView name, jobPosition, department, contactDetail;
        public EditText empid;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            jobPosition = itemView.findViewById(R.id.jobPosition);
            department = itemView.findViewById(R.id.department);
            contactDetail = itemView.findViewById(R.id.contactDetail);

            empid = itemView.findViewById(R.id.employeeId);
        }
    }
}
