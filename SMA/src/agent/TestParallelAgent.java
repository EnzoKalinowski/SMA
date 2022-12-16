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
		double min=0 ,max=1 ,delta=0.1;
		double intervalSize = (max - min) / nbAgent;
		double aMin,aMax;
		
		
		for(int i = 0; i < agents.length; i++) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			aMin=min+i*intervalSize;
			aMax=min+(i+1-delta)*intervalSize;
			msg.setContent("MyFunction"+aMin+","+aMax+","+delta);
			msg.addReceiver(agents[i]);
			send(msg);
		}
		long tstart = System.currentTimeMillis();
		//TODO a faire le calcul en local sans behaviours !!!
		//TODO afficher le premier temps 
		long tend = System.currentTimeMillis();
		long tempsUn = tend-tstart;
		System.out.println("duree une = "+tempsUn);
		//agnet.lenth --> nb d'agents retournes dispos 
		double resultUn = 0;
		double resultDeux = 0;
		for(int i = 0; i < agents.length; i++) {
			aMin=min+i*intervalSize;
			aMax=min+(i+1-delta)*intervalSize;
			MyFunction f = new MyFunction(aMin,aMax,delta);
			resultUn = resultUn + f.eval();
			
		}
		aMin = 0;
		aMax = 0;
		//TODO nouveau timer 
		long tstartDeux = System.currentTimeMillis();
		ACLMessage message = new ACLMessage(ACLMessage.INFORM);
		//TODO dans le for agent de i  on envoie le mess de requette 
		for (int i = 0; i < agents.length; i++) { 
		    AID agentAid = agents[i]; 
		    message = new ACLMessage(ACLMessage.INFORM);
		    //message.setContent(generateMessageContent(agentArguments, range));
		    message.setContent("MyFunction,"+aMin+","+aMax+","+delta);
		    message.addReceiver(agentAid);
		    send(message); 
		    }
		long tendDeux = System.currentTimeMillis();
		
		// !! ON PARLE DU RETOUR ICI 
		
		addBehaviour(new SimpleBehaviour() {
			
			private int i = 0;
			
			private double total = 0.0;
			
			@Override
			public void action() {
				// TODO Auto-generated method stub  ici faire le recieve + calcul
				// enlever cette erreur que je ne comprend pas !!!
				
				ACLMessage msg= receive();
                if (msg!=null) {
                	total += Double.parseDouble(msg.getContent());
                }
                else {
                	block();
                }
                // block() is not necessary here, but it saves resources:
                // the behaviour will not be scheduled until a message is received
                // (which wakes up all behaviours)
                
                
                
				
			}
			
			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return i >= nbAgent;
			}

			@Override
			public int onEnd() {
				long tfin = System.currentTimeMillis();
				long duration = tendDeux-tstartDeux;
				System.out.println("duree deux = "+duration);
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
