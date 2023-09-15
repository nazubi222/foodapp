package com.example.foodorderapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.PhotoBanner;

import java.util.List;

public class PhotoBannerAdapter extends RecyclerView.Adapter<PhotoBannerAdapter.PhotoBannerViewHolder>{
    private List<PhotoBanner> listPhotoBanner;

    public PhotoBannerAdapter(List<PhotoBanner> listPhotoBanner) {
        this.listPhotoBanner = listPhotoBanner;
    }

    @NonNull
    @Override
    public PhotoBannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_photo_banner, parent, false);
        return new PhotoBannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoBannerViewHolder holder, int position) {
        PhotoBanner photoBanner = listPhotoBanner.get(position);
        if(photoBanner == null){
            return;
        }
        holder.imageView.setImageResource(photoBanner.getResourceId());


    }

    @Override
    public int getItemCount() {
        if(listPhotoBanner != null){
            return listPhotoBanner.size();
        }
        return 0;
    }

    public class PhotoBannerViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageView;

        public PhotoBannerViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imghomephoto_item);
        }
    }
}
