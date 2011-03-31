package pl.lome.graph.grenchmark;

import java.io.IOException;

import pl.lome.graph.grenchmark.impl.Grenchmark;
import pl.lome.graph.grenchmark.impl.Neo4jGrench;
import pl.lome.graph.grenchmark.impl.OrientDBGrench;
import pl.lome.graph.grenchmark.impl.TinkerBench;
import pl.lome.graph.grenchmark.step.CommitTransactionStep;
import pl.lome.graph.grenchmark.step.GremlinStep;
import pl.lome.graph.grenchmark.step.MultiplyGraphStep;
import pl.lome.graph.grenchmark.step.StartTransactionStep;


public class Runner {
	static int[] multiplicators = new int[]{1,10,100,200,500,1000,2000,5000,10000,20000};

	// control test iterations here 
	//static final int[] multiplicators = new int[] { 10, 100, 30000, 40000 };

	// define test steps 
	private static Grenchmark setupBenchmark(int multiplicator, Grenchmark grench) {
		grench.AddStep(new StartTransactionStep(), "TxStart");
		grench.AddStep(new MultiplyGraphStep("data/sample.xml", multiplicator),
				"Create");
		grench.AddStep(new GremlinStep("gremlin/step1.grm"), "Step1");
		grench.AddStep(new GremlinStep("gremlin/step2.grm"), "Step2");
		grench.AddStep(new GremlinStep("gremlin/step3.grm"), "Step3");
		grench.AddStep(new GremlinStep("gremlin/step4.grm"), "Step4");
		grench.AddStep(new GremlinStep("gremlin/step5.grm"), "Step5");
		grench.AddStep(new GremlinStep("gremlin/step6.grm"), "Step6");
		grench.AddStep(new CommitTransactionStep(), "Commit");
		return grench;
	}
	
	// define test steps 
	private static Grenchmark setupBenchmark2(int multiplicator, Grenchmark grench) {
		grench.AddStep(new StartTransactionStep(), "TxStart");
		grench.AddStep(new MultiplyGraphStep("data/sample.xml", multiplicator),
				"Create");
		grench.AddStep(new CommitTransactionStep(), "Commit");
		grench.AddStep(new StartTransactionStep(), "TxStart");
		grench.AddStep(new GremlinStep("gremlin/step1.grm"), "Step1");
		grench.AddStep(new CommitTransactionStep(), "Commit");
		grench.AddStep(new StartTransactionStep(), "TxStart");
		grench.AddStep(new GremlinStep("gremlin/step2.grm"), "Step2");
		grench.AddStep(new CommitTransactionStep(), "Commit");
		grench.AddStep(new StartTransactionStep(), "TxStart");
		grench.AddStep(new GremlinStep("gremlin/step3.grm"), "Step3");
		grench.AddStep(new CommitTransactionStep(), "Commit");
		grench.AddStep(new StartTransactionStep(), "TxStart");
		grench.AddStep(new GremlinStep("gremlin/step4.grm"), "Step4");
		grench.AddStep(new CommitTransactionStep(), "Commit");
		grench.AddStep(new StartTransactionStep(), "TxStart");
		grench.AddStep(new GremlinStep("gremlin/step5.grm"), "Step5");
		grench.AddStep(new CommitTransactionStep(), "Commit");
		grench.AddStep(new StartTransactionStep(), "TxStart");
		grench.AddStep(new GremlinStep("gremlin/step6.grm"), "Step6");
		grench.AddStep(new CommitTransactionStep(), "Commit");
		return grench;
	}

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {

		System.out.print("Engine\tn\t");
		setupBenchmark(1, new TinkerBench()).printHeader();
		
		for (int m : multiplicators) {
			run(m, new TinkerBench(), "Tinker");
			run(m, new Neo4jGrench(), "Neo");
			// run(m, new OrientDBGrench(), "Orient");
		}

	}

	private static void run(int multiplier, Grenchmark benchmark, String name)
			throws Exception {
		setupBenchmark(multiplier, benchmark);

		benchmark.run();

		System.out.print(name+"\t" + multiplier + "\t");
		benchmark.printResults();
	}

}
