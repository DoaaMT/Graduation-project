package com.example.saudiestate.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saudiestate.Models.Estates;
import com.example.saudiestate.R;

import java.util.List;


/**
 * Created by Home on 11/7/2017.
 */

public class EstatesAdapter extends RecyclerView.Adapter<EstatesAdapter.MyViewHolder> {

    private List<Estates> basketList;
    Context context;
    android.support.v4.app.FragmentManager fragmentManager;

    public class MyViewHolder extends RecyclerView.ViewHolder {

TextView name_estates;
ImageView img_estates;
        public MyViewHolder(View view) {
            super(view);
            name_estates = view.findViewById(R.id.name_estates);
            img_estates = view.findViewById(R.id.img_estste);
        }
    }


    public EstatesAdapter(Context context, List<Estates> basketList, android.support.v4.app.FragmentManager fragmentManager) {
        this.context = context;
        this.basketList = basketList;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_estates, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Estates estates = basketList.get(position);
        holder.name_estates.setText(estates.Name);


    }

    @Override
    public int getItemCount() {
        return basketList.size();
    }
}