package by.osmsql.model;
import java.util.ArrayList;

import org.xml.sax.Attributes;

import by.osmsql.element.*;

//ver 1.0
public class LogicOSM {
	private Element eParent;
	private SqlDriver sql;
	private ArrayList<Element> role;
	private ArrayList<Element> tagKey;
	private ArrayList<TagElement> tagValue;
	private ArrayList<TagElement> houseNumber;
	private ArrayList<TagElement> postCode;
	private ArrayList<TagElement> city;
	private ArrayList<TagElement> street;
	private ArrayList<TagElement> name;
	private ArrayList<TagElement> country;

	public LogicOSM(SqlDriver sql) {
		this.sql = sql;
		this.tagKey = sql.getTagKey();
		this.tagValue = sql.getTagValue();
		this.houseNumber = sql.getHouseNumber();
		this.postCode = sql.getPostCode();
		this.street = sql.getStreet();
		this.city = sql.getCity();
		this.postCode = sql.getPostCode();
		this.name = sql.getName();
		this.country = sql.getCountry();
		this.role = new ArrayList<Element>();
	}

	public long getTagKeyId(String key) {
		long id = -1;
		for (Element e : tagKey) {
			if (e.getName().equals(key)) {
				id = e.getId();
				return id;
			}
		}
		return id;
	}

	public TagElement getTag(Long id, String value) {
		TagElement tagElement;
		if (id < 100) {
			for (TagElement tE : this.tagValue) {
				if ((tE.getName().equals(value)) && (tE.getKeyId() == id)) {
					tagElement = new TagElement(tE.getId(), value, id);
					return tagElement;
				}
			}
			tagElement = new TagElement(sql.getiTagValue(), value, id);
			this.tagValue.add(tagElement);
			sql.insertTagValue(value, id);
			return tagElement;
		} else if (id == 100) {
			for (TagElement tE : this.houseNumber) {
				if ((tE.getName().equals(value)) && (tE.getKeyId() == id)) {
					tagElement = new TagElement(tE.getId(), value, id);
					return tagElement;
				}
			}
			tagElement = new TagElement(sql.getiTagValue(), value, id);
			this.houseNumber.add(tagElement);
			sql.insertTagValue(value, id);
			return tagElement;
		} else if (id == 102) {
			for (TagElement tE : this.street) {
				if ((tE.getName().equals(value)) && (tE.getKeyId() == id)) {
					tagElement = new TagElement(tE.getId(), value, id);
					return tagElement;
				}
			}
			tagElement = new TagElement(sql.getiTagValue(), value, id);
			this.street.add(tagElement);
			sql.insertTagValue(value, id);
			return tagElement;
		} else if (id == 104) {
			for (TagElement tE : this.postCode) {
				if ((tE.getName().equals(value)) && (tE.getKeyId() == id)) {
					tagElement = new TagElement(tE.getId(), value, id);
					return tagElement;
				}
			}
			tagElement = new TagElement(sql.getiTagValue(), value, id);
			this.postCode.add(tagElement);
			sql.insertTagValue(value, id);
			return tagElement;
		} else if (id == 105) {
			for (TagElement tE : this.city) {
				if ((tE.getName().equals(value)) && (tE.getKeyId() == id)) {
					tagElement = new TagElement(tE.getId(), value, id);
					return tagElement;
				}
			}
			tagElement = new TagElement(sql.getiTagValue(), value, id);
			this.city.add(tagElement);
			sql.insertTagValue(value, id);
			return tagElement;
		}

		else if (id == 106) {
			for (TagElement tE : this.country) {
				if ((tE.getName().equals(value)) && (tE.getKeyId() == id)) {
					tagElement = new TagElement(tE.getId(), value, id);
					return tagElement;
				}
			}
			tagElement = new TagElement(sql.getiTagValue(), value, id);
			this.country.add(tagElement);
			sql.insertTagValue(value, id);
			return tagElement;
		} else if (id == 115) {
			for (TagElement tE : this.name) {
				if ((tE.getName().equals(value)) && (tE.getKeyId() == id)) {
					tagElement = new TagElement(tE.getId(), value, id);
					return tagElement;
				}
			}
			tagElement = new TagElement(sql.getiTagValue(), value, id);
			this.name.add(tagElement);
			sql.insertTagValue(value, id);
			return tagElement;
		} else {
			tagElement = new TagElement(sql.getiTagValue(), value, id);
			sql.insertTagValue(value, id);
			return tagElement;
		}
	}

	public long getRoleIndex(String r) {
		long index = 1;
		for (Element e : this.role) {
			if (e.getName().equals(r)) {
				index = e.getId();
				return index;
			}
		}
		sql.insertRole(r);
		index = sql.getiRole();
		Element e = new Element(r, index);
		role.add(e);

		return index;
	}

	public void newElement(String eName, Attributes attr) {
		switch (eName) {
		case "node":
			Node node = new Node(attr);
			eParent = null;
			eParent = new Element("node", node.getId());
			sql.insertNode(node.getId(), node.getLat(), node.getLon());
			node = null;
			break;
		case "way":
			Way way = new Way(attr);
			eParent = null;
			eParent = new Element("way", way.getId());
			sql.insertWay(way.getId());
			way = null;
			break;
		case "relation":
			Relation relation = new Relation(attr);
			eParent = null;
			eParent = new Element("relation", relation.getId());
			sql.insertRelation(relation.getId());
			relation = null;
			break;
		case "nd":
			Nd nd = new Nd(attr);
			sql.insertNd(eParent.getId(), nd.getRef());
			nd = null;
			break;
		case "member":
			Member member = new Member(attr);
			long idRole = this.getRoleIndex(member.getRole());
			if (member.getType().equals("node")) {
				sql.insertMemberNode(member.getRef(), eParent.getId(), idRole);

			} else if (member.getType().equals("way")) {
				sql.insertMemberWay(member.getRef(), eParent.getId(), idRole);
			} else if (member.getType().equals("relation")) {
				sql.insertMemberRelation(member.getRef(), eParent.getId(),
						idRole);
			} else {
				// error
			}
			member = null;
			break;
		case "tag":
			Tag tag = new Tag(attr);
			long keyId = getTagKeyId(tag.getK());
			if (keyId > 0) {
				TagElement tagElement = this.getTag(keyId, tag.getV());
				if (eParent.getName().equals("node")) {
					sql.insertNodeTag(eParent.getId(), tagElement.getId());
				} else if (eParent.getName().equals("way")) {
					sql.insertWayTag(eParent.getId(), tagElement.getId());
				} else if (eParent.getName().equals("relation")) {
					sql.insertRelationTag(eParent.getId(), tagElement.getId());
				} else {
					// error
				}
			}
			tag = null;
			break;
		}

	}
}
