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
import com.jimfo.popular_movies.utils.GeneralUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.jimfo.popular_movies.utils.GeneralUtils.getWxH;

public class MovieAdapterRV extends RecyclerView.Adapter<MovieAdapterRV.CustomViewHolder> {

    private static final String TAG = MovieAdapterRV.class.getSimpleName();

    private ArrayList<Film> mFilms;
    private Context mContext;
    private ItemClickListener mListener;
    private LayoutInflater mInflater;

    /**
     * Purpose : Constructor
     * @param context : Context of the calling Activity
     * @param films   : ArrayList<Film></Film>
     */
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

    /**
     * Purpose : Update the contents of the itemView to reflect the item at the given position.
     *
     * @param holder : Custom ViewHolder
     * @param position : Index of the item
     */
    @Override
    public void onBindViewHolder(MovieAdapterRV.CustomViewHolder holder, int position) {
        Film film = mFilms.get(position);
        int[] wxh = getWxH(mContext);

        holder.aiv.setLayoutParams(new RecyclerView.LayoutParams(wxh[0], wxh[1]));
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

        AdjustableImageView aiv;

        CustomViewHolder(View itemView) {
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
