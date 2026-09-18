import java.util.concurrent.Semaphore;

public class LaundryResources {

    private final Semaphore washers = new Semaphore(6, true);
    private final Semaphore dryers = new Semaphore(4, true);
    private final Semaphore kiosks = new Semaphore(2, true);
    private boolean paymentAvailable = true;

    private int customersServed = 0;
    private long totalProcessingTime = 0;

    private int currentWashersInUse = 0;
    private int maxWashersInUse = 0;

    private int currentDryersInUse = 0;
    private int maxDryersInUse = 0;

    private int currentKiosksInUse = 0;
    private int maxKiosksInUse = 0;

    private int paymentQueueLength = 0;
    private boolean ownerCalled = false;

    private LaundryGUI gui;

    public Semaphore getWashers() {
        return washers;
    }

    public Semaphore getDryers() {
        return dryers;
    }

    public Semaphore getKiosks() {
        return kiosks;
    }

    public void setGui(LaundryGUI gui) {
        this.gui = gui;
    }


    public void log(String message) {

        String logMessage =
                "[" + Thread.currentThread().getName() + "] " + message;

        System.out.println(logMessage);

        if (gui != null) {
            gui.appendLog(logMessage);
        }
    }
    public synchronized void disablePayment() {
        paymentAvailable = false;
    }

    public synchronized void enablePayment() {
        paymentAvailable = true;
        notifyAll();
    }

    public synchronized boolean isPaymentAvailable() {
        return paymentAvailable;
    }

    public synchronized void incrementWasherUsage() {

        currentWashersInUse++;

        if (currentWashersInUse > maxWashersInUse) {
            maxWashersInUse = currentWashersInUse;
        }
    }

    public synchronized void decrementWasherUsage() {
        currentWashersInUse--;
    }

    public synchronized int getCurrentWashersInUse() {
        return currentWashersInUse;
    }

    public synchronized int getMaxWashersInUse() {
        return maxWashersInUse;
    }

    public synchronized void incrementDryerUsage() {

        currentDryersInUse++;

        if (currentDryersInUse > maxDryersInUse) {
            maxDryersInUse = currentDryersInUse;
        }
    }

    public synchronized void decrementDryerUsage() {
        currentDryersInUse--;
    }

    public synchronized int getCurrentDryersInUse() {
        return currentDryersInUse;
    }

    public synchronized int getMaxDryersInUse() {
        return maxDryersInUse;
    }

    public synchronized void incrementKioskUsage() {

        currentKiosksInUse++;

        if (currentKiosksInUse > maxKiosksInUse) {
            maxKiosksInUse = currentKiosksInUse;
        }
    }

    public synchronized void decrementKioskUsage() {
        currentKiosksInUse--;
    }

    public synchronized int getCurrentKiosksInUse() {
        return currentKiosksInUse;
    }

    public synchronized int getMaxKiosksInUse() {
        return maxKiosksInUse;
    }

    public synchronized void recordCustomerDone(long processingTime) {

        customersServed++;
        totalProcessingTime += processingTime;
    }

    public synchronized int getCustomersServed() {
        return customersServed;
    }

    public synchronized long getTotalProcessingTime() {
        return totalProcessingTime;
    }

    public synchronized double getAverageTimeSeconds() {

        if (customersServed == 0) {
            return 0;
        }

        return totalProcessingTime / 1000.0 / customersServed;
    }

    public synchronized void incrementPaymentQueue() {

        paymentQueueLength++;

        System.out.println(
                "Payment queue: " + paymentQueueLength
        );

        if (paymentQueueLength >= 30 && !ownerCalled) {

            ownerCalled = true;

            System.out.println();
            System.out.println(
                    "*** OWNER CALLED IN — KIOSKS TURNING ON ***"
            );
            System.out.println();

            enablePayment();
        }
    }

    public synchronized void decrementPaymentQueue() {

        if (paymentQueueLength > 0) {
            paymentQueueLength--;
        }
    }

    public synchronized int getPaymentQueueLength() {
        return paymentQueueLength;
    }

    public synchronized boolean isOwnerCalled() {
        return ownerCalled;
    }
}