package com.abhidas.myhealthnews;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    ArrayList<List_items> str;
    ArrayList<String>  content ;
    Context context;

    public RecyclerAdapter(Context context, ArrayList<List_items> str ,ArrayList<String>  content){
        this.str = str;
        this.context = context;
        this.content=content;
    }

    @NonNull
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.custom_design,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {

        holder.textView1.setText(str.get(position).title);
        String s=str.get(position).description;
        if(s==null|| s.equals("null") || s.equals("")){
            holder.textView2.setText("Click here !");
        } else {
            holder.textView2.setText(s);
        }

        // load image with glide
        Glide.with(context)
                .load(str.get(position).getImage())
                .into(holder.imageView1);

        holder.textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("content", content.get(position));
                context.startActivity(intent);
            }
        });
        holder.textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("content", content.get(position));
                context.startActivity(intent);
            }
        });
        holder.imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ArticleActivity.class);
                intent.putExtra("content", content.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return str.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView1;
        TextView textView2;
        ImageView imageView1;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1= itemView.findViewById(R.id.title);
            textView2= itemView.findViewById(R.id.description);
            imageView1 = itemView.findViewById(R.id.imageView);
        }
    }


}
