package com.example.farmerweathernotes.model;

import java.util.Date;

public class DailyNoteRequest {
    private int locationId;
    private Date date;
    private String rainObs;
    private String activity;
    private String comments;

    public DailyNoteRequest(int locationId, Date date, String rainObs, String activity, String comments) {
        this.locationId = locationId;
        this.date = date;
        this.rainObs = rainObs;
        this.activity = activity;
        this.comments = comments;
    }

    public int getLocationId() { return locationId; }
    public void setLocationId(int locationId) { this.locationId = locationId; }
    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    public String getRainObs() { return rainObs; }
    public void setRainObs(String rainObs) { this.rainObs = rainObs; }
    public String getActivity() { return activity; }
    public void setActivity(String activity) { this.activity = activity; }
    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }
}