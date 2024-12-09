package com.example.dailypuzzle.graphql;

public class UserProgress {
    private String username;
    private int totalSolved;
    private Double averageSolveTime;

    public UserProgress(String username, int totalSolved, Double averageSolveTime) {
        this.username = username;
        this.totalSolved = totalSolved;
        this.averageSolveTime = averageSolveTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTotalSolved() {
        return totalSolved;
    }

    public void setTotalSolved(int totalSolved) {
        this.totalSolved = totalSolved;
    }

    public Double getAverageSolveTime() {
        return averageSolveTime;
    }

    public void setAverageSolveTime(Double averageSolveTime) {
        this.averageSolveTime = averageSolveTime;
    }

    //http://localhost:8080/graphiql to test queries
    //query {
    //    userProgress(userId: 1) {
    //        username
    //        totalSolved
    //        averageSolveTime
    //    }
    //}
//    query {
//        leaderboard {
//            username
//                    totalSolved
//            averageSolveTime
//        }
//    }


}
