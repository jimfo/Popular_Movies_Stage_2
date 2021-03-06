package com.jimfo.popular_movies;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jimfo.popular_movies.data.AppDatabase;
import com.jimfo.popular_movies.databinding.ActivityDetailBinding;
import com.jimfo.popular_movies.model.Film;
import com.jimfo.popular_movies.model.Review;
import com.jimfo.popular_movies.model.Trailer;
import com.jimfo.popular_movies.utils.GeneralUtils;
import com.jimfo.popular_movies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

import static com.jimfo.popular_movies.utils.GeneralUtils.getDAWxH;
import static com.jimfo.popular_movies.utils.GeneralUtils.getTrailerWidthAndHieght;

public class DetailActivity extends AppCompatActivity {

    private static final String TRAILER_KEY = "trailers";
    private static final String REVIEW_KEY = "reviews";
    private static final String MOVIE_KEY = "movie";

    private ActivityDetailBinding mBinding;
    private AppDatabase mDb;
    private boolean mSaved;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    private TextView reviewTV;
    private ImageView trailerIV;
    private Context mContext;
    private int reviewPosition = 0;
    private int trailerPosition = 0;
    private String youtubeURL;
    private String youtubeFront;
    private String youtubeBack;
    private Film movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);
        reviewTV = findViewById(R.id.review_tv);
        trailerIV = findViewById(R.id.trailer_iv);
        mContext = getApplicationContext();
        youtubeURL = this.getString(R.string.baseYoutubeUrl);
        youtubeFront = this.getString(R.string.youtubeImg);
        youtubeBack = this.getString(R.string.youtubeIdx);

        // Get info from intent
        Intent i = getIntent();
        Bundle mExtras = i.getExtras();
        if (mExtras != null && mExtras.containsKey(this.getResources().getString(R.string.movieKey))) {

            movie = mExtras.getParcelable(this.getResources().getString(R.string.movieKey));

            if (movie != null) {
                DetailViewModelFactory factory = new DetailViewModelFactory(mDb, movie.getmId());
                final DetailViewModel viewModel
                        = ViewModelProviders.of(this, factory).get(DetailViewModel.class);
                viewModel.getMovie().observe(this, new Observer<Film>() {
                    @Override
                    public void onChanged(@Nullable Film film) {
                        viewModel.getMovie().removeObserver(this);
                        if (film != null) {
                            movie = film;
                            mSaved = true;
                            setHeartState(true);
                        } else {
                            mSaved = false;
                            setHeartState(false);
                        }
                    }
                });

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Fade fade = new Fade();
                    View decor = getWindow().getDecorView();
                    fade.excludeTarget(decor.findViewById(R.id.action_bar_container), true);
                    fade.excludeTarget(android.R.id.statusBarBackground, true);
                    fade.excludeTarget(android.R.id.navigationBarBackground, true);

                    getWindow().setEnterTransition(fade);
                    getWindow().setExitTransition(fade);
                }

                displayInfo(movie);

                if (null != savedInstanceState) {
                    mReviews = savedInstanceState.getParcelableArrayList(REVIEW_KEY);
                    mTrailers = savedInstanceState.getParcelableArrayList(TRAILER_KEY);
                } else {
                    getReviews();
                    getTrailers();
                }

            }
        }
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putParcelable(MOVIE_KEY, movie);
        savedInstanceState.putParcelableArrayList(REVIEW_KEY, mReviews);
        savedInstanceState.putParcelableArrayList(TRAILER_KEY, mTrailers);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        movie = savedInstanceState.getParcelable(MOVIE_KEY);
        mReviews = savedInstanceState.getParcelableArrayList(REVIEW_KEY);
        mTrailers = savedInstanceState.getParcelableArrayList(TRAILER_KEY);
    }

    public void getReviews() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mReviews = new ArrayList<>(NetworkUtils.fetchReviews(mContext, movie.getmId()));
            }
        });

    }

    public void getTrailers() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                mTrailers = new ArrayList<>(NetworkUtils.fetchTrailers(mContext, movie.getmId()));
            }
        });
    }

    /**
     * Purpose : Display Film data in various views
     *
     * @param film : A Film object
     */
    public void displayInfo(Film film) {
        int[] wxh = getDAWxH(this);

        mBinding.setFilm(film);

        mBinding.backDrop.setLayoutParams(new RelativeLayout.LayoutParams(wxh[2], wxh[3]));

        if (null == film.getmBackdrop()) {
            Picasso.with(this).load(R.drawable.npa).into(mBinding.backDrop);
        } else {
            Picasso.with(this).load(film.getmBackdrop()).into(mBinding.backDrop);
        }

        mBinding.moviePoster.setLayoutParams(new RelativeLayout.LayoutParams(wxh[0], wxh[1]));
        Picasso.with(this).load(film.getmMoviePoster()).into(mBinding.moviePoster);

        if (null != film.getmBackdrop()) {
            loadImage(film.getmBackdrop());
        } else {
            setColors(R.color.colorPrimaryDark);
        }

        setTitle(film.getmTitle());
    }

    /**
     * Display the backdrop image and capture the bitmap
     *
     * @param path : Backdrop image path
     * @see com.jimfo.popular_movies.utils.GeneralUtils#getDominantColor(Bitmap)
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

                // Unused required method
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

                // Unused required method
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

        mBinding.leftBtn.setBackgroundColor(color);
        mBinding.rightBtn.setBackgroundColor(color);
        mBinding.reviewLabel.setBackgroundColor(color);
        mBinding.trailerLabel.setBackgroundColor(color);
        mBinding.rightTrailerBtn.setBackgroundColor(color);
        mBinding.leftTrailerBtn.setBackgroundColor(color);

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

    public void setHeartState(boolean s) {
        if (s) {
            mBinding.saveIcon.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_favorite_solid, 0);
        } else {
            mBinding.saveIcon.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings_da, menu);
        return true;
    }

    public void updateDb(boolean saved) {
        if (saved) {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().insertMovie(movie);
                }
            });

            setHeartState(mSaved);
        } else {
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    mDb.movieDao().deleteMovie(movie);
                }
            });
            setHeartState(mSaved);
        }

    }

    public void onReviewChange(View v) {

        reviewTV.setVisibility(View.VISIBLE);

        switch (v.getId()) {
            case R.id.leftBtn:
                reviewPosition--;
                reviewPosition = setPosition(reviewPosition, mReviews.size());
                reviewTV.setText(mReviews.get(reviewPosition).toString());
                break;

            case R.id.rightBtn:
                reviewPosition++;
                reviewPosition = setPosition(reviewPosition, mReviews.size());
                reviewTV.setText(mReviews.get(reviewPosition).toString());
                break;

            default:
                break;

        }
    }

    public void onTrailerChange(View v) {

        trailerIV.setVisibility(View.VISIBLE);

        int[] wxh = getTrailerWidthAndHieght(mContext);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wxh[0], wxh[1]);
        layoutParams.gravity = Gravity.CENTER;
        trailerIV.setLayoutParams(layoutParams);

        if (mTrailers.size() != 0) {
            switch (v.getId()) {

                case R.id.leftTrailerBtn:
                    trailerPosition--;
                    trailerPosition = setPosition(trailerPosition, mTrailers.size());
                    Picasso.with(this).load(youtubeFront + mTrailers.get(trailerPosition).getmKey() + youtubeBack)
                            .into(trailerIV);

                    break;
                case R.id.rightTrailerBtn:
                    trailerPosition++;
                    trailerPosition = setPosition(trailerPosition, mTrailers.size());
                    Picasso.with(this).load(youtubeFront + mTrailers.get(trailerPosition).getmKey() + youtubeBack)
                            .into(trailerIV);
                    break;

                default:
                    break;
            }
        } else {
            Toast.makeText(this, "No Trailers Available", Toast.LENGTH_LONG).show();
        }
    }

    public void removeWidget(View v) {

        switch (v.getId()) {

            case R.id.review_label:
                reviewTV.setVisibility(View.GONE);
                break;

            case R.id.trailer_label:
                trailerIV.setVisibility(View.GONE);
                break;

            default:
                break;
        }
    }

    public void playVideo(View v) {
        Intent browse = new Intent("android.intent.action.VIEW",
                Uri.parse(youtubeURL + mTrailers.get(trailerPosition).getmKey())
        );
        startActivity(browse);
    }

    public int setPosition(int pos, int size) {

        int newPosition = pos;

        if ((pos == 0 && size == 0) || (pos == 0 && size == 1)) {
            newPosition = 0;
        } else if (pos > size - 1) {
            newPosition = 0;
        } else if (pos < 0) {
            newPosition = size - 1;
        }

        return newPosition;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_favorite:
                mSaved = !mSaved;
                updateDb(mSaved);
                return true;

            default: {
                return super.onOptionsItemSelected(item);
            }
        }
    }
}

