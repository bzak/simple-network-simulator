package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.collections15.bag.HashBag;
import org.openrdf.query.algebra.Str;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;
import com.tinkerpop.gremlin.compiler.GremlinParser.return_statement_return;

import pl.lome.graph.grenchmark.step.Step;

public class HomophilyStep extends Step {
	private int bondsToCreate;
	private Random rand;
	private String attributeName;
	
	public HomophilyStep(int bondsToCreate) {
		this.bondsToCreate = bondsToCreate;
		this.rand = new Random();
		this.attributeName = "value";
	}
	
	public HomophilyStep(int bondsToCreate, String attributeName) {
		this.bondsToCreate = bondsToCreate;
		this.rand = new Random();
		this.attributeName = attributeName;
	}

	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		ArrayList<Vertex> infected = new ArrayList<Vertex>(); 
		for (Vertex v : graph.getVertices()) {			
			if (new Attitude(v,attributeName).getValue() > 0.5) {
				infected.add(v);
			}
		}
		
		int counter = 0; int loopStop = 0;
		if (infected.size() > 0)
		while (counter < bondsToCreate && loopStop < 50) {			
			Vertex v1 = infected.get(rand.nextInt(infected.size()));
			Vertex v2 = infected.get(rand.nextInt(infected.size()));
			
			if (!isConnected(v1, v2) && v1 != v2) {
				graph.addEdge(null, v1, v2, "homophily");
				graph.addEdge(null, v2, v1, "homophily");
				MarkBestFriendsStep.homophilicBond(graph,v1,v2);
				MarkBestFriendsStep.homophilicBond(graph,v2,v1);
				counter++;
			}
			else {
				loopStop++;
			}
		}

	}

	private boolean isConnected(Vertex v1, Vertex v2) {
		for (Edge edge : v1.getOutEdges()) {
			if (edge.getInVertex() == v2) {
				return true;
			}
		}
		return false;
	}

}
