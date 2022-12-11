package agent;

import functions.Function;
import functions.MyFunction;
import jade.core.behaviours.*;
import jade.core.Agent;
import jade.lang.acl.*;


public class ComputeAgent extends Agent{
	protected Function f;
	
	protected void setup() {
		addBehaviour(new CyclicBehaviour() {
			
			public void action() {
				ACLMessage msg =receive();
				if(msg!=null) {
					System.out.println(" - "+ myAgent.getLocalName()+" <- "+ msg.getContent());
					
					String[] splitMsg= msg.getContent().split(",");
					
					double min = Double.parseDouble(splitMsg[1]);
					double max = Double.parseDouble(splitMsg[2]);
					double delta = Double.parseDouble(splitMsg[3]);
					
					switch (splitMsg[0]) {
					
						case "MyFunction": {
							f=new MyFunction(Double.parseDouble(splitMsg[0]), Double.parseDouble(splitMsg[1]), Double.parseDouble(splitMsg[2]));
	
							break;
						}
						default:
							throw new IllegalArgumentException("Unexpected value: " + splitMsg[0]);
					}
					
					double computeResult=f.eval();
					
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(""+computeResult);
					send(reply);
				}
				block();
			}
		});
	}
	
	protected void takeDown() {
		
	}
	
}
