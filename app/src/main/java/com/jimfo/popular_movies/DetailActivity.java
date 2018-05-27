package com.jimfo.popular_movies;

import android.content.Intent;
import android.databinding.DataBindingUtil;
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
import android.widget.RelativeLayout;

import com.jimfo.popular_movies.databinding.ActivityDetailBinding;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.utils.Colors;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

public class DetailActivity extends AppCompatActivity {


    private static final String TAG = DetailActivity.class.getSimpleName();

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

    public void displayInfo(Film film){
        int[] wxh = getWxH();

        mBinding.movieTitle.setText(film.getmTitle());
        mBinding.movieRelease.setText(film.getmReleaseDate().substring(0, 4));
        mBinding.movieRating.setText(createRatingString(film.getmVoteAverage()));
        mBinding.plotSummary.setText(film.getmPlotSummary());

        mBinding.moviePoster.setLayoutParams(new RelativeLayout.LayoutParams(wxh[0], wxh[1]));
        Picasso.with(this).load(film.getmMoviePoster()).into(mBinding.moviePoster);

        mBinding.backDrop.setLayoutParams(new RelativeLayout.LayoutParams(wxh[2], wxh[3]));
        Picasso.with(this).load(film.getmBackdrop()).into(mBinding.backDrop);

        loadImage(film.getmBackdrop());

        setTitle(film.getmTitle());
    }

    public SpannableString createRatingString(String va){
        String rating = va + getResources().getString(R.string.out_of);
        SpannableString ss1 = new SpannableString(rating);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, ss1.length() - 3, 0);
        return ss1;
    }

    public int[] getWxH(){

        int[] WxH = new int[4];
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        final float scale = this.getResources().getDisplayMetrics().density;
        int pixels = (int) (width * scale + 0.5f);
        WxH[0] = (pixels / 3);
        WxH[1] = (int) ((pixels / 3) * 1.25f);
        WxH[2] = pixels;
        WxH[3] = (pixels / 2);

        return WxH;
    }

    public void loadImage(String path) {
        final Target target = new Target() {
            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                assert mBinding.backDrop != null;

                mBinding.backDrop.setImageBitmap(bitmap);

                setColors(Colors.getDominantColor(bitmap));
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
     * Purpose : to get the average color of the image
     *
     * @param color : The color
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
