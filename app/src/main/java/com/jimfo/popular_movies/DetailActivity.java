package com.jimfo.popular_movies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.Colors;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailActivity extends AppCompatActivity {


    private static final String TAG = DetailActivity.class.getSimpleName();

    Bundle mExtras;
    ImageView moviePoster, backDrop;
    Button reviewsBtn, trailersBtn;
    TextView plot_summary, reviewsTv;
    TextView movieTitle, movieRelease, movieRating;
    RelativeLayout relLay;
    Film movie;
    String selection = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String title;
        String releaseDate;
        String voteAverage;
        String plotSummary = "";
        String moviePath = "";
        String backdropPath = "";

        moviePoster = findViewById(R.id.movie_poster);
        backDrop = findViewById(R.id.back_drop);
        plot_summary = findViewById(R.id.plot_summary);
        relLay = findViewById(R.id.rel_lay);
        movieTitle = findViewById(R.id.movie_title);
        movieRelease = findViewById(R.id.movie_release);
        movieRating = findViewById(R.id.movie_rating);
        reviewsBtn = findViewById(R.id.reviews_btn);
        trailersBtn = findViewById(R.id.trailers_btn);

        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (width * scale + 0.5f);
        int pWidth = (pixels / 3);
        int pHeight = (int) ((pixels / 3) * 1.25f);
        int bHeight = (pixels / 2);
        SpannableString ss1;

        Intent i = getIntent();
        mExtras = i.getExtras();

        if (mExtras != null) {
            if (mExtras.containsKey(this.getResources().getString(R.string.movieKey))) {
                movie = mExtras.getParcelable(this.getResources().getString(R.string.movieKey));
                if (movie != null) {
                    title = movie.getmTitle();
                    releaseDate = movie.getmReleaseDate().substring(0, 4);
                    voteAverage = movie.getmVoteAverage();
                    plotSummary = movie.getmPlotSummary();
                    moviePath = movie.getmMoviePoster();
                    backdropPath = movie.getmBackdrop();
                    setTitle(title);

                    movieTitle.setText(title);
                    movieRelease.setText(releaseDate);
                    String rating = voteAverage + getResources().getString(R.string.out_of);
                    ss1 = new SpannableString(rating);
                    ss1.setSpan(new RelativeSizeSpan(2f), 0, ss1.length() - 3, 0);
                    movieRating.setText(ss1);
                }
            }
            if (mExtras.containsKey(this.getResources().getString(R.string.extrasKey))) {
                selection = mExtras.getString(this.getResources().getString(R.string.extrasKey));
            }
        }

        moviePoster.setLayoutParams(new RelativeLayout.LayoutParams(pWidth, pHeight));
        backDrop.setLayoutParams(new RelativeLayout.LayoutParams(pixels, bHeight));

        Picasso.with(this).load(moviePath).into(moviePoster);
        loadImage(backdropPath);
        Picasso.with(this).load(backdropPath).into(backDrop);
        plot_summary.setText(plotSummary);
    }

    public void loadImage(String path) {
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                assert backDrop != null;

                backDrop.setImageBitmap(bitmap);

                setColors(Colors.getDominantColor(bitmap));
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        backDrop.setTag(target);
        Picasso.with(this).load(path).into(target);

    }

    /**
     * Purpose : to get the average color of the image
     *
     * @param color : The color
     */
    private void setColors(int color) {

        reviewsBtn.setBackgroundColor(color);
        trailersBtn.setBackgroundColor(color);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
