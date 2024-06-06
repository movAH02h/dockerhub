public class OrderStruct {
    private int start_floor;
    private int end_floor;
    private int total_weight;
    private int orderNumber;

    public OrderStruct(int orderNumber, int sourceFloor, int targetFloor, int passengersWeight) {
        Main.order_index++;
        this.start_floor = sourceFloor;
        this.end_floor = targetFloor;
        this.total_weight = passengersWeight;
        this.orderNumber = orderNumber;
    }

    public int getStartFloor() { return start_floor; }
    public void setStartFloor(int new_start_floor) { this.start_floor = new_start_floor; }

    public int getEndFloor() { return end_floor; }
    public void setEndFloor(int new_end_floor) { this.end_floor = new_end_floor; }

    public int getTotalWeight() { return total_weight; }
    public void setTotalWeight(int new_total_weight) { this.total_weight = new_total_weight; }

    public int getOrderNumber() { return orderNumber; }
    public void setOrderNumber(int new_order_number) { this.orderNumber = new_order_number; }
}