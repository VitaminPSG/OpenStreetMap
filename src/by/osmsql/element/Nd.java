package by.osmsql.element;

import org.xml.sax.Attributes;

public class Nd {
private long ref;
public Nd(Attributes attr)
{
	this.ref = Long.valueOf(attr.getValue("","ref"));
}
public long getRef() {
	return ref;
}
public void setRef(long ref) {
	this.ref = ref;
}
}
