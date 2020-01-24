package com.example.arenalive;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ScheduleSportAdapter extends RecyclerView.Adapter<ScheduleSportAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<ScheduleSport> mEvents;
    private FragmentManager mFragmentManager;
    private int layoutId;

    public ScheduleSportAdapter(ArrayList<ScheduleSport> events, Context context, int layoutId) {
        mEvents = events;
        mContext = context;
        mFragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
        this.layoutId = layoutId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View listItemView = inflater.inflate(R.layout.list_item_schedule, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ScheduleSport sportsEvent = mEvents.get(position);

        TextView timeHoursTextView = holder.timeHoursTextView;
        TextView timeAOPTextView = holder.timeAOPTextView;
        TextView nameTextView = holder.nameTextView;
        TextView subtitleTextView = holder.subtitleTextView;
        final TextView statusTextView = holder.statusTextView;

        timeHoursTextView.setText(sportsEvent.getTime());
        timeAOPTextView.setText(sportsEvent.getAoP());

        nameTextView.setText(sportsEvent.getSportName());
        subtitleTextView.setText(sportsEvent.getVsTeams());

        if (sportsEvent.getSportStatus() == ScheduleSport.MATCH_IN_PROGRESS) {
            statusTextView.setText(mContext.getString(R.string.live));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLiveScoreFragment(sportsEvent.getDocumentId(), sportsEvent.getSportType());
                }
            });
        } else if (sportsEvent.getSportStatus() == ScheduleSport.MATCH_COMPLETED && sportsEvent.getSportType() != ScheduleSport.SPORT_TYPE_NO_LIVE) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openLiveScoreFragment(sportsEvent.getDocumentId(), sportsEvent.getSportType());
                }
            });
            statusTextView.setText(mContext.getString(R.string.completed));
        } else if (sportsEvent.getSportStatus() == ScheduleSport.MATCH_COMPLETED && sportsEvent.getSportType() == ScheduleSport.SPORT_TYPE_NO_LIVE) {
            String winnerText = mContext.getString(R.string.completed_no_live) + " " + sportsEvent.getWinner();
            statusTextView.setText(winnerText);
        } else if (sportsEvent.getSportStatus() == ScheduleSport.MATCH_NOT_STARTED) {
            statusTextView.setText(mContext.getString(R.string.scheduled));
        }

    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    private void openLiveScoreFragment(String documentID, long sportType) {
        if (sportType == ScheduleSport.SPORT_TYPE_ONE)
            mFragmentManager.beginTransaction().replace(layoutId, new TypeOneScoresFragment(documentID, layoutId), documentID).addToBackStack(documentID).commit();
        else if (sportType == ScheduleSport.SPORT_TYPE_TWO)
            mFragmentManager.beginTransaction().replace(layoutId, new TypeTwoScoresFragment(documentID, layoutId), documentID).addToBackStack(documentID).commit();
        else if (sportType == ScheduleSport.SPORT_TYPE_THREE)
            mFragmentManager.beginTransaction().replace(layoutId, new TypeThreeScoresFragment(documentID, layoutId), documentID).addToBackStack(documentID).commit();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView timeHoursTextView;
        public TextView timeAOPTextView;
        public TextView nameTextView;
        public TextView subtitleTextView;
        public TextView statusTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            timeHoursTextView = itemView.findViewById(R.id.list_schedule_sport_time_hours);
            timeAOPTextView = itemView.findViewById(R.id.list_schedule_sport_aop);
            nameTextView = itemView.findViewById(R.id.list_schedule_sport_name_text_view);
            subtitleTextView = itemView.findViewById(R.id.list_schedule_sport_subtitle);
            statusTextView = itemView.findViewById(R.id.list_schedule_match_status);
        }

    }

}