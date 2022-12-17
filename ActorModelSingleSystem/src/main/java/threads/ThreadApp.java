package threads;

import message.SegmentMessage;

public class ThreadApp {

    public static void main(String[] args) {

        ThreadCoordinator coordinator = new ThreadCoordinator();

//        coordinator.startThreads(new SegmentMessage(0, 100));

        coordinator.startThreads(new SegmentMessage(0, 4000));

    }

}
