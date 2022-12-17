package singleSystem.actor;

import akka.actor.UntypedActor;
import message.PrimeResult;
import message.SegmentMessage;

import java.util.ArrayList;
import java.util.List;

public class PrimeWorker  extends UntypedActor {

    @Override
    public void onReceive(Object msg) throws Exception {
        // receive partition from PrimeMaster
        if (msg instanceof SegmentMessage) {
            List<Long> primes = new ArrayList<>();
            for (long i = ((SegmentMessage) msg).getStart(); i <
                    ((SegmentMessage) msg).getEnd(); i++) {
                // add to result, if element is a prime number
                if (isPrime(i)) {
                    primes.add(i);
                }
            }
            // send resulting subset of primes to PrimeMaster
            this.getSender().tell(new PrimeResult(primes),
                    this.getSelf());
        }
    }

    // 	not the fastest algorithm but works
    /**
     * @return whether n is a prime number or not
     */
    private boolean isPrime(long n) {

    	// if n <= 1 -> n not prime
        if (n <= 1) {
            return false;
        }

        // we only have to check if n modulo j == 0 for values that are
        // smaller than or equal n / 2
        // values above n / 2 are multiple of values below n / 2!
        // takes more time for bigger numbers!!
        for (int j = 2; j <= n / 2; j++) {
            if (n % j == 0) {
                return false;
            }
        }
        // if nothing of the if clauses above are true
        // n is a prime!
        return true;
    }


}
