package com.wesleydungan.stroopeffectchallenge;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;


public class MainActivity extends Activity
{
  MainActivity context = this;

  TextView progress_value_text_view;
  TextView time_value_text_view;
  public TextView color_name_text_view;

  Button red_button;
  Button green_button;
  Button blue_button;
  Button white_button;
  Button yellow_button;
  Button orange_button;

  int progress;

  Handler handler;
  boolean running;
  boolean accept_click;
  long start_time;

  Random rand;
  int last_color_index;
  int last_name_index;
  int color_button_id;


  View.OnClickListener onClickListener = new View.OnClickListener()
  {
    @Override
    public void onClick(final View v)
    {
      if (running && accept_click && (color_button_id == v.getId()))
      {
        // correct click
        progress += 1;

        if (progress > 30)
        {
          running = false;

          long time = System.currentTimeMillis() - start_time;
          String time_string = StartActivity.formattedTimeString(time);

          time_value_text_view.setText(time_string);

          String dialog_message = String.format(getString(R.string.done), time_string);

          long best_time = StartActivity.getBestTime();

          if (time < best_time)
          {
            StartActivity.setBestTime(time);

            if (best_time < 600000)
            {
              String best_time_string = StartActivity.formattedTimeString(best_time);
              dialog_message += " ";
              dialog_message += String.format(getString(R.string.new_best_time),
                      best_time_string);
            }
          }

          AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
          alertDialogBuilder.setCancelable(false);
          alertDialogBuilder.setMessage(dialog_message);

          alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener()
          {
            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
              finish();
            }
          });

          AlertDialog alertDialog = alertDialogBuilder.create();
          alertDialog.show();
        }
        else
        {
          progress_value_text_view.setText(String.format("%d/30", progress));
          nextChallenge();
        }
      }
      else
      {
        // incorrect click
        if (accept_click)
        {
          Animation wrong_click_anim = AnimationUtils.loadAnimation(context, R.anim.wrong_click);

          wrong_click_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation)
            {
              // the user won't be able to click again until the animation is over
              accept_click = false;
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
              accept_click = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
          });

          color_name_text_view.startAnimation(wrong_click_anim);
        }
      }
    }
  };


  Runnable runnable = new Runnable()
  {
    @Override
    public void run()
    {
      if (running)
      {
        long duration = System.currentTimeMillis() - start_time;
        String time_string = StartActivity.formattedTimeString(duration);
        time_value_text_view.setText(time_string);
        handler.postDelayed(runnable, 100);
      }
    }
  };


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    progress_value_text_view = (TextView) findViewById(R.id.progressValueTextView);
    time_value_text_view = (TextView) findViewById(R.id.timeValueTextView);

    color_name_text_view = (TextView) findViewById(R.id.colorNameTextView);

    red_button = (Button) findViewById(R.id.redButton);
    red_button.setOnClickListener(onClickListener);

    green_button = (Button) findViewById(R.id.greenButton);
    green_button.setOnClickListener(onClickListener);

    blue_button = (Button) findViewById(R.id.blueButton);
    blue_button.setOnClickListener(onClickListener);

    white_button = (Button) findViewById(R.id.whiteButton);
    white_button.setOnClickListener(onClickListener);

    yellow_button = (Button) findViewById(R.id.yellowButton);
    yellow_button.setOnClickListener(onClickListener);

    orange_button = (Button) findViewById(R.id.orangeButton);
    orange_button.setOnClickListener(onClickListener);

    rand = new Random();
    last_color_index = -1;
    last_name_index = -1;
    progress = 0;
    nextChallenge();

    Animation first_word_anim = AnimationUtils.loadAnimation(context, R.anim.first_word);

    first_word_anim.setAnimationListener(new Animation.AnimationListener() {
      @Override
      public void onAnimationStart(Animation animation) {
        running = true;
        accept_click = true;
      }

      @Override
      public void onAnimationEnd(Animation animation)
      {
        handler = new Handler();
        start_time = System.currentTimeMillis();
        handler.postDelayed(runnable, 100);
      }

      @Override
      public void onAnimationRepeat(Animation animation) {}
    });

    color_name_text_view.startAnimation(first_word_anim);
  }


  public void nextChallenge()
  {
    Resources res = getResources();

    int new_color_index;

    do
    {
      // use 4 instead of 6 when the yellow and orange buttons are "gone"
      new_color_index = rand.nextInt(4);
    }
    while (new_color_index == last_color_index);

    switch (new_color_index)
    {
    case 0:
      // red
      color_button_id = R.id.redButton;
      color_name_text_view.setTextColor(res.getColor(R.color.red));
      break;
    case 1:
      // green
      color_button_id = R.id.greenButton;
      color_name_text_view.setTextColor(res.getColor(R.color.green));
      break;
    case 2:
      // blue
      color_button_id = R.id.blueButton;
      color_name_text_view.setTextColor(res.getColor(R.color.blue));
      break;
    case 3:
      // white
      color_button_id = R.id.whiteButton;
      color_name_text_view.setTextColor(res.getColor(R.color.white));
      break;
    case 4:
      // yellow
      color_button_id = R.id.yellowButton;
      color_name_text_view.setTextColor(res.getColor(R.color.yellow));
      break;
    case 5:
      // orange
      color_button_id = R.id.orangeButton;
      color_name_text_view.setTextColor(res.getColor(R.color.orange));
      break;
    }

    int new_name_index;

    do
    {
      // use 4 instead of 6 when the yellow and orange buttons are "gone"
      new_name_index = rand.nextInt(4);
    }
    while ((new_name_index == last_name_index) || (new_name_index == new_color_index));

    switch (new_name_index)
    {
    case 0:
      // red
      color_name_text_view.setText(getString(R.string.red).toUpperCase());
      break;
    case 1:
      // green
      color_name_text_view.setText(getString(R.string.green).toUpperCase());
      break;
    case 2:
      // blue
      color_name_text_view.setText(getString(R.string.blue).toUpperCase());
      break;
    case 3:
      // white
      color_name_text_view.setText(getString(R.string.white).toUpperCase());
      break;
    case 4:
      // yellow
      color_name_text_view.setText(getString(R.string.yellow).toUpperCase());
      break;
    case 5:
      // orange
      color_name_text_view.setText(getString(R.string.orange).toUpperCase());
      break;
    }

    last_color_index = new_color_index;
    last_name_index = new_name_index;
  }
}
