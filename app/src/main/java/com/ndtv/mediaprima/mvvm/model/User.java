package com.ndtv.mediaprima.mvvm.model;

/**
 * Created by nitesh on 2/2/2017.
 */

public class User extends PostResponse {

    private final String first_name;
    private final String last_name;
    private final String email;
    private final String password;
    private final String password_confirm;
    private final int birth_year;
    private final int birth_month;
    private final int birth_day;
    private final String gender;


    private User(String first_name, String last_name ,String email, String password, String password_confirm, int birth_year,
                 int birth_month, int birth_day, String gender) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.password_confirm = password_confirm;
        this.birth_year = birth_year;
        this.birth_month = birth_month;
        this.birth_day = birth_day;
        this.gender = gender;
    }

    public static class Builder {

        private String name;
        private String last_name;
        private String email;
        private String password;
        private String password_confirm;
        private int birth_year;
        private int birth_month;
        private int birth_day;
        private String gender;

        public Builder(final String name,final String last_name ,final String email, final String password, final String password_confirm,
                       final int birth_day, final int birth_month, final int birth_year, final String gender) {
            this.name = name;
            this.last_name = last_name;
            this.email = email;
            this.password = password;
            this.password_confirm = password_confirm;
            this.birth_year = birth_year;
            this.birth_month = birth_month;
            this.birth_day = birth_day;
            this.gender = gender;
        }

        public User build() {
            return new User(name, last_name ,email, password, password_confirm, birth_year, birth_month, birth_day, gender);
        }
    }
}
