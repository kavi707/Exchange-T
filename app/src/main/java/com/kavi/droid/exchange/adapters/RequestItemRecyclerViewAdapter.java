package com.kavi.droid.exchange.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kavi.droid.exchange.R;
import com.kavi.droid.exchange.models.TicketRequest;
import com.kavi.droid.exchange.services.imageLoader.ImageLoadingManager;

import java.util.List;

import static android.view.View.INVISIBLE;

/**
 * Created by pl36586 on 12/7/17.
 */

public class RequestItemRecyclerViewAdapter extends RecyclerView.Adapter<RequestItemRecyclerViewAdapter.RequestItemViewHolder> {

    private Context context;
    private List<TicketRequest> ticketRequestList;
    private ImageLoadingManager imageLoadingManager;

    public RequestItemRecyclerViewAdapter(List<TicketRequest> ticketRequestList, Context context) {
        this.context = context;
        this.ticketRequestList = ticketRequestList;
        imageLoadingManager = new ImageLoadingManager(context);
    }

    @Override
    public RequestItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_request_item, parent, false);
        RequestItemViewHolder requestItemViewHolder = new RequestItemViewHolder(v);
        return requestItemViewHolder;
    }

    @Override
    public void onBindViewHolder(RequestItemViewHolder holder, int position) {

        TicketRequest ticketRequest = ticketRequestList.get(position);

        imageLoadingManager.loadImageToImageView(ticketRequest.getUserPicUrl(), holder.userPicImageView, true);
        if (ticketRequest.getReqType() == TicketRequest.I_HAVE) {
            holder.reqTypeTextView.setText("I Have");
            holder.requestTileHolder.setBackgroundColor(context.getResources().getColor(R.color.i_have));
        } else if (ticketRequest.getReqType() == TicketRequest.I_NEED) {
            holder.reqTypeTextView.setText("I Need");
            holder.requestTileHolder.setBackgroundColor(context.getResources().getColor(R.color.i_need));
        }

        if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_KANDY) {
            holder.reqStartEndTextView.setText("COLOMBO - KANDY");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.KANDY_COLOMBO) {
            holder.reqStartEndTextView.setText("KANDY - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_BADULLA) {
            holder.reqStartEndTextView.setText("COLOMBO - BADULLA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.BADULLA_COLOMBO) {
            holder.reqStartEndTextView.setText("BADULLA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_KURUNEGALA) {
            holder.reqStartEndTextView.setText("COLOMBO - KURUNEGALA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.KURUNEGALA_COLOMBO) {
            holder.reqStartEndTextView.setText("KURUNEGALA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_ANURADHAPURA) {
            holder.reqStartEndTextView.setText("COLOMBO - ANURADHAPURA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.ANURADHAPURA_COLOMBO) {
            holder.reqStartEndTextView.setText("ANURADHAPURA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_VAUNIYA) {
            holder.reqStartEndTextView.setText("COLOMBO - VAUNIYA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.VAUNIYA_COLOMBO) {
            holder.reqStartEndTextView.setText("VAUNIYA - COLOMBO");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.COLOMBO_JAFNA) {
            holder.reqStartEndTextView.setText("COLOMBO - JAFNA");
        } else if (ticketRequest.getStartToEnd() == TicketRequest.JAFNA_COLOMBO) {
            holder.reqStartEndTextView.setText("JAFNA - COLOMBO");
        }

        if (ticketRequest.getReqDescription() != null && !ticketRequest.getReqDescription().equals("")) {
            holder.reqDescriptionTextView.setText(ticketRequest.getReqDescription());
        } else {
            holder.reqDescriptionTextView.setVisibility(INVISIBLE);
        }
        holder.ticketDateTextView.setText(ticketRequest.getTicketDate());
        holder.ticketDayTextView.setText(ticketRequest.getTicketDay());
        holder.ticketTimeTextView.setText(ticketRequest.getTicketTime());
        holder.timeAgoTextView.setText(getTimeDiffString(ticketRequest.getReqDate()));
    }

    @Override
    public int getItemCount() {
        return ticketRequestList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    private String getTimeDiffString(long reqDateTime) {
        String timeDiffString = null;

        long currentUnixTime = System.currentTimeMillis() / 1000L;

        long timeDiff = currentUnixTime - reqDateTime;

        long validSeconds = timeDiff % 60;

        long hours = timeDiff / (60*60);
        long minutes = timeDiff / 60;
        long days = 0;
        if (hours != 0)
            days = hours / 24;

        if (days > 0) {
            timeDiffString = days + " days ago";
        } else if (days == 0 && hours > 0) {
            timeDiffString = hours + " hours ago";
        } else if (hours == 0 && minutes > 0) {
            timeDiffString = minutes + " minutes ago";
        } else if (minutes == 0 && validSeconds > 0){
            timeDiffString = validSeconds + " seconds ago";
        } else {
            timeDiffString = "now";
        }

        return timeDiffString;
    }

    public static class RequestItemViewHolder extends RecyclerView.ViewHolder {
        private CardView requestItemCardView;
        private RelativeLayout requestTileHolder;
        private ImageView userPicImageView;
        private TextView reqTypeTextView;
        private TextView reqStartEndTextView;
        private TextView reqDescriptionTextView;
        private TextView timeAgoTextView;
        private TextView ticketDateTextView;
        private TextView ticketDayTextView;
        private TextView ticketTimeTextView;

        RequestItemViewHolder(View itemView) {
            super(itemView);
            requestItemCardView = (CardView)itemView.findViewById(R.id.requestItemCardView);
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
}
