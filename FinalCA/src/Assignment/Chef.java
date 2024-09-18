package Assignment;

public class Chef extends Thread {
    private Restaurant restaurant;

    public Chef(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void run() {
        while (restaurant.hasDishes()) {
            try {
                Dish dish;
                synchronized (restaurant) {
                    dish = restaurant.getDish();
                }
                System.out.println("Chef is preparing " + dish.getItem() + " for table " + dish.getTableNumber());
                Thread.sleep(3000); // Simulating meal preparation time
                dish.setReady(true);
                synchronized (restaurant) {
                    restaurant.notify(); // Notify waiter that dish is ready
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}