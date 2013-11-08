package by.osmsql.element;

public class TagElement {
private long id;
private String name;
private long keyId;
public TagElement(long id, String name, long keyId) {
	this.id = id;
	this.name = name;
	this.keyId = keyId;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public long getKeyId() {
	return keyId;
}
public void setKeyId(long keyId) {
	this.keyId = keyId;
}

}
