package com.example.arenalive;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScheduleSport {

    public static final long SPORT_CRICKET = 100;
    public static final long SPORT_FOOTBALL = 101;
    public static final long SPORT_VOLLEYBALL = 102;
    public static final long SPORT_BASKETBALL = 103;
    public static final long SPORT_HOCKEY = 104;
    public static final long SPORT_BADMINTON = 105;
    public static final long SPORT_TENNIS = 106;
    public static final long SPORT_CHESS = 107;
    public static final long SPORT_CARROM = 108;
    public static final long SPORT_SQUASH = 109;
    public static final long SPORT_TABLE_TENNIS = 110;
    public static final long SPORT_SNOOKER = 111;
    public static final long SPORT_THROWBALL = 112;
    public static final long SPORT_DUALTHON = 113;
    public static final long SPORT_BODY_BUILDING = 114;
    public static final long SPORT_POWER_LIFTING = 115;
    public static final long SPORT_FRISBEE = 116;

    public static final long SPORT_TYPE_ONE = 151;
    public static final long SPORT_TYPE_TWO = 152;
    public static final long SPORT_TYPE_THREE = 153;
    public static final long SPORT_TYPE_NO_LIVE = 154;

    public static final long MATCH_NOT_STARTED = 1000;
    public static final long MATCH_IN_PROGRESS = 1001;
    public static final long MATCH_COMPLETED = 1002;

    private String documentId;
    private Date dateAndTime;
    private long mSportCode;
    private long sportStatus;
    private String teamA;
    private String teamB;
    private String winner;


    public ScheduleSport(String documentId, Date dateAndTime, long sportCode, long sportStatus, String teamA, String teamB, String winner) {
        this.documentId = documentId;
        this.dateAndTime = dateAndTime;
        mSportCode = sportCode;
        this.sportStatus = sportStatus;
        this.teamA = teamA;
        this.teamB = teamB;
        this.winner = winner;
    }

    public static String getSportName(long sportCode) {
        if (sportCode == SPORT_CRICKET)
            return "Cricket";
        else if (sportCode == SPORT_FOOTBALL)
            return "Football";
        else if (sportCode == SPORT_VOLLEYBALL)
            return "Volleyball";
        else if (sportCode == SPORT_BASKETBALL)
            return "Basketball";
        else if (sportCode == SPORT_HOCKEY)
            return "Hockey";
        else if (sportCode == SPORT_BADMINTON)
            return "Badminton";
        else if (sportCode == SPORT_TENNIS)
            return "Tennis";
        else if (sportCode == SPORT_CHESS)
            return "Chess";
        else if (sportCode == SPORT_CARROM)
            return "Carrom";
        else if (sportCode == SPORT_SQUASH)
            return "Squash";
        else if (sportCode == SPORT_TABLE_TENNIS)
            return "Table tennis";
        else if (sportCode == SPORT_SNOOKER)
            return "Snooker";
        else if (sportCode == SPORT_THROWBALL)
            return "Throwball";
        else if (sportCode == SPORT_DUALTHON)
            return "Duathlon";
        else if (sportCode == SPORT_BODY_BUILDING)
            return "Body building";
        else if (sportCode == SPORT_POWER_LIFTING)
            return "Power lifting";
        else if (sportCode == SPORT_FRISBEE)
            return "Frisbee";
        else
            return "Invalid sport";
    }

    public Date getDateAndTime() {
        return dateAndTime;
    }

    public long getSportCode() {
        return mSportCode;
    }

    public String getWinner() {
        return winner;
    }

    public long getSportStatus() {
        return sportStatus;
    }

    public long getSportType() {
        if (mSportCode == SPORT_FOOTBALL || mSportCode == SPORT_VOLLEYBALL || mSportCode == SPORT_BASKETBALL || mSportCode == SPORT_HOCKEY || mSportCode == SPORT_THROWBALL)
            return SPORT_TYPE_ONE;
        else if (mSportCode == SPORT_CRICKET)
            return SPORT_TYPE_TWO;
        else if (mSportCode == SPORT_TENNIS || mSportCode == SPORT_BADMINTON || mSportCode == SPORT_SQUASH || mSportCode == SPORT_TABLE_TENNIS)
            return SPORT_TYPE_THREE;
        else return SPORT_TYPE_NO_LIVE;
    }

    public String getSportName() {
        if (mSportCode == SPORT_CRICKET)
            return "Cricket";
        else if (mSportCode == SPORT_FOOTBALL)
            return "Football";
        else if (mSportCode == SPORT_VOLLEYBALL)
            return "Volleyball";
        else if (mSportCode == SPORT_BASKETBALL)
            return "Basketball";
        else if (mSportCode == SPORT_HOCKEY)
            return "Hockey";
        else if (mSportCode == SPORT_BADMINTON)
            return "Badminton";
        else if (mSportCode == SPORT_TENNIS)
            return "Tennis";
        else if (mSportCode == SPORT_CHESS)
            return "Chess";
        else if (mSportCode == SPORT_CARROM)
            return "Carrom";
        else if (mSportCode == SPORT_SQUASH)
            return "Squash";
        else if (mSportCode == SPORT_TABLE_TENNIS)
            return "Table tennis";
        else if (mSportCode == SPORT_SNOOKER)
            return "Snooker";
        else if (mSportCode == SPORT_THROWBALL)
            return "Throwball";
        else if (mSportCode == SPORT_DUALTHON)
            return "Duathlon";
        else if (mSportCode == SPORT_BODY_BUILDING)
            return "Body building";
        else if (mSportCode == SPORT_POWER_LIFTING)
            return "Power lifting";
        else if (mSportCode == SPORT_FRISBEE)
            return "Frisbee";
        else
            return "Invalid sport";
    }

    public String getVsTeams() {
        return teamA + " vs " + teamB;
    }

    public String getTeamA() {
        return teamA;
    }

    public String getTeamB() {
        return teamB;
    }

    public String getTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.getDefault());
        return simpleDateFormat.format(dateAndTime);
    }

    public String getAoP() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("a", Locale.getDefault());
        return simpleDateFormat.format(dateAndTime);
    }

    public int getDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd");
        return Integer.parseInt(formatter.format(dateAndTime));
    }

    public String getDocumentId() {
        return documentId;
    }
}