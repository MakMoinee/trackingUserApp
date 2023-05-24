package com.thesis.trackinguserapp.models;

import lombok.Data;

@Data
public class Devices {
    private String docID;
    private String userID;
    private float deviceID;
    private int deviceUserID;
    private String dateCreated;

    public Devices() {
    }

    public Devices(DeviceBuilder builder) {
        this.docID = builder.docID;
        this.userID = builder.userID;
        this.deviceID = builder.deviceID;
        this.deviceUserID = builder.deviceUserID;
        this.dateCreated = builder.dateCreated;

    }

    public static class DeviceBuilder {
        private String docID;
        private String userID;
        private float deviceID;
        private int deviceUserID;
        private String dateCreated;

        public DeviceBuilder() {
        }

        public DeviceBuilder setDocID(String docID) {
            this.docID = docID;
            return this;
        }

        public DeviceBuilder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public DeviceBuilder setDeviceID(float deviceID) {
            this.deviceID = deviceID;
            return this;
        }

        public DeviceBuilder setDeviceUserID(int deviceUserID) {
            this.deviceUserID = deviceUserID;
            return this;
        }

        public DeviceBuilder setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
            return this;
        }

        public Devices build() {
            return new Devices(this);
        }
    }
}
