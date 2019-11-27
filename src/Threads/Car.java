package HW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.*;

public class Car {
    public static final int NUMBER_OF_TIRES = 4;
    public static final int NUMBER_OF_SEATS = 5;
    private Engine engine;
    private Frame frame;
    private ArrayList<Tire> tires = new ArrayList<>();
    private ArrayList<Seat> seats = new ArrayList<>();
    private BlockingQueue<Components> components = new LinkedBlockingQueue<>();

    private interface Components extends Runnable {
    }

    private static class Engine implements Runnable, Components {

        private Engine() {
        }

        @Override
        public void run() {
            try {
                Thread.sleep(7000);
                System.out.println("An engine is made!");
            } catch (InterruptedException e) {
                System.out.println("Error in engine creation");
            }
        }
    }

    private static class Frame implements Runnable, Components {

        private Frame() {
        }

        @Override
        public void run() {
            try {
                Thread.sleep(5000);
                System.out.println("A frame is made! ");
            } catch (InterruptedException e) {
                System.out.println("Error in frame creation");
            }
        }
    }

    private static class Tire implements Runnable, Components {

        private Tire() {
        }

        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                System.out.println("A tire is made! ");
            } catch (InterruptedException e) {
                System.out.println("Error in tire creation");
            }
        }
    }

    private static class Seat implements Runnable, Components {

        private Seat() {
        }

        @Override
        public void run() {
            try {
                Thread.sleep(3000);
                System.out.println("A seat is made! ");
            } catch (InterruptedException e) {
                System.out.println("Error in seat creation");
            }
        }
    }

    Car() {
        this.engine = new Engine();
        this.frame = new Frame();
        for (int i = 0; i < NUMBER_OF_TIRES; i++) {
            tires.add(new Tire());
        }
        for (int i = 0; i < NUMBER_OF_SEATS; i++) {
            seats.add(new Seat());
        }

        Iterator<Tire> itTire = tires.iterator();
        Iterator<Seat> itSeat = seats.iterator();
        try {
            components.put(this.engine);
            for (int i = 0; i < 3; i++) {
                components.put(itTire.next());
                components.put(itSeat.next());
            }
            components.put(itTire.next());
            components.put(this.frame);
            components.put(new Seat());
            components.put(new Seat());
        } catch (InterruptedException e) {
            System.out.println("Error");
        }

        ExecutorService executor = Executors.newFixedThreadPool(3);

        System.out.println("Creating a car.....");
        while (components.size() != 0) {
            try {
                executor.execute(components.take());
            } catch (InterruptedException e) {
                System.out.println("Error!");
            }
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("A car is born!");
    }
}
