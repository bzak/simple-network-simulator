package pl.lome.gephi;

import java.util.Set;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Graph;
import com.tinkerpop.blueprints.pgm.Vertex;

public class GephiVertex implements Vertex{

	private Vertex vertex;
	private GephiStreaming gephi;
	
	public GephiVertex(Vertex vertex, GephiStreaming gephi) {
		this.setVertex(vertex);
		this.gephi = gephi;
	}

	@Override
	public Object getId() {
		return getVertex().getId();
	}

	@Override
	public Object getProperty(String arg0) {
		return getVertex().getProperty(arg0);
	}

	@Override
	public Set<String> getPropertyKeys() {
		return getVertex().getPropertyKeys();
	}

	@Override
	public Object removeProperty(String arg0) {
		return getVertex().removeProperty(arg0);
	}

	@Override
	public void setProperty(String arg0, Object arg1) {
		getVertex().setProperty(arg0, arg1);
		gephi.ChangeVertex(getVertex());
	}

	@Override
	public Iterable<Edge> getInEdges() {
		return getVertex().getInEdges();
	}

	@Override
	public Iterable<Edge> getOutEdges() {
		return getVertex().getOutEdges();
	}

	private void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	public Vertex getVertex() {
		return vertex;
	}

}
