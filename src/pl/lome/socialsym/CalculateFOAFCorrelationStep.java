package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class CalculateFOAFCorrelationStep extends CalculateCorellationStep {
	public static ArrayList<Double> results = new ArrayList<Double>();
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		for (Vertex v : graph.getVertices()) {
			calculateNeighbourAvg(v);
		}
		
		ArrayList<Double> inScores = new ArrayList<Double>();
		ArrayList<Double> inValues = new ArrayList<Double>();
		ArrayList<Double> outScores = new ArrayList<Double>();
		ArrayList<Double> outValues = new ArrayList<Double>();
		ArrayList<Double> allScores = new ArrayList<Double>();
		ArrayList<Double> allValues = new ArrayList<Double>();
		ArrayList<Double> mutualScores = new ArrayList<Double>();
		ArrayList<Double> mutualValues = new ArrayList<Double>();
		
		for (Vertex v : graph.getVertices()) {
			double attitude = new Attitude(v).getValue();
			if (v.getProperty("in_foaf_avg") != null) {
				AddScore(inScores, v.getProperty("in_foaf_avg"));
				AddScore(inValues, attitude);
			}
			if (v.getProperty("out_foaf_avg") != null) {
				AddScore(outScores, v.getProperty("out_foaf_avg"));
				AddScore(outValues, attitude);
			}
			if (v.getProperty("all_foaf_avg") != null) {
				AddScore(allScores, v.getProperty("all_foaf_avg"));
				AddScore(allValues, attitude);
			}
			if (v.getProperty("mutual_foaf_avg") != null) {
				AddScore(mutualScores, v.getProperty("mutual_foaf_avg"));
				AddScore(mutualValues, attitude);
			}			
		}
		double result = getPearsonCorrelation(allValues, allScores);
		results.add(result);
//		System.out.println();
//		System.out.println("mutual foaf corelation = "+getPearsonCorrelation(mutualValues, mutualScores)+ "  ["+mutualValues.size()+"]");
//		System.out.println("out foaf corelation = "+getPearsonCorrelation(outValues, outScores)+ "  ["+outValues.size()+"]");
//		System.out.println("in foaf corelation = "+getPearsonCorrelation(inValues, inScores) + "  ["+inValues.size()+"]");
//		System.out.println();
		System.out.println("all foaf corelation = "+result+ "  ["+allValues.size()+"]");
		System.out.println();
	}
	
	protected void calculateNeighbourAvg(Vertex v) {
		// find "in" vertices (remove duplicate relations with hashmap)
		HashMap<String, Vertex> inV = new HashMap<String, Vertex>();
		for (Edge firstEdge : v.getInEdges()) {
			if (firstEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
				for (Edge edge : firstEdge.getOutVertex().getInEdges()) {
					if (edge.getOutVertex() != v && edge.getLabel().equalsIgnoreCase("close") && !isMutual(edge)) {
						Vertex vertex = edge.getOutVertex();
						inV.put(vertex.getId().toString(), vertex);
					}
				}
			}
		}
		inV.remove(v.getId().toString());
		
		// find "out" vertices (remove duplicate relations with hashmap)
		HashMap<String, Vertex> outV = new HashMap<String, Vertex>();
		for (Edge firstEdge : v.getOutEdges()) {
			if (firstEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
				for (Edge edge : firstEdge.getInVertex().getOutEdges()) {
					if (edge.getInVertex() != v && edge.getLabel().equalsIgnoreCase("close") && !isMutual(edge)) {
						Vertex vertex = edge.getInVertex();
						outV.put(vertex.getId().toString(), vertex);
					}		
				}
			}			
		}
		outV.remove(v.getId().toString());
		
		HashMap<String, Vertex> mutualV = new HashMap<String, Vertex>();
		for (Edge firstEdge : v.getOutEdges()) {
			if (firstEdge.getLabel().equalsIgnoreCase("close") && isMutual(firstEdge)) {
				for (Edge edge : firstEdge.getInVertex().getOutEdges()) {
					if (edge.getInVertex() != v && edge.getLabel().equalsIgnoreCase("close") && isMutual(edge)) {
						Vertex vertex = edge.getInVertex();
						mutualV.put(vertex.getId().toString(), vertex);
					}		
				}
			}			
		}
		mutualV.remove(v.getId().toString());
		
		HashMap<String, Vertex> allV = FOAFVerticies(v);
		
		neighbourSetAverage(v, new ArrayList<Vertex>( inV.values() ), "in_foaf_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( outV.values() ), "out_foaf_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( allV.values() ), "all_foaf_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( mutualV.values()), "mutual_foaf_avg");
	}

	protected HashMap<String, Vertex> FOAFVerticies(Vertex v) {
		// fill all connected verticies
		HashMap<String, Vertex> result = new HashMap<String, Vertex>();
		HashMap<String, Vertex> friends = allFriendsVerticies(v);
		for (Vertex friend : friends.values()) {
			HashMap<String, Vertex> firendsofID = allFriendsVerticies(friend);
			for (String id : firendsofID.keySet()) {
				result.put(id, firendsofID.get(id));
			}
		}
		result.remove(v.getId().toString());		
		for (String id : friends.keySet()) {
			result.remove(id);
		}
		return result;
	}
	
	protected Iterable<Edge> union(Iterable<Edge> outEdges, Iterable<Edge> inEdges) {
		ArrayList<Edge> result = new ArrayList<Edge>();
		for (Edge edge : outEdges) {
			result.add(edge);			
		}
		for (Edge edge : inEdges) {
			result.add(edge);			
		}
		return result;
	}
	
	protected Iterable<Edge> union4(Iterable<Edge> e1, Iterable<Edge> e2, Iterable<Edge> e3, Iterable<Edge> e4) {
		ArrayList<Edge> result = new ArrayList<Edge>();
		for (Edge edge : e1) {
			result.add(edge);			
		}
		for (Edge edge : e2) {
			result.add(edge);			
		}
		for (Edge edge : e3) {
			result.add(edge);			
		}
		for (Edge edge : e4) {
			result.add(edge);			
		}
		return result;
	}

	protected boolean isMutual(Edge edge1) {
		boolean toRelation = false, fromRelation = false;
		Vertex source = edge1.getInVertex();
		Vertex target = edge1.getOutVertex();
		for (Edge edge : source.getOutEdges()) {
			if (edge.getLabel().equalsIgnoreCase("close") && edge.getInVertex() == target) {
				toRelation = true;
			}
		}
		for (Edge edge : target.getOutEdges()) {
			if (edge.getLabel().equalsIgnoreCase("close") && edge.getInVertex() == source) {
				fromRelation = true;
			}
		}
		return toRelation && fromRelation;
	}
}
