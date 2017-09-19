package com.kavi.droid.exchange.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kwijewardana on 9/15/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class AddRequestActivity extends Activity {

    private ImageView profilePicImageView;

    private TextView nameTextView;
    private TextView ticketDateTextView;
    private TextView ticketTimeTextView;

    private EditText phoneNumEditText;
    private EditText emailEditText;

    private Context context = this;
    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;

    private ImageLoadingManager imageLoadingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        setUpViews();
        setCurrentDateTime();
    }

    private void setUpViews() {

        imageLoadingManager = new ImageLoadingManager(context);

        profilePicImageView = (ImageView) findViewById(R.id.profilePicImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);
        phoneNumEditText = (EditText) findViewById(R.id.phoneNumEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        if(SharedPreferenceManager.isUserLogIn(context)) {
            imageLoadingManager.loadImageToImageView(SharedPreferenceManager.getLoggedUserImageUrl(context),
                    profilePicImageView, true);
            if (SharedPreferenceManager.getLoggedUserName(context) != null)
                nameTextView.setText(SharedPreferenceManager.getLoggedUserName(context));
            if (SharedPreferenceManager.getLoggedUserNumber(context) != null)
                phoneNumEditText.setText(SharedPreferenceManager.getLoggedUserNumber(context));
            if (SharedPreferenceManager.getLoggedUserEmail(context) != null)
                emailEditText.setText(SharedPreferenceManager.getLoggedUserEmail(context));
        }

        ticketDateTextView = (TextView) findViewById(R.id.ticketDateTextView);
        ticketDateTextView.setKeyListener(null);
        ticketDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ticketDateTextView.setText(orderDate(year, month + 1, day));
                    }
                }, selectedYear, selectedMonth, selectedDay).show();
            }
        });

        ticketTimeTextView = (TextView) findViewById(R.id.ticketTimeTextView);
        ticketTimeTextView.setKeyListener(null);
        ticketTimeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                        String ampm, hourString, minuteString;

                        if(hour > 12) {
                            hour = hour - 12;
                            ampm = "PM";
                        } else {
                            ampm = "AM";
                        }

                        if (hour < 10) {
                            hourString = "0" + hour;
                        } else {
                            hourString = String.valueOf(hour);
                        }

                        if (minute < 10) {
                            minuteString = "0" + minute;
                        } else {
                            minuteString = String.valueOf(minute);
                        }

                        ticketTimeTextView.setText(hourString + ":" + minuteString + " " + ampm);
                    }
                }, selectedHour, selectedMinute, true).show();
            }
        });
    }

    private void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();

        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);

        selectedHour = calendar.get(Calendar.HOUR);
        selectedMinute = calendar.get(Calendar.MINUTE);
    }

    private String orderDate(int year, int month, int day) {

        String dateString = null;
        try {
            String givenDate = year + "-" + month + "-" + day;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(givenDate);

            dateFormat = new SimpleDateFormat("MMM-dd-yyyy");

            dateString = dateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return dateString;
    }
}
