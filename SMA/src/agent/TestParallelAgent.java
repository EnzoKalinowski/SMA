package agent;

import functions.MyFunction;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.*;
import functions.MyFunction;
import agent.ComputeAgent;
import java.util.concurrent.TimeUnit;
/**
 * 
 *  Agent calling ComputeAgents for compute sub-integrals and calculate integral to compare computing speed
 *
 */
public class TestParallelAgent extends Agent {

	protected void setup() {
		//sleep 1 second for waiting ComputerAgent creation
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		//read agent arguments
		Object[] arguments = getArguments();
				
		String function="";
		double min=0,max=0,delta=0;
		
		if((arguments != null) && (arguments.length==4)) {
			 function= (String)arguments[0];
			 min = Double.parseDouble((String)arguments[1]);
			 max = Double.parseDouble((String)arguments[2]);
			 delta = Double.parseDouble((String)arguments[3]);
		}else {
			throw new IllegalArgumentException("Unexpected value: " + arguments.toString());
		}
		
		System.out.println("Arguments: "+function+","+min+","+max+","+delta);

		AID[] agents = null;
		
		try {
			// looking for calculator service : return current agent
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd = new ServiceDescription();
			sd.setType("calculator");
			dfd.addServices(sd);
			DFAgentDescription[] result = DFService.search(this, dfd);
			agents = new AID[result.length];
			
			for (int i = 0; i < result.length; i++) {
				agents[i] = result[i].getName();
			}	
		System.out.println("Search returns: " + agents.length + " elements");
		
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
		int nbAgent = agents.length;
		double intervalSize = (max - min) / nbAgent;
		double aMin,aMax;
		
		//local computing 
		long tstart = System.currentTimeMillis();
		double resultLocal = 0;
		for(int i = 0; i < agents.length; i++) {
			aMin=min+i*intervalSize;
			aMax=min+(i+1)*intervalSize-delta;
			switch (function) {
				case "MyFunction": {
					MyFunction f=new MyFunction(aMin, aMax, delta);
					resultLocal = resultLocal + f.eval();
	
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + function);
			}
			
			
		}
		long tend = System.currentTimeMillis();
		long localComputeDuration = tend-tstart;
		
		System.out.println("\nR??sultat du calcul local:"+resultLocal);
		System.out.println("Dur??e du calcul local: "+localComputeDuration+"\n");
		
		//distributed computing
		long tStartDistrib = System.currentTimeMillis();
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		
		for (int i = 0; i < agents.length; i++) { 
		    AID agentAid = agents[i]; 
		    aMin=min+i*intervalSize;
			aMax=min+(i+1)*intervalSize-delta;
		    message = new ACLMessage(ACLMessage.INFORM);
		    message.setContent("MyFunction,"+aMin+","+aMax+","+delta);
		    message.addReceiver(agentAid);
		    send(message); 
	    }
					
		addBehaviour(new SimpleBehaviour() {
			
			private int i = 0;
			
			private double total = 0.0;
			
			@Override
			public void action() {
				//waiting for ComputeAgent replies
				ACLMessage msg= receive();

                if (msg!=null) {
                	total += Double.parseDouble(msg.getContent());
                	i+=1;
                }
                else {
                	block();
                }
				
			}
			
			@Override
			public boolean done() {
				//all agents replied
				return i >= nbAgent;
			}

			@Override
			public int onEnd() {
				//result of distributed computing
				long tEndDistrib = System.currentTimeMillis();
				long distribComputeDuration = tEndDistrib-tStartDistrib;
				System.out.println("\nR??sultat du calcul distribu??: "+total);
				System.out.println("Dur??e calcul distribu?? "+distribComputeDuration+"\n");
				return 0;
			}			
		});
	}

	/**
	 * Useful wrapper for registering services : hides the usage of the DFD
	 */
	void register(ServiceDescription sd) {
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.setName(getAID());
		dfd.addServices(sd);

		try {
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
