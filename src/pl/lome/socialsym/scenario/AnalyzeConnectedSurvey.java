package pl.lome.socialsym.scenario;

import pl.lome.graph.grenchmark.step.LoadGraphMLStep;
import pl.lome.graph.grenchmark.step.SaveGraphMLStep;
import pl.lome.graph.grenchmark.step.Step;

import com.tinkerpop.gremlin.GremlinScriptEngine;

public class AnalyzeConnectedSurvey extends SocialGrench {
	
	protected void executeSteps(GremlinScriptEngine engine) throws Exception {

		loadGraphML("/home/blazej/workspace/SocialSym.bak/vis/connectedSurvey/ecology.graphml").execute(engine, graph);
						
		calculateCorrelations().execute(engine, graph);		
		
		printCorrelationsCSV();
		
		new SaveGraphMLStep("/home/blazej/workspace/SocialSym.bak/vis/connectedSurvey/calculated.graphml").execute(engine, graph);
		
		System.out.println("done.");	
	}
	
	public static void main(String[] args) throws Exception {				
		AnalyzeConnectedSurvey scenario = new AnalyzeConnectedSurvey();
		scenario.run();
	}
}
