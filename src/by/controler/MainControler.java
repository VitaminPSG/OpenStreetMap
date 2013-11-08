package by.osmsql.controler;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import by.osmsql.model.SqlDriver;
import by.osmsql.model.XML;
import by.osmsql.view.Window;


public class MainControler{
	private final Window window;
	private final SqlDriver sql;
	
public MainControler()
{
	this.window = new Window();
	window.start();
	this.sql = new SqlDriver(window);
}
public void init()
{
	System.out.println("Метод run из Controller");
	try {
		window.getFrame().setVisible(true);
		window.addLog("Hello");
		window.getConnected().addActionListener(new ActionListener() {
			 @Override 
	            public void actionPerformed(ActionEvent e) { 
				if(sql.getConnection()) sql.loadSchema();
				window.getConnected().setEnabled(false);
				window.getExport().setEnabled(true);
				}
		});
		window.getExport().addActionListener(new ActionListener() {
			 @Override 
	            public void actionPerformed(ActionEvent e) { 
				 window.addLog("Export");
				 window.getExport().setEnabled(false);
				 XML xml = new XML(sql, window);
				 xml.start();
			 }
		});
		
	} catch (Exception e) {
		e.printStackTrace();
	}
}



}
