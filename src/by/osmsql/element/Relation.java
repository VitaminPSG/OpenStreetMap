package by.osmsql.element;

import org.xml.sax.Attributes;

public class Relation {
	private long  id;
	public Relation(Attributes attr)
	{
		this.id =  Long.valueOf(attr.getValue("","id"));
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
