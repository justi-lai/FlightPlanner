import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

public class Flights {
    public ArrayList<LinkedList<Flight>> data = new ArrayList<>();
    public ArrayList<String> key = new ArrayList<>();

    public Flights() {
        this.data = new ArrayList<>();
        this.key = new ArrayList<>();
    }

    public Flights(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner s = new Scanner(file);
        if (!s.hasNextInt()) {
            System.out.println("***Input File not formatted correctly***");
        }
        s.useDelimiter("\\||\\n");
        int numFlights = s.nextInt();
        s.nextLine();
        for (int i = 0; i < numFlights; i++) {
            String from = s.next();
            String dest = s.next();
            double cost = Double.parseDouble(s.next());
            double time = Double.parseDouble(s.next());
            Flight temp = new Flight(dest, cost, time);
            addFlight(from, temp);
            if (s.hasNextLine()) {
                s.nextLine();
            }
        }
        s.close();
    }

    public LinkedList<Flight> getNeighbors(int index) {
        return data.get(index);
    }

    public void addFlight(String from, Flight flight) {
        if (!key.contains(from)) {
            key.add(from);
            LinkedList<Flight> temp = new LinkedList<>();
            data.add(temp);
            if (!key.contains(flight.dest)) {
                key.add(flight.dest);
                LinkedList<Flight> temp2 = new LinkedList<>();
                data.add(temp2);
            }
            data.get(key.indexOf(from)).add(flight);
        }
        else {
            int indexFrom = key.indexOf(from);
            if (!key.contains(flight.dest)) {
                key.add(flight.dest);
                LinkedList<Flight> temp2 = new LinkedList<>();
                data.add(temp2);
            }
            data.get(indexFrom).add(flight);
        }
    }

    public double getTime(int current, int dest) {
        for (Flight f : data.get(current)) {
            if (f.dest.equals(key.get(dest))) {
                return f.time;
            }
        }
        return -1;
    }

    public double getCost(int current, int dest) {
        for (Flight f : data.get(current)) {
            if (f.dest.equals(key.get(dest))) {
                return f.cost;
            }
        }
        return -1;
    }
}
