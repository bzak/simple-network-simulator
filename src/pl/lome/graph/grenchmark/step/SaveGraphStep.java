package pl.lome.graph.grenchmark.step;

import java.io.File;
import java.io.FileReader;

import javax.script.ScriptException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

/**
 * Serializes graph into GraphML, png & dot file  
 */
public class SaveGraphStep extends Step {
	public enum Options { GraphML, Dot, PNG }
	
	private Options options;
	private String filename;
	private String graphMLFilename;
	private String dotFilename;
	private String simpleDotFilename;
	private String pngFilename;
	
	public SaveGraphStep(String filename, Options options) {
		this(filename);
		this.options = options;
	}
	
	public SaveGraphStep(String filename) {
		this.filename = filename;
		this.graphMLFilename = filename + ".graphml";
		this.dotFilename = filename + ".dot";
		this.simpleDotFilename = filename + "-simple.dot";
		this.pngFilename = filename + ".png";
		this.options = Options.PNG;
	}

	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {		
		saveGraphML(engine);	
		if (options == Options.Dot || options == Options.PNG) {
			saveDot("xslt/GraphML2dot.xslt", dotFilename);
			saveDot("xslt/GraphML2SimpleDot.xslt", simpleDotFilename);
		}
		if (options == Options.PNG) {
			savePng();
		}
	}

	private void savePng() {
		//String cmd = "dot -Kdot -Tpng "+dotFilename+" -o "+pngFilename;
		String cmd = "dot -Kneato -Tpng "+dotFilename+" -o "+pngFilename;
		try {
	      Process p = Runtime.getRuntime().exec(cmd);
	      p.waitFor();
	      System.out.println(p.exitValue());
	    }
	    catch (Exception err) {
	      err.printStackTrace();
	    }
	}

	private void saveDot(String xslt, String fn) throws TransformerFactoryConfigurationError,
			TransformerConfigurationException, TransformerException {
		File xmlFile = new File(graphMLFilename);		
        File xsltFile = new File(xslt);
 
        javax.xml.transform.Source xmlSource =
                new javax.xml.transform.stream.StreamSource(xmlFile);
        javax.xml.transform.Source xsltSource =
                new javax.xml.transform.stream.StreamSource(xsltFile);
        javax.xml.transform.Result result =
                new javax.xml.transform.stream.StreamResult(new File(fn));
 
        // create an instance of TransformerFactory
        javax.xml.transform.TransformerFactory transFact =
                javax.xml.transform.TransformerFactory.newInstance(  );
 
        javax.xml.transform.Transformer trans =
                transFact.newTransformer(xsltSource);
 
        trans.transform(xmlSource, result);
	}

	private void saveGraphML(GremlinScriptEngine engine) throws ScriptException {
		engine.eval("g:save('"+graphMLFilename+"')");
	}


}
