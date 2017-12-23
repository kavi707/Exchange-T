package com.kavi.droid.exchange.viewHolders;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kavi.droid.exchange.R;

/**
 * Created by pl36586 on 12/22/17.
 */

public class RequestItemViewHolder extends RecyclerView.ViewHolder {

    public CardView requestCardView;
    public RelativeLayout requestTileHolder;
    public ImageView userPicImageView;
    public TextView reqTypeTextView;
    public TextView reqStartEndTextView;
    public TextView reqDescriptionTextView;
    public TextView timeAgoTextView;
    public TextView ticketDateTextView;
    public TextView ticketDayTextView;
    public TextView ticketTimeTextView;

    public RequestItemViewHolder(View itemView) {
        super(itemView);

        requestCardView = (CardView) itemView.findViewById(R.id.requestCardView);
        requestTileHolder = (RelativeLayout) itemView.findViewById(R.id.requestTileHolder);
        userPicImageView = (ImageView) itemView.findViewById(R.id.userImageView);
        reqTypeTextView = (TextView) itemView.findViewById(R.id.reqTypeTextView);
        reqStartEndTextView = (TextView) itemView.findViewById(R.id.reqStartEndTextView);
        reqDescriptionTextView = (TextView) itemView.findViewById(R.id.reqDescriptionTextView);
        timeAgoTextView = (TextView) itemView.findViewById(R.id.timeAgoTextView);
        ticketDateTextView = (TextView) itemView.findViewById(R.id.ticketDateTextView);
        ticketDayTextView = (TextView) itemView.findViewById(R.id.ticketDayTextView);
        ticketTimeTextView = (TextView) itemView.findViewById(R.id.ticketTimeTextView);
    }
}
