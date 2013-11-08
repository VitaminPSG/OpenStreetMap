package by.osmsql.model;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import by.osmsql.view.Window;


public class XML extends Thread {
	private Window window;
	private SqlDriver sql;
	public XML(SqlDriver sql, Window window )
	{
		this.window = window;
		this.sql = sql;
	}
	@Override
	public void run()
	{
		 SAXParserFactory factory = SAXParserFactory.newInstance();
	        factory.setValidating(false);
	        factory.setNamespaceAware(false);
	        SAXParser parser;
	        InputStream xmlData = null;
	        try
	        {
	          xmlData = new FileInputStream(window.getFilePathValue().getText());
	          parser = factory.newSAXParser();
	          
	          XMLReader reader = new XMLReader();
	          window.addLog("Приступили к чтению файла");
	          parser.parse(xmlData, reader);
	          window.addLog("Количествво строк: " + Long.toString(reader.getLine()));
	          window.addLog("node: " + Long.toString(reader.getNode()));
	          window.addLog("way: " + Long.toString(reader.getWay()));
	          window.addLog("relation: " + Long.toString(reader.getRelation()));
	          window.addLog("Производим запись в MySQL");
	          xmlData.close();
	          xmlData = new FileInputStream(window.getFilePathValue().getText());
	          XMLParser xml =new XMLParser(sql, window, reader.getLine());
	          parser.parse(xmlData, xml);
	        } catch (FileNotFoundException e)
	        {
	            e.printStackTrace();
	            // обработки ошибки, файл не найден
	        } catch (ParserConfigurationException e)
	        {
	            e.printStackTrace();
	            // обработка ошибки Parser
	        } catch (SAXException e)
	        {
	            e.printStackTrace();
	            // обработка ошибки SAX
	        } catch (IOException e)
	        {
	            e.printStackTrace();
	            // обработка ошибок ввода
	        } 
	}	 
	}

