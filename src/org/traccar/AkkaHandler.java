package org.traccar;

import java.util.List;

import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.traccar.akka.AkkaSupervisorActor;
import org.traccar.helper.Log;
import org.traccar.model.Position;

@ChannelHandler.Sharable
public class AkkaHandler extends SimpleChannelHandler {

	/**
	 * Akka Client System
	 */
	private AkkaSupervisorActor akkaSupervisorActor;

	AkkaHandler(AkkaSupervisorActor newAkkaSupervisorActor) {
		super();
		akkaSupervisorActor = newAkkaSupervisorActor;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		if (e.getMessage() instanceof Position) {
			sendPosition((Position) e.getMessage());
		} else if (e.getMessage() instanceof List) {
			List<Position> positions = (List<Position>) e.getMessage();
			for (Position position : positions) {
				sendPosition(position);
			}
		}
	}

	private void sendPosition(Position position) {
		try {
			akkaSupervisorActor.send(position);
		} catch (Exception error) {
			Log.warning(error);
		}
	}

}
