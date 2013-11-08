package by.osmsql.view;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.TextArea;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.JProgressBar;

public class Window extends Thread {

	private JFrame window;
	private JTextField userValue;
	private JTextField passValue;
	private JTextField dbNameValue;
	private TextArea textArea;
	private JButton btnConnected;
	private JButton btnExport;
	private JTextField filePathValue;
	private JTextField urlValue;
	private JProgressBar progressBar;

	public Window() {
		initialize();
	}

	@Override
	public void run() {

	}

	private void initialize() {
		window = new JFrame();
		window.setTitle("OSMtoMySQL");
		window.setResizable(false);
		window.setBounds(100, 100, 420, 450);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.getContentPane().setLayout(null);

		JLabel dbUrl = new JLabel("DB URL");
		dbUrl.setBounds(10, 29, 100, 14);
		window.getContentPane().add(dbUrl);

		urlValue = new JTextField();
		urlValue.setText("jdbc:mysql://localhost/");
		urlValue.setBounds(120, 26, 203, 20);
		window.getContentPane().add(urlValue);
		urlValue.setColumns(10);

		JLabel user = new JLabel("User");
		user.setBounds(10, 54, 100, 14);
		window.getContentPane().add(user);

		userValue = new JTextField();
		userValue.setText("root");
		userValue.setBounds(120, 51, 203, 20);
		window.getContentPane().add(userValue);
		userValue.setColumns(10);

		JLabel pass = new JLabel("Password");
		pass.setBounds(10, 79, 100, 14);
		window.getContentPane().add(pass);

		passValue = new JTextField();
		passValue.setBounds(120, 76, 203, 20);
		window.getContentPane().add(passValue);
		passValue.setColumns(10);

		JLabel dbName = new JLabel("DB Name");
		dbName.setBounds(10, 104, 100, 14);
		window.getContentPane().add(dbName);

		dbNameValue = new JTextField();
		dbNameValue.setText("Belarus");

		dbNameValue.setBounds(120, 101, 203, 20);
		window.getContentPane().add(dbNameValue);
		dbNameValue.setColumns(10);

		btnConnected = new JButton("Connect");

		btnConnected.setBounds(120, 159, 89, 23);
		window.getContentPane().add(btnConnected);

		btnExport = new JButton("Start");
		btnExport.setBounds(234, 159, 89, 23);
		btnExport.setEnabled(false);
		window.getContentPane().add(btnExport);

		textArea = new TextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 237, 394, 175);
		window.getContentPane().add(textArea);

		JLabel filePath = new JLabel("FilePath");
		filePath.setBounds(10, 129, 46, 14);
		window.getContentPane().add(filePath);

		filePathValue = new JTextField();
		filePathValue.setText("BY.osm");
		filePathValue.setColumns(10);
		filePathValue.setBounds(120, 126, 203, 20);
		window.getContentPane().add(filePathValue);

		progressBar = new JProgressBar();
		progressBar.setMaximum(1000);
		progressBar.setBounds(10, 202, 394, 20);
		progressBar.setStringPainted(true);
		window.getContentPane().add(progressBar);

	}

	public void addPercent() {
		int newValue = this.progressBar.getValue() + 1;
		this.progressBar.setValue(newValue);
		String val = String.valueOf(((double) newValue) / 100);
		this.progressBar.setString(val);
		System.out.println("текущий прогресс: " + val);
	}

	public void addLog(String str) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		this.textArea.append(sdf.format(cal.getTime()) + " > " + str + "\n");
	}

	public JTextField getUrlValue() {
		return urlValue;
	}

	public void setUrlValue(JTextField urlValue) {
		this.urlValue = urlValue;
	}

	public JTextField getUserValue() {
		return userValue;
	}

	public void setUserValue(JTextField userValue) {
		this.userValue = userValue;
	}

	public JTextField getPassValue() {
		return passValue;
	}

	public void setPassValue(JTextField passValue) {
		this.passValue = passValue;
	}

	public JTextField getDbNameValue() {
		return dbNameValue;
	}

	public void setDbNameValue(JTextField dbNameValue) {
		this.dbNameValue = dbNameValue;
	}

	public JFrame getFrame() {
		return window;
	}

	public void setFrame(JFrame frame) {
		this.window = frame;
	}

	public JButton getConnected() {
		return btnConnected;
	}

	public void setConnected(JButton btnConnected) {
		this.btnConnected = btnConnected;
	}

	public JButton getExport() {
		return btnExport;
	}

	public void setExport(JButton btnExport) {
		this.btnExport = btnExport;
	}

	public JTextField getFilePathValue() {
		return filePathValue;
	}

	public void setFilePathValue(JTextField filePathValue) {
		this.filePathValue = filePathValue;
	}

	public JProgressBar getProgressBar() {
		return progressBar;
	}

	public void setProgressBar(JProgressBar progressBar) {
		this.progressBar = progressBar;
	}
}
