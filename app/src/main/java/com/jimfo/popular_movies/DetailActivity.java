package com.jimfo.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jimfo.popular_movies.databinding.ActivityDetailBinding;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.GeneralUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import static com.jimfo.popular_movies.utils.GeneralUtils.getDAWxH;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = DetailActivity.class.getSimpleName();
    private static final String PREFERENCES = "PREFS";
    private static final String STATE_SAVED = "SAVED";
    private static final String STATE_ID = "ID";
    private boolean saved = false;
    private SharedPreferences prefs;

    ActivityDetailBinding mBinding;
    Bundle mExtras;
    Film movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        prefs = this.getSharedPreferences(PREFERENCES,0);
        checkPreferences();

        // Get info from intent
        Intent i = getIntent();
        mExtras = i.getExtras();
        if (mExtras != null) {
            if (mExtras.containsKey(this.getResources().getString(R.string.movieKey))) {
                movie = mExtras.getParcelable(this.getResources().getString(R.string.movieKey));
                if (movie != null) {
                    displayInfo(movie);
                }
            }
        }


    }

    /**
     * Purpose     : Display Film data in various views
     * @param film : A Film object
     */
    public void displayInfo(Film film){
        int[] wxh = getDAWxH(this);

        mBinding.setFilm(film);
       // mBinding.movieTitle.setText(getResources().getString(R.string.title_space,film.getmTitle()));
       // mBinding.movieRelease.setText(getResources().getString(R.string.release,film.getmReleaseDate()));
       // mBinding.movieRating.setText(getResources().getString(R.string.out_of,film.getmVoteAverage()));
       // mBinding.plotSummary.setText(film.getmPlotSummary());
       // mBinding.movieLanguage.setText(getResources().getString(R.string.release,film.getmLanguage()));

        mBinding.moviePoster.setLayoutParams(new RelativeLayout.LayoutParams(wxh[0], wxh[1]));
        Picasso.with(this).load(film.getmMoviePoster()).into(mBinding.moviePoster);

        mBinding.backDrop.setLayoutParams(new RelativeLayout.LayoutParams(wxh[2], wxh[3]));
        Picasso.with(this).load(film.getmBackdrop()).into(mBinding.backDrop);

        loadImage(film.getmBackdrop());

        setTitle(film.getmTitle());
    }

    /**
     * Display the backdrop image and capture the bitmap
     * @see com.jimfo.popular_movies.utils.GeneralUtils#getDominantColor(Bitmap)
     * @param path : Backdrop image path
     */
    public void loadImage(String path) {
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                assert mBinding.backDrop != null;

                setColors(GeneralUtils.getDominantColor(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        };
        mBinding.backDrop.setTag(target);
        Picasso.with(this).load(path).into(target);
    }

    /**
     * Purpose : To set the color of various views
     *
     * @param color : The color
     * @see com.jimfo.popular_movies.utils.GeneralUtils#getDominantColor(Bitmap)
     */
    private void setColors(int color) {

        mBinding.reviewsBtn.setBackgroundColor(color);
        mBinding.trailersBtn.setBackgroundColor(color);

        Window window = this.getWindow();

        if (Build.VERSION.SDK_INT >= 21) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(color);
            ActionBar bar = getSupportActionBar();
            if (bar != null)
                bar.setBackgroundDrawable(new ColorDrawable(color));
        }
    }

    public void setHeartState(boolean s){
        if (s) {
            mBinding.saveIcon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_favorite_solid, 0);
        }
        else{
            mBinding.saveIcon.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_da, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.action_favorite:
                SharedPreferences.Editor editor = prefs.edit();
                saved = !saved;
                editor.putBoolean(STATE_SAVED, saved);
                editor.putString(STATE_ID, movie.getmId());
                editor.apply();
                setHeartState(saved);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void checkPreferences() {

        prefs = getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);

        if (prefs.contains(STATE_SAVED)) {
            setHeartState(prefs.getBoolean(STATE_SAVED, saved));
        }
    }

}
