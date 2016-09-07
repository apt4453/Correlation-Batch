package wave1;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class main_gui extends JFrame {
	
	private static boolean debug = false;

	private JPanel contentPane;
	private JTextField txt_file_log;
	private JTextField txt_file_auto;
	private JTextField txt_file_no_auto;
	private JTextField txt_file_event_table;
	private JTextField txt_file_result;

	public static input_event_parser parser1 = new input_event_parser();
	private JTextField txtRklnetsorttxt;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				
				if(debug) {
					process1(false);
					return;
				}

				try {
					main_gui frame = new main_gui();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

	public static void process1(boolean is_no_auto) {
		int ret = 0;
		do {
			ret = parser1.load_table();
			if(ret < 0) {
				ret = parser1.make_table();
			}
			if(ret < 0)
				break;
			
			ret = parser1.load_log_auto();
			if(ret < 0)
				break;

			ret = parser1.make_collect(is_no_auto);
			if(ret < 0)
				break;

		} while(false);
	}
	
	public void btn_get_txt() {
		config.file_input_log_path = txt_file_log.getText();
		config.file_input_report_auto_path = txt_file_auto.getText();
		config.file_input_report_no_auto_path = txt_file_no_auto.getText();
		config.file_output_event_table_path = txt_file_event_table.getText();
		config.file_output_result_path = txt_file_result.getText();
	}
	
	public void btn_maketable_handler()
	{
		btn_get_txt();
		parser1.make_table();		
	}

	public void btn_process1_handler(boolean is_no_auto)
	{
		btn_get_txt();
		process1(is_no_auto);
	}

	
	/**
	 * Create the frame.
	 */
	public main_gui() {
		setTitle("Wave1 - Parser");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 444, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblinput = new JLabel("-Input:");
		lblinput.setBounds(12, 10, 71, 15);
		contentPane.add(lblinput);
		
		JLabel lblNewLabel = new JLabel(".Log:");
		lblNewLabel.setBounds(22, 30, 61, 15);
		contentPane.add(lblNewLabel);
		
		txt_file_log = new JTextField();
		txt_file_log.setText(config.file_input_log_path);
		txt_file_log.setBounds(87, 25, 335, 21);
		contentPane.add(txt_file_log);
		txt_file_log.setColumns(10);
		
		JLabel lblInputReport = new JLabel(".Auto:");
		lblInputReport.setBounds(22, 50, 61, 15);
		contentPane.add(lblInputReport);
		
		txt_file_auto = new JTextField();
		txt_file_auto.setText(config.file_input_report_auto_path);
		txt_file_auto.setColumns(10);
		txt_file_auto.setBounds(87, 50, 335, 21);
		contentPane.add(txt_file_auto);
		
		JLabel lblInputNoauto = new JLabel(".NoAuto:");
		lblInputNoauto.setBounds(22, 75, 61, 15);
		contentPane.add(lblInputNoauto);
		
		txt_file_no_auto = new JTextField();
		txt_file_no_auto.setText(config.file_input_report_no_auto_path);
		txt_file_no_auto.setColumns(10);
		txt_file_no_auto.setBounds(87, 75, 335, 21);
		contentPane.add(txt_file_no_auto);

		JLabel lbloutput = new JLabel("-Output:");
		lbloutput.setBounds(12, 100, 71, 15);
		contentPane.add(lbloutput);

		JLabel lbltable = new JLabel(".Table:");
		lbltable.setBounds(22, 124, 61, 15);
		contentPane.add(lbltable);
		
		txt_file_event_table = new JTextField();
		txt_file_event_table.setText(config.file_output_event_table_path);
		txt_file_event_table.setColumns(10);
		txt_file_event_table.setBounds(87, 119, 335, 21);
		contentPane.add(txt_file_event_table);
		
		JLabel lbl_result = new JLabel(".Result:");
		lbl_result.setBounds(22, 175, 61, 15);
		contentPane.add(lbl_result);
		
		txt_file_result = new JTextField();
		txt_file_result.setText(config.file_output_result_path);
		txt_file_result.setColumns(10);
		txt_file_result.setBounds(87, 170, 335, 21);
		contentPane.add(txt_file_result);
		
		JButton btn_make_table = new JButton("MakeTable");
		btn_make_table.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btn_maketable_handler();	
			}
		});
		btn_make_table.setBounds(12, 215, 97, 23);
		contentPane.add(btn_make_table);
		
		JButton btn_process1 = new JButton("Process1");
		btn_process1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btn_process1_handler(false);
			}
		});
		btn_process1.setBounds(243, 215, 97, 23);
		contentPane.add(btn_process1);
		
		JButton btn_sort_log = new JButton("Sort Log-file");
		btn_sort_log.setEnabled(false);
		btn_sort_log.setBounds(12, 248, 116, 23);
		contentPane.add(btn_sort_log);
		
		JLabel lblsort = new JLabel(".Sort Log:");
		lblsort.setEnabled(false);
		lblsort.setBounds(22, 149, 61, 15);
		contentPane.add(lblsort);
		
		txtRklnetsorttxt = new JTextField();
		txtRklnetsorttxt.setEnabled(false);
		txtRklnetsorttxt.setText("R:\\_KLNET_sorted_log.txt");
		txtRklnetsorttxt.setColumns(10);
		txtRklnetsorttxt.setBounds(87, 144, 335, 21);
		contentPane.add(txtRklnetsorttxt);
		
		JButton btn_no_auto = new JButton("Process1(NoAuto)");
		btn_no_auto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				btn_process1_handler(true);
			}
		});
		btn_no_auto.setBounds(243, 248, 137, 23);
		contentPane.add(btn_no_auto);
			
	}
}
