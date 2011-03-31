package pl.lome.socialsym;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraph;

import pl.lome.gephi.GephiGraph;
import pl.lome.graph.grenchmark.impl.Grenchmark;
import pl.lome.graph.grenchmark.impl.TinkerBench;
import pl.lome.graph.grenchmark.step.CommitTransactionStep;
import pl.lome.graph.grenchmark.step.GremlinStep;
import pl.lome.graph.grenchmark.step.SaveGraphMLStep;
import pl.lome.graph.grenchmark.step.SaveGraphStep;
import pl.lome.graph.grenchmark.step.SaveGraphStep.Options;
import pl.lome.graph.grenchmark.step.StartTransactionStep;
import pl.lome.graph.grenchmark.step.XsltTransformStep;

public class SocialSym {
	static CalculateCorellationStep corr;
	static CalculateFOAFCorrelationStep foaf;
	static CalculateFOAF2Correlation foaf2;
	
	private static Grenchmark scalefreeBench(Grenchmark grench) {
		grench.AddStep(new ScaleFreeNetworkStep(1000), "ScaleFree");				
		return grench;
	}
	
	private static Grenchmark initRandomBench(Grenchmark grench) {
		grench.AddStep(new ConfoundingStep(100, 1, 1), "Cofounding");		
		return grench;
	}
	
	private static Grenchmark confoundingBench(Grenchmark grench) {
//		grench.AddStep(new ConfoundingStep(5, 0.05, 0.2), "Cofounding");		
		for (int i = 0; i < 10; i++) {
			grench.AddStep(new Confounding2NodesStep(0.2, 1), "Cofounding");					
		}
//		for (int i = 0; i < 20; i++) {
//			grench.AddStep(new Confounding2NodesStep(0.5), "Cofounding");					
//		}
		return grench;
	}
	

	private static Grenchmark contagionBench(Grenchmark grench) {				
		grench.AddStep(new BestFriendsContagion(1000, 0.05, 0.1), "Contagion");
		
		return grench;
	}
	
	private static Grenchmark homophilyBench(Grenchmark grench) {						
		grench.AddStep(new HomophilyStep(1), "Homophily");
		return grench;
	}
	
	private static Grenchmark clearAttitudesBench(Grenchmark grench) {						
		grench.AddStep(new ClearValuesStep(), "Clear");
		return grench;
	}
	
	private static Grenchmark bestFriendsBench(Grenchmark grench) {						
		grench.AddStep(new MarkBestFriendsStep(0.5, 0.5), "BestFriends");
		return grench;
	}

	private static Grenchmark correlations(Grenchmark grench) {						
		corr = new CalculateCorellationStep(); grench.AddStep(corr, "Correlations");				
		foaf = new CalculateFOAFCorrelationStep(); grench.AddStep(foaf, "FOAF");
		foaf2 = new CalculateFOAF2Correlation(); grench.AddStep(foaf2, "FOAF2");
		return grench;
	}
	
	private static Grenchmark saveGraphBench(Grenchmark grench) {
		grench.AddStep(new SaveGraphMLStep("vis/social.graphml"), "GraphML");
		grench.AddStep(new XsltTransformStep("vis/social.graphml","xslt/nodeAttributesCSV.xslt","vis/correlations.csv"), "CSV");
		grench.AddStep(new XsltTransformStep("vis/social.graphml","xslt/GraphML2dot.xslt","vis/social.dot"), "CSV");
		
		return grench;
	}
	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void mainGephi(String[] args) throws Exception {
		
		Graph tinkerGraph = new TinkerGraph();
		Graph graph = new GephiGraph(tinkerGraph);
		//Graph graph = tinkerGraph;
		
		scalefreeBench(new TinkerBench( graph)).run();
		initRandomBench(new TinkerBench(graph)).run();		
		bestFriendsBench(new TinkerBench(graph)).run();
		
		Grenchmark contagion  = contagionBench(new TinkerBench(graph));
		Grenchmark homophily  = homophilyBench(new TinkerBench( graph ));
		Grenchmark confounding  = confoundingBench(new TinkerBench(graph));
		Grenchmark corr  = correlations(new TinkerBench(tinkerGraph));
		
		new StreamGraphStep().execute(graph);
							
		for (int i = 0; i < 1000; i++) {		
			corr.run();
//			contagion.run();			
//			homophily.run();
			confounding.run();
			System.out.println("step="+i);
			if (i % 100 == 0) {
				dumpCSVData();
			}
		}
			
		corr.run();
		
		//saveGraphBench(new TinkerBench(graph)).run();
		
		dumpCSVData();
		
		System.out.println("done.");		
	}
	
	/**
	 * Latent hompohily
	 */
	public static void main(String[] args) throws Exception {
		
		Graph tinkerGraph = new TinkerGraph();
		//Graph graph = new GephiGraph(tinkerGraph);
		Graph graph = tinkerGraph;
		
		Grenchmark scaleFree = scalefreeBench(new TinkerBench( graph));
		Grenchmark initRandom = initRandomBench(new TinkerBench(graph));		
		Grenchmark bestFriends = bestFriendsBench(new TinkerBench(graph));
		
		Grenchmark contagion  = contagionBench(new TinkerBench(graph));
		Grenchmark homophily  = homophilyBench(new TinkerBench( graph ));
		Grenchmark confounding  = confoundingBench(new TinkerBench(graph));
		Grenchmark clear  = clearAttitudesBench(new TinkerBench(graph));
		Grenchmark corr  = correlations(new TinkerBench(tinkerGraph));
		
		//new StreamGraphStep().execute(graph);
		
		scaleFree.run();
		initRandom.run();
		bestFriends.run();
		
		for (int i = 0; i < 200; i++) {		
			if (i % 10 == 0) corr.run();						
//			contagion.run();			
			homophily.run();
//			confounding.run();
			System.out.println("step="+i);
		}
		dumpCSVData();
			
		corr.run();
		
		clear.run();
		initRandom.run();
		
		for (int i = 0; i < 200; i++) {		
			if (i % 10 == 0) corr.run();						
			contagion.run();			
//			homophily.run();
//			confounding.run();
			System.out.println("step="+i);
			if (i % 100 == 0) {
				dumpCSVData();
			}
		}
		
		//saveGraphBench(new TinkerBench(graph)).run();
		
		dumpCSVData();
		
		System.out.println("done.");		
	}

	private static void dumpCSVData() {
		System.out.println("mutual; out; in; friends; foaf; foaf2; avg attt");
		for (int i = 0; i < CalculateCorellationStep.results.size(); i++) {
			System.out.print( CalculateCorellationStep.results.get(i).mutualCorr + ";"); 
			System.out.print( CalculateCorellationStep.results.get(i).outCorr + ";"); 
			System.out.print( CalculateCorellationStep.results.get(i).inCorr + ";");
			System.out.print( CalculateCorellationStep.results.get(i).allCorr + ";"); 
			System.out.print( CalculateFOAFCorrelationStep.results.get(i)+ ";"); 
			System.out.print( CalculateFOAF2Correlation.results.get(i)+ ";");
			System.out.println( CalculateCorellationStep.results.get(i).avgAtt+ ";");
		}
	}
	
	public static void main1(String[] args) throws Exception {

		System.out.print("Engine\tn\t");
		for (int repeats = 0; repeats < 100; repeats++) {
			TinkerGraph graph = new TinkerGraph();
			
			scalefreeBench(new TinkerBench(graph)).run();
			initRandomBench(new TinkerBench(graph)).run();		
			bestFriendsBench(new TinkerBench(graph)).run();
			
			Grenchmark contagion  = contagionBench(new TinkerBench(graph));
			Grenchmark homophily  = homophilyBench(new TinkerBench(graph));
			Grenchmark confounding  = confoundingBench(new TinkerBench(graph));
			Grenchmark corr  = correlations(new TinkerBench(graph));
			for (int i = 0; i < 100; i++) {		
//				contagion.run();			
//				homophily.run();
				confounding.run();
			}			
			System.out.println("step="+repeats);
			corr.run();
		}
				
		System.out.println("mutual; out; in; friends; foaf; foaf2");
		for (int i = 0; i < CalculateCorellationStep.results.size(); i++) {
			System.out.print( CalculateCorellationStep.results.get(i).mutualCorr + ";"); 
			System.out.print( CalculateCorellationStep.results.get(i).outCorr + ";"); 
			System.out.print( CalculateCorellationStep.results.get(i).inCorr + ";");
			System.out.print( CalculateCorellationStep.results.get(i).allCorr + ";"); 
			System.out.print( CalculateFOAFCorrelationStep.results.get(i)+ ";"); 
			System.out.println( CalculateFOAF2Correlation.results.get(i)+ ";"); 
		}
		
		// run(m, new Neo4jGrench(), "Neo");
		// run(m, new OrientDBGrench(), "Orient");
		System.out.println("done.");		
	}

}
