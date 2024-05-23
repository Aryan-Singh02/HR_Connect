package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.connect.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private Context mContext;
    private List<Integer> mImageUrls;

    public ImageAdapter(Context context, List<Integer> imageUrls) {
        mContext = context;
        mImageUrls = imageUrls;
    }


    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.holiday_card,parent,false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ImageViewHolder holder, int position) {
            int imageURl = mImageUrls.get(position);
            Picasso.get().load(imageURl).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return mImageUrls.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.holiday_cards);
        }
    }
}
