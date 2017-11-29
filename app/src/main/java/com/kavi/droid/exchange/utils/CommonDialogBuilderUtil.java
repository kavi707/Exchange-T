package com.kavi.droid.exchange.utils;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kavi.droid.exchange.R;

/**
 * Created by kavi707 on 11/28/17.
 * @author Kavimal Wijewardana <kavi707@gmail.com>
 */

public class CommonDialogBuilderUtil {

    private Context context;

    private String title = null;
    private String contentMsg = null;
    private String firstActionName = null;
    private String secondActionName = null;
    private boolean isFirstActionAvailable = false;
    private boolean isSecondActionAvailable = false;
    private FirstActionInterface firstActionImpl;
    private SecondActionInterface secondActionImpl;

    public interface FirstActionInterface {
        void firstAction();
    }

    public interface SecondActionInterface {
        void secondAction();
    }

    public CommonDialogBuilderUtil(Context context) {
        this.context = context;
    }

    public CommonDialogBuilderUtil title(String title) {
        this.title = title;
        return this;
    }

    public CommonDialogBuilderUtil content(String content) {
        this.contentMsg = content;
        return this;
    }

    public CommonDialogBuilderUtil setFirstActionListener(String actionBtnName, FirstActionInterface firstActionInf) {
        this.isFirstActionAvailable = true;
        this.firstActionName = actionBtnName;
        this.firstActionImpl = firstActionInf;
        return this;
    }

    public CommonDialogBuilderUtil setSecondActionListener(String actionBtnName, SecondActionInterface secondActionInf) {
        this.isSecondActionAvailable = true;
        this.secondActionName = actionBtnName;
        this.secondActionImpl = secondActionInf;
        return this;
    }

    public Dialog build() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_comon);

        TextView titleTextView = (TextView) dialog.findViewById(R.id.titleTextView);
        TextView msgTextView = (TextView) dialog.findViewById(R.id.msgTextView);
        final Button firstActionBtn = (Button) dialog.findViewById(R.id.firstActionButton);
        Button secondActionBtn = (Button) dialog.findViewById(R.id.secondActionButton);

        if (this.title != null)
            titleTextView.setText(this.title);

        if (this.contentMsg != null)
            msgTextView.setText(this.contentMsg);

        if (isFirstActionAvailable) {
            firstActionBtn.setVisibility(View.VISIBLE);
            firstActionBtn.setText(this.firstActionName);
            firstActionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firstActionImpl.firstAction();
                    dialog.dismiss();
                }
            });
        } else {
            firstActionBtn.setVisibility(View.GONE);
        }

        if (isSecondActionAvailable) {
            secondActionBtn.setVisibility(View.VISIBLE);
            secondActionBtn.setText(this.secondActionName);
            secondActionBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    secondActionImpl.secondAction();
                    dialog.dismiss();
                }
            });
        } else {
            secondActionBtn.setVisibility(View.GONE);
        }

        return dialog;
    }
}
