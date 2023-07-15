package com.qa.api.fetch.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import net.miginfocom.swing.MigLayout;

public class ApiFetch extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField uriInput;
	private JTextField dataInput;
	private JTextField outputFolder;
	private JTextField filter;
	private JTextField skip;
	private JTextField ClientID;
	private JTextArea Request;

	private String inputFilePath;
	private long lines;
	private String outputFolderPath;
	private String URL;
	private String Filter;
	private long Skip;
	private boolean REST;
	private String RequestLine;
	private int Counter;
	private String Client;
	private String logs = "";
	private String newRequestLine;
	private static boolean pause = false;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ApiFetch frame = new ApiFetch();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ApiFetch() {
		setTitle("Data Helper");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[60px][60px][300px,grow][][60px]", "[][][][][][][200px][][][grow][]"));

		JLabel labelURL = new JLabel("URL");
		contentPane.add(labelURL, "cell 0 0,alignx left,aligny center");

		uriInput = new JTextField();
		contentPane.add(uriInput, "cell 1 0 4 1,growx");
		uriInput.setColumns(10);

		JLabel labelInput = new JLabel("Input");
		contentPane.add(labelInput, "cell 0 1,alignx left,aligny center");

		dataInput = new JTextField();
		contentPane.add(dataInput, "cell 1 1 3 1,growx");
		dataInput.setColumns(10);

		JLabel labelOUTPUT = new JLabel("Output Folder");
		contentPane.add(labelOUTPUT, "cell 0 2,alignx left,aligny center");

		outputFolder = new JTextField();
		contentPane.add(outputFolder, "cell 1 2 3 1,growx");
		outputFolder.setColumns(10);

		JLabel labelFilter = new JLabel("Filter");
		contentPane.add(labelFilter, "cell 0 3,alignx left,aligny center");

		filter = new JTextField();
		filter.setColumns(10);
		contentPane.add(filter, "cell 1 3 3 1,growx");

		JLabel labelSkip = new JLabel("Skip");
		contentPane.add(labelSkip, "cell 0 4,alignx left,aligny center");

		skip = new JTextField();
		contentPane.add(skip, "cell 1 4");
		skip.setColumns(10);

		JCheckBox restCheck = new JCheckBox("REST");
		contentPane.add(restCheck, "flowx,cell 2 4");

		JLabel clientIdLabel = new JLabel("Client ID");
		contentPane.add(clientIdLabel, "cell 3 4,alignx trailing");

		ClientID = new JTextField();
		contentPane.add(ClientID, "cell 4 4,growx");
		ClientID.setColumns(10);

		JLabel labelRequest = new JLabel("Request");
		contentPane.add(labelRequest, "cell 0 5,alignx left,aligny center");

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, "cell 0 6 5 1,grow");

		Request = new JTextArea();
		scrollPane.setViewportView(Request);
		
		JButton pauseResume = new JButton("Pause/Resume");
		pauseResume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Thread(new Runnable() {
			        public void run(){
			        	pause = !pause;
			        }
			    }).start();
				
			}
		});
		contentPane.add(pauseResume, "cell 3 7");

		JLabel labelLog = new JLabel("Log");
		contentPane.add(labelLog, "cell 0 8,alignx left,aligny center");

		JLabel statusUpdate = new JLabel("");
		contentPane.add(statusUpdate, "cell 2 8");

		JScrollPane scrollPane_1 = new JScrollPane();
		contentPane.add(scrollPane_1, "cell 0 9 5 1,grow");

		JTextArea log = new JTextArea();
		scrollPane_1.setViewportView(log);

		JButton browseButton = new JButton("Browse");
		browseButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				j.setAcceptAllFileFilterUsed(false);
				FileNameExtensionFilter restrict = new FileNameExtensionFilter("Only .txt files", "txt");
				j.addChoosableFileFilter(restrict);
				int r = j.showSaveDialog(null);

				if (r == JFileChooser.APPROVE_OPTION)

				{
					// set the label to the path of the selected file
					inputFilePath = j.getSelectedFile().getAbsolutePath();
					dataInput.setText(inputFilePath);
					skip.setText("0");

				}
				// if the user cancelled the operation
				else
					dataInput.setText("Please select the data file...(only .txt files)");

			}
		});
		contentPane.add(browseButton, "cell 4 1,growx");

		JButton browseButtonFolder = new JButton("Browse");
		browseButtonFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser j = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				j.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r = j.showSaveDialog(null);

				if (r == JFileChooser.APPROVE_OPTION) {
					outputFolder.setText(j.getSelectedFile().getAbsolutePath());
				}

			}
		});
		contentPane.add(browseButtonFolder, "cell 4 2,growx");

		JButton buttonStart = new JButton("Start");
		buttonStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new Thread(new Runnable() {
					public void run() {

						logs = "";
						Counter = 0;

						String SkipTest = skip.getText();
						if (StringUtils.isBlank(SkipTest))
							Skip = 0;
						else
							Skip = Integer.parseInt(skip.getText());
						outputFolderPath = outputFolder.getText();

						inputFilePath = dataInput.getText();
						lines = new com.qa.api.fetch.helper.Helpers().countLineFast(inputFilePath);
						statusUpdate.setText("Total records : " + (lines - Skip));

						URL = uriInput.getText();
						if (StringUtils.isBlank(URL)) {
							uriInput.setText("Please Enter a URL");
						}

						if (StringUtils.isBlank(outputFolderPath)) {
							outputFolder.setText("Please Enter a output folder");
						} else {
							if (Skip == 0)
								try {
									FileUtils.cleanDirectory(new File(outputFolderPath));
								} catch (IOException e1) {
									e1.printStackTrace();
								}
						}

						if (StringUtils.isBlank(inputFilePath))
							dataInput.setText("Please select the data file...(only .txt files)");

						Filter = filter.getText();

						REST = restCheck.isSelected();
						RequestLine = Request.getText();
						Client = ClientID.getText();

						BufferedReader reader;

						try {

							reader = new BufferedReader(new FileReader(inputFilePath));
							String line = reader.readLine();

							while (line != null) {
								Counter++;

								if (Counter > Skip) {
									
									while(pause)
									{
										Thread.sleep(1000);
									}

									String key = line;
									newRequestLine = RequestLine.replace("{data}", key);

									logs = Counter + ") " + key + " -- executing" + "\n" + logs;

									statusUpdate.setText("");
									statusUpdate.setText("Total records : " + (lines - Skip));
									contentPane.update(contentPane.getGraphics());

									Request.setText("");
									Request.setText(newRequestLine);
									Request.update(Request.getGraphics());

									log.setText("");
									log.setText(logs);
									log.update(log.getGraphics());

									if (REST) {

										if (StringUtils.isBlank(RequestLine)) {

											if (key.contains(":")) {
												logs = new com.qa.api.fetch.helper.Helpers().PerformAPIActions(
														URL.replace("{data1}", key.split(":")[0]).replace("{data2}",
																key.split(":")[1]),
														"", Client, Filter, outputFolderPath, key, true, true, logs);
											} else {
												logs = new com.qa.api.fetch.helper.Helpers().PerformAPIActions(
														URL.replace("{data}", key), "", Client, Filter,
														outputFolderPath, key, true, true, logs);
											}

										} else {

											logs = new com.qa.api.fetch.helper.Helpers().PerformAPIActions(URL,
													newRequestLine, Client, Filter, outputFolderPath, key, true, false,
													logs);
										}

										// Soap
									} else {
										logs = new com.qa.api.fetch.helper.Helpers().PerformAPIActions(URL,
												newRequestLine, Client, Filter, outputFolderPath, key, false, false,
												logs);
									}
								}

								line = reader.readLine();
							}
							reader.close();
							Request.setText("");
							Request.setText(newRequestLine);
							Request.update(Request.getGraphics());

							log.setText("");
							log.setText(logs);
							log.update(log.getGraphics());

						} catch (Exception ex) {
							ex.printStackTrace();
						}

					}
				}).start();

			}

		});
		contentPane.add(buttonStart, "cell 4 7,growx");

		JLabel lblNewLabel = new JLabel("Info");
		lblNewLabel.setToolTipText("@author : Viraj J - virajjohndev@gmail.com   version : 1.0.0");
		contentPane.add(lblNewLabel, "cell 4 10,alignx trailing");

	}

}
