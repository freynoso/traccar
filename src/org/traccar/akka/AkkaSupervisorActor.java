package org.traccar.akka;

import java.util.Properties;

import org.traccar.helper.Log;
import org.traccar.model.Position;
import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Props;
import com.typesafe.config.ConfigFactory;

public class AkkaSupervisorActor {
	
	private final ActorSystem system;
	private ActorSelection remoteActor;

	public AkkaSupervisorActor(Properties props) {

		system = ActorSystem.create("AkkaActor", 
										ConfigFactory.parseString(
											props.getProperty("akkaActor.config")
										).getConfig("akkaActor")
									);
		remoteActor = system.actorSelection(props.getProperty("akkaActor.remoteActorPath"));
			
	}
	
	public void send(Position position) {
		// Create a local actor and pass the reference of the remote actor
		ActorRef actor = system.actorOf(Props.create(AkkaActor.class, remoteActor));
		Log.debug("New actor: " + actor.hashCode());
		// Send position to the local client actor
		actor.tell(position, ActorRef.noSender()); 
	}

}
