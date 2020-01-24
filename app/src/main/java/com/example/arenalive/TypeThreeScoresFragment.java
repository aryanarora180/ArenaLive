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


public class TypeThreeScoresFragment extends Fragment {

    private String documentID;

    private FirebaseFirestore db;

    private ConstraintLayout viewsLayout;
    private ProgressBar progressBar;

    public TypeThreeScoresFragment(String documentID) {
        this.documentID = documentID;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_type_three_scores, container, false);

        db = FirebaseFirestore.getInstance();

        viewsLayout = root.findViewById(R.id.live_score_constraint);
        progressBar = root.findViewById(R.id.live_score_loader);

        final TextView sportNameTextView = root.findViewById(R.id.live_score_sport_name);
        final TextView teamANameTextView = root.findViewById(R.id.live_score_score_team_a);
        final TextView teamBNameTextView = root.findViewById(R.id.live_score_type_three_score_team_b);
        final TextView scoreTextView = root.findViewById(R.id.live_score_type_three_score);
        final TextView setIndicatorTextView = root.findViewById(R.id.live_score_type_three_set_indicator);
        final TextView set1WinnerTextView = root.findViewById(R.id.live_score_type_three_set_1_winner);
        final TextView set2WinnerTextView = root.findViewById(R.id.live_score_type_three_set_2_winner);
        final TextView set3WinnerTextView = root.findViewById(R.id.live_score_type_three_set_3_winner);
        final LinearLayout liveIndicator = root.findViewById(R.id.live_indicator_linear_layout);
        final ImageView liveIndicatorImageView = root.findViewById(R.id.live_indicator_image_view);

        setLoadingView();

        db.collection(getString(R.string.firebase_collection_schedule)).document(documentID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    setScoreView();
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
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

                            long currentSet = document.getLong(getString(R.string.firebase_collection_schedule_field_currentSet));
                            setIndicatorTextView.setText(getString(R.string.set) + " " + currentSet);

                            if (currentSet == 2L)
                                scoreTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setTwoScore)));
                            else if (currentSet == 3L)
                                scoreTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setThreeScore)));
                            else
                                scoreTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setOneScore)));

                            set1WinnerTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setOneWinner)));
                            set2WinnerTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setTwoWinner)));
                            set3WinnerTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setThreeWinner)));

                            db.collection(getString(R.string.firebase_collection_schedule)).document(documentID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                @Override
                                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (snapshot != null && snapshot.exists()) {
                                        long currentSet = snapshot.getLong(getString(R.string.firebase_collection_schedule_field_currentSet));
                                        setIndicatorTextView.setText(getString(R.string.set) + " " + currentSet);

                                        if (currentSet == 2L)
                                            scoreTextView.setText(snapshot.getString(getString(R.string.firebase_collection_schedule_field_setTwoScore)));
                                        else if (currentSet == 3L)
                                            scoreTextView.setText(snapshot.getString(getString(R.string.firebase_collection_schedule_field_setThreeScore)));
                                        else
                                            scoreTextView.setText(snapshot.getString(getString(R.string.firebase_collection_schedule_field_setOneScore)));

                                        set1WinnerTextView.setText(snapshot.getString(getString(R.string.firebase_collection_schedule_field_setOneWinner)));
                                        set2WinnerTextView.setText(snapshot.getString(getString(R.string.firebase_collection_schedule_field_setTwoWinner)));
                                        set3WinnerTextView.setText(snapshot.getString(getString(R.string.firebase_collection_schedule_field_setThreeWinner)));
                                    }
                                }
                            });
                        } else if (matchStatus == ScheduleSport.MATCH_COMPLETED) {

                            liveIndicator.setVisibility(View.GONE);

                            sportNameTextView.setText(ScheduleSport.getSportName(document.getLong(getString(R.string.firebase_collection_schedule_field_sportCode))));
                            teamANameTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)));
                            teamBNameTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)));

                            long currentSet = document.getLong(getString(R.string.firebase_collection_schedule_field_currentSet));
                            setIndicatorTextView.setText(getString(R.string.set) + " " + currentSet);

                            if (currentSet == 2L)
                                scoreTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setTwoScore)));
                            else if (currentSet == 3L)
                                scoreTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setThreeScore)));
                            else
                                scoreTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setOneScore)));

                            set1WinnerTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setOneWinner)));
                            set2WinnerTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setTwoWinner)));
                            set3WinnerTextView.setText(document.getString(getString(R.string.firebase_collection_schedule_field_setThreeWinner)));
                        }
                    }

                }
            }
        });
        return root;
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