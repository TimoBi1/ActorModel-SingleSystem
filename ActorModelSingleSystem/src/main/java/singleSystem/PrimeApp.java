package singleSystem;

import akka.actor.UntypedActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import message.SegmentMessage;
import singleSystem.actor.PrimeMaster;

import java.sql.Timestamp;

public class PrimeApp extends UntypedActor {

    public static void main(String[] args) {

        //create the actor system and startup the PrimeMaster actor
        ActorSystem actorSystem = ActorSystem.create("system");
        ActorRef primeMaster = actorSystem.actorOf(Props.create(PrimeMaster.class), "master");

        //send the PrimeMaster actor the segment of numbers
        System.out.println(new Timestamp(System.currentTimeMillis()));
//        primeMaster.tell(new SegmentMessage(0, 100), actorSystem.actorOf(
//                Props.create(PrimeMaster.class), "app"));

        primeMaster.tell(new SegmentMessage(0, 400), actorSystem.actorOf(
                Props.create(PrimeMaster.class), "app"));


    }

    @Override
    public void onReceive(Object message) throws Throwable {

    }
}
