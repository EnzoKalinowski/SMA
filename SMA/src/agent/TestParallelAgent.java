package agent;

import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.*;

public class TestParallelAgent extends Agent{
	
	  protected void setup() {
		    // registering calculateur service for current agent
		    ServiceDescription sd  = new ServiceDescription();
		    try {
		        sd.setType( getArguments()[0].toString() );
		    }
		    catch(Exception e){
		        sd.setType( "calculateur" );
		    }
		    sd.setName( getLocalName() );
		    register( sd );

		    try {
		      // looking for any service : return current agent
		      DFAgentDescription dfd = new DFAgentDescription();
		      DFAgentDescription[] result = DFService.search(this, dfd);

		      System.out.println("Generic search returns: " + result.length 
		                         + " elements" );
		      if (result.length>0)
		        System.out.println(" " + result[0].getName() );

		      // looking for calculateur service : return current agent
		      sd  = new ServiceDescription();
		      sd.setType( "calculateur" );
		      dfd.addServices(sd);
		      result = DFService.search(this, dfd);
		      System.out.println("Search for calculateur: " + result.length + " elements" );
		      if (result.length>0)
		        System.out.println(" " + result[0].getName() );

		      // finally, looking for (non-existent) SELLER agent
		      sd.setType( "seller" );
		      result = DFService.search(this, dfd);
		      if (result==null) System.out.println("Search1 returns null");
		      else {
		        System.out.println("Search for SELLER: " + result.length 
		                           + " elements" );
		        if (result.length>0)
		          System.out.println(" " + result[0].getName() );
		      }
		    }
		    catch (FIPAException fe) { fe.printStackTrace(); }

		    System.exit(0);
		  }

		  /**
		   * Useful wrapper for registering services : hides the usage of the
		   * DFD
		   */
		  void register( ServiceDescription sd) {
		    DFAgentDescription dfd = new DFAgentDescription();
		    dfd.setName(getAID());
		    dfd.addServices(sd);

		    try {
		      DFService.register(this, dfd );
		    }
		    catch (FIPAException fe) { fe.printStackTrace(); }
	  }

}
