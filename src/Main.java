import java.util.ArrayList;
import java.util.List;

public class Main {
    public static int order_index = 1;
    public static void main(String [] args){
        List<OrderStruct> waitingDistribution = new ArrayList<>();

        Elevator elevator1 = new Elevator(1, waitingDistribution);
        Elevator elevator2 = new Elevator(2, waitingDistribution);
        elevator1.start();
        elevator2.start();

        Generator generator = new Generator(elevator1, elevator2, waitingDistribution);
        generator.start();
    }
}