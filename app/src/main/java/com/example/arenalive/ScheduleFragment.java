package com.example.arenalive;


import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ScheduleFragment extends Fragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private ScheduleSportAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public static final int FILTER_NOT_STARTED = 0;
    public static final int FILTER_LIVE = 1;
    public static final int FILTER_COMPLETED = 2;

    private Chip mChip23;
    private Chip mChip24;
    private Chip mChip25;
    private Chip mChip26;
    private ChipGroup mChipGroup;
    private ProgressBar mProgressBar;

    private int selectedDay;
    public static final int FILTER_NONE = 8004;
    private RapidFloatingActionLayout rfaLayout;
    private RapidFloatingActionButton filterFab;
    private RapidFloatingActionHelper filterFabHelpler;
    private int selectedFilter;

    ArrayList<ScheduleSport> mAllScheduleEvents;
    ArrayList<ScheduleSport> mScheduleEventsToDisplay;

    private FirebaseFirestore db;

    private int layoutId;

    public ScheduleFragment(int layoutId) {
        this.layoutId = layoutId;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_schedule, container, false);

        db = FirebaseFirestore.getInstance();

        rfaLayout = root.findViewById(R.id.schedule_filter_layout);
        filterFab = root.findViewById(R.id.schedule_filter_fab);

        setupFab();

        mAllScheduleEvents = new ArrayList<>();
        mScheduleEventsToDisplay = new ArrayList<>();

        mRecyclerView = root.findViewById(R.id.sports_schedule_recycler);
        mProgressBar = root.findViewById(R.id.schedule_progress_bar);

        getAllScheduleEvents();

        mChip23 = root.findViewById(R.id.schedule_23_chip);
        mChip24 = root.findViewById(R.id.schedule_24_chip);
        mChip25 = root.findViewById(R.id.schedule_25_chip);
        mChip26 = root.findViewById(R.id.schedule_26_chip);
        mChipGroup = root.findViewById(R.id.schedule_chip_group);

        //Seeing what day it is and setting the respective Chip
        Date curDate = new Date();
        Date dateJan24 = new Date(1579824001L);
        Date dateJan25 = new Date(1579910401L);
        Date dateJan26 = new Date(1579996801L);
        Date dateJan27 = new Date(1580083201L);

        if (curDate.after(dateJan24) && curDate.before(dateJan25)) {
            mChip24.setChecked(true);
            selectedDay = 24;
        } else if (curDate.after(dateJan25) && curDate.before(dateJan26)) {
            mChip25.setChecked(true);
            selectedDay = 25;
        } else if (curDate.after(dateJan26) && curDate.before(dateJan27)) {
            mChip26.setChecked(true);
            selectedDay = 26;
        } else {
            mChip23.setChecked(true);
            selectedDay = 23;
        }

        selectedFilter = FILTER_NONE;

        mChip23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChipGroup.getCheckedChipId() == View.NO_ID) {
                    mChip23.setChecked(true);
                }
                setLoadingView();
                selectedDay = 23;
                filterScheduleEvents();
                setRecyclerData();
            }
        });

        mChip24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChipGroup.getCheckedChipId() == View.NO_ID) {
                    mChip24.setChecked(true);
                }
                setLoadingView();
                selectedDay = 24;
                filterScheduleEvents();
                setRecyclerData();
            }
        });

        mChip25.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChipGroup.getCheckedChipId() == View.NO_ID) {
                    mChip25.setChecked(true);
                }
                setLoadingView();
                selectedDay = 25;
                filterScheduleEvents();
                setRecyclerData();
            }
        });

        mChip26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mChipGroup.getCheckedChipId() == View.NO_ID) {
                    mChip26.setChecked(true);
                }
                setLoadingView();
                selectedDay = 26;
                filterScheduleEvents();
                setRecyclerData();
            }
        });

        return root;
    }

    private void setRecyclerData() {
        mAdapter = new ScheduleSportAdapter(mScheduleEventsToDisplay, getContext(), layoutId);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        setRecyclerView();
    }

    private void getAllScheduleEvents() {
        setLoadingView();
        mScheduleEventsToDisplay.clear();
        db.collection(getString(R.string.firebase_collection_schedule)).orderBy(getString(R.string.firebase_collection_schedule_field_date))
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                mAllScheduleEvents.add(new ScheduleSport(document.getId(),
                                        document.getDate(getString(R.string.firebase_collection_schedule_field_date)),
                                        document.getLong(getString(R.string.firebase_collection_schedule_field_sportCode)),
                                        document.getLong(getString(R.string.firebase_collection_schedule_field_matchStatus)),
                                        document.getString(getString(R.string.firebase_collection_schedule_field_name_team_a)),
                                        document.getString(getString(R.string.firebase_collection_schedule_field_name_team_b)),
                                        document.getString(getString(R.string.firebase_collection_schedule_field_winner))));
                            }
                            filterScheduleEvents();
                            setRecyclerView();
                            setRecyclerData();
                        } else {
                            Log.d("ScheduleFragment", "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void filterScheduleEvents() {
        mScheduleEventsToDisplay.clear();
        for (ScheduleSport sportEvent : mAllScheduleEvents) {
            if (selectedFilter == FILTER_NONE) {
                if (selectedDay == sportEvent.getDay()) {
                    mScheduleEventsToDisplay.add(sportEvent);
                }
            } else if (selectedFilter == FILTER_NOT_STARTED) {
                if (selectedDay == sportEvent.getDay() && sportEvent.getSportStatus() == ScheduleSport.MATCH_NOT_STARTED)
                    mScheduleEventsToDisplay.add(sportEvent);
            } else if (selectedFilter == FILTER_LIVE) {
                if (selectedDay == sportEvent.getDay() && sportEvent.getSportStatus() == ScheduleSport.MATCH_IN_PROGRESS) {
                    mScheduleEventsToDisplay.add(sportEvent);
                }
            } else if (selectedFilter == FILTER_COMPLETED) {
                if (selectedDay == sportEvent.getDay() && sportEvent.getSportStatus() == ScheduleSport.MATCH_COMPLETED)
                    mScheduleEventsToDisplay.add(sportEvent);
            }

        }
    }

    private void setLoadingView() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void setRecyclerView() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void setupFab() {
        RapidFloatingActionContentLabelList fabContent = new RapidFloatingActionContentLabelList(getContext());
        fabContent.setOnRapidFloatingActionContentLabelListListener(this);
        List<RFACLabelItem> options = new ArrayList<>();
        options.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.scheduled))
                .setWrapper(FILTER_NOT_STARTED)
        );
        options.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.only_live))
                .setWrapper(FILTER_LIVE)
        );
        options.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.only_completed))
                .setWrapper(FILTER_COMPLETED)
        );
        options.add(new RFACLabelItem<Integer>()
                .setLabel(getString(R.string.no_filter))
                .setWrapper(FILTER_NONE)
        );
        fabContent.setItems(options);
        filterFabHelpler = new RapidFloatingActionHelper(getContext(), rfaLayout, filterFab, fabContent).build();
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        //do nothing since we're not gonna use icons
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        switch (position) {
            case FILTER_NOT_STARTED:
                selectedFilter = FILTER_NOT_STARTED;
                Log.d("Filters", "Filter: not started");
                filterScheduleEvents();
                setRecyclerData();
                mAdapter.notifyDataSetChanged();
                filterFabHelpler.toggleContent();
                break;
            case FILTER_LIVE:
                selectedFilter = FILTER_LIVE;
                Log.d("Filters", "Filter: live");
                filterScheduleEvents();
                setRecyclerData();
                mAdapter.notifyDataSetChanged();
                filterFabHelpler.toggleContent();
                break;
            case FILTER_COMPLETED:
                selectedFilter = FILTER_COMPLETED;
                Log.d("Filters", "Filter: completed");
                filterScheduleEvents();
                setRecyclerData();
                mAdapter.notifyDataSetChanged();
                filterFabHelpler.toggleContent();
                break;
            case FILTER_NONE:
                Log.d("Filters", "Filter: none");
                filterScheduleEvents();
                setRecyclerData();
                mAdapter.notifyDataSetChanged();
                filterFabHelpler.toggleContent();
            default:
                selectedFilter = FILTER_NONE;
                Log.d("Filters", "Filter: default");
                filterScheduleEvents();
                setRecyclerData();
                mAdapter.notifyDataSetChanged();
                filterFabHelpler.toggleContent();
        }
    }

}