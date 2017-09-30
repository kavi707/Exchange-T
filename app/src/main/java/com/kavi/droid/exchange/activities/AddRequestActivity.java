package com.kavi.droid.exchange.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.models.Destination;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;
import com.kavi.droid.exchange.services.sharedPreferences.SharedPreferenceManager;
import com.kavi.droid.exchange.utils.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
    private EditText reqNoteEditText;

    private Spinner typeSelectSpinner;
    private Spinner ticketCountSpinner;
    private Spinner destinationSpinner;

    private Button submitReqBtn;
    private Button cancelReqBtn;

    private Context context = this;
    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour, selectedMinute;

    private ImageLoadingManager imageLoadingManager;
    private List<String> destinationNameList;

    private CommonUtils commonUtils = new CommonUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);

        initDestinationList();
        setCurrentDateTime();

        setUpViews();
    }

    private void setUpViews() {

        imageLoadingManager = new ImageLoadingManager(context);

        profilePicImageView = (ImageView) findViewById(R.id.profilePicImageView);
        nameTextView = (TextView) findViewById(R.id.nameTextView);

        phoneNumEditText = (EditText) findViewById(R.id.phoneNumEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        reqNoteEditText = (EditText) findViewById(R.id.reqNoteEditText);

        typeSelectSpinner = (Spinner) findViewById(R.id.typeSelectSpinner);
        ticketCountSpinner = (Spinner) findViewById(R.id.ticketCountSpinner);
        destinationSpinner = (Spinner) findViewById(R.id.destinationSpinner);

        submitReqBtn = (Button) findViewById(R.id.submitReqBtn);
        cancelReqBtn = (Button) findViewById(R.id.cancelReqBtn);

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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, destinationNameList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(dataAdapter);

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

        cancelReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        submitReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = nameTextView.getText().toString();
                String contactNum = phoneNumEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String type = typeSelectSpinner.getSelectedItem().toString();
                String qty = ticketCountSpinner.getSelectedItem().toString();
                String selectedDestination = destinationSpinner.getSelectedItem().toString();
                String ticketDate = ticketDateTextView.getText().toString();
                String ticketTime = ticketTimeTextView.getText().toString();
                String reqNote = reqNoteEditText.getText().toString();

                TicketRequest ticketRequest = new TicketRequest();
                ticketRequest.setName(name);
                ticketRequest.setUserPicUrl(SharedPreferenceManager.getLoggedUserImageUrl(context));
                ticketRequest.setReqDescription(reqNote);
                ticketRequest.setTicketDate(ticketDate);
                ticketRequest.setTicketTime(ticketTime);
                ticketRequest.setPhoneNo(contactNum);
                ticketRequest.setEmail(email);

                if (!qty.equals("Select Qty")) {
                    int ticketQty = Integer.parseInt(qty);
                    ticketRequest.setQty(ticketQty);
                }

                ticketRequest.setReqType(commonUtils.getTypeFromName(type));
                ticketRequest.setStartToEnd(commonUtils.getDestinationFromName(selectedDestination));
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

    private void initDestinationList() {
        destinationNameList = new ArrayList<>();
        destinationNameList.add("COLOMBO - KANDY");
        destinationNameList.add("KANDY - COLOMBO");
        destinationNameList.add("COLOMBO - BADULLA");
        destinationNameList.add("BADULLA - COLOMBO");
        destinationNameList.add("COLOMBO - KURUNEGALA");
        destinationNameList.add("KURUNEGALA - COLOMBO");
        destinationNameList.add("COLOMBO - ANURADHAPURA");
        destinationNameList.add("ANURADHAPURA - COLOMBO");
        destinationNameList.add("COLOMBO - JAFNA");
        destinationNameList.add("JAFNA - COLOMBO");
        destinationNameList.add("COLOMBO - VAUNIYA");
        destinationNameList.add("VAUNIYA - COLOMBO");
    }
}