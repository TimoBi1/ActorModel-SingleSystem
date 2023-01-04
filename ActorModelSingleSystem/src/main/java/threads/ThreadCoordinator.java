package threads;

import message.SegmentMessage;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadCoordinator {

    private static final int THREAD_COUNT = 12;

    private final List<Long> primeResults = new ArrayList<>();
    // Number of recieved results
    private int resultCount = 0;

    // List where all started threads should be added to
    // needed for accessing the thread objects
    // as there is no dynamic naming of variables in Java
    // and we need to iterate through this list to check whether all threads are finished
    private List<WorkerThread> threadList = new ArrayList<>();

    public ThreadCoordinator() {}

    public void startThreads(message.SegmentMessage sm) {

        System.out.println(new Timestamp(System.currentTimeMillis()));

        int segLength = (int) ((sm.getEnd() - sm.getStart()) / THREAD_COUNT);

        for (int cnt = 0; cnt < THREAD_COUNT; cnt++) {
            long rightBound = sm.getEnd();
            if (cnt != THREAD_COUNT - 1) {
                rightBound = sm.getStart() + (cnt + 1) * segLength;
            }
            // create new thread and send interval
            String name = "Thread " + cnt;
            // add to list as Java does not have dynamically naming of variables
            threadList.add(createThread(name, new SegmentMessage(sm.getStart() + cnt * segLength, rightBound)));
            // start created thread
            startThread(name);
            // check thread status
            // if terminated get Value
            // check if procedure is finished and if so sort results and writ them into file
            checkThreads();
        }

    }

    /***
     * Created a thread based on the given name and segmentMessage obj
     * @param name
     * @param sm
     * @return
     */
    private WorkerThread createThread(String name, SegmentMessage sm) {

        return new WorkerThread(name, sm);

    }

    /***
     * starts the thread by the given name
     * @param name
     */
    private void startThread(String name) {

        threadList.forEach( thread -> {
            if (thread.getName().equalsIgnoreCase(name)) {
                thread.start();
            }
        });

    }

    /***
     * check all threads
     * if terminated, remove thread from list
     */
    private void checkThreads() {
    	
    	// temporary list to save all threads that can be removed as they are finished.
    	// it's possible that there are more than one threads finished when calling this method
        List<WorkerThread> toRemove = new ArrayList<>();

        threadList.forEach( thread -> {
            if (thread.getState().equals(Thread.State.TERMINATED)) {
                primeResults.addAll(thread.getPrimes());
                toRemove.add(thread);
            }
            // wait for the last thread(s) to finish
            if (resultCount == THREAD_COUNT -1 && !thread.getState().equals(Thread.State.TERMINATED)) {
                try {
                    thread.join();
                    primeResults.addAll(thread.getPrimes());
                    toRemove.add(thread);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        
        threadList.removeAll(toRemove);
        try {
            checkIfFinished();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    /***
     * chesk if all segments are done
     * if so, print all results to a txt file and terminate the program
     * @throws FileNotFoundException
     */
    private void checkIfFinished() throws FileNotFoundException {

        if (++resultCount >= THREAD_COUNT) {
            Collections.sort(primeResults);
            final PrintWriter p = new PrintWriter("./output_threads.txt");
            p.println("Size: " + primeResults.size());
            primeResults.forEach(prime -> p.print(prime + ", "));
            p.close();
            System.out.println("Done.");
            System.out.println(new Timestamp(System.currentTimeMillis()));
            System.exit(0);
        }

    }

}
