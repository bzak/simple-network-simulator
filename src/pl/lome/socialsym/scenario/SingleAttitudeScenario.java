package pl.lome.socialsym.scenario;

import pl.lome.gephi.GephiGraph;
import pl.lome.graph.grenchmark.step.Step;
import pl.lome.socialsym.ConfoundingStep;
import pl.lome.socialsym.DecomposeGraphStep;
import pl.lome.socialsym.InfectRandomlyStep;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class SingleAttitudeScenario extends SocialGrench {
	
	protected void executeSteps(GremlinScriptEngine engine) throws Exception {
		//Graph graph = new GephiGraph(this.graph);
		
		scaleFree().execute(engine, graph);
		initRandom(0).execute(engine, graph);
		markBestFriends().execute(engine, graph);
		
		Step homophily = homophily();
		Step contagion = contagion(1000, 0.005 , 0.005);		
		Step confounding = confounding();
		Step random = new InfectRandomlyStep(5, 0.2, "value");
		for (int i = 0; i < 1170; i++) {		
			random.execute(engine, graph);			
//			random.execute(engine, graph);			
//			homophily.execute(engine, graph);
//			confounding.execute(engine, graph);
			System.out.println("step="+i);
			if (i % 10 == 0) calculateCorrelations().execute(engine, graph);				
//			calculateCorrelations().execute(engine, graph);		
		}
		
		for (int i = 0; i < 7000; i++) {		
			homophily.execute(engine, graph);
			System.out.println("step="+i);
			if (i % 10 == 0) calculateCorrelations().execute(engine, graph);				
//			calculateCorrelations().execute(engine, graph);		
		}
				
		calculateCorrelations().execute(engine, graph);		
		
		printCorrelationsCSV();
		
		// decompose
		
		Step decompose = new DecomposeGraphStep(10, 0);
		for (int i = 0; i < 100; i++) {
			decompose.execute(engine, graph);
			calculateCorrelations().execute(engine, graph);		
		}
		
		printCorrelationsCSV();
		
		System.out.println("done.");	
	}
	
	public static void main(String[] args) throws Exception {				
		SingleAttitudeScenario scenario = new SingleAttitudeScenario();
		scenario.run();
	}
}
