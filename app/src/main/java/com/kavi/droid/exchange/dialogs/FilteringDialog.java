package com.kavi.droid.exchange.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReq;
import com.kavi.droid.exchange.services.connections.dto.FilterTicketReqDate;
import com.kavi.droid.exchange.utils.CommonUtils;

import java.util.Calendar;
import java.util.List;

/**
 * Created by kavi707 on 12/17/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class FilteringDialog extends Dialog {

    private TextView ticketFromDateTextView;
    private TextView ticketToDateTextView;

    private Spinner destinationSpinner;
    private Spinner ticketCountSpinner;

    private Button filterActionButton;

    private Context context;
    private boolean isINeedSelected;
    private boolean isDateFilterSet = false;
    private List<String> destinationNameList;
    private List<String> qtyList;
    private int selectedYear, selectedMonth, selectedDay;

    private CommonUtils commonUtils = new CommonUtils();
    OnFilteringDialogResult onFilteringDialogResult;

    public FilteringDialog(@NonNull Context context, boolean isINeedSelected) {
        super(context);
        this.context = context;
        this.isINeedSelected = isINeedSelected;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_filtering);

        destinationNameList = commonUtils.initDestinationList();
        qtyList = commonUtils.initQtyList();

        setCurrentDateTime();
        setUpViews();
    }

    // Callback Interface
    public interface OnFilteringDialogResult {
        void filter(FilterTicketReq filterTicketReq);
    }

    public void setFilterDialogResult (OnFilteringDialogResult dialogResult) {
        onFilteringDialogResult = dialogResult;
    }

    private void setUpViews() {

        ticketFromDateTextView = (TextView) findViewById(R.id.ticketFromDateTextView);
        ticketToDateTextView = (TextView) findViewById(R.id.ticketToDateTextView);
        ticketToDateTextView.setEnabled(false);
        ticketToDateTextView.setHintTextColor(context.getResources().getColor(R.color.light_red));
        filterActionButton = (Button) findViewById(R.id.filterActionButton);

        destinationSpinner = (Spinner) findViewById(R.id.destinationSpinner);
        ArrayAdapter<String> destinationAdapter = new ArrayAdapter<>(context, R.layout.spinner_filter_item, destinationNameList);
        destinationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        destinationSpinner.setAdapter(destinationAdapter);

        ticketCountSpinner = (Spinner) findViewById(R.id.ticketCountSpinner);
        ArrayAdapter<String> qtyAdapter = new ArrayAdapter<>(context, R.layout.spinner_filter_item, qtyList);
        qtyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ticketCountSpinner.setAdapter(qtyAdapter);

        ticketFromDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ticketFromDateTextView.setText(commonUtils.orderDate(year, month + 1, day));

                        isDateFilterSet = true;

                        ticketToDateTextView.setEnabled(true);
                        ticketToDateTextView.setHintTextColor(context.getResources().getColor(R.color.light_green));
                    }
                }, selectedYear, selectedMonth, selectedDay).show();
            }
        });

        ticketToDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ticketToDateTextView.setText(commonUtils.orderDate(year, month + 1, day));
                    }
                }, selectedYear, selectedMonth, selectedDay).show();
            }
        });

        filterActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String qty = ticketCountSpinner.getSelectedItem().toString();
                String selectedDestination = destinationSpinner.getSelectedItem().toString();
                String ticketFromDateString = ticketFromDateTextView.getText().toString();
                String ticketToDateString = ticketToDateTextView.getText().toString();

                FilterTicketReq filterTicketReq = new FilterTicketReq();
                if (!qty.equals("Select Qty")) {
                    int ticketQty = Integer.parseInt(qty);
                    filterTicketReq.setQtyFilter(ticketQty);
                } else {
                    filterTicketReq.setQtyFilter(-1);
                }

                if (isINeedSelected) {
                    filterTicketReq.setTypeFilter(TicketRequest.I_NEED);
                } else {
                    filterTicketReq.setTypeFilter(TicketRequest.I_HAVE);
                }

                if (!selectedDestination.equals("Select Destination")) {
                    filterTicketReq.setDestinationFilter(commonUtils.getDestinationFromName(selectedDestination));
                } else {
                    filterTicketReq.setDestinationFilter(-1);
                }

                if (isDateFilterSet) {
                    FilterTicketReqDate filterTicketReqDate = new FilterTicketReqDate();

                    if (!ticketFromDateString.equals("Ticket Date")) {
                        filterTicketReqDate.setFromDateTimestamp(commonUtils.getTimestampFromDate(ticketFromDateString));
                    } else {
                        filterTicketReqDate.setToDateTimestamp(-1);
                    }

                    if (!ticketToDateString.equals("Ticket Date")) {
                        filterTicketReqDate.setToDateTimestamp(commonUtils.getTimestampFromDate(ticketToDateString));
                    } else {
                        filterTicketReqDate.setToDateTimestamp(-1);
                    }

                    filterTicketReq.setDateFilter(filterTicketReqDate);
                }

                onFilteringDialogResult.filter(filterTicketReq);
                dismiss();
            }
        });
    }

    private void setCurrentDateTime() {
        Calendar calendar = Calendar.getInstance();

        selectedYear = calendar.get(Calendar.YEAR);
        selectedMonth = calendar.get(Calendar.MONTH);
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
