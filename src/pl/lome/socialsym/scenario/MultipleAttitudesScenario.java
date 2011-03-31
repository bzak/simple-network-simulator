package pl.lome.socialsym.scenario;

import pl.lome.gephi.GephiGraph;
import pl.lome.graph.grenchmark.step.Step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class MultipleAttitudesScenario extends SocialGrench {
	protected void executeSteps(GremlinScriptEngine engine) throws Exception {
		Graph graph = new GephiGraph(this.graph);
		
		scaleFree().execute(engine, graph);
		initRandom().execute(engine, graph);
		initRandom(100, "attitude_B").execute(engine, graph);
		initRandom(100, "attitude_C").execute(engine, graph);
		initRandom(100, "attitude_D").execute(engine, graph);
		initRandom(100, "attitude_E").execute(engine, graph);
		markBestFriends().execute(engine, graph);
		
		Step homophily = homophily(3);
		Step homophilyB = homophily(3, "attitude_B");
		Step homophilyC = homophily(3, "attitude_C");
		Step homophilyD = homophily(3, "attitude_D");
		Step homophilyE = homophily(2, "attitude_E");		
		Step contagion = contagion();
		
		for (int i = 0; i < 500; i++) {		
			if (i % 10 == 0) calculateCorrelations().execute(engine, this.graph);
			
			//contagion.execute(engine, graph);			
			homophily.execute(engine, graph);
			homophilyB.execute(engine, graph);
			homophilyC.execute(engine, graph);
			homophilyD.execute(engine, graph);			
			homophilyE.execute(engine, graph);
//			confounding.run();
			System.out.println("step="+i);
		}
				
		calculateCorrelations().execute(engine, this.graph);		
		
		printCorrelationsCSV();
		
		System.out.println("done.");	
	}
	
	public static void main(String[] args) throws Exception {				
		MultipleAttitudesScenario scenario = new MultipleAttitudesScenario();
		scenario.run();
	}
}
