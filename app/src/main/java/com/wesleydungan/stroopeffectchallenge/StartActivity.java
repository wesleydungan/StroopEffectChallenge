package com.wesleydungan.stroopeffectchallenge;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class StartActivity extends ActionBarActivity
{
  static Context context;

  TextView best_time_text_view;


  static long getBestTime()
  {
    SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(),
            Context.MODE_PRIVATE);
    return prefs.getLong("best_time", 600000);
  }


  static void setBestTime(long time)
  {
    SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(),
            Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putLong("best_time", time);
    editor.apply();
  }


  static void clearBestTime()
  {
    SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(),
            Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.remove("best_time");
    editor.apply();
  }


  static String formattedTimeString(long time)
  {
    if (time >= 600000)
    {
      return context.getString(R.string.best_time_default_value);
    }
    else
    {
      long minutes = time / 60000;
      long seconds = (time / 1000) % 60;
      long milliseconds = time % 10;
      return String.format("%dm%02d.%ds", minutes, seconds, milliseconds);
    }
  }


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_start);

    context = this;

    best_time_text_view = (TextView) findViewById(R.id.bestTimeTextView);
  }


  @Override
  protected void onResume()
  {
    String time_string = formattedTimeString(getBestTime());
    best_time_text_view.setText(time_string);
    super.onResume();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }


  @Override
  public boolean onOptionsItemSelected(MenuItem item)
  {
    switch (item.getItemId())
    {
      case R.id.action_clear_best_time:
        clearBestTime();
        String time_string = formattedTimeString(getBestTime());
        best_time_text_view.setText(time_string);
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }


  public void onExitClick(View view)
  {
    finish();
  }


  public void onStartClick(View view)
  {
    Intent intent = new Intent(this, MainActivity.class);
    startActivity(intent);
  }
}
