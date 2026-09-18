import java.util.Random;

public class Main {

    static final boolean CONGESTED_MODE = true;

    public static void main(String[] args) throws InterruptedException {

        long simulationStartTime = System.currentTimeMillis();

        Random random = new Random();

        LaundryResources resources = new LaundryResources();

        if (CONGESTED_MODE) {
            resources.disablePayment();
        }

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "       SMART LAUNDRY FACILITY — SIMULATION"
        );

        System.out.println(
                "=================================================="
        );

        System.out.println();

        // Create the GUI
        LaundryGUI gui = new LaundryGUI(resources);
        resources.setGui(gui);
        gui.show();

        // Create 50 customers
        Thread[] customerThreads = new Thread[50];

        for (int customerId = 1; customerId <= 50; customerId++) {

            Customer customer = new Customer(
                    customerId,
                    resources,
                    CONGESTED_MODE
            );

            customerThreads[customerId - 1] =
                    new Thread(
                            customer,
                            "Customer-" + customerId
                    );

            customerThreads[customerId - 1].start();

            // Customers arrive at random intervals
            int arrivalDelay = random.nextInt(3001);

            Thread.sleep(arrivalDelay);
        }

        // Wait for all customers to finish
        for (Thread customerThread : customerThreads) {
            customerThread.join();
        }

        // Calculate actual simulation runtime
        long simulationRuntime =
                System.currentTimeMillis() - simulationStartTime;

        System.out.println();

        System.out.println(
                "=================================================="
        );

        System.out.println(
                "           SIMULATION COMPLETED"
        );

        System.out.println(
                "=================================================="
        );

        System.out.println();

        System.out.printf(
                "Total customers served      : %d%n",
                resources.getCustomersServed()
        );

        System.out.printf(
                "Total Processing Time       : %d seconds%n",
                resources.getTotalProcessingTime() / 1000
        );

        System.out.printf(
                "Average total time/customer : %.2f seconds%n",
                resources.getAverageTimeSeconds()
        );

        System.out.printf(
                "Simulation Runtime          : %.2f seconds%n",
                simulationRuntime / 1000.0
        );

        System.out.printf(
                "Max concurrent washers      : %d%n",
                resources.getMaxWashersInUse()
        );

        System.out.printf(
                "Max concurrent dryers       : %d%n",
                resources.getMaxDryersInUse()
        );

        System.out.printf(
                "Max concurrent kiosks       : %d%n",
                resources.getMaxKiosksInUse()
        );

        System.out.println();

        System.out.println(
                "=================================================="
        );
    }
}