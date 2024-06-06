import java.util.List;
import java.util.Random;

public class Generator extends Thread {
    private List<OrderStruct> waitingDistribution;
    private Elevator elevator1;
    private Elevator elevator2;

    public Generator(Elevator elevator1, Elevator elevator2, List<OrderStruct> waitingDistribution) {
        this.elevator1 = elevator1;
        this.elevator2 = elevator2;
        this.waitingDistribution = waitingDistribution;
    }

    @Override
    public void run() {
        Random rand = new Random();
        while (true) {
            int total_weight = rand.nextInt(Constants.MAX_WEIGHT_PASSENGER - Constants.MIN_WEIGHT_PASSENGER + 1) + Constants.MIN_WEIGHT_PASSENGER;

            int start_floor = rand.nextInt(Constants.NUMBER_OF_FLOORS) + 1;
            int end_floor = start_floor;
            while (start_floor == end_floor)
            {
                end_floor = rand.nextInt(Constants.NUMBER_OF_FLOORS) + 1;
            }

            OrderStruct request = new OrderStruct(Main.order_index, start_floor, end_floor, total_weight);

            waitingDistribution.add(request);
            try
            {
                Elevator suitableElevator = appropriateElevator(request);

                System.out.println("Заказ №" + request.getOrderNumber() + " добавлен к лифту №" + suitableElevator.getNumber() +
                        "  |  " + request.getStartFloor() + " ----> " + request.getEndFloor() + "  |  Вес пассажиров " + total_weight);

                suitableElevator.addPointToElevatorPath(request.getStartFloor());
                Thread.sleep(8000);
            }
            catch (InterruptedException ie)
            {
                ie.printStackTrace();
            }

        }
    }

    private Elevator appropriateElevator(OrderStruct order){
        int isOnTheWayElevator1 = elevator1.appropriatePoint(order.getStartFloor());
        int isOnTheWayElevator2 = elevator2.appropriatePoint(order.getStartFloor());
        int first = Math.abs(elevator1.getCurrentFloor() - order.getStartFloor());
        int second = Math.abs(elevator2.getCurrentFloor() - order.getStartFloor());

        if(elevator1.getElevatorState().equals("STAY") && elevator2.getElevatorState().equals("STAY")){
            if (second < first) {
                return elevator2;
            }
            return elevator1;

        } else if(!elevator1.getElevatorState().equals("STAY") && elevator2.getElevatorState().equals("STAY")){

            if(isOnTheWayElevator1 != -1){
                return  elevator1;
            }

            return elevator2;
        } else if (elevator1.getElevatorState().equals("STAY") && !elevator2.getElevatorState().equals("STAY")){

            if(isOnTheWayElevator2 != -1){
                return  elevator2;
            }
            return elevator1;
        }

        if (isOnTheWayElevator1 != -1) { return elevator1; }
        else if (isOnTheWayElevator2 != -1) { return elevator2; }

        return elevator1;

    }
}