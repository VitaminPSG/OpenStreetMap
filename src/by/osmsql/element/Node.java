package by.osmsql.element;
import org.xml.sax.Attributes;
public class Node {
private long  id;
private float lat;
private float lon;
public Node(Attributes attr)
{
	this.id =  Long.valueOf(attr.getValue("","id"));
	this.lat = Float.valueOf(attr.getValue("","lat"));
	this.lon = Float.valueOf(attr.getValue("","lon"));
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public float getLat() {
	return lat;
}
public void setLat(float lat) {
	this.lat = lat;
}
public float getLon() {
	return lon;
}
public void setLon(float lon) {
	this.lon = lon;
}
}
