package by.osmsql.element;

import org.xml.sax.Attributes;

public class Member {
private String type;
private Long ref;
private String role;
public Member(Attributes attr)
{
	this.type = attr.getValue("","type");
	this.ref = Long.valueOf(attr.getValue("","ref"));
	this.role = attr.getValue("","role");
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}
public Long getRef() {
	return ref;
}
public void setRef(Long ref) {
	this.ref = ref;
}
public String getRole() {
	return role;
}
public void setRole(String role) {
	this.role = role;
}

}
