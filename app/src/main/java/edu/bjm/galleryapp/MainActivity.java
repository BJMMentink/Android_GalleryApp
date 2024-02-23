package edu.bjm.galleryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener{
    public static final String TAG = "MainActivity";
    Game[] games = {
            new Game("Red Dead Redemption 2", ""),
            new Game("Prey", ""),
            new Game("Resident Evil 4 Remake", ""),
    };
    int[] imgs = {
            R.drawable.rdr2,
            R.drawable.prey,
            R.drawable.re4,
            R.drawable.rdr2_2,
            R.drawable.prey_2,
            R.drawable.re4_2,
    };
    int[] textfiles = {
            R.raw.rdr2,
            R.raw.prey,
            R.raw.re4,
    };
    int galleryNo = 0;
    boolean isFront = true;
    ImageView imgGallery;
    TextView tvGallery;
    GestureDetector gestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgGallery = findViewById(R.id.imageView);
        tvGallery = findViewById(R.id.tvInfo);
        updateToNextCard();


        gestureDetector = new GestureDetector(this, this);
        Log.d(TAG, "onCreate: Complete");
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.gallery_menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: ");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_select_game) {
            return true;
        } else if (id == R.id.game_rdr2) {
            galleryNo = 0;
            updateToNextCard();
            return true;
        } else if (id == R.id.game_prey) {
            galleryNo = 1;
            updateToNextCard();
            return true;
        } else if (id == R.id.game_re4) {
            galleryNo = 2;
            updateToNextCard();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void updateToNextCard(){
        games[galleryNo].setCapital(readFile(textfiles[galleryNo]));
        isFront = true;
        imgGallery.setImageResource(imgs[galleryNo]);
        tvGallery.setText(games[galleryNo].getName());
    }
    private String readFile(int fileId){
        InputStream inputStream;
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        StringBuffer stringBuffer;
        try {
            inputStream = getResources().openRawResource(fileId);
            inputStreamReader = new InputStreamReader(inputStream);
            bufferedReader = new BufferedReader(inputStreamReader);
            stringBuffer = new StringBuffer();

            String data;

            while((data = bufferedReader.readLine()) != null){
                stringBuffer.append(data).append("\n");

            }
            // cleanup objects
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            Log.d(TAG, "readFile: " + stringBuffer.toString());
            return stringBuffer.toString();
        }catch (Exception ex){
            Log.d(TAG, "readFile: " + ex.getMessage());
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
    @Override
    public  boolean onTouchEvent(MotionEvent motionEvent){

        return gestureDetector.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        Log.d(TAG, "onDown: ");
        return false;
    }

    @Override
    public void onShowPress(@NonNull MotionEvent e) {
        Log.d(TAG, "onShowPress: ");

    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent motionEvent) {
        Log.d(TAG, "onSingleTapUp: ");
        String message;

        try {
            Animation fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    try {
                        if(isFront){
                            imgGallery.setImageResource(imgs[galleryNo + 3]);
                            tvGallery.setTextSize(getResources().getDimension(R.dimen.back_text_size));
                            tvGallery.setText(games[galleryNo].getDescription());
                            tvGallery.setTextColor(getResources().getColor(R.color.back_color));
                        }
                        else {
                            imgGallery.setImageResource(imgs[galleryNo]);
                            tvGallery.setTextSize(getResources().getDimension(R.dimen.front_text_size));
                            tvGallery.setText(games[galleryNo].getName());
                            tvGallery.setTextColor(getResources().getColor(R.color.front_color));
                        }
                        isFront = !isFront;

                        Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
                        imgGallery.startAnimation(fadeIn);
                        tvGallery.startAnimation(fadeIn);

                    } catch (Exception e){
                        Log.e(TAG, "onSingleTapUp: " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });

            // Apply fade out animation to views
            imgGallery.startAnimation(fadeOut);
            tvGallery.startAnimation(fadeOut);

            Log.d(TAG, "onSingleTapUp: " );
        } catch (Exception e){
            Log.e(TAG, "onSingleTapUp: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean onScroll(@Nullable MotionEvent e1, @NonNull MotionEvent e2, float distanceX, float distanceY) {
        Log.d(TAG, "onScroll: ");
        return false;
    }

    @Override
    public void onLongPress(@NonNull MotionEvent e) {
        Log.d(TAG, "onLongPress: ");
    }

    @Override
    public boolean onFling(@Nullable MotionEvent motionEvent1, @NonNull MotionEvent motionEvent2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling: ");

        int numCards = games.length;
        try {
            int x1 = (int) (motionEvent1 != null ? motionEvent1.getX() : 0);
            int x2 = (int) motionEvent2.getX();

            Animation move;
            if (x1 < x2) {
                move = AnimationUtils.loadAnimation(this, R.anim.moveright);
                // swipe right
                Log.d(TAG, "onFling: Right");
                galleryNo = (galleryNo - 1 + numCards) % numCards;

            } else {
                move = AnimationUtils.loadAnimation(this, R.anim.moveleft);
                // swipe left
                Log.d(TAG, "onFling: Left");
                galleryNo = (galleryNo + 1) % numCards;
            }

            move.setAnimationListener(new AnimationListener());
            imgGallery.startAnimation(move);
            tvGallery.startAnimation(move);

        } catch (Exception ex) {
            Log.e(TAG, "onFling: " + ex.getMessage());
            ex.printStackTrace();
        }

        return false;
    }


    private class AnimationListener implements Animation.AnimationListener{

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

            updateToNextCard();
            tvGallery.setTextSize(getResources().getDimension(R.dimen.front_text_size));
            tvGallery.setTextColor(getResources().getColor(R.color.front_color));


            Log.d(TAG, "onAnimationEnd: ");
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }


}