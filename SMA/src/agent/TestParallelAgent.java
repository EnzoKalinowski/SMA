package agent;

import functions.MyFunction;
import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.*;
import functions.MyFunction;
import agent.ComputeAgent;
/**
 * 
 * @brief Agent calling ComputeAgents for compute sub-integrals and calculate integral to compare computing speed
 *
 */
public class TestParallelAgent extends Agent {

	protected void setup() {
		// registering calculateur service for current agent
		Object[] arguments = getArguments();
		
		//cast les arguments function,min,max,delta
		
		String function="";
		double min=0,max=0,delta=0;
		
		if((arguments != null) && (arguments.length==4)) {
			 function= (String)arguments[0];
			 min = Double.parseDouble((String)arguments[1]);
			 max = Double.parseDouble((String)arguments[2]);
			 delta = Double.parseDouble((String)arguments[3]);
		}
		
		System.out.println(function+","+min+","+max+","+delta);
		
		ServiceDescription sd = new ServiceDescription();
		sd.setType("calculateur");

		AID[] agents = null;

		try {
			// looking for any service : return current agent
			DFAgentDescription dfd = new DFAgentDescription();
			dfd.addServices(sd);
			DFAgentDescription[] result = DFService.search(this, dfd);
			agents = new AID[result.length];
			for (int i = 0; i < result.length; i++) {
				agents[i] = result[i].getName();
		}	

		System.out.println("Generic search returns: " + agents.length + " elements");

		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		int nbAgent = agents.length;
		double intervalSize = (max - min) / nbAgent;
		double aMin,aMax;
		
		long tstart = System.currentTimeMillis();
		double resultUn = 0;

		for(int i = 0; i < agents.length; i++) {
			aMin=min+i*intervalSize;
			aMax=min+(i+1-delta)*intervalSize;
			switch (function) {
				case "MyFunction": {
					MyFunction f=new MyFunction(min, max, delta);
					resultUn = resultUn + f.eval();
	
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: " + function);
			}
			
			
		}
		long tend = System.currentTimeMillis();
		long tempsUn = tend-tstart;
		System.out.println("Durée calcul standard = "+tempsUn);
		
		aMin = 0;
		aMax = 0;
		long tstartDeux = System.currentTimeMillis();
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		
		System.out.println("Avant l'envoie aux agents");
		for (int i = 0; i < agents.length; i++) { 
			System.out.println("i:"+i);
		    AID agentAid = agents[i]; 

		    message = new ACLMessage(ACLMessage.INFORM);
		    //message.setContent(generateMessageContent(agentArguments, range));
		    message.setContent("MyFunction,"+aMin+","+aMax+","+delta);
		    message.addReceiver(agentAid);
		    send(message); 
		    System.out.println("Messages envoyé à l'agent"+ i);
	    }
					
		addBehaviour(new SimpleBehaviour() {
			
			private int i = 0;
			
			private double total = 0.0;
			
			@Override
			public void action() {
				// TODO Auto-generated method stub 
				
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
				// TODO Auto-generated method stub
				return i >= nbAgent;
			}

			@Override
			public int onEnd() {
				long tendDeux = System.currentTimeMillis();
				long duration = tendDeux-tstartDeux;
				System.out.println("Durée calcul distribué "+duration);
				return 0;
			}			
		});
	
		System.exit(0);
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
