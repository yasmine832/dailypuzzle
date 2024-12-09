//package com.example.dailypuzzle.graphql;
//
//import com.example.dailypuzzle.model.User;
//import com.example.dailypuzzle.repository.SolvedPuzzleRepository;
//import com.example.dailypuzzle.repository.UserRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//
//
////resolvers= java classes that fetch requested d when graphql query is executed
//@Component
//public class GraphQLResolvers implements GraphQLQueryResolver {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private SolvedPuzzleRepository solvedPuzzleRepository;
//
//    // Resolver for userProgress query
//    public UserProgress getUserProgress(Long userId) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        int totalSolved = solvedPuzzleRepository.countByUser(user);
//        Double averageSolveTime = solvedPuzzleRepository.calculateAverageSolveTime(userId);
//
//        return new UserProgress(user.getUsername(), totalSolved, averageSolveTime);
//    }
//
//    // Resolver for leaderboard query
//    public List<UserProgress> getLeaderboard() {
//        return userRepository.findAll()
//                .stream()
//                .map(user -> new UserProgress(
//                        user.getUsername(),
//                        solvedPuzzleRepository.countByUser(user),
//                        solvedPuzzleRepository.calculateAverageSolveTime(user.getId())))
//                .sorted(Comparator.comparingInt(UserProgress::getTotalSolved).reversed())
//                .collect(Collectors.toList());
//    }
//}
