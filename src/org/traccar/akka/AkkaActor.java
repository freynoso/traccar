package org.traccar.akka;

import org.traccar.helper.Log;
import org.traccar.model.Position;

import akka.actor.ActorSelection;
import akka.actor.UntypedActor;

public class AkkaActor extends UntypedActor {

	private ActorSelection remote;

	public AkkaActor(ActorSelection inActor) {
		remote = inActor;
	}

	@Override
	public void onReceive(Object message) {

		if (message instanceof Position) {
			Log.debug("Sending position to remote actor.");
			Position position = (Position) message;
			remote.tell(position, getSelf());
			//getSelf().tell(akka.actor.PoisonPill.getInstance(), getSelf());
			getContext().stop(getSelf());
		} else {
			Log.debug("Unknown message. " + message.toString());
		}

	}

}