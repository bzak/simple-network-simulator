package pl.lome.graph.grenchmark.step;

import java.io.File;

import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.gremlin.GremlinScriptEngine;

public class XsltTransformStep extends Step {
	private String source;
	private String dest;
	private String xslt;
	
	public XsltTransformStep(String source, String xslt, String dest) {
		this.source = source;
		this.dest = dest;
		this.xslt = xslt;
	}
	
	@Override
	public void execute(GremlinScriptEngine engine, Graph graph)
			throws Exception {
		File xmlFile = new File(source);		
        File xsltFile = new File(xslt);
 
        javax.xml.transform.Source xmlSource =
                new javax.xml.transform.stream.StreamSource(xmlFile);
        javax.xml.transform.Source xsltSource =
                new javax.xml.transform.stream.StreamSource(xsltFile);
        javax.xml.transform.Result result =
                new javax.xml.transform.stream.StreamResult(new File(dest));
 
        // create an instance of TransformerFactory
        javax.xml.transform.TransformerFactory transFact =
                javax.xml.transform.TransformerFactory.newInstance(  );
 
        javax.xml.transform.Transformer trans =
                transFact.newTransformer(xsltSource);
 
        trans.transform(xmlSource, result);

	}

}
