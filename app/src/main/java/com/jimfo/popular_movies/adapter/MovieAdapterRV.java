package com.jimfo.popular_movies.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jimfo.popular_movies.ItemClickListener;
import com.jimfo.popular_movies.R;
import com.jimfo.popular_movies.model.AdjustableImageView;
import com.jimfo.popular_movies.model.Film;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapterRV extends RecyclerView.Adapter<MovieAdapterRV.CustomViewHolder> {

    private static final String TAG = MovieAdapterRV.class.getSimpleName();

    private ArrayList<Film> mFilms;
    private Context mContext;
    private ItemClickListener mListener;
    private LayoutInflater mInflater;

    public MovieAdapterRV(Context context, ArrayList<Film> films) {
        this.mFilms = films;
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }


    @Override
    public MovieAdapterRV.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.grid_item, parent, false);
        return new CustomViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieAdapterRV.CustomViewHolder holder, int position) {
        Film film = mFilms.get(position);

        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        final float scale = mContext.getResources().getDisplayMetrics().density;
        int pixels = (int) (width * scale + 0.5f);
        int mWidth;
        int mHeight;

        if (mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mWidth = (pixels / 2);
            mHeight = (int) ((pixels / 2) * 1.5f);
        }
        else {
            width = (int) (displayMetrics.widthPixels / displayMetrics.density);
            pixels = (int) (width * scale + 0.5f);
            mWidth = (pixels / 3);
            mHeight = (int) ((pixels / 3) * 1.5f);
        }

        holder.aiv.setLayoutParams(new RecyclerView.LayoutParams(mWidth, mHeight));
        Picasso.with(mContext).load(film.getmMoviePoster()).into(holder.aiv);
    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public AdjustableImageView aiv;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.aiv = itemView.findViewById(R.id.movie_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onClick(view, getAdapterPosition());
            }
        }
    }
}
