package by.osmsql.element;

import org.xml.sax.Attributes;

public class Tag {
private String k;
private String v;
public Tag(Attributes attr)
{
	this.k = attr.getValue("","k");
	this.v = attr.getValue("","v");
}
public String getK() {
	return k;
}
public void setK(String k) {
	this.k = k;
}
public String getV() {
	return v;
}
public void setV(String v) {
	this.v = v;
}
}
