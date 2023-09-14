package com.example.gallery;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHoler>{
    ArrayList<MainData> arrayList;

    public MainAdapter(ArrayList<MainData> arrayList) {
        this.arrayList = arrayList;
    }

    public class ViewHoler extends RecyclerView.ViewHolder {

        protected ImageView iv_picture;
        protected TextView tv_title,tv_date;
        public ViewHoler(@NonNull View itemView) {
            super(itemView);
            this.iv_picture = itemView.findViewById(R.id.iv);
            this.tv_title = itemView.findViewById(R.id.tv1);
            this.tv_date = itemView.findViewById(R.id.tv2);

        }
    }

    @NonNull
    @Override
    public MainAdapter.ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        ViewHoler holer = new ViewHoler(view);
        return holer;
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHoler holder, int position) {
        holder.iv_picture.setImageURI(arrayList.get(position).getPath());
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_date.setText(arrayList.get(position).getDate());

        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String curName = holder.tv_title.getText().toString();
                Toast.makeText(view.getContext(),curName,Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return  true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return  arrayList.size();
    }


}