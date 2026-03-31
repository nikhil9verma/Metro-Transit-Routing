package com.Bus.Routing.Bus.dto;


public class ChatRequest {

    public String message;
    public String time; // HH:mm format

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
