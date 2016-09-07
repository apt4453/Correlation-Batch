package wave1;

public class config {
	
	public static String file_input_log_path = "R:\\KLNET_218.146.22.197.txt";
	public static String file_input_report_auto_path = "R:\\KLNET_AUTO.csv";
	public static String file_input_report_no_auto_path = "R:\\KLNET_no_AUTO.csv";
	public static String file_output_event_table_path = "R:\\_KLNET_event_table.csv";
	public static String file_output_result_path = "R:\\_KLNET_result.csv";
	
	
	public static int input_log_line_word_count = 11;
	public static int input_log_line_word_valid_count = 9;
	public static int input_log_line_event_name_index = 6;

	public static int input_log_auto_line_word_count = 8;
	
	public static int group_check_time_min = 5;
	public static int group_check_time_ms = group_check_time_min*60*1000;
	public static int group_check_time_ms_half = group_check_time_ms/2;

	public static int group_check_time_ms_6hour = 6*60*60*1000;
	
}
