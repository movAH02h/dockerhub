import java.util.*;

public class Elevator extends Thread{
    private int number;
    private int current_weight;
    private int current_floor;
    private String status;
    private List<OrderStruct> ordersInElevator;
    private Deque<Integer> elevatorPath;

    private List<OrderStruct> waitingDistribution;

    public Elevator(int id, List<OrderStruct> waitingDistribution){
        this.number = id;
        this.current_weight = 0;
        this.current_floor = 1;
        this.status = "STAY";

        this.ordersInElevator = new ArrayList<>();
        this.elevatorPath = new ArrayDeque<>();

        this.waitingDistribution = waitingDistribution;
    }

    public synchronized int getCurrentFloor() { return current_floor; }
    public synchronized void setCurrentFloor(int new_current_floor) { this.current_floor = new_current_floor; }

    public synchronized String getElevatorState() { return status; }
    public synchronized void setElevatorPath(String new_status) { this.status = new_status; }

    public synchronized int getNumber(){ return number; }
    public synchronized void setNumber(int new_number) { this.number = new_number; }

    private void changeStatus()
    {
        if (!elevatorPath.isEmpty())
        {
            int next = elevatorPath.peek();
            if (next > current_floor)
            {
                status = "GO UP";
            }
            else
            {
                status = "GO DOWN";
            }
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!elevatorPath.isEmpty()) {
                if (elevatorPath.peek() == current_floor){
                    elevatorPath.remove();

                    for (Iterator<OrderStruct> iterator = ordersInElevator.iterator(); iterator.hasNext();)
                    {
                        OrderStruct order = iterator.next();
                        if (order.getEndFloor() == current_floor) {

                            System.out.println("Пассажиры заказа №" + order.getOrderNumber() + " высажены на " + current_floor + " этаже");

                            current_weight -= order.getTotalWeight();

                            iterator.remove();
                        }
                    }

                    for (Iterator<OrderStruct> iterator = waitingDistribution.iterator(); iterator.hasNext();)
                    {
                        OrderStruct order = iterator.next();
                        if (order.getStartFloor() == current_floor &&
                                current_weight + order.getTotalWeight() <= Constants.MAX_WEIGHT_ELEVATOR) {

                            System.out.println("Пассажиры заказа №" + order.getOrderNumber() + " посажены на " + current_floor + " этаже");

                            current_weight += order.getTotalWeight();

                            iterator.remove();

                            ordersInElevator.add(order);

                            addPointToElevatorPath(order.getEndFloor());
                        }
                    }
                    changeStatus();
                }else{
                    moveElevator();
                }
                continue;
            }
            status = "STAY";
        }
    }

    private void moveElevator() {
        if (!elevatorPath.isEmpty())
        {
            if (elevatorPath.peek() > current_floor) {
                status = "GO UP";
                current_floor++;
                System.out.println("Лифт №" + number + " едет вверх на этаж №" + current_floor);
            } else if (elevatorPath.peek() < current_floor) {
                status = "GO DOWN";
                current_floor--;
                System.out.println("Лифт №" + number + " едет вниз на этаж №" + current_floor);
            }
            else { return; }

            try {
                Thread.sleep(800);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void addPointToElevatorPath(int pathPoint){
        int pointStatus = appropriatePoint(pathPoint);
        if (pointStatus == 1)
        {
            elevatorPath.addFirst(pathPoint);
            System.out.println("добавлен в начало");
        }
        else if (pointStatus == -1)
        {
            elevatorPath.addLast(pathPoint);
        }
    }

    public int appropriatePoint(int pathPoint) {
        if (elevatorPath.isEmpty()) {
            return -1;
        }

        if(elevatorPath.contains(pathPoint)){
            return 0;
        }

        int firstPoint = elevatorPath.peek();

        if (status.equals("GO UP") && pathPoint > current_floor && pathPoint <= firstPoint ||
                status.equals("GO DOWN") && pathPoint < current_floor && pathPoint >= firstPoint)
        {
            return 1;
        }

        return -1;
    }
}