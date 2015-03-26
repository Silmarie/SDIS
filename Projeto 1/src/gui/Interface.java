package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.JTextPane;
import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JButton;

import protocols.Config;

public class Interface extends JFrame {

	private Config config;
	JTextField mcIP, mdbIP, mdrIP;
	JSpinner mcPort, mdbPort, mdrPort;

	private static final long serialVersionUID = 1L;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Interface frame = new Interface();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	// quando a thread come�a tem que estar pronto para enviar
	// pedidos e para receber pedidos logo ter� que se juntar a um
	// servidor de multicast para ficar a espera de pedidos, sempre
	// que se pretende criar um backup � criado � criado um
	// datagramsocket para o efeito que depois � fechado!

	// duvida? criar apenas 1 receiver que recebe todos os pedidos e os
	// interpreta ou separar logo por threads??

	/**
	 * Create the frame.
	 * 
	 * @throws ParseException
	 * @throws IOException
	 */
	public Interface() throws ParseException, IOException {

		getContentPane().setBackground(Color.LIGHT_GRAY);
		setBackground(Color.LIGHT_GRAY);
		setTitle("Dropbox LAN version");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 620, 560);
		setLocationRelativeTo(null);
		getContentPane().setLayout(null);

		// DEFINICOES DE IP
		configurations();

		// MANAGE FILES
		manageFiles();

		// LOGS TEXT
		logsText();

		// CONFIGURATIONS LOAD
		config = new Config();
		if (!config.readConfigurations()) {
			JOptionPane.showMessageDialog(null,
					"Input configurations and save it!");
		} else {
			mcIP.setText(config.getConfig()[0]);
			mcPort.setValue(Integer.parseInt(config.getConfig()[1]));

			mdbIP.setText(config.getConfig()[2]);
			mdbPort.setValue(Integer.parseInt(config.getConfig()[3]));

			mdrIP.setText(config.getConfig()[4]);
			mdrPort.setValue(Integer.parseInt(config.getConfig()[5]));
		}

	}

	private void manageFiles() {
		JLabel lblManageFiles = new JLabel("Manage Files");
		lblManageFiles.setHorizontalAlignment(SwingConstants.CENTER);
		lblManageFiles.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblManageFiles.setBounds(0, 180, 600, 30);
		getContentPane().add(lblManageFiles);

		JPanel maganeFiles = new JPanel();
		maganeFiles.setBounds(0, 211, 600, 140);
		getContentPane().add(maganeFiles);
		maganeFiles.setLayout(null);

		JLabel lblReplicationDegree = new JLabel("Replication Degree");
		lblReplicationDegree.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblReplicationDegree.setBounds(12, 9, 130, 25);
		maganeFiles.add(lblReplicationDegree);

		JSpinner replicationDegreeSpinner = new JSpinner();
		replicationDegreeSpinner.setFont(new Font("Tahoma", Font.PLAIN, 15));
		replicationDegreeSpinner.setBounds(154, 9, 40, 25);
		maganeFiles.add(replicationDegreeSpinner);

		JLabel lblProtocolVersion = new JLabel("Protocol Version");
		lblProtocolVersion.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblProtocolVersion.setBounds(206, 9, 110, 25);
		maganeFiles.add(lblProtocolVersion);

		JSpinner protocolVersionSpinner = new JSpinner();
		protocolVersionSpinner.setFont(new Font("Tahoma", Font.PLAIN, 15));
		protocolVersionSpinner.setBounds(328, 9, 40, 25);
		maganeFiles.add(protocolVersionSpinner);

		JButton btnChooseFile = new JButton("Choose File");
		btnChooseFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnChooseFile.setBounds(380, 9, 110, 25);
		maganeFiles.add(btnChooseFile);
		btnChooseFile.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JFileChooser fileChooser = new JFileChooser();
				int temp = fileChooser.showOpenDialog(null);
				if (temp == JFileChooser.APPROVE_OPTION) {
					String path = fileChooser.getSelectedFile()
							.getAbsolutePath();
					System.out.println(path);

				}
			}
		});

		JButton btnBackup = new JButton("Backup");
		btnBackup.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnBackup.setBounds(502, 9, 86, 25);
		maganeFiles.add(btnBackup);

		JButton btnRestore = new JButton("Restore");
		btnRestore.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnRestore.setBounds(502, 41, 86, 25);
		maganeFiles.add(btnRestore);

		JButton btnDelete = new JButton("Delete");
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDelete.setBounds(502, 74, 86, 25);
		maganeFiles.add(btnDelete);

		JButton btnClaimSpace = new JButton("Claim");
		btnClaimSpace.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnClaimSpace.setBounds(502, 107, 86, 25);
		maganeFiles.add(btnClaimSpace);

	}

	private void configurations() throws ParseException {
		JLabel configLabel = new JLabel("Configura\u00E7\u00F5es");
		configLabel.setHorizontalAlignment(SwingConstants.CENTER);
		configLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		configLabel.setBounds(0, 0, 600, 30);
		getContentPane().add(configLabel);

		JPanel IPdefinitions = new JPanel();
		IPdefinitions.setBorder(null);
		IPdefinitions.setBounds(0, 30, 600, 150);
		getContentPane().add(IPdefinitions);
		IPdefinitions.setLayout(null);

		JLabel lblMulticastControlChannel = new JLabel(
				"Multicast control channel");
		lblMulticastControlChannel.setBounds(12, 0, 205, 34);
		lblMulticastControlChannel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		IPdefinitions.add(lblMulticastControlChannel);

		JLabel lblMulticastDataChannel = new JLabel(
				"Multicast backup channel IP");
		lblMulticastDataChannel.setBounds(12, 34, 205, 34);
		lblMulticastDataChannel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		IPdefinitions.add(lblMulticastDataChannel);

		JLabel lblMulticastRestoreChannel = new JLabel(
				"Multicast restore channel IP");
		lblMulticastRestoreChannel.setBounds(12, 68, 205, 34);
		lblMulticastRestoreChannel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		IPdefinitions.add(lblMulticastRestoreChannel);

		MaskFormatter ip = new MaskFormatter("###.###.###.###");
		String expr = "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";
		Pattern pattern = Pattern.compile(expr);

		mcIP = new JTextField();
		mcIP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mcIP.setBounds(229, 9, 200, 22);
		IPdefinitions.add(mcIP);

		mdbIP = new JTextField();
		mdbIP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mdbIP.setBounds(229, 40, 200, 22);
		IPdefinitions.add(mdbIP);

		mdrIP = new JTextField();
		mdrIP.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mdrIP.setBounds(229, 71, 200, 22);
		IPdefinitions.add(mdrIP);

		JLabel lblPort = new JLabel("Port 1");
		lblPort.setBounds(467, 1, 51, 34);
		lblPort.setFont(new Font("Tahoma", Font.PLAIN, 16));
		IPdefinitions.add(lblPort);

		JLabel lblPort_1 = new JLabel("Port 2");
		lblPort_1.setBounds(467, 36, 51, 34);
		lblPort_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		IPdefinitions.add(lblPort_1);

		JLabel lblPort_2 = new JLabel("Port 3");
		lblPort_2.setBounds(467, 71, 51, 34);
		lblPort_2.setFont(new Font("Tahoma", Font.PLAIN, 16));
		IPdefinitions.add(lblPort_2);

		mcPort = new JSpinner();
		mcPort.setModel(new SpinnerNumberModel(1024, 1024, 65000, 1));
		mcPort.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mcPort.setBounds(518, 7, 70, 25);
		IPdefinitions.add(mcPort);

		mdbPort = new JSpinner();
		mdbPort.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mdbPort.setModel(new SpinnerNumberModel(1024, 1024, 65000, 1));
		mdbPort.setBounds(518, 39, 70, 25);
		IPdefinitions.add(mdbPort);

		mdrPort = new JSpinner();
		mdrPort.setModel(new SpinnerNumberModel(1024, 1024, 65000, 1));
		mdrPort.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mdrPort.setBounds(518, 71, 70, 25);
		IPdefinitions.add(mdrPort);

		JButton btnSave = new JButton("Save");
		btnSave.setBounds(250, 112, 100, 25);
		IPdefinitions.add(btnSave);
		btnSave.addActionListener(new ActionListener() {

			String args[] = new String[6];
			
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					args[0]= mcIP.getText();
					args[1]= Integer.toString((int) mcPort.getValue());
					args[2]= mdbIP.getText();
					args[3]= Integer.toString((int) mdbPort.getValue());
					args[4]= mdrIP.getText();
					args[5]= Integer.toString((int) mdrPort.getValue());
					
					config.storeConfigurations(args);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});

		// on submit dos ips
		// Matcher matcher = pattern.matcher(ip da caixa de texto);
		// return matcher.matches();
	}

	private void logsText() {
		JLabel lblLogs = new JLabel("Logs");
		lblLogs.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogs.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblLogs.setBounds(0, 351, 600, 30);
		getContentPane().add(lblLogs);

		JTextPane logsOut = new JTextPane();
		logsOut.setBounds(0, 381, 600, 132);
		getContentPane().add(logsOut);

	}
}