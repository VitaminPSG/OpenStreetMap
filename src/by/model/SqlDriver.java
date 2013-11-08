package by.osmsql.model;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import by.osmsql.element.Element;
import by.osmsql.element.TagElement;
import by.osmsql.view.Window;

public final class SqlDriver {
	private long iTagKey;
	private long iTagUK;
	private long iTagValue;
	private long iTagUValue;
	private long iNd;
	private long iTagNode;
	private long iTagWay;
	private long iTagRelation;
	private long iMember;
	private long iRole;
	private Statement statement;
	private Connection connection;
	private Window window;
	private Element e;

	public SqlDriver(Window w) {
		this.window = w;
		this.iRole = 1;
		this.iNd = 1;
		this.iMember = 1;
		this.iTagNode = 1;
		this.iTagWay = 1;
		this.iTagRelation = 1;
		this.iTagUK = 1;
		this.iTagUValue = 1;
		this.iTagValue = 1;
		this.e = new Element("node", 0);
	}

	private boolean initStart() {
		boolean result = false;
		if (update("USE " + window.getDbNameValue().getText()) >= 0) {
			
			try {
				ResultSet rs = execute("SELECT * FROM `relation`  ORDER BY `id` DESC LIMIT 1");
				if (rs != null) {
					if (rs.next()) {
						long id = rs.getLong("id");
						update("DELETE FROM `member_node` WHERE `id_relation` = "
								+ id);
						update("DELETE FROM `member_way` WHERE `id_relation` = "
								+ id);
						update("DELETE FROM `member_relation` WHERE `id_relation` = "
								+ id);
						update("DELETE FROM `relation_tag` WHERE `id_relation` = "
								+ id);
						this.e = new Element("relation", id);
						System.out.println("Найден элемент relation id:"+id);
						rs.close();
						rs = null;
					return true;
					}
				}

				rs = execute("SELECT * FROM `way` ORDER BY `id` DESC LIMIT 1");
				if (rs != null) {
					if (rs.next()) {
						long id = rs.getLong("id");
						update("DELETE FROM `way_tag` WHERE `id_way` = " + id);
						update("DELETE FROM `nd` WHERE `id_way` = " + id);
						this.e = new Element("way", id);
						System.out.println("Найден элемент way id:"+id);
						rs.close();
						rs = null;
					return true;
					}
				}
				rs = execute("SELECT * FROM `node` ORDER BY `id` DESC LIMIT 1");
				if (rs != null) {
					if (rs.next()) {
						long id = rs.getLong("id");
						update("DELETE FROM `node_tag` WHERE `id_node` = " + id);
						this.e = new Element("node", id);
						System.out.println("Найден элемент node id:"+id);
						rs.close();
						rs = null;
					return true;
					}
				}
			} catch (SQLException e) {
				System.out.println("Ошибка поиска последнего элемента");
			}
		}
		return result;
	}

	private void setIndex() {
		try {
			ResultSet rs = execute("SELECT `id` FROM `member_node` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iMember = rs.getLong("id");
				System.out.println("iMemberNode: " + iMember);
			}
			rs.close();
			rs = null;
			rs = execute("SELECT `id` FROM `member_relation` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iMember = iMember > rs.getLong("id") ? iMember : rs
						.getLong("id");
				System.out.println("iMemberRelation: " + iMember);
			}
			rs.close();
			rs = null;
			rs = execute("SELECT `id` FROM `member_way` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iMember = iMember > rs.getLong("id") ? iMember : rs
						.getLong("id");
				System.out.println("iMemberWay: " + iMember);
			}
			rs.close();
			rs = null;
			iMember++;
			rs = execute("SELECT `id` FROM `nd` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iNd = rs.getLong("id") + 1;
				System.out.println("iNd: " + iNd);
			}
			rs.close();
			rs = null;
			rs = execute("SELECT `id` FROM `node_tag` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iTagNode = rs.getLong("id") + 1;
				System.out.println("iTagNode: " + iTagNode);
			}
			rs.close();
			rs = null;
			rs = execute("SELECT `id` FROM `relation_tag` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iTagRelation = rs.getLong("id") + 1;
				System.out.println("iTagRelation: " + iTagRelation);
			}
			rs.close();
			rs = null;
			rs = execute("SELECT `id` FROM `role` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iRole = rs.getLong("id") + 1;
				System.out.println("iRole: " + iRole);
			}
			rs.close();
			rs = null;
			rs = execute("SELECT `id` FROM `tag_value` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iTagValue = rs.getLong("id") + 1;
				System.out.println("iTagValue: " + iTagValue);
			}
			rs.close();
			rs = null;
			rs = execute("SELECT `id` FROM `way_tag` ORDER BY `id` DESC LIMIT 1");
			if (rs.next()) {
				iTagWay = rs.getLong("id") + 1;
				System.out.println("iTagWay: " + iTagWay);
			}
			rs.close();
			rs = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void loadSchema() {
		if (initStart()) {
			window.addLog("Таблица уже создана");
			setIndex();
		} else {
			window.addLog("Загружаем схему");
			update("CREATE DATABASE IF NOT EXISTS "
					+ window.getDbNameValue().getText());
			update("USE " + window.getDbNameValue().getText());
			getShema("shema.sh");

		}

	}

	public long getiTagKey() {
		return iTagKey;
	}

	public void setiTagKey(long iTagKey) {
		this.iTagKey = iTagKey;
	}

	public long getiTagUK() {
		return iTagUK;
	}

	public void setiTagUK(long iTagUK) {
		this.iTagUK = iTagUK;
	}

	public long getiTagValue() {
		return iTagValue;
	}

	public void setiTagValue(long iTagValue) {
		this.iTagValue = iTagValue;
	}

	public long getiTagUValue() {
		return iTagUValue;
	}

	public void setiTagUValue(long iTagUValue) {
		this.iTagUValue = iTagUValue;
	}

	public long getiTagNode() {
		return iTagNode;
	}

	public void setiTagNode(long iTagNode) {
		this.iTagNode = iTagNode;
	}

	public long getiTagWay() {
		return iTagWay;
	}

	public void setiTagWay(long iTagWay) {
		this.iTagWay = iTagWay;
	}

	public long getiTagRelation() {
		return iTagRelation;
	}

	public void setiTagRelation(long iTagRelation) {
		this.iTagRelation = iTagRelation;
	}

	public long getiRole() {
		return iRole;
	}

	public void setiRole(long iRole) {
		this.iRole = iRole;
	}

	public boolean getConnection() {
		String url = window.getUrlValue().getText();
		String user = window.getUserValue().getText();
		String pass = window.getPassValue().getText();
		window.addLog("Connected to: " + url);
		boolean result = false;
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			connection = DriverManager.getConnection(url, user, pass);
			if (connection != null) {
				window.addLog("Connection Successful !\n");
				result = true;
			}
			if (connection == null) {
				window.addLog("Connection Error !\n");
				result = false;
			}
			statement = connection.createStatement();
		} catch (SQLException e) {
			window.addLog(e.toString());
			result = false;
		}
		return result;
	}

	public int update(String sql) {

		int rs = -1;
		try {
			rs = statement.executeUpdate(sql);
		} catch (SQLException e) {
		}
		System.out.println("sql [" + rs + "]-> " + sql);
		return rs;
	}

	public ResultSet execute(String sql) {
		ResultSet rs = null;
		try {
			rs = this.statement.executeQuery(sql);
			System.out.println("sql [ ]<- " + sql);
		} catch (SQLException e) {
			System.out.println("sql [x]<- " + sql);
		}
		return rs;
	}

	public ArrayList<Element> getTagKey() {
		ArrayList<Element> tagKey = new ArrayList<Element>();
		ResultSet rs = execute("SELECT * FROM  `tag_key`");
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("k");
				Element e = new Element(name, id);
				tagKey.add(e);
			}
			rs.close();
			rs = null;
			return tagKey;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tagKey;
	}

	public ArrayList<TagElement> getTagValue() {
		ArrayList<TagElement> tag = new ArrayList<TagElement>();
		ResultSet rs = execute("SELECT * FROM `tag_value` WHERE `id_tag_key` < 30");
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("v");
				long idTag = rs.getLong("id_tag_key");
				TagElement e = new TagElement(id, name, idTag);
				tag.add(e);
			}
			rs.close();
			rs = null;
			return tag;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}

	public ArrayList<TagElement> getHouseNumber() {
		ArrayList<TagElement> tag = new ArrayList<TagElement>();
		ResultSet rs = execute("SELECT * FROM `tag_value` WHERE `id_tag_key` = 100");
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("v");
				TagElement e = new TagElement(id, name, 100);
				tag.add(e);
			}
			rs.close();
			rs = null;
			return tag;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}

	public ArrayList<TagElement> getCity() {
		ArrayList<TagElement> tag = new ArrayList<TagElement>();
		ResultSet rs = execute("SELECT * FROM `tag_value` WHERE `id_tag_key` = 105");
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("v");
				TagElement e = new TagElement(id, name, 105);
				tag.add(e);
			}
			rs.close();
			rs = null;
			return tag;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}
	public ArrayList<TagElement> getStreet() {
		ArrayList<TagElement> tag = new ArrayList<TagElement>();
		ResultSet rs = execute("SELECT * FROM `tag_value` WHERE `id_tag_key` = 102");
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("v");
				TagElement e = new TagElement(id, name, 102);
				tag.add(e);
			}
			rs.close();
			rs = null;
			return tag;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}

	public ArrayList<TagElement> getPostCode() {
		ArrayList<TagElement> tag = new ArrayList<TagElement>();
		ResultSet rs = execute("SELECT * FROM `tag_value` WHERE `id_tag_key` = 104");
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("v");
				TagElement e = new TagElement(id, name, 104);
				tag.add(e);
			}
			rs.close();
			rs = null;
			return tag;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}

	public ArrayList<TagElement> getName() {
		ArrayList<TagElement> tag = new ArrayList<TagElement>();
		ResultSet rs = execute("SELECT * FROM `tag_value` WHERE `id_tag_key` = 115");
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("v");
				TagElement e = new TagElement(id, name, 115);
				tag.add(e);
			}
			rs.close();
			rs = null;
			return tag;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}

	public ArrayList<TagElement> getCountry() {
		ArrayList<TagElement> tag = new ArrayList<TagElement>();
		ResultSet rs = execute("SELECT * FROM `tag_value` WHERE `id_tag_key` = 106");
		try {
			while (rs.next()) {
				long id = rs.getLong("id");
				String name = rs.getString("v");
				TagElement e = new TagElement(id, name, 32);
				tag.add(e);
			}
			rs.close();
			rs = null;
			return tag;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tag;
	}

	public boolean insertNode(long id, float lat, float lon) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `node`(`id`, `lat`, `lon`) VALUES (?,?,?)");
			ps.setLong(1, id);
			ps.setFloat(2, lat);
			ps.setFloat(3, lon);
			ps.executeUpdate();
			ps.close();
			ps = null;
			result = true;
		} catch (SQLException e) {
		//	System.out.println("Ошибка! INSERT INTO `node`(`id`, `lat`, `lon`) VALUES ("id + ", " + lat + ", " + lon + ")");
		}
		return result;
	}

	public boolean insertWay(long id) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `way`(`id`) VALUES (?)");
			ps.setLong(1, id);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
		} catch (SQLException e) {
			System.out.println("Ошибка! INSERT INTO `way`(`id`) VALUES (" + id
					+ ")");
		}
		return result;
	}

	public boolean insertRelation(long id) {

		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `relation`(`id`) VALUES (?)");
			ps.setLong(1, id);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
		} catch (SQLException e) {
			System.out.println("Ошибка! INSERT INTO `relation`(`id`) VALUES ("
					+ id + ")");
		}
		return result;
	}

	public boolean insertNd(long idWay, long idNode) {

		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `nd`(`id`,`id_way`,`id_node`) VALUES (?,?,?)");
			ps.setLong(1, this.iNd);
			ps.setLong(2, idWay);
			ps.setLong(3, idNode);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
			this.iNd++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `nd`(`id`,`id_way`,`id_node`)  VALUES ("
							+ this.iNd + ", " + idWay + ", " + idNode + ")");
		}
		return result;
	}

	public boolean insertTagKey(String k) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `tag_key`(`id`,`k`) VALUES (?,?)");
			ps.setLong(1, iTagKey);
			ps.setString(2, k);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
			iTagKey++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `tag_key`(`id`,`k`)  VALUES ("
							+ iTagKey + ", " + k + ")");
		}
		return result;
	}

	public boolean insertUcertainKey(String k) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `uncertain_key`(`id`,`k`) VALUES (?,?)");
			ps.setLong(1, iTagUK);
			ps.setString(2, k);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
			iTagUK++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `uncertain_key`(`id`,`k`)  VALUES ("
							+ iTagUK + ", " + k + ")");
		}
		return result;
	}

	public boolean insertTagValue(String v, Long id) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `tag_value`(`id`,`v`,`id_tag_key`) VALUES (?,?,?)");
			ps.setLong(1, iTagValue);
			ps.setString(2, v);
			ps.setLong(3, id);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
			iTagValue++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `tag_value`(`id`,`v`,,`id_tag_key)  VALUES ("
							+ iTagValue + ", " + v + "," + id + ")");
		}
		return result;
	}

	public boolean insertUcertainValue(String v, int idKey) {

		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `uncertain_value`(`id`,`v`,`id_tag_key`) VALUES (?,?,?)");
			ps.setLong(1, iTagUValue);
			ps.setString(2, v);
			ps.setInt(3, idKey);
			ps.executeUpdate();
			ps.close();
			ps = null;
			result = true;
			iTagUValue++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `uncertain_value`(`id`,`v`,`id_tag_key)  VALUES ("
							+ iTagUValue + ", " + v + "," + idKey + ")");
		}
		return result;
	}

	public boolean insertNodeTag(long idNode, long idTag) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `node_tag`(`id`,`id_node`,`id_tag`) VALUES (?,?,?)");
			ps.setLong(1, iTagNode);
			ps.setLong(2, idNode);
			ps.setLong(3, idTag);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
			iTagNode++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `node_tag`(`id`,`id_node`,`id_tag)  VALUES ("
							+ iTagNode + ", " + idNode + "," + idTag + ")");
		}
		return result;
	}

	public boolean insertWayTag(long idWay, long l) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `way_tag`(`id`,`id_way`,`id_tag`) VALUES (?,?,?)");
			ps.setLong(1, iTagWay);
			ps.setLong(2, idWay);
			ps.setLong(3, l);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
			iTagWay++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `way_tag`(`id`,`id_way`,`id_tag)  VALUES ("
							+ iTagWay + ", " + idWay + "," + l + ")");
		}
		return result;
	}

	public boolean insertRelationTag(long idRelation, long idValue) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `relation_tag`(`id`,`id_relation`,`id_tag`) VALUES (?,?,?)");
			ps.setLong(1, iTagRelation);
			ps.setLong(2, idRelation);
			ps.setLong(3, idValue);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
			iTagRelation++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `relation_tag`(`id`,`id_relation`,`id_tag)  VALUES ("
							+ iTagRelation
							+ ", "
							+ idRelation
							+ ","
							+ idValue
							+ ")");
		}
		return result;
	}

	public boolean insertMemberNode(long idNode, long idRelation, long idRole) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `member_node` (`id`,`id_node`, `id_relation`, `id_role` ) VALUES (?,?,?,?)");
			ps.setLong(1, iMember);
			ps.setLong(2, idNode);
			ps.setLong(3, idRelation);
			ps.setLong(4, idRole);
			ps.executeUpdate();
			result = true;
			ps.close();
			ps = null;
			iMember++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `member_node`(`id`,`id_node`,`id_relation`, `id_role`)  VALUES ("
							+ iMember
							+ ", "
							+ idNode
							+ ","
							+ idRelation
							+ ","
							+ idRole + ")");
		}
		return result;
	}

	public boolean insertMemberWay(long idWay, long idRelation, long idRole) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `member_way` (`id`,`id_way`, `id_relation`, `id_role` ) VALUES (?,?,?,?)");
			ps.setLong(1, iMember);
			ps.setLong(2, idWay);
			ps.setLong(3, idRelation);
			ps.setLong(4, idRole);
			ps.executeUpdate();
			ps.close();
			ps = null;
			result = true;
			iMember++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `member_way`(`id`,`id_way`,`id_relation`, `id_role`)  VALUES ("
							+ iMember
							+ ", "
							+ idWay
							+ ","
							+ idRelation
							+ ","
							+ idRole + ")");

		}
		return result;
	}

	public boolean insertMemberRelation(long idRel, long idRelation, long idRole) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `member_relation` (`id`,`id_rel`, `id_relation`, `id_role` ) VALUES (?,?,?,?)");
			ps.setLong(1, iMember);
			ps.setLong(2, idRel);
			ps.setLong(3, idRelation);
			ps.setLong(4, idRole);
			ps.executeUpdate();
			ps.close();
			ps = null;
			result = true;
			iMember++;
		} catch (SQLException e) {
			System.out
					.println("Ошибка! INSERT INTO `member_relation`(`id`,`id_way`,`id_relation`, `id_role`)  VALUES ("
							+ iMember
							+ ", "
							+ idRel
							+ ","
							+ idRelation
							+ ","
							+ idRole + ")");

		}
		return result;
	}

	public boolean insertRole(String v) {
		boolean result = false;
		try {
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO `role` (`id`,`v`) VALUES (?,?)");
			ps.setLong(1, iRole);
			ps.setString(2, v);
			ps.executeUpdate();
			ps.close();
			ps = null;
			result = true;
			iRole++;
		} catch (SQLException e) {
			System.out.println("Ошибка" + e.getMessage()
					+ "! INSERT INTO `role`(`id`,`v`)  VALUES (" + iRole + ", "
					+ v + ")");
		}
		return result;
	}

	private void getShema(String file) {
		BufferedReader shema = null;
		try {
			shema = new BufferedReader(new FileReader(file));

			String line;
			line = shema.readLine();
			while (line != null) {
				update(line);
				line = shema.readLine();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				shema.close();
				shema = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public Element getE() {
		return e;
	}

	public void setE(Element e) {
		this.e = e;
	}
}
