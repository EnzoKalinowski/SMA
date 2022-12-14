package agent;

import jade.core.*;
import jade.core.behaviours.*;
import jade.domain.FIPAAgentManagement.*;
import jade.lang.acl.*;
import jade.domain.*;
import functions.Function;
import ComputeAgent;

public class testEwenn extends Agent {
	protected void setup() {
	    int min;
	    int max;
	    double delta;
	    float pas;
	    try {
			DFAgentDescription[] result = Search.setup();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //tab = appel de bihaviour search
	    //appel des deux behaviours parallels 
	    
	    addBehaviour( new ParallelBehaviour( this, min, max, delta, pas, result ) );
	  }
}

class ParallelBehaviour extends SimpleBehaviour
{

  /** keep track of current time when program is started */
  protected static long   t0 = System.currentTimeMillis();
  protected static long   t1;
  protected static long   t2;

  /** for indentation mechanism: the initial spaces */
  protected String tab;
  
  protected int min;
  
  protected int max;
  
  protected double delta;
  
  protected float pas;
  
  protected DFAgentDescription[] result;
  
  protected float resultat;

  /** constructor for this behaviour.
   * @param a the agent
   * @param nbIter the number of iteration before disapearing
   * @param dt the delay between two iterations
   */
  public ParallelBehaviour( Agent a, int min, int max, double delta, float pas, DFAgentDescription result ) {
    super(a);

  }

  /** display message, then block for the indicated delay.
   * When blocked, the behaviour is out of the waiting queue.
   * It can be brought back to the queue when a message is received,
   *   and when the agent is restarted. <p>
   * Notice that block() is not sleep(): the delay is relative to the time
   *   of <em>next</em> execution of the method. Moreover, two subsequent
   *   calls to block() will not bring any difference with a single call.
   */
  public void action()
  {
	  addBehaviour( new CalculateBehaviour( this, MyFunction,  mina,  maxa,  delta ) );
	  for (float i =min;i<max;i= i+pas) {
		  addBehaviour( new CalculateBehaviour( this, MyFunction,  mina,  maxa,  delta ) );
	  }
  }

  /** Only return true after the given number of iterations */
  public  boolean done() {  
	  return resultat;
  }

}

class Search extends Agent {


	  /**
	   * The method creates and register a service (name taken from 
	   * command line, default BUYER) for this agent,
	   *    then searches the DF for BUYER and SELLER services
	   */
	  protected void setup() {

	    // registering BUYER service for current agent
	    ServiceDescription sd  = new ServiceDescription();
	    try {
	        sd.setType( getArguments()[0].toString() );
	    }
	    catch(Exception e){
	        sd.setType( "buyer" );
	    }
	    sd.setName( getLocalName() );
	    register( sd );

	    try {
	      // looking for any service : return current agent
	      DFAgentDescription dfd = new DFAgentDescription();
	      DFAgentDescription[] result = DFService.search(this, dfd);

	      // looking for BUYER service : return current agent
	      sd  = new ServiceDescription();
	      sd.setType( "calculator" );
	      dfd.addServices(sd);
	      result = DFService.search(this, dfd);
	      System.out.println("Search for calculator: " + result.length + " elements" );
	      if (result.length>0)
	        System.out.println(" " + result[0].getName() );
		  Search.resultat(result);

	    }
	    catch (FIPAException fe) { fe.printStackTrace(); }
	    System.exit(0);
	  }

	  DFAgentDescriptio resultat(DFAgentDescription[] result) {
		  return result;
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

class CalculateBehaviour extends SimpleBehaviour
{

	protected Fonction MyFunction;
	protected float mina;
	protected float maxa;
	protected float delat;
	
	 public CalculateBehaviour( Agent a, Fonction MyFunction, float mina, float maxa, float delta ) {
		    super(a);

		  }
	@Override
	public void action() {
		// TODO Auto-generated method stub
		f = new MyFunction(min,max,delta);
		float result = f.eval();
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return f;
	}
}

