package Assignment;

public class Waiter extends Thread {
    private Restaurant restaurant;

    public Waiter(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void run() {
        while (restaurant.hasDishes()) {
            try {
                Dish dish;
                synchronized (restaurant) {
                    dish = restaurant.getDish();
                }
                synchronized (restaurant) {
                    while (!dish.isReady()) {
                        restaurant.wait(); // Wait for the dish to be prepared
                    }
                }
                System.out.println("Waiter is serving " + dish.getItem() + " to table " + dish.getTableNumber());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}