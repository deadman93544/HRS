package com.hrs.booking.auth;


// List of Privileges Allowed for Role Creation, Also this helps in managing Authorizations of API using Spring Security
public final class HRSAuthorisations {

    public static final class Privileges {
        public static final String SELF_READ = "ROLE_SELF_READ";          // User can read all details related to his User Profile
        public static final String SELF_WRITE = "ROLE_SELF_WRITE";        // User can edit his profile related details

        public static final String USER_READ  = "ROLE_USER_READ";         // User can read All User Related details
        public static final String USER_WRITE = "ROLE_USER_WRITE";        // User can create, edit and archive other Users too.

        public static final String ROLE_READ = "ROLE_ROLE_READ";          // User can read Role related details.
        public static final String ROLE_WRITE = "ROLE_ROLE_WRITE";        // User can create, edit and delete Roles.

        public static final String HOTEL_READ = "ROLE_HOTEL_READ";        // User can read Hotel related details.
        public static final String HOTEL_WRITE = "ROLE_HOTEL_WRITE";      // User can write, edit and delete Hotels.

        public static final String BOOKING_WRITE = "ROLE_BOOKING_WRITE";  // User can read all their bookings.
        public static final String BOOKING_READ = "ROLE_BOOKING_READ";    // User can create, edit and cancel their bookings.
    }

}
