package com.thesis.trackinguserapp.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Users {
    private String docID;
    private String email;
    private String password;
    private String firstName;
    private String middleName;
    private String lastName;

    public Users(UserBuilder userBuilder) {
        this.docID = userBuilder.docID;
        this.email = userBuilder.email;
        this.password = userBuilder.password;
        this.firstName = userBuilder.firstName;
        this.middleName = userBuilder.middleName;
        this.lastName = userBuilder.lastName;
    }

    public static class UserBuilder {
        private String docID;
        private String email;
        private String password;
        private String firstName;
        private String middleName;
        private String lastName;

        public UserBuilder() {

        }

        public UserBuilder setDocID(String docID) {
            this.docID = docID;
            return this;
        }

        public UserBuilder setEmail(String email) {
            this.email = email;
            return this;
        }

        public UserBuilder setPassword(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder setFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public UserBuilder setMiddleName(String middleName) {
            this.middleName = middleName;
            return this;
        }

        public UserBuilder setLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Users build() {
            return new Users();
        }
    }
}
