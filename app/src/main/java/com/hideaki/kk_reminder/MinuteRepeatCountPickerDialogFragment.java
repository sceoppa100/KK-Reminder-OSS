package com.hideaki.kk_reminder;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.Locale;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.hideaki.kk_reminder.UtilClass.LOCALE;

public class MinuteRepeatCountPickerDialogFragment extends DialogFragment {

  private EditText count;

  @NonNull
  @Override
  public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

    View view = View.inflate(getContext(), R.layout.minute_repeat_count_picker, null);
    final MainActivity activity = (MainActivity)getActivity();
    checkNotNull(activity);

    count = view.findViewById(R.id.count);
    count.requestFocus();
    count.setText(String.valueOf(MainEditFragment.minuteRepeat.getOrg_count()));
    count.setSelection(count.getText().length());

    ImageView plus = view.findViewById(R.id.plus);
    plus.setColorFilter(activity.accent_color);
    GradientDrawable drawable = (GradientDrawable)plus.getBackground();
    drawable = (GradientDrawable)drawable.mutate();
    drawable.setStroke(3, activity.accent_color);
    drawable.setCornerRadius(8.0f);

    plus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if("".equals(count.getText().toString())) {
          count.setText(String.valueOf(MainEditFragment.minuteRepeat.getOrg_count()));
        }
        if(Integer.parseInt(count.getText().toString()) < 999) {
          count.setText(String.valueOf(Integer.parseInt(count.getText().toString()) + 1));
          count.setSelection(count.getText().length());
        }
      }
    });

    ImageView minus = view.findViewById(R.id.minus);
    minus.setColorFilter(activity.accent_color);
    drawable = (GradientDrawable)minus.getBackground();
    drawable = (GradientDrawable)drawable.mutate();
    drawable.setStroke(3, activity.accent_color);
    drawable.setCornerRadius(8.0f);

    minus.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if("".equals(count.getText().toString())) {
          count.setText(String.valueOf(MainEditFragment.minuteRepeat.getOrg_count()));
        }
        if(Integer.parseInt(count.getText().toString()) > 1) {
          count.setText(String.valueOf(Integer.parseInt(count.getText().toString()) - 1));
          count.setSelection(count.getText().length());
        }
      }
    });

    return new AlertDialog.Builder(activity)
        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {

            if("".equals(count.getText().toString())) {
              count.setText(String.valueOf(MainEditFragment.minuteRepeat.getOrg_count()));
            }
            int set_count = Integer.parseInt(count.getText().toString());
            if(set_count > 0) {
              MainEditFragment.minuteRepeat.setOrg_count(set_count);

              String interval = "";
              int hour = MainEditFragment.minuteRepeat.getHour();
              if(hour != 0) {
                interval += activity.getResources().getQuantityString(R.plurals.hour, hour, hour);
                if(!LOCALE.equals(Locale.JAPAN)) interval += " ";
              }
              int minute = MainEditFragment.minuteRepeat.getMinute();
              if(minute != 0) {
                interval += activity.getResources().getQuantityString(R.plurals.minute, minute, minute);
                if(!LOCALE.equals(Locale.JAPAN)) interval += " ";
              }
              int count = MainEditFragment.minuteRepeat.getOrg_count();
              String label = activity.getResources().getQuantityString(R.plurals.repeat_minute_count_format,
                  count, interval, count);
              MinuteRepeatEditFragment.label_str = label;
              MinuteRepeatEditFragment.label.setSummary(label);

              MainEditFragment.minuteRepeat.setLabel(label);

              //項目のタイトル部に現在の設定値を表示
              MinuteRepeatEditFragment.count_picker.setTitle(getResources().getQuantityString(R.plurals.times, count, count));
            }
          }
        })
        .setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {}
        })
        .setView(view)
        .create();
  }
}
