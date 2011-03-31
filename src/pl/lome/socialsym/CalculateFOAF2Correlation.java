package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.HashMap;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class CalculateFOAF2Correlation extends CalculateFOAFCorrelationStep {
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
			if (v.getProperty("in_foaf2_avg") != null) {
				AddScore(inScores, v.getProperty("in_foaf2_avg"));
				AddScore(inValues, attitude);
			}
			if (v.getProperty("out_foaf2_avg") != null) {
				AddScore(outScores, v.getProperty("out_foaf2_avg"));
				AddScore(outValues, attitude);
			}
			if (v.getProperty("all_foaf2_avg") != null) {
				AddScore(allScores, v.getProperty("all_foaf2_avg"));
				AddScore(allValues, attitude);
			}
			if (v.getProperty("mutual_foaf2_avg") != null) {
				AddScore(mutualScores, v.getProperty("mutual_foaf2_avg"));
				AddScore(mutualValues, attitude);
			}			
		}
		double result = getPearsonCorrelation(allValues, allScores);
		results.add(result);
//		System.out.println();
//		System.out.println("mutual foaf2 corelation = "+getPearsonCorrelation(mutualValues, mutualScores) + "  ["+mutualValues.size()+"]");
//		System.out.println("out foaf2 corelation = "+getPearsonCorrelation(outValues, outScores) + "  ["+outValues.size()+"]");
//		System.out.println("in foaf2 corelation = "+getPearsonCorrelation(inValues, inScores) + "  ["+inValues.size()+"]");
		System.out.println("all foaf2 corelation = " + result + "  ["+allValues.size()+"]");
		System.out.println();

	}
	
	protected void calculateNeighbourAvg(Vertex v) {
		// find "in" vertices (remove duplicate relations with hashmap)
		HashMap<String, Vertex> inV = new HashMap<String, Vertex>();
		for (Edge firstEdge : v.getInEdges()) {
			if (firstEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
				for (Edge secondEdge : firstEdge.getOutVertex().getInEdges()) {
					if (secondEdge.getOutVertex() != v && secondEdge.getLabel().equalsIgnoreCase("close") && !isMutual(secondEdge)) {
						for (Edge edge : secondEdge.getOutVertex().getInEdges()) {
							if (edge.getLabel().equalsIgnoreCase("close") && !isMutual(edge)) {
								Vertex vertex = edge.getOutVertex();
								inV.put(vertex.getId().toString(), vertex);
							}
						}
					}
				}
			}
		}
		
		// find "out" vertices (remove duplicate relations with hashmap)
		HashMap<String, Vertex> outV = new HashMap<String, Vertex>();
		for (Edge firstEdge : v.getOutEdges()) {
			if (firstEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
				for (Edge secondEdge : firstEdge.getInVertex().getOutEdges()) {
					if (secondEdge.getInVertex() != v && secondEdge.getLabel().equalsIgnoreCase("close") && !isMutual(secondEdge)) {
						for (Edge edge : secondEdge.getInVertex().getOutEdges()) {
							if (edge.getLabel().equalsIgnoreCase("close") && !isMutual(edge)) {
								Vertex vertex = edge.getInVertex();
								outV.put(vertex.getId().toString(), vertex);
							}		
						}
					}
				}
			}			
		}
		
		HashMap<String, Vertex> mutualV = new HashMap<String, Vertex>();
		for (Edge firstEdge : v.getOutEdges()) {
			if (firstEdge.getLabel().equalsIgnoreCase("close") && isMutual(firstEdge)) {
				for (Edge secondEdge : firstEdge.getInVertex().getOutEdges()) {
					if (secondEdge.getInVertex() != v && secondEdge.getLabel().equalsIgnoreCase("close") && isMutual(secondEdge)) {
						for (Edge edge : secondEdge.getInVertex().getOutEdges()) {
							if (edge.getInVertex() != v && edge.getInVertex() != firstEdge.getInVertex() 
									&& edge.getInVertex() != secondEdge.getInVertex() && edge.getLabel().equalsIgnoreCase("close") && isMutual(edge)) {
								Vertex vertex = edge.getInVertex();
								mutualV.put(vertex.getId().toString(), vertex);
							}		
						}
					}
				}
			}			
		}
		
		HashMap<String, Vertex> allV = FOAF2Verticies(v);
		
		neighbourSetAverage(v, new ArrayList<Vertex>( inV.values() ), "in_foaf2_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( outV.values() ), "out_foaf2_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( allV.values() ), "all_foaf2_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( mutualV.values()), "mutual_foaf2_avg");
	}
	
	protected HashMap<String, Vertex> FOAF2Verticies(Vertex v) {
		HashMap<String, Vertex> result = new HashMap<String, Vertex>();
		
		HashMap<String, Vertex> foafs = FOAFVerticies(v);
		for (Vertex friendOfaFriend : foafs.values()) {
			HashMap<String, Vertex> firendsOfFOAFs = allFriendsVerticies(friendOfaFriend);
			for (String id : firendsOfFOAFs.keySet()) {
				result.put(id, firendsOfFOAFs.get(id));
			}
		}		
		// remove friends and FOAFs
		HashMap<String, Vertex> friends = allFriendsVerticies(v);
		for (String id : friends.keySet()) {
			result.remove(id);
		}
		for (String id : foafs.keySet()) {
			result.remove(id);
		}
		//if (allV.size() >0) System.out.print(".");
		return result;
		// fill all connected verticies
//		HashMap<String, Vertex> allV = new HashMap<String, Vertex>();
//		for (Edge firstEdge : union( v.getOutEdges(), v.getInEdges())) {
//			if (firstEdge.getLabel().equalsIgnoreCase("close")) {
//				for (Edge edge : union4( firstEdge.getInVertex().getOutEdges() , firstEdge.getInVertex().getInEdges(),
//										firstEdge.getOutVertex().getOutEdges(), firstEdge.getOutVertex().getInEdges())
//						
//				) {
//					if (edge != firstEdge && edge.getLabel().equalsIgnoreCase("close")) {
//						Vertex vertex = edge.getInVertex();
//						allV.put(vertex.getId().toString(), vertex);
//					}		
//				}
//			}			
//		}
//		allV.remove(v.getId().toString());
//		HashMap<String, Vertex> friendsV = allFriendsVerticies(v);
//		for (String id : friendsV.keySet()) {
//			allV.remove(id);
//		}
//		return allV;
	}
}
