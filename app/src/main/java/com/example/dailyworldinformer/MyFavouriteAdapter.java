package com.example.dailyworldinformer;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.dailyworldinformer.Model.Article;
import com.example.dailyworldinformer.Model.FavouriteAritcle;

import java.util.List;

public class MyFavouriteAdapter extends RecyclerView.Adapter<MyFavouriteAdapter.MyViewHolder> {

    Context context;
    List<FavouriteAritcle>favouriteAritcleList;

    public MyFavouriteAdapter(Context context, List<FavouriteAritcle> favouriteAritcleList) {
        this.context = context;
        this.favouriteAritcleList = favouriteAritcleList;
    }

    @NonNull
    @Override
    public MyFavouriteAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyFavouriteAdapter.MyViewHolder holder, int position) {
        final MyFavouriteAdapter.MyViewHolder holders = holder;

        FavouriteAritcle model = favouriteAritcleList.get(position);

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        Glide.with(context)
                .load(model.getImage())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {

                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                }).transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageView);
        holder.title.setText(model.getTitle());
        holder.desc.setText(model.getDescription());
        holder.source.setText(model.getSource());
        holder.time.setText(" \u2022 "+Utils.DateToTimeFormat(model.getPublishAt()));
        holder.publishAt.setText(Utils.DateFormat(model.getPublishAt()));
        holder.author.setText(model.getAuthor());
    }

    @Override
    public int getItemCount() {
        return favouriteAritcleList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title,desc,author,publishAt,source,time;
        ImageView imageView;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.titleId);
            desc = itemView.findViewById(R.id.description);
            author = itemView.findViewById(R.id.author);
            publishAt = itemView.findViewById(R.id.publishAt);
            source = itemView.findViewById(R.id.source);
            time = itemView.findViewById(R.id.timeId);


            imageView = itemView.findViewById(R.id.img);
            progressBar =itemView.findViewById(R.id.progress_load_photo);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

            Intent intent = new Intent(context,NewsDetailsActivity.class);

             FavouriteAritcle article = favouriteAritcleList.get(getAdapterPosition());
            intent.putExtra("url",article.getUrl());
            intent.putExtra("title",article.getTitle());
            intent.putExtra("img",article.getImage());
            intent.putExtra("date",article.getPublishAt());
            intent.putExtra("source",article.getSource());
            intent.putExtra("author",article.getAuthor());
            intent.putExtra("description",article.getDescription());
            context.startActivity(intent);

        }
    }
}
