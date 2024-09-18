package Assignment;

public class Dish {
    private int tableNumber;
    private String item;
    private boolean ready;

    public Dish(int tableNumber, String item) {
        this.tableNumber = tableNumber;
        this.item = item;
        this.ready = false;
    }

    public int getTableNumber() {
        return tableNumber;
    }

    public String getItem() {
        return item;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}
