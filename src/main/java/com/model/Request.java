package com.model;

public class Request {
    private String hashSum;
    private String message;
    private int attempt;

    public Request(String hashSum, String message, int attempt) {
        this.message = message;
        this.hashSum = hashSum;
        this.attempt = attempt;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getHashSum() {
        return hashSum;
    }

    public void setHashSum(String hashSum) {
        this.hashSum = hashSum;
    }

    public int getAttempt() {
        return attempt;
    }

    public void setAttempt(int attempt) {
        this.attempt = attempt;
    }
}
