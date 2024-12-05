package com.example.greenpulse.responses;

public class UserResponse {
    public String status;
    public String message;
    public int statusCode;
    public Data data;
    public class Data{
        public int userId;
        public String username;
        public String email;
        public Object dp;
        public String role;
    }
}
