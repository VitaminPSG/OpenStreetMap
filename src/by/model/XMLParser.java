package by.osmsql.model;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import by.osmsql.view.Window;

public class XMLParser extends DefaultHandler {
	private int ipmplement;
	private long line;
	private LogicOSM logic;
	private Window widnow;
	private long onePercent;
	private long nextPercent;
	private boolean extension;
	private String elemName;
	private Long idStart;

	public XMLParser(SqlDriver sql, Window window, long maxLine) {
		this.line = 1;
		this.widnow = window;
		this.logic = new LogicOSM(sql);
		this.onePercent = (long) (maxLine / 1000);
		this.nextPercent = onePercent;
		this.ipmplement = 0;
		if (sql.getE().getId() != 0) {
			this.extension = true;
			this.elemName = sql.getE().getName();
			this.idStart = sql.getE().getId();
			System.out.println("Поиск элемента");
		} else {
			this.extension = false;
			System.out.println("Новая запись");
		}
			
	}

	@Override
	public void startElement(String uri, String name, String eName,
			Attributes atts) {
		if (ipmplement == 0) {
			// root element
		} else if (!extension) {
			logic.newElement(eName, atts);
		} else {
			if (eName.equals(this.elemName)) {
				Long id = Long.valueOf(atts.getValue("", "id"));
				if (id.equals(this.idStart)) {
					extension = false;
					this.widnow.addLog("Продолжает разбор");
					logic.newElement(eName, atts);
				}
			}
		}
		ipmplement++;
		this.line++;
		if (this.line > this.nextPercent) {
			this.nextPercent += this.onePercent;
			int curVal = this.widnow.getProgressBar().getValue();
			int newVal = curVal + 1;
			this.widnow.getProgressBar().setValue(newVal);
			this.widnow.getProgressBar().setString(
					String.valueOf(((double) newVal) / 10) + "%");
		}
	}

	@Override
	public void endElement(String uri, String name, String eName) {
		ipmplement--;
	}

	@Override
	public void startDocument() throws SAXException {

		this.widnow.addLog("Начало разбора документа!");
		if (extension) {
			this.widnow.addLog("Парсинг уже был запущен");
			this.widnow.addLog("Ищем элемент: " + this.elemName + " id="
					+ this.idStart);
		}
		super.startDocument();
	}

	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
		this.widnow.addLog("Разбор документа окончен!");
		this.widnow.addLog("Количество строк: " + this.line);
	}

}