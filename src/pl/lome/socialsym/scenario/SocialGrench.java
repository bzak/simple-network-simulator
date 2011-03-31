package pl.lome.socialsym.scenario;

import java.io.PrintStream;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.compiler.GremlinParser.return_statement_return;

import pl.lome.graph.grenchmark.impl.Grenchmark;
import pl.lome.graph.grenchmark.impl.TinkerBench;
import pl.lome.graph.grenchmark.step.CompositeStep;
import pl.lome.graph.grenchmark.step.LoadGraphMLStep;
import pl.lome.graph.grenchmark.step.SaveGraphMLStep;
import pl.lome.graph.grenchmark.step.Step;
import pl.lome.graph.grenchmark.step.XsltTransformStep;
import pl.lome.socialsym.BestFriendsContagion;
import pl.lome.socialsym.CalculateCorellationStep;
import pl.lome.socialsym.CalculateFOAF2Correlation;
import pl.lome.socialsym.CalculateFOAFCorrelationStep;
import pl.lome.socialsym.ClearValuesStep;
import pl.lome.socialsym.Confounding2NodesStep;
import pl.lome.socialsym.ConfoundingStep;
import pl.lome.socialsym.HomophilyStep;
import pl.lome.socialsym.MarkBestFriendsStep;
import pl.lome.socialsym.ScaleFreeNetworkStep;

public class SocialGrench extends TinkerBench {

	protected Step scaleFree() {
		return scaleFree(1000);
	}
	
	protected Step scaleFree(int networkSize) {
		return new ScaleFreeNetworkStep(networkSize);				
	}
	
	protected Step loadGraphML(String filename) {
		return new LoadGraphMLStep(filename);				
	}
	
	protected Step initRandom() { 
		return initRandom(100);
	}
	
	protected Step initRandom(int infected) {
		return initRandom(infected, "value");
	}
	
	protected Step initRandom(int infected, String attributeName) {
		CompositeStep composite = new CompositeStep();
		composite.add(new ClearValuesStep(attributeName));
		composite.add(new ConfoundingStep(infected, 1, 1, attributeName));
		return composite;
	}
	
	protected Step confounding() {
		return confounding(0.2, 10); 
		//return confoundingBench(0.5, 20); 
	}
	
	protected Step confounding(double weight, int repeats) {				
		return new Confounding2NodesStep(0.2, repeats);									
	}
	
	protected Step contagion() {				
		return contagion(1000, 0.05, 0.1);		
	}
	
	protected Step contagion(int nodesToInfect, double weight, double mutualWeight) {				
		return new BestFriendsContagion(nodesToInfect, weight, mutualWeight);		
	}
	
	protected Step homophily() {
		return homophily(1);
	}
	
	protected Step homophily(int bondsToCreate) {						
		return new HomophilyStep(bondsToCreate);
	}
		
	protected Step homophily(int bondsToCreate, String attributeName) {						
		return new HomophilyStep(bondsToCreate, attributeName);
	}
	
	protected Step clearAttitudes() {						
		return new ClearValuesStep();
	}
	
	protected Step markBestFriends() {						
		return new MarkBestFriendsStep(0.5, 0.5);
	}

	protected Step calculateCorrelations() {		
		CompositeStep composite = new CompositeStep();
		composite.add(new CalculateCorellationStep());				
		composite.add(new CalculateFOAFCorrelationStep());
		composite.add(new CalculateFOAF2Correlation());
		return composite;
	}
	
	protected Step saveGraph() {
		CompositeStep composite = new CompositeStep();
		composite.add(new SaveGraphMLStep("vis/social.graphml"));
		composite.add(new XsltTransformStep("vis/social.graphml","xslt/nodeAttributesCSV.xslt","vis/correlations.csv"));
		composite.add(new XsltTransformStep("vis/social.graphml","xslt/GraphML2dot.xslt","vis/social.dot"));		
		return composite;
	}

	protected void printCorrelationsCSV() {
		printCorrelations(System.out);		
	}
	
	protected void printCorrelations(PrintStream out) {
		out.println("mutual; out; in; friends; foaf; foaf2; avg attt; friends att; inf att; outf att; mutualf att; numberOfVerticles");
		for (int i = 0; i < CalculateCorellationStep.results.size(); i++) {
			out.print( CalculateCorellationStep.results.get(i).mutualCorr + ";"); 
			out.print( CalculateCorellationStep.results.get(i).outCorr + ";"); 
			out.print( CalculateCorellationStep.results.get(i).inCorr + ";");
			out.print( CalculateCorellationStep.results.get(i).allCorr + ";"); 
			out.print( CalculateFOAFCorrelationStep.results.get(i)+ ";"); 
			out.print( CalculateFOAF2Correlation.results.get(i)+ ";");
			out.print( CalculateCorellationStep.results.get(i).avgAtt+ ";");
			out.print( CalculateCorellationStep.results.get(i).friendsAtt+ ";");
			out.print( CalculateCorellationStep.results.get(i).inFriendsAtt+ ";");
			out.print( CalculateCorellationStep.results.get(i).outFriendsAtt+ ";");
			out.print( CalculateCorellationStep.results.get(i).mutualFriendsAtt+ ";");
			out.println( CalculateCorellationStep.results.get(i).numberOfVerticles+ ";");
		}
	}
}
