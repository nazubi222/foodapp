package com.example.foodorderapp.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodorderapp.R;
import com.example.foodorderapp.model.Location;
import com.example.foodorderapp.viewmodel.LocationUserViewModel;

import java.util.List;

public class LocationUserAdapter extends RecyclerView.Adapter<LocationUserAdapter.LocationUserViewHolder>{

    private List<Location> listLocation;
    private Context context;

    private IClickListener mIClickListener;
    private int mSelectedItem = -1;

    public interface IClickListener {
        void onClickDeleteLocation(Location location, String documentSnapshot);
        void onClickRadioBtLocation(Location location);
    }

    public LocationUserAdapter(Context context, List<Location> listLocation, IClickListener iClickListener){
        this.context = context;
        this.listLocation = listLocation;
        this.mIClickListener = iClickListener;
    }

//    public LocationUserAdapter(FragmentProfileLocation fragmentProfileLocation, List<Location> listLocation) {
//        this.fragmentProfileLocation = fragmentProfileLocation;
//        this.listLocation = listLocation;
//    }

    @NonNull
    @Override
    public LocationUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        LocationUserViewHolder viewHolder = new LocationUserViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationUserViewHolder holder,int position) {
        Location location = listLocation.get(position);
        if (location == null){
            return;
        }
        String city =listLocation.get(position).getCity();
        String districk =listLocation.get(position).getDistrict();
        String ward =listLocation.get(position).getWard();
        String other =listLocation.get(position).getOther();

        holder.tvProfileLocationOther.setText(other);
        holder.tvProfileLocationMain.setText(city + "/" + districk +"/" + ward);

        holder.radioButton.setChecked(position == mSelectedItem);
        int point = position;
        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedItem =point;
                notifyDataSetChanged();
                mIClickListener.onClickRadioBtLocation(location);

            }
        });

        holder.deleteLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickDeleteLocation(location, location.getId());
//                listLocation.remove(location);
//                notifyDataSetChanged();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Bạn có chắc chắn muốn xóa địa chỉ này không ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listLocation.remove(location);
                                notifyDataSetChanged();
                                Toast.makeText(context, "Xóa địa chỉ thành công!", Toast.LENGTH_SHORT).show();
//                                LocationUserViewModel locationUserViewModel= new LocationUserViewModel();
//                                final String documentSnapshot = location.getId();
//                                Log.d("location12", "Name: " +location.getCity() +
//                                        location.getDistrict() + location.getWard() + location.getOther() + "/"+location.getId());
//
//                                locationUserViewModel.deleteLocation(location, documentSnapshot);
//                                locationUserAdapter.notifyDataSetChanged();
//                                Toast.makeText(getActivity(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return listLocation == null ? 0 : listLocation.size();
    }


    public class LocationUserViewHolder extends RecyclerView.ViewHolder{
        private TextView tvProfileLocationOther, tvProfileLocationMain, deleteLocation;
        private RadioButton radioButton;

        public LocationUserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvProfileLocationMain = itemView.findViewById(R.id.tvprofile_locationmain);
            tvProfileLocationOther = itemView.findViewById(R.id.tvprofile_locationother);
            radioButton = itemView.findViewById(R.id.rdbt_location);
            deleteLocation = itemView.findViewById(R.id.tvdeleteLocation);
        }
    }
}
