package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.HashMap;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class CalculateFOAF3Correlation extends CalculateFOAF2Correlation {
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
			if (v.getProperty("in_foaf3_avg") != null) {
				AddScore(inScores, v.getProperty("in_foaf3_avg"));
				AddScore(inValues, attitude);
			}
			if (v.getProperty("out_foaf3_avg") != null) {
				AddScore(outScores, v.getProperty("out_foaf3_avg"));
				AddScore(outValues, attitude);
			}
			if (v.getProperty("all_foaf3_avg") != null) {
				AddScore(allScores, v.getProperty("all_foaf3_avg"));
				AddScore(allValues, attitude);
			}
			if (v.getProperty("mutual_foaf3_avg") != null) {
				AddScore(mutualScores, v.getProperty("mutual_foaf3_avg"));
				AddScore(mutualValues, attitude);
			}			
		}
		
//		System.out.println();
		System.out.println("mutual foaf3 corelation = "+getPearsonCorrelation(mutualValues, mutualScores) + "  ["+mutualValues.size()+"]");
		System.out.println("out foaf3 corelation = "+getPearsonCorrelation(outValues, outScores) + "  ["+outValues.size()+"]");
		System.out.println("in foaf3 corelation = "+getPearsonCorrelation(inValues, inScores) + "  ["+inValues.size()+"]");
		System.out.println();
	}
	
	protected void calculateNeighbourAvg(Vertex v) {
		// find "in" vertices (remove duplicate relations with hashmap)
		HashMap<String, Vertex> inV = new HashMap<String, Vertex>();
		for (Edge firstEdge : v.getInEdges()) {
			if (firstEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
				for (Edge secondEdge : firstEdge.getOutVertex().getInEdges()) {
					if (secondEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
						for (Edge thirdEdge : secondEdge.getOutVertex().getInEdges()) {
							if (thirdEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
								for (Edge edge : thirdEdge.getOutVertex().getInEdges()) {
									if (edge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
										Vertex vertex = edge.getOutVertex();
										inV.put(vertex.getId().toString(), vertex);
									}
								}
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
					if (secondEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
						for (Edge thirdEdge : secondEdge.getInVertex().getOutEdges()) {
							if (thirdEdge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
								for (Edge edge : thirdEdge.getInVertex().getOutEdges()) {
									if (edge.getLabel().equalsIgnoreCase("close") && !isMutual(firstEdge)) {
										Vertex vertex = edge.getInVertex();
										outV.put(vertex.getId().toString(), vertex);
									}		
								}
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
					if (secondEdge.getLabel().equalsIgnoreCase("close") && isMutual(firstEdge)) {
						for (Edge thirdEdge : secondEdge.getInVertex().getOutEdges()) {
							if (thirdEdge.getLabel().equalsIgnoreCase("close") && isMutual(firstEdge)) {
								for (Edge edge : thirdEdge.getInVertex().getOutEdges()) {
									if (edge.getLabel().equalsIgnoreCase("close") && isMutual(firstEdge)) {
										Vertex vertex = edge.getInVertex();
										mutualV.put(vertex.getId().toString(), vertex);
									}		
								}
							}
						}
					}
				}
			}			
		}
		
		// fill all connected verticies
		HashMap<String, Vertex> allV = new HashMap<String, Vertex>();
		for (Edge firstEdge : union( v.getOutEdges(), v.getInEdges())) {
			if (firstEdge.getLabel().equalsIgnoreCase("close")) {
				for (Edge edge : union4( firstEdge.getInVertex().getOutEdges() , firstEdge.getInVertex().getInEdges(),
										firstEdge.getOutVertex().getOutEdges(), firstEdge.getOutVertex().getInEdges())						
				) {
					if (edge.getLabel().equalsIgnoreCase("close")) {
						Vertex vertex = edge.getInVertex();
						allV.put(vertex.getId().toString(), vertex);
					}		
				}
			}			
		}
		
		neighbourSetAverage(v, new ArrayList<Vertex>( inV.values() ), "in_foaf3_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( outV.values() ), "out_foaf3_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( allV.values() ), "all_foaf3_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( mutualV.values()), "mutual_foaf3_avg");
	}
}
