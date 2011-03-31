package pl.lome.socialsym;

import java.util.HashSet;
import java.util.Random;

import org.omg.CORBA.Object;
import org.restlet.Route;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;
import com.tinkerpop.gremlin.compiler.GremlinParser.return_statement_return;

import pl.lome.graph.grenchmark.step.Step;

public class ScaleFreeNetworkStep extends Step {
	private int nodes;
	private int edges;
	private Random rand;
	private Roulette roulette;
	private int linkingCoef = 2;
	
	public ScaleFreeNetworkStep(int nodes) {
		this.nodes = nodes;
		this.edges = 0;
		this.rand = new Random();
		this.roulette = new Roulette();
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {		
		// create initial graph with 2 connected vertices
		Vertex V1 = graph.addVertex(null);
		V1 = graph.getVertex(V1.getId());
		new Attitude(V1).initialValue();
		Vertex V2 = graph.addVertex(null);
		V2 = graph.getVertex(V2.getId());
		new Attitude(V2).initialValue();
		addEdge(graph, V1, V2);
		roulette.Update(V1.getId(), 1);
		roulette.Update(V2.getId(), 1);
		
		for (int i = 0; i < nodes; i++) {
			addNode(engine, graph);
		}
	}

	private void addNode(GremlinScriptEngine engine, Graph graph) {
		Vertex vertex = graph.addVertex(null);
		vertex = graph.getVertex(vertex.getId());
		new Attitude(vertex).initialValue();
		HashSet<Integer> chosen = new HashSet<Integer>();
		
		for (int i = 0; i < linkingCoef; i++) {			
			int id = roulette.RandomSlot();						
			if (!chosen.contains(id) && id != (Integer.parseInt(vertex.getId().toString()))) {
				chosen.add(id);
				Vertex v = graph.getVertex(id);
				addEdge(graph, vertex, v);
			}
		}
	}

	private void addEdge(Graph graph, Vertex v1, Vertex v2) {
		graph.addEdge(null, v1, v2, "friend");
		graph.addEdge(null, v2, v1, "friend");
		edges++;
		roulette.Update(v2.getId(), vDegree(v2));
		roulette.Update(v1.getId(), vDegree(v1));
	}

	private int vDegree(Vertex v) {
		int indegree =0;
		for (@SuppressWarnings("unused") Edge e : v.getInEdges()) indegree++;
		return indegree;
	}
	
}
