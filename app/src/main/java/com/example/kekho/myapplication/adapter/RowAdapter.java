package com.example.kekho.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kekho.myapplication.R;
import com.example.kekho.myapplication.model.RowModel;

import java.util.ArrayList;

/**
 * Created by atbic on 2/9/2017.
 */

public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {
ArrayList<RowModel> rowModels;
    Context context;

    public RowAdapter(ArrayList<RowModel> rowModels, Context context) {
        this.rowModels = rowModels;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater= LayoutInflater.from(parent.getContext());
        View view=layoutInflater.inflate(R.layout.item_navi,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.txtTen.setText(rowModels.get(position).getTen());
        holder.imgLogo.setImageResource(rowModels.get(position).getAnhLogo());




    }

    @Override
    public int getItemCount() {
        return rowModels.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imgLogo;
        TextView txtTen;

       public ViewHolder(View itemView) {
           super(itemView);

           imgLogo=(ImageView)itemView.findViewById(R.id.imgLogo);

           txtTen=(TextView)itemView.findViewById(R.id.txtTen);



       }
   }
}
