package com.example.arenalive;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


public class TypeTwoScoresFragment extends Fragment {

    private String documentID;

    private FirebaseFirestore db;

    private ConstraintLayout viewsLayout;
    private ProgressBar progressBar;

    private ImageView back;
    private int layoutId;

    public TypeTwoScoresFragment(String documentID, int layoutId) {
        this.documentID = documentID;
        this.layoutId = layoutId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_type_two_scores, container, false);


        db = FirebaseFirestore.getInstance();

        viewsLayout = root.findViewById(R.id.live_score_constraint);
        progressBar = root.findViewById(R.id.live_score_loader);

        final TextView sportNameTextView = root.findViewById(R.id.live_score_sport_name);
        final TextView teamANameTextView = root.findViewById(R.id.live_score_score_team_a);
        final TextView teamBNameTextView = root.findViewById(R.id.live_score_type_three_score_team_b);
        final TextView teamAScoreTextView = root.findViewById(R.id.live_score_type_three_score);
        final TextView teamBScoreTextView = root.findViewById(R.id.live_score_type_two_score_b);
        final TextView teamAOversTextView = root.findViewById(R.id.live_score_type_two_overs_a);
        final TextView teamBOversTextView = root.findViewById(R.id.live_score_type_two_overs_b);
        final LinearLayout liveIndicator = root.findViewById(R.id.live_indicator_linear_layout);
        final ImageView liveIndicatorImageView = root.findViewById(R.id.live_indicator_image_view);

        back = root.findViewById(R.id.scoreBackImageView);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(layoutId, new ScheduleFragment(layoutId)).commit();
            }
        });

        setLoadingView();

        db.collection(getString(R.string.firebase_collection_schedule)).document(documentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        setScoreView();
                        long matchStatus = document.getLong(getString(R.string.firebase_collection_schedule_field_matchStatus));
                        if (matchStatus == ScheduleSport.MATCH_IN_PROGRESS) {
                            Animation animation = new AlphaAnimation(1, 0);
                            animation.setDuration(600);
                            animation.setInterpolator(new LinearInterpolator());
                            animation.setRepeatCount(Animation.INFINITE);
                            animation.setRepeatMode(Animation.REVERSE);
                            liveIndicatorImageView.startAnimation(animation);

                            sportNameTextView.setText(ScheduleSport.getSportName(document.getLong(getString(R.string.firebase_collection_schedule_field_sportCode))));
                            teamANameTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)));
                            teamBNameTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)));
                            teamAScoreTextView.setText(formatScore(document.getLong(getString(R.string.firebase_collection_schedule_field_score_team_a)), document.getLong(getString(R.string.firebase_collection_schedule_field_wickets_team_a))));
                            teamAOversTextView.setText(formatOvers(document.getString(getString(R.string.firebase_collection_schedule_field_overs_team_a))));
                            teamBScoreTextView.setText(formatScore(document.getLong(getString(R.string.firebase_collection_schedule_field_score_team_b)), document.getLong(getString(R.string.firebase_collection_schedule_field_wickets_team_b))));
                            teamBOversTextView.setText(formatOvers(document.getString(getString(R.string.firebase_collection_schedule_field_overs_team_b))));

                            db.collection(getString(R.string.firebase_collection_schedule)).document(documentID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (snapshot != null && snapshot.exists()) {
                                        teamAScoreTextView.setText(formatScore(snapshot.getLong(getString(R.string.firebase_collection_schedule_field_score_team_a)), snapshot.getLong(getString(R.string.firebase_collection_schedule_field_wickets_team_a))));
                                        teamAOversTextView.setText(formatOvers(snapshot.getString(getString(R.string.firebase_collection_schedule_field_overs_team_a))));
                                        teamBScoreTextView.setText(formatScore(snapshot.getLong(getString(R.string.firebase_collection_schedule_field_score_team_b)), snapshot.getLong(getString(R.string.firebase_collection_schedule_field_wickets_team_b))));
                                        teamBOversTextView.setText(formatOvers(snapshot.getString(getString(R.string.firebase_collection_schedule_field_overs_team_b))));
                                    }
                                }
                            });
                        } else if (matchStatus == ScheduleSport.MATCH_COMPLETED) {
                            liveIndicator.setVisibility(View.GONE);
                            sportNameTextView.setText(ScheduleSport.getSportName(document.getLong(getString(R.string.firebase_collection_schedule_field_sportCode))));
                            teamANameTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)));
                            teamBNameTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)));
                            teamAScoreTextView.setText(formatScore(document.getLong(getString(R.string.firebase_collection_schedule_field_score_team_a)), document.getLong(getString(R.string.firebase_collection_schedule_field_wickets_team_a))));
                            teamAOversTextView.setText(formatOvers(document.getString(getString(R.string.firebase_collection_schedule_field_overs_team_a))));
                            teamBScoreTextView.setText(formatScore(document.getLong(getString(R.string.firebase_collection_schedule_field_score_team_b)), document.getLong(getString(R.string.firebase_collection_schedule_field_wickets_team_b))));
                            teamBOversTextView.setText(formatOvers(document.getString(getString(R.string.firebase_collection_schedule_field_overs_team_b))));
                        }
                    }
                }
            }
        });
        return root;
    }

    private String formatScore(long teamScore, long wicketsTaken) {
        if (teamScore != 0)
            return teamScore + "/" + wicketsTaken;
        else
            return "-";
    }

    private String formatOvers(String overs) {
        if (!overs.equals("0.0"))
            return "(" + overs + ")";
        else
            return "";
    }

    private void setLoadingView() {
        progressBar.setVisibility(View.VISIBLE);
        viewsLayout.setVisibility(View.INVISIBLE);
    }

    private void setScoreView() {
        progressBar.setVisibility(View.INVISIBLE);
        viewsLayout.setVisibility(View.VISIBLE);
    }

}
