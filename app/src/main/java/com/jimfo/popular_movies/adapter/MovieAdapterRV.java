package com.jimfo.popular_movies.adapter;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jimfo.popular_movies.R;
import com.jimfo.popular_movies.model.AdjustableImageView;
import com.jimfo.popular_movies.model.Film;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.jimfo.popular_movies.utils.GeneralUtils.getWxH;

public class MovieAdapterRV extends RecyclerView.Adapter<MovieAdapterRV.CustomViewHolder> {

    private List<Film> mFilms;
    private Context mContext;
    private ItemClickListener mListener;
    private LayoutInflater mInflater;

    /**
     * Purpose : Constructor
     *
     * @param context : Context of the calling Activity
     * @param films   : ArrayList<Film> films
     */
    public MovieAdapterRV(Context context, ItemClickListener listener, List<Film> films) {
        this.mFilms = films;
        this.mContext = context;
        this.mListener = listener;
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
     * @param holder   : Custom ViewHolder
     * @param position : Index of the item
     */
    @Override
    public void onBindViewHolder(final MovieAdapterRV.CustomViewHolder holder, final int position) {

        Film film = mFilms.get(position);
        int[] wxh = getWxH(mContext);

        holder.aiv.setLayoutParams(new RecyclerView.LayoutParams(wxh[0], wxh[1]));

        Picasso.with(mContext).load(film.getmMoviePoster()).into(holder.aiv);

        ViewCompat.setTransitionName(holder.aiv, ViewCompat.getTransitionName(holder.aiv));
    }

    @Override
    public int getItemCount() {
        return mFilms.size();
    }

    public List<Film> getMovies() {
        return mFilms;
    }

    public void setMovies(List<Film> movies) {
        mFilms = new ArrayList<>(movies);
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId, ImageView iv);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AdjustableImageView aiv;

        CustomViewHolder(View itemView) {
            super(itemView);
            this.aiv = itemView.findViewById(R.id.movie_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            mListener.onItemClickListener(position, aiv);
        }
    }
}
