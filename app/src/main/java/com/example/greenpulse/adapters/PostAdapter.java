package com.example.greenpulse.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.greenpulse.R;
import com.example.greenpulse.responses.AllPostResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{

    Context context;
    List<AllPostResponse.Datum>postItems;

    public PostAdapter(Context context, List<AllPostResponse.Datum> postItems) {
        this.context = context;
        this.postItems = postItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AllPostResponse.Datum postItem = postItems.get(position);
        holder.title.setText(postItem.title);
        Picasso.get().load(postItem.image).into(holder.postImage);
        holder.username.setText(postItem.userId+"");
        holder.createdAt.setText(postItem.createdAt.toString());
        holder.commentCount.setText(postItem.commentCount);
    }

    @Override
    public int getItemCount() {
        return postItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView userDP;
        ImageView postImage;
        TextView username,createdAt,title,commentCount;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userDP = itemView.findViewById(R.id.profile_image_post_item);
            commentCount = itemView.findViewById(R.id.tv_comment_count);
            username = itemView.findViewById(R.id.tv_user_name_post_item);
            createdAt = itemView.findViewById(R.id.pub_date_tv_post_item);
            title = itemView.findViewById(R.id.title_post_item);
            postImage = itemView.findViewById(R.id.post_image_post_item);
        }
    }
}
