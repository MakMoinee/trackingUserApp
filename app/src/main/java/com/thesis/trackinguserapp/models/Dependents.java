package com.thesis.trackinguserapp.models;


import lombok.Data;

@Data
public class Dependents {
    private String docID;

    private String userID;
    private String dependentName;
    private int deviceID;
    private int deviceUserID;
    private String dependentEmail;
    private String dependentPhoneNumber;

    public Dependents() {

    }

    public Dependents(DependentBuilder builder) {
        this.docID = builder.docID;
        this.userID = builder.userID;
        this.dependentName = builder.dependentName;
        this.deviceID = builder.deviceID;
        this.deviceUserID = builder.deviceUserID;
        this.dependentEmail = builder.dependentEmail;
        this.dependentPhoneNumber = builder.dependentPhoneNumber;
    }

    public static class DependentBuilder {
        private String docID;
        private String dependentName;
        private int deviceID;
        private int deviceUserID;
        private String dependentEmail;
        private String dependentPhoneNumber;

        private String userID;

        public DependentBuilder() {
        }

        public DependentBuilder setDocID(String docID) {
            this.docID = docID;
            return this;
        }

        public DependentBuilder setDependentName(String dependentName) {
            this.dependentName = dependentName;
            return this;
        }

        public DependentBuilder setDeviceID(int deviceID) {
            this.deviceID = deviceID;
            return this;
        }

        public DependentBuilder setDeviceUserID(int deviceUserID) {
            this.deviceUserID = deviceUserID;
            return this;
        }

        public DependentBuilder setDependentEmail(String dependentEmail) {
            this.dependentEmail = dependentEmail;
            return this;
        }

        public DependentBuilder setDependentPhoneNumber(String dependentPhoneNumber) {
            this.dependentPhoneNumber = dependentPhoneNumber;
            return this;
        }

        public DependentBuilder setUserID(String userID) {
            this.userID = userID;
            return this;
        }

        public Dependents build() {
            return new Dependents(this);
        }
    }
}
