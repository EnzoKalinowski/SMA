package agent;

import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.*;

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

		//TODO faire le découpage 
		//TODO a faire le calcul en local sans behaviours !!!
		//TODO afficher le premier temps 
		//agnet.lenth --> nb d'agents retournes dispos 
		
		//TODO nouveau timer 
		long tstart = System.currentTimeMillis();
		//TODO dans le for agent de i  on envoie le mess de requette 
		
		// !! ON PARLE DU RETOUR ICI 
		
		addBehaviour(new SimpleBehaviour() {
			
			private int i = 0;
			
			private double total = 0.0;
			
			@Override
			public void action() {
				// TODO Auto-generated method stub  ici fqire le recieve + calcul
				
			}
			
			@Override
			public boolean done() {
				// TODO Auto-generated method stub
				return i >= nbAgent;
			}

			@Override
			public int onEnd() {
				long tfin = System.currentTimeMillis();
				long duration = tstart-tfin;
				System.out.println("duree = "+duration);
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
