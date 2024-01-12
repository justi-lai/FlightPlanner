/**
 * Justin Lai JXL210110
 * CS 3345.502 Project 1 - Graph Algorithm
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import java.util.Stack;

public class Main {
    static Flights flights = new Flights();

    public static void main(String[] args) {
        ArrayList<Plan> plans;
        System.out.println(args[0]);
        try {
            flights = new Flights(args[0]);
            plans = parseRequest(args[1]);
        } catch (FileNotFoundException e) {
            System.out.println("try in main failed");
            throw new RuntimeException(e);
        }

        File outputFile = new File(args[2]);
        try {
            FileWriter writer = new FileWriter(outputFile);

            for (int i = 0; i < plans.size(); i++) {
                ArrayList<Path> paths = findPaths(plans.get(i));
                String intro = "Flight " + (i + 1) + ": " + plans.get(i).from + ", " + plans.get(i).dest + " (";
                if (plans.get(i).cost == 'T' || plans.get(i).cost == 't') {
                    intro += "Time)";
                } else {
                    intro += "Cost)";
                }
                writer.write(intro + "\n");

                int num = 3;
                if (paths.size() == 0) {
                    writer.write("No Path Found\n\n");
                    continue;
                } else if (paths.size() < 3) {
                    num = paths.size();
                }

                for (int j = 0; j < num; j++) {
                    Path bestPath = paths.get(0);
                    for (Path currentPath : paths) {
                        if (plans.get(i).cost == 'T' || plans.get(i).cost == 't') {
                            if (bestPath.time > currentPath.time) {
                                bestPath = currentPath;
                            }
                        } else {
                            if (bestPath.cost > currentPath.cost) {
                                bestPath = currentPath;
                            }
                        }
                    }
                    bestPath = paths.remove(paths.indexOf(bestPath));
                    writer.write("Path " + (j + 1) + ": ");
                    for (int k = 0; k < bestPath.pathNames.size(); k++) {
                        if (k != bestPath.pathNames.size() - 1) {
                            writer.write(bestPath.pathNames.get(k) + " -> ");
                        } else {
                            String time = String.format("%.2f", bestPath.time);
                            String cost = String.format("%.2f", bestPath.cost);
                            writer.write(bestPath.pathNames.get(k) + ". Time: " + time + " Cost: " + cost + "\n");
                        }
                    }
                }
                writer.write("\n");
            }

            writer.close();

        } catch (IOException e) {
            System.out.println("try2 in main failed");
            throw new RuntimeException(e);
        }

    }

    /**
     * Finds the shortest path according to the flight plan
     * @param plan Path object that contains the to and from, as well as the cost type
     * @return Path object that contains the shortest path and cost
     */
    public static ArrayList<Path> findPaths(Plan plan) {
        int startNode = flights.key.indexOf(plan.from);
        int endNode = flights.key.indexOf(plan.dest);
        ArrayList<Path> paths = new ArrayList<>();

        Stack<Path> stack = new Stack<>();
        stack.push(new Path(startNode, new LinkedList<>(), new LinkedList<>(), 0, 0));

        while (!stack.isEmpty()) {
            Path current = stack.pop();
            int currentNode = current.currentNode;
            LinkedList<Integer> currentPath = current.path;
            currentPath.add(currentNode);
            current.pathNames.add(flights.key.get(currentNode));

            if (currentNode == endNode) {
                paths.add(current);
            }
            else {
                LinkedList<Flight> neighbors = flights.getNeighbors(currentNode);
                for (Flight neighbor : neighbors) {
                    int neighborIndex = flights.key.indexOf(neighbor.dest);
                    if (!currentPath.contains(neighborIndex)) {
                        double nextTime = 0;
                        double nextCost = 0;
                        if (flights.getTime(currentNode, neighborIndex) >= 0) {
                            nextTime = current.time + flights.getTime(currentNode, neighborIndex);
                        } else {
                            System.out.println("\n\n\n\n*******nextTime is not available, output is wrong********\n\n\n\n");
                        }
                        if (flights.getCost(currentNode, neighborIndex) >= 0) {
                            nextCost = current.cost + flights.getCost(currentNode, neighborIndex);
                        } else {
                            System.out.println("\n\n\n\n*******nextCost is not available, output is wrong********\n\n\n\n");
                        }
                        Path nextPath = new Path(neighborIndex, new LinkedList<>(currentPath), new LinkedList<>(current.pathNames), nextTime, nextCost);
                        stack.push(nextPath);
                    }
                }
            }
        }

        return paths;
    }

    /**
     * Parses in the PathsToCalculateFile to a linked list
     * @param fileName name of the file
     * @return a linked list containing the data of PathsToCalculateFile
     */
    public static ArrayList<Plan> parseRequest(String fileName) throws FileNotFoundException {
        File file = new File(fileName);
        Scanner s = new Scanner(file);
        ArrayList<Plan> plans = new ArrayList<>();
        if (!s.hasNextInt()) {
            System.out.println("***Input File not formatted correctly***");
        }
        s.useDelimiter("\\||\\n");
        int numPlans = s.nextInt();
        s.nextLine();
        for (int i = 0; i < numPlans; i++) {
            String from = s.next();
            String dest = s.next();
            char costType = s.next().charAt(0);
            plans.add(new Plan(from, dest, costType));
        }

        s.close();

        return plans;
    }
}
