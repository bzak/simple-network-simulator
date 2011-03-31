package pl.lome.gephi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.client.methods.HttpPost;

import com.tinkerpop.blueprints.pgm.Edge;
import com.tinkerpop.blueprints.pgm.Vertex;

public class GephiStreaming {
	
	public String AddNodeJSON(Vertex vertex) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"an\":{\"");
		sb.append(vertex.getId());
		sb.append("\"");
		//if (!vertex.getPropertyKeys().isEmpty()) {
			sb.append(":{");
			sb.append("\"label\":\""+vertex.getId()+"\"");
			int c = 1;
			for (String key : vertex.getPropertyKeys()) {
				if (c > 0) sb.append(",");
				sb.append("\""+key +"\":" +vertex.getProperty(key));
				c++; 
			}
			sb.append(",\"size\":10");
			sb.append("}");		
		//}
		sb.append("}}");
		return sb.toString();
	}
	
	public String AddEgdeJSON(Edge edge) {
		StringBuffer sb = new StringBuffer();
		sb.append("{\"ae\":{\"");
		sb.append(edge.getId());
		sb.append("\"");
		sb.append(":{");
		sb.append("\"source\":\""+edge.getOutVertex().getId()+"\"");
		sb.append(",\"target\":\""+edge.getInVertex().getId()+"\"");
		sb.append(",\"directed\":true");
		sb.append("}}");
		sb.append("}"); // end node
		return sb.toString();
	}
	
	private void SendJSON(String json) {
		URL url;
		HttpClient client = new HttpClient();
	    client.getParams().setParameter("http.useragent", "Test Client");

	    BufferedReader br = null;

	    PostMethod post = new PostMethod("http://127.0.0.1:8080/workspace0?operation=updateGraph");
	    
	    post.setRequestEntity(new StringRequestEntity(json));
	    try {
			client.executeMethod(post);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	   
	}

	public String convertStreamToString(InputStream is) throws IOException {
		if (is != null) {
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try {
				Reader reader = new BufferedReader(new InputStreamReader(is,
						"UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1) {
					writer.write(buffer, 0, n);
				}
			} finally {
				is.close();
			}
			return writer.toString();
		} else {
			return "";
		}
	}

	
	public void AddNode(Vertex vertex) {
		String json = AddNodeJSON(vertex); 
//		System.out.println(json);
		SendJSON(json);
	}
	
	public void AddEdge(Edge edge) {
		String json = AddEgdeJSON(edge);
//		System.out.println(json);
		SendJSON(json);
	}

	public void ChangeVertex(Vertex vertex) {
		String json = AddNodeJSON(vertex);
		json = json.replaceFirst("\"an\":", "\"cn\":");
//		System.out.println(json);
		SendJSON(json);
	}
}
