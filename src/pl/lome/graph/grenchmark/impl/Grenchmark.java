package pl.lome.graph.grenchmark.impl;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.script.ScriptContext;
import javax.sound.midi.SysexMessage;

import pl.lome.graph.grenchmark.step.Step;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;


public abstract class Grenchmark {
	protected Graph graph;
	private ArrayList<Step> steps;
	private HashMap<Step, String> stepNames;
	private HashMap<Step, Long> results;
	private String lastStepname;
	
	public Grenchmark() {
		this.steps = new ArrayList<Step>();
		this.results = new HashMap<Step, Long>();
		this.stepNames = new HashMap<Step, String>();
	}

	protected abstract Graph initGraph();

	protected abstract void cleanupGraph();

	public void run() throws Exception {
		System.gc();

		this.graph = initGraph();

		GremlinScriptEngine engine = new GremlinScriptEngine();				
		engine.getBindings(ScriptContext.ENGINE_SCOPE).put("$_g", graph);
		engine.getContext().setWriter(new OutputStreamWriter(System.out));		
		try {			
			executeSteps(engine);
		} catch (Exception e) {
			String message = GremlinScriptEngine.exceptionInPrintableFormat(e);
			System.out.println(lastStepname);
			System.out.println(message);
			throw new Exception(e);
		} finally {
			cleanupGraph();
		}

	}

	protected void executeSteps(GremlinScriptEngine engine)
			throws Exception {
		for (Step step : steps) {
			lastStepname = stepNames.get(step);
			long start = System.currentTimeMillis();
			step.execute(engine, graph);
			results.put(step, System.currentTimeMillis() - start);				
		}		
	}

	public Grenchmark AddStep(Step step, String name) {
		this.steps.add(step);
		this.stepNames.put(step, name);
		return this;
	}

	public void printHeader() {
		for (Step step : steps) {
			System.out.print(stepNames.get(step) + "\t");
		}
		System.out.println("Total");
	}

	public void printResults() {
		long sum = 0;
		for (Step step : steps) {
			System.out.print(results.get(step) + "\t");
			sum += results.get(step);
		}
		System.out.println(sum + "\t");
	}
}
