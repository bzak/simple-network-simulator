package pl.lome.gephi;

import java.util.ArrayList;
import java.util.List;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;

public class GephiGraph implements Graph {

	private Graph graph;
	private GephiStreaming gephi;
	
	public GephiGraph(Graph backend) {
		graph = backend;
		gephi = new GephiStreaming();
	}
	
	@Override
	public Edge addEdge(Object arg0, Vertex v1, Vertex v2, String arg3) {
		if (v1 instanceof GephiVertex) {
			v1 = ((GephiVertex)v1).getVertex();
		}
		if (v2 instanceof GephiVertex) {
			v2 = ((GephiVertex)v2).getVertex();
		}
		Edge e= graph.addEdge(arg0, v1, v2, arg3);
		if (arg3.equalsIgnoreCase("close")) {
			gephi.AddEdge(e);
		}
		return e;
	}

	@Override
	public Vertex addVertex(Object arg0) {		
		Vertex v = graph.addVertex(arg0);
		gephi.AddNode(v);
		return v;
	}

	@Override
	public void clear() {
		graph.clear();		
	}

	@Override
	public Edge getEdge(Object arg0) {
		return graph.getEdge(arg0);
	}

	@Override
	public Iterable<Edge> getEdges() {
		return graph.getEdges();
	}

	@Override
	public Vertex getVertex(Object arg0) {
		return new GephiVertex(graph.getVertex(arg0), gephi);
	}

	@Override
	public Iterable<Vertex> getVertices() {
		ArrayList<Vertex> result = new ArrayList<Vertex>();
		Iterable<Vertex> back = graph.getVertices();
		for (Vertex vertex : back) {
			result.add(new GephiVertex(vertex, gephi));
		}
		return result;
	}

	@Override
	public void removeEdge(Edge arg0) {
		graph.removeEdge(arg0);		
	}

	@Override
	public void removeVertex(Vertex arg0) {
		graph.removeVertex(arg0);		
	}

	@Override
	public void shutdown() {
		graph.shutdown();
	}

}
