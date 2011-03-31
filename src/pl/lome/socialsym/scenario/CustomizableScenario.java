package pl.lome.socialsym.scenario;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;

import pl.lome.gephi.GephiGraph;
import pl.lome.graph.grenchmark.step.Step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class CustomizableScenario extends SocialGrench {
	public int networkSize;
	public int infected;
	public boolean contagion;
	
	public int contagionMultiplier;
	public double contagionWeight;
	public double contagionMutual;

	public boolean homophily;
	public int homophilyMultiplier;

	public boolean confounding;
	public int confoundingMultiplier;
	public double confoundingWeight;
	
	public boolean outputGephi;
	public boolean outputCSV;
	public String outputCVSfilename; 
	
	private final PropertyChangeSupport support = new PropertyChangeSupport(this);

	  // Provide delegating methods to add / remove listeners to / from the support class.  
	public void addPropertyChangeListener(PropertyChangeListener l) {
	   support.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
	   support.removePropertyChangeListener(l);
	}

	  
	protected void executeSteps(GremlinScriptEngine engine) throws Exception {
		Graph graph = this.graph;
		if (outputGephi) {
			graph = new GephiGraph(this.graph);
		}
		
		scaleFree(networkSize).execute(engine, graph);
		initRandom(infected).execute(engine, graph);
		markBestFriends().execute(engine, graph);
		
		Step homophily = homophily(homophilyMultiplier);
		Step contagion = contagion(contagionMultiplier, contagionWeight, contagionMutual);		
		Step confounding = confounding(confoundingWeight, confoundingMultiplier);
		
		for (int i = 0; i < 100; i++) {
			if (this.contagion) {
				contagion.execute(engine, graph);
			}
			if (this.homophily) {
				homophily.execute(engine, graph);
			}
			if (this.confounding) {
				confounding.execute(engine, graph);
			}			
			calculateCorrelations().execute(engine, this.graph);		
			support.firePropertyChange("step", i, i+1);
		}
				
		calculateCorrelations().execute(engine, graph);		
		
		if (this.outputCSV) {
			PrintStream stream = null;
			try {
				FileOutputStream out = new FileOutputStream(this.outputCVSfilename);
                stream = new PrintStream( out );    			
    			printCorrelations(stream);
			}
			finally {
				if (stream != null) stream.close();
			}
		}
				
		System.out.println("done.");	
	}
}
