package by.osmsql.model;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

public class XMLReader extends DefaultHandler {
	private long line;
	private long node;
	private long way;
	private long relation;

	public XMLReader() {
		this.line = 0;
		this.node = 0;
		this.way = 0;
		this.relation = 0;
	}

	

	@Override
	public void startElement(String uri, String name, String eName,
			Attributes atts) {
		this.line++;
		if (eName.equals("way"))
			this.way++;
		if (eName.equals("node"))
			this.node++;
		if (eName.equals("relation"))
			this.relation++;

	}

	@Override
	public void endElement(String uri, String name, String eName) {

	}

	@Override
	public void startDocument() throws SAXException {
		super.startDocument();

	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();

	}
	public long getLine() {
		return line;
	}

	public long getNode() {
		return node;
	}

	public long getWay() {
		return way;
	}

	public long getRelation() {
		return relation;
	}

}