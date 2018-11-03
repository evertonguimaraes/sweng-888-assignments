package com.sweng888.androiduiandlogin_ericbratter.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class User {
    @NonNull private String firstName;
    @NonNull private String lastName;
    @NonNull private String email;
    @NonNull private String password;
    private String phoneNumber;
    private String birthday;
}
