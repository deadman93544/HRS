package com.hrs.booking.auth;

public final class HRSAuthorisations {

    public static final class Privileges {
        public static final String SELF_READ = "ROLE_SELF_READ";
        public static final String SELF_WRITE = "ROLE_SELF_WRITE";

        public static final String USER_READ  = "ROLE_USER_READ";
        public static final String USER_WRITE = "ROLE_USER_WRITE";

        public static final String ROLE_READ = "ROLE_ROLE_READ";
        public static final String ROLE_WRITE = "ROLE_ROLE_WRITE";

        public static final String HOTEL_READ = "ROLE_HOTEL_READ";
        public static final String HOTEL_WRITE = "ROLE_HOTEL_WRITE";

        public static final String BOOKING_WRITE = "ROLE_BOOKING_WRITE";
        public static final String BOOKING_READ = "ROLE_BOOKING_READ";
    }

}
