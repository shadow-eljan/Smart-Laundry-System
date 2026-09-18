import java.util.Random;

public class Customer implements Runnable {

    private final int id;
    private final LaundryResources resources;
    private final boolean congestedMode;
    private final long startTime;
    private final Random random = new Random();

    public Customer(int id, LaundryResources resources, boolean congestedMode) {
        this.id = id;
        this.resources = resources;
        this.congestedMode = congestedMode;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        try {
            resources.log("Customer " + id + " arrived at the laundry");

            wash();
            dry();
            pay();

            long totalTime = System.currentTimeMillis() - startTime;

            resources.recordCustomerDone(totalTime);

            resources.log("Customer " + id + " exited (total time: "
                    + totalTime / 1000.0 + "s)");

        } catch (InterruptedException e) {
            resources.log("Customer " + id + " was interrupted");
            Thread.currentThread().interrupt();
        }
    }

    private void wash() throws InterruptedException {

        resources.log("Customer " + id + " waiting for washer");

        resources.getWashers().acquire();
        resources.incrementWasherUsage();

        resources.log("Customer " + id + " started washing");

        // 5% chance of washer failure
        if (random.nextInt(100) < 5) {

            resources.log("Customer " + id + " washer failed, retrying");

            resources.decrementWasherUsage();
            resources.getWashers().release();

            Thread.sleep(1000);

            resources.log("Customer " + id + " waiting for washer");

            resources.getWashers().acquire();
            resources.incrementWasherUsage();

            resources.log("Customer " + id + " started washing again");
        }

        // Washing takes 4–6 seconds
        int washingTime = 4000 + random.nextInt(2001);
        Thread.sleep(washingTime);

        resources.log("Customer " + id + " finished washing");

        resources.decrementWasherUsage();
        resources.getWashers().release();
    }

    private void dry() throws InterruptedException {

        resources.log("Customer " + id + " waiting for dryer");

        resources.getDryers().acquire();
        resources.incrementDryerUsage();

        resources.log("Customer " + id + " started drying");

        // Drying takes 3–5 seconds
        int dryingTime = 3000 + random.nextInt(2001);
        Thread.sleep(dryingTime);

        resources.log("Customer " + id + " finished drying");

        resources.decrementDryerUsage();
        resources.getDryers().release();
    }


    private void pay() throws InterruptedException {

        if (congestedMode) {

            resources.log(
                    "Customer " + id + " waiting for payment"
            );

            resources.incrementPaymentQueue();

            // Wait until owner turns the kiosks on
            synchronized (resources) {

                while (!resources.isPaymentAvailable()) {
                    resources.wait();
                }
            }

            resources.log(
                    "Customer " + id + " can now use payment kiosk"
            );
        }

        // Get one of the two kiosks
        resources.getKiosks().acquire();
        resources.incrementKioskUsage();

        resources.log(
                "Customer " + id + " started payment"
        );

        // 5% chance of kiosk failure
        if (random.nextInt(100) < 5) {

            resources.log(
                    "Customer " + id + " payment kiosk failed"
            );

            resources.log(
                    "Customer " + id + " retrying payment after 2 seconds"
            );

            Thread.sleep(2000);
        }

        // Payment takes 1–2 seconds
        int paymentTime = 1000 + random.nextInt(1001);
        Thread.sleep(paymentTime);

        resources.log(
                "Customer " + id + " payment completed"
        );

        resources.decrementPaymentQueue();
        resources.decrementKioskUsage();
        resources.getKiosks().release();
    }
}