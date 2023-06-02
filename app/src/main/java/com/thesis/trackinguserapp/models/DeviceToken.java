package com.thesis.trackinguserapp.models;

import lombok.Data;

@Data
public class DeviceToken {
    private String docID;
    private  String userID;
    private String deviceToken;

    public DeviceToken() {
    }

    public DeviceToken(DeviceTokenBuilder builder) {
        this.docID = builder.docID;
        this.deviceToken = builder.deviceToken;
        this.userID = builder.userID;
    }

    public static class DeviceTokenBuilder {
        private String docID;
        private String deviceToken;
        private  String userID;

        public DeviceTokenBuilder() {
        }

        public DeviceTokenBuilder setDocID(String docID) {
            this.docID = docID;
            return this;
        }

        public DeviceTokenBuilder setDeviceToken(String deviceToken) {
            this.deviceToken = deviceToken;
            return this;
        }

        public DeviceTokenBuilder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public DeviceToken build() {
            return new DeviceToken(this);
        }


    }
}
