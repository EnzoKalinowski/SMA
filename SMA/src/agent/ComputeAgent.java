package agent;

import functions.Function;
import functions.MyFunction;
import jade.core.behaviours.*;
import jade.core.Agent;
import jade.lang.acl.*;
import jade.domain.*;
import jade.domain.FIPAAgentManagement.*;


/**
 * 
 * @brief Agent calculating integrals 
 *
 */
public class ComputeAgent extends Agent{
	protected Function f;
	
	/**
	 * 
	 */
	protected void setup() {
		//registration to the service "calculator"
		ServiceDescription sd = new ServiceDescription();
		
		sd.setType("calculator");
		sd.setName(getLocalName());
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);
		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		//behaviour of waiting integral to compute
		addBehaviour(new CyclicBehaviour(this) {
			
			public void action() {
				ACLMessage msg = receive();
				if(msg!=null) {
					System.out.println(" - "+ myAgent.getLocalName()+" <- "+ msg.getContent());
					
					String[] splitMsg= msg.getContent().split(",");
					
					double min = Double.parseDouble(splitMsg[1]);
					double max = Double.parseDouble(splitMsg[2]);
					double delta = Double.parseDouble(splitMsg[3]);
					System.out.println(splitMsg[0]);
					switch (splitMsg[0]) {
					
						case "MyFunction": {
							f=new MyFunction(min, max, delta);
	
							break;
						}
						default:
							throw new IllegalArgumentException("Unexpected value: " + splitMsg[0]);
					}
					
					double computeResult=f.eval();
					
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(""+computeResult);
//					System.out.println(reply);
					send(reply);//reply the integral result
					
				} else {
					block();
				}
			}
		});
	}
	

	protected void takeDown() {
		System.out.println("Deregister Compute Agent");
		try {
			DFService.deregister(this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
