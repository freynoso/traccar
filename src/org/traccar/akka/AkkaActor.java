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
			Position position = (Position) message;
			Log.debug("Sending position to remote actor.");
			remote.tell(position, getSelf());
			// getSelf().tell(akka.actor.PoisonPill.getInstance(), getSelf());
			getContext().stop(getSelf());
		}
	}

}