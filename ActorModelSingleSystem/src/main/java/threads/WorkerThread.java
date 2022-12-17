package threads;

import message.SegmentMessage;

import java.util.ArrayList;
import java.util.List;

public class WorkerThread extends Thread {

    private List<Long> primes = new ArrayList<>();

    private SegmentMessage sm;

    public WorkerThread(String name, SegmentMessage sm) {
        this.setName(name);
        this.sm = sm;
    }

    public void run() {

        System.out.println(this.getName() + " is running!");

        for (long i = sm.getStart(); i < sm.getEnd(); i++) {
            // add to result, if element is a prime number
            if (isPrime(i)) {
                primes.add(i);
            }
        }

        System.out.println(this.getName() + " is done!");

    }

    /**
     * @return whether n is a prime number
     */
    private boolean isPrime(long n) {

        if (n <= 1) {
            return false;
        }

        for (int j = 2; j <= n / 2; j++) {
            if (n % j == 0) {
                return false;
            }
        }
        return true;
    }

    public List<Long> getPrimes() {
        return primes;
    }
}
