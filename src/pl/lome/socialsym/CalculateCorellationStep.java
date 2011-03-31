package pl.lome.socialsym;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;
import com.tinkerpop.gremlin.GremlinScriptEngine;

import pl.lome.graph.grenchmark.step.Step;

public class CalculateCorellationStep extends Step {
	public class Result {
		public double mutualCorr, inCorr, outCorr, allCorr, avgAtt, friendsAtt, inFriendsAtt, outFriendsAtt, mutualFriendsAtt, numberOfVerticles;
	}
	public static ArrayList<Result> results = new ArrayList<Result>(); 
	
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
		double totalAttitude = 0; int numberOfVerticles = 0;
		double friendsAttitude = 0;
		double inFriendsAttitude = 0;
		double outFriendsAttitude = 0;
		double mutualFriendsAttitude = 0;
		for (Vertex v : graph.getVertices()) {
			double attitude = new Attitude(v).getValue();
			totalAttitude += attitude; numberOfVerticles++;
			if (v.getProperty("in_avg") != null) {
				AddScore(inScores, v.getProperty("in_avg"));
				AddScore(inValues, attitude);
				inFriendsAttitude += Double.valueOf( v.getProperty("in_avg").toString() );
			}
			if (v.getProperty("out_avg") != null) {
				AddScore(outScores, v.getProperty("out_avg"));
				AddScore(outValues, attitude);
				outFriendsAttitude += Double.valueOf( v.getProperty("out_avg").toString() );
			}
			if (v.getProperty("all_avg") != null) {
				AddScore(allScores, v.getProperty("all_avg"));
				AddScore(allValues, attitude);
				friendsAttitude += Double.valueOf( v.getProperty("all_avg").toString() );
			}
			if (v.getProperty("mutual_avg") != null) {
				AddScore(mutualScores, v.getProperty("mutual_avg"));
				AddScore(mutualValues, attitude);
				mutualFriendsAttitude += Double.valueOf( v.getProperty("mutual_avg").toString() );
			}			
		}
		double  avgAtt = (totalAttitude/(double) numberOfVerticles);
		System.out.println("avg att="+avgAtt);
		
		
		Result result = new Result();
		result.inCorr = getPearsonCorrelation(inValues, inScores);
		result.outCorr = getPearsonCorrelation(outValues, outScores);
		result.mutualCorr = getPearsonCorrelation(mutualValues, mutualScores);
		result.allCorr = getPearsonCorrelation(allValues, allScores);
		result.avgAtt = avgAtt;
		result.friendsAtt = Average(allScores); //friendsAttitude / (double) numberOfVerticles;
		result.inFriendsAtt =  Average(inScores); //inFriendsAttitude / (double) numberOfVerticles;
		result.outFriendsAtt = Average(outScores);// outFriendsAttitude / (double) numberOfVerticles;
		result.mutualFriendsAtt = Average(mutualScores); //mutualFriendsAttitude / (double) numberOfVerticles;
		result.numberOfVerticles = numberOfVerticles;
		this.results.add(result);
		
		System.out.println("friends att="+result.friendsAtt);
		System.out.println("inf  att="+result.inFriendsAtt);
		System.out.println("outf att="+result.outFriendsAtt);
		System.out.println("mutf att="+result.mutualFriendsAtt);
		
//		System.out.println();
		System.out.println("mutual corelation = "+result.mutualCorr);
		System.out.println("out corelation = "+ result.outCorr);
		System.out.println("in corelation = "+ result.inCorr);
		System.out.println();
		System.out.println("all corelation = "+result.allCorr);
		System.out.println();
		System.out.println("avg attitude = "+result.avgAtt);
		System.out.println();
	}

	private double Average(ArrayList<Double> allScores) {
		double sum = 0;
		for (Iterator iterator = allScores.iterator(); iterator.hasNext();) {
			sum += (Double) iterator.next();			
		}
		return sum / (double) allScores.size();
	}

	protected void AddScore(ArrayList<Double> inScores, Object property) {
		if (property != null) {
			Double d = (Double.valueOf(property.toString()));
			inScores.add(d);
		}
		else {
			inScores.add(0D);
		}
	}

	protected void calculateNeighbourAvg(Vertex v) {
		HashMap<String, Vertex> inV = inFriendsVerticies(v);
		
		HashMap<String, Vertex> outV = outFriendsVerticies(v);
		
		// fill all connected verticies
		ArrayList<Vertex> allV = new ArrayList<Vertex>( allFriendsVerticies(v).values() );
//		allV.addAll(inV.values());
//		allV.addAll(outV.values());
		
		// find mutual
		ArrayList<Vertex> mutualV = new ArrayList<Vertex>();
		for (String id : inV.keySet()) {
			if (outV.containsKey(id)) {
				mutualV.add(inV.get(id));
			}
		}
		
		// remove mutual from in and out
		for (Vertex vertex : mutualV) {
			inV.remove(vertex);
			outV.remove(vertex);
		}
		
		neighbourSetAverage(v, new ArrayList<Vertex>( inV.values() ), "in_avg");
		neighbourSetAverage(v, new ArrayList<Vertex>( outV.values() ), "out_avg");
		neighbourSetAverage(v, allV, "all_avg");
		neighbourSetAverage(v, mutualV, "mutual_avg");
	}

	protected HashMap<String, Vertex> inFriendsVerticies(Vertex v) {
		// find "in" vertices (remove duplicate relations with hashmap)
		HashMap<String, Vertex> inV = new HashMap<String, Vertex>();
		for (Edge edge : v.getInEdges()) {
			if (edge.getLabel().equalsIgnoreCase("close")) {
				Vertex vertex = edge.getOutVertex();
				inV.put(vertex.getId().toString(), vertex);
			}
		}
		return inV;
	}

	protected HashMap<String, Vertex> outFriendsVerticies(Vertex v) {
		// find "out" vertices (remove duplicate relations with hashmap)
		HashMap<String, Vertex> outV = new HashMap<String, Vertex>();
		for (Edge edge : v.getOutEdges()) {
			if (edge.getLabel().equalsIgnoreCase("close")) {
				Vertex vertex = edge.getInVertex();
				outV.put(vertex.getId().toString(), vertex);
			}
		}
		return outV;
	}
	
	protected HashMap<String, Vertex> allFriendsVerticies(Vertex v) {
		// fill all connected verticies
		HashMap<String, Vertex> allV = new HashMap<String, Vertex>();
		HashMap<String, Vertex> outV = outFriendsVerticies(v);
		HashMap<String, Vertex> inV = inFriendsVerticies(v);
		for (String id : outV.keySet()) {
			allV.put(id, outV.get(id));
		}
		for (String id : inV.keySet()) {
			allV.put(id, inV.get(id));
		}
		return allV;
	}

	protected void neighbourSetAverage(Vertex v, ArrayList<Vertex> values,
			String propName) {
			if (values.size() > 0) {
				double sum = 0;
				for (Vertex vertex : values) {
					sum+=new Attitude(vertex).getValue();
				}
				double avg = sum / (double) values.size();
				v.setProperty(propName, avg);
				//System.out.println(propName+" = " + avg);
			}			
		}
	

	protected double getPearsonCorrelation(ArrayList<Double> scores1, ArrayList<Double> scores2) {
		double result = 0;
		double sum_sq_x = 0;
		double sum_sq_y = 0;
		double sum_coproduct = 0;
		double mean_x = scores1.get(0);
		double mean_y = scores2.get(0);
		for (int i = 2; i < scores1.size() + 1; i += 1) {
			double sweep = Double.valueOf(i - 1) / i;
			double delta_x = scores1.get(i-1) - mean_x;
			double delta_y = scores2.get(i-1) - mean_y;
			sum_sq_x += delta_x * delta_x * sweep;
			sum_sq_y += delta_y * delta_y * sweep;
			sum_coproduct += delta_x * delta_y * sweep;
			mean_x += delta_x / i;
			mean_y += delta_y / i;
		}
		double pop_sd_x = (double) Math.sqrt(sum_sq_x / scores1.size());
		double pop_sd_y = (double) Math.sqrt(sum_sq_y / scores1.size());
		double cov_x_y = sum_coproduct / scores1.size();
		result = cov_x_y / (pop_sd_x * pop_sd_y);
		return result;
	}

}
