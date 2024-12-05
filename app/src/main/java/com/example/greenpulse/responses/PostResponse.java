package com.example.greenpulse.responses;

import java.time.LocalDateTime;
import java.util.Date;

public class PostResponse {
    public String status;
    public String message;
    public int statusCode;
    public Data data;
    public class Data{
        public Object postId;
        public String title;
        public String content;
        public String image;
        public LocalDateTime createdAt;
        public int userId;
        public int commentCount;
    }

}
