package com.example.farmerweathernotes.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "location_profile")
public class LocationProfile implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private double latitude;
    private double longitude;

    public LocationProfile() {}

    public LocationProfile(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected LocationProfile(Parcel in) {
        id = in.readInt();
        name = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
    }

    public static final Creator<LocationProfile> CREATOR = new Creator<LocationProfile>() {
        @Override
        public LocationProfile createFromParcel(Parcel in) {
            return new LocationProfile(in);
        }

        @Override
        public LocationProfile[] newArray(int size) {
            return new LocationProfile[size];
        }
    };

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    @Override
    public int describeContents() { return 0; }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
    }
}