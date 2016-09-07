package wave1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class input_event_parser {
	
	public HashSet<String> event_table = new HashSet<String>();

	public Map<Integer,String> event_map = new HashMap<Integer,String>();
	public Map<String,Integer> event_map_r = new HashMap<String,Integer>();
	
	public ArrayList<st_auto> auto_list = new ArrayList<st_auto>();
	public ArrayList<st_auto> no_auto_list = new ArrayList<st_auto>();

	public ArrayList<st_event> event_list = new ArrayList<st_event>();
	public ArrayList<st_event> event_group = new ArrayList<st_event>();

	public SimpleDateFormat date_fmt_auto = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	public SimpleDateFormat date_fmt_event = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	public BufferedWriter result_file_writer = null;
		
	public class st_auto {
		String site_name = "";
		Date   attack_time = new Date(0);
		long   a_time = 0;
		Date   reg_time = new Date(0);
		String rule_name = "";
		String agent_ip = "";
		String src_ip = "";
		String src_port = "";
		int    i_s_port = 0;
		String dst_ip = "";
		String dst_port = "";
		int    i_d_port = 0;
	
		public int set(String[] in) {
			int ret = -1;
			do {
				if(in.length < config.input_log_auto_line_word_count) {
					break;
				}
				
				site_name = in[0];
				//attack_time = in[1];
				//a_time = in[1];
				//reg_time = in[2];
				
				try {
					attack_time = date_fmt_auto.parse(in[1]);
					a_time = attack_time.getTime();
					reg_time = date_fmt_auto.parse(in[2]);
				} catch (ParseException e) {
					e.printStackTrace();
					break;
				}
				
				rule_name = in[3];
				agent_ip = in[4];
				src_ip = in[5];
				//src_port = in[6];
				dst_ip = in[6];
				dst_port = in[7];
				
				ret = 0;				
			} while(false);
			return ret;
		}
		
		public String toString() {
			StringBuilder str = new StringBuilder();
			
			
			str.append("[" + site_name + "],");
			
			String t = date_fmt_auto.format(attack_time);
			str.append("[" + t + "],");

			t = date_fmt_auto.format(reg_time);
			str.append("[" + t + "],");

			str.append("[" + rule_name + "],");
			str.append("[" + agent_ip + "],");
			str.append("[" + src_ip + "],");
			str.append("[" + src_port + "],");
			str.append("[" + dst_ip + "],");
			str.append("[" + dst_port + "]");
			return str.toString();
		}
	};
	
	
	public class st_event {
		String index = "";
		Date   attack_time = new Date(0);
		long   a_time = 0;

		String agent_ip = "";

		String event_name = "";

		String src_ip = "";
		String src_port = "";
		int    i_s_port = 0;
		
		String dst_ip = "";
		String dst_port = "";
		int    i_d_port = 0;
		
		String protocol_type = "";
		String opt = "";
	
		public int set(String[] in) {
			int ret = -1;
			do {
				if(in.length < config.input_log_line_word_valid_count) {
					break;
				}
				
				index = in[0];

				try {
					attack_time = date_fmt_event.parse(in[1] + " " + in[2]);
					a_time = attack_time.getTime();
				} catch (ParseException e) {
					e.printStackTrace();
					break;
				}
				
				agent_ip = in[3];
				src_ip = in[4];
				src_port = in[5];
				
				event_name = in[6];
				
				dst_ip = in[7];
				dst_port = in[8];
				
				protocol_type = "";
				if(in.length > 9) {
					protocol_type = in[9];
				}
				
				opt = "";
				if(in.length > 10) {
					opt = in[10];
				}
				
				ret = 0;				
			} while(false);
			return ret;
		}
		
		
		public String toString() {
			StringBuilder str = new StringBuilder();
			
			str.append("[" + index + "],");
			
			String t = date_fmt_event.format(attack_time);
			str.append("[" + t + "],");

			str.append("[" + agent_ip + "],");
			str.append("[" + src_ip + "],");
			str.append("[" + src_port + "],");

			str.append("[" + event_name + "],");

			str.append("[" + dst_ip + "],");
			str.append("[" + dst_port + "]");

			str.append("[" + protocol_type + "],");
			str.append("[" + opt + "]");

			return str.toString();
		}
	};
		
	
	
	
	
	
	public int parser1_event_name(String line) {
		//System.out.println(line);

		String[] token = app.split_token_log_line(line);
		// Print Token
//		System.out.print("Token: c=" + token.length + " ");
//		for( String s : token ) {
//			System.out.print(s + ",");
//		}
//		System.out.println("");

		if(token.length > config.input_log_line_event_name_index) {
			String s = token[config.input_log_line_event_name_index];
			event_table.add(s);
			//System.out.println(s);
		}
		
		return 0;
	}
	

	public st_auto parser1_auto_line(String line) {

//		System.out.println(line);

		String[] token = app.split_token_csv(line);
		// Print Token
//		System.out.print("Token: c=" + token.length + " ");
//		for( String s : token ) {
//			System.out.print("[" + s + "]" + ",");
//		}
//		System.out.println("");
		
		int ret;
		st_auto st = new st_auto();
		ret = st.set(token);
		//System.out.println(st.toString());
		if(ret < 0) {
			st = null;
		}
		return st;
	}
	
	
	public int load_table() {
		
		int ret = -1;

		event_table.clear();
		event_map.clear();	
		event_map_r.clear();	
		
		
		int line_count = 0;
		String s;		
		
		BufferedReader in_file = null;
		try {
			////////////////////////////////////////////////////////////////
			in_file = new BufferedReader(new FileReader(config.file_output_event_table_path));
			while ((s = in_file.readLine()) != null) {
				String[] token = s.split(",");
				int index = -1;
				try { 
					index = Integer.parseInt(token[0]);
				} catch(Exception e) {
					break;
				}
				if(token.length <2 || index < 0) {
					break;
				}	        		
				event_map.put(index, token[1]);
				event_map_r.put(token[1],index);
				line_count++;
			}
			
			ret = line_count;

		} catch (IOException e) {
			//e.printStackTrace();
		} finally {
			if(in_file != null) {
				try {
					in_file.close();
				} catch (IOException e) {
					//e.printStackTrace();
				}		
			}
			in_file = null;
		}	
		
		for(int i=1; i<=event_map.size(); i++) {
			String val = event_map.get(i);
			String out_line = i + "," + val;
			System.out.println(out_line);
		}
		
		return ret;
	}
	
	public int result_file_writer_open(String file_path)
	{
		int ret = -1;
		try {
			result_file_writer = new BufferedWriter( new FileWriter(file_path));
			ret = 0;
		} catch ( IOException e) {
			System.err.println(e);
		}		
		return ret;
	}
	
	public int result_file_writer_close()
	{
		int ret = -1;
		try {
			if( result_file_writer != null) {
				result_file_writer.close();
				result_file_writer = null;
			}
			ret = 0;
		} catch ( IOException e) {
			System.err.println(e);
		}		
		return ret;
	}	

	public int result_file_writeln(String str)
	{
		int ret = -1;
		try {
			if( result_file_writer != null) {
				result_file_writer.write(str + "\r\n");
			}
			ret = str.length() + 2;
		} catch ( IOException e) {
			System.err.println(e);
		}		
		return ret;
	}	
	
	
	public int make_table() {

		int result = -1;

		System.out.println("-----------------------------------------------");
		System.out.println("Start ");
		System.out.println("-----------------------------------------------");

		event_table.clear();
		event_map.clear();	
		event_map_r.clear();	
		
		
		int event_count = 0;
		int line_count = 0;
		String s;
		
		BufferedReader in_file = null;
		try {
			
			////////////////////////////////////////////////////////////////
			in_file = new BufferedReader(new FileReader(config.file_input_log_path));
			while ((s = in_file.readLine()) != null) {
				int ret = parser1_event_name(s);
				if(ret < 0) {
					break;
				}	        		

				if( (line_count & 0x2FFFF) == 0 ) {
					System.out.print(line_count + ". ");
				}

				line_count++;
				//	        	if(line_count >= 10000000) {
				//	        		break;
				//	        	}
			}

			System.out.println("");
			System.out.println("-----------------------------------------------");

			BufferedWriter writer = null;
			try {
				writer = new BufferedWriter( new FileWriter(config.file_output_event_table_path));
				event_count = 1;
				for(String es : event_table) {
					String out_line = event_count + "," + es;
					event_map.put( event_count, es);
					event_map_r.put(es, event_count);

					writer.write(out_line);
					writer.newLine();
					
					System.out.println(out_line);
					event_count++;
				}
			} catch ( IOException e) {
				System.err.println(e);
			} finally {
				try {
					if ( writer != null) {
						writer.close( );
					}
				} catch ( IOException e) {
					System.err.println(e);
				}
			}
			
			System.out.println("");
			System.out.println("-----------------------------------------------");
			System.out.println("Line counter=" + line_count);	
			System.out.println("Event counter=" + event_table.size());	
			System.out.println("");
			
			result = event_count;
			
			////////////////////////////////////////////////////////////////
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if(in_file != null) {
				try {
					in_file.close();
				} catch (IOException e) {
					System.err.println(e);
				}		
			}
			in_file = null;			
		}

		System.out.println("-----------------------------------------------");
		System.out.println("End ");
		System.out.println("-----------------------------------------------");
		
		event_table.clear();

		return result;
	}
	
	
	public int load_log_auto() {

		int ret;

		auto_list.clear();
		no_auto_list.clear();
		
		String s;

		System.out.println("-----------------------------------------------");
		BufferedReader in_file = null;
		try {
			in_file = new BufferedReader(new FileReader(config.file_input_report_auto_path));
			while ((s = in_file.readLine()) != null) {
				st_auto st = parser1_auto_line(s);
				if(st == null) {
					break;
				}	        		
				auto_list.add(st);
			}
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if(in_file != null) {
				try {
					in_file.close();
				} catch (IOException e) {
					System.err.println(e);
				}		
			}
			in_file = null;			
		}
		System.out.println("-----------------------------------------------");
		
		in_file = null;
		try {
			in_file = new BufferedReader(new FileReader(config.file_input_report_no_auto_path));
			while ((s = in_file.readLine()) != null) {
				st_auto st = parser1_auto_line(s);
				if(st == null) {
					break;
				}	        		
				no_auto_list.add(st);
			}
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if(in_file != null) {
				try {
					in_file.close();
				} catch (IOException e) {
					System.err.println(e);
				}		
			}
			in_file = null;			
		}
		System.out.println("-----------------------------------------------");
		
		
		Collections.sort(auto_list, new Comparator<st_auto>(){
			public int compare(st_auto obj1, st_auto obj2) {
				return (obj1.a_time < obj2.a_time) ? -1: (obj1.a_time > obj2.a_time) ? 1:0 ;
			}
		}); 		

		Collections.sort(no_auto_list, new Comparator<st_auto>(){
			public int compare(st_auto obj1, st_auto obj2) {
				return (obj1.a_time < obj2.a_time) ? -1: (obj1.a_time > obj2.a_time) ? 1:0 ;
			}
		}); 		
		
		
		for(st_auto t : auto_list) {
			System.out.println(t);
			break;
		}
	
		System.out.println("-----------------------------------------------");
		for(st_auto t : no_auto_list) {
			System.out.println(t);
			break;
		}

		System.out.println("Auto   =" + auto_list.size());
		System.out.println("NoAuto =" + no_auto_list.size());
		System.out.println("-----------------------------------------------");
		
		return 0;
	}	
	

	public void remove_past_event(long at_time) {
		long c_time = at_time - config.group_check_time_ms_half;
		int size = event_list.size();
		int index = 0;
		for(int i=0; i<size; i++) {
			st_event e = event_list.get(index);
			if( e.a_time > c_time ) {
				index++;
				continue;
			}
			event_list.remove(index);
		}
	}
	
	
	public int get_event_index(String str) {
		int ret = -1;
		try {
			ret = event_map_r.get(str);
		} catch(Exception e) {
			ret = -1;
		}
		return ret;
	}
	
	
	private boolean debug_stop = false;

	@SuppressWarnings("deprecation")
	public int make_subset(st_auto val, StringBuilder occ_event) {
		
		Map<Integer,Integer> data = new HashMap<Integer,Integer>();
		
//		System.out.println(date_fmt_auto.format(val.attack_time));
//		if( date_fmt_auto.format(val.attack_time).equals("2016-04-20 20:07")) {
//			debug_stop = true;
//		}

		for(st_event t : event_list) {
			
			if(t.a_time > (val.a_time+config.group_check_time_ms_half) ) {
				continue;
			}
			
			int index = get_event_index(t.event_name);
			if(index <= 0) { 
				System.out.println("invalid attack name : " + t.event_name);
				continue;
			}
			
			try {
				int count = data.get(index);
				count++;
				data.replace(index, count);
			} catch(Exception e) {
				data.put(index, 1);
			}

			if(debug_stop) {
				System.out.println("event=" + index + "  " + t);
			}

		}		
		
		Set<Integer> keys = data.keySet();

		int e_count = 0;
		for(int k : keys) {
			int v = data.get(k);
			//System.out.println( "" + k + "(" + event_map.get(k) + ")" + "=" + v);
			if(e_count > 0) {
				occ_event.append(":");
			}
			occ_event.append("e" + k + "=" + v);
			e_count += v ;
		}
		
		if(debug_stop) {
			System.out.println( occ_event);
			System.exit(0);
		}
		
		
		
		
		return e_count;
	}

	
	public int make_collect(boolean is_no_auto) {

		int ret;
		
		String out_file = config.file_output_result_path;
		
		ArrayList<st_auto> rule_event_list = null;
		
		if(is_no_auto) {
			rule_event_list = no_auto_list;
		} else {
			rule_event_list = auto_list;
		}
		
		if(is_no_auto) {
			int index = out_file.lastIndexOf('.');
			if(index < 0) {
				out_file = out_file + "_noauto";
			} else {
				out_file = out_file.substring(0, index) + "_noauto" + out_file.substring(index);
			}
		}

		System.out.println("-----------------------------------------------");
		System.out.println("Start ");
		System.out.println("-----------------------------------------------");

		int event_count = 0;
		int line_count = 0;
		String s;
		
		event_list.clear();
		
		st_auto val;
		st_event et = null;

		BufferedReader in_file = null;
		try {
			
			ret = result_file_writer_open(out_file);
			if(ret < 0) {
				throw new IOException();
			}

			in_file = new BufferedReader(new FileReader(config.file_input_log_path));
			
			for(int i=0; i< rule_event_list.size(); i++) {
				
				val = rule_event_list.get(i);
				long ts = val.a_time;
			
				// load time
				if( et != null ) {
					event_list.add(et);
					et = null;
				}
			
				// load event
				while ((s = in_file.readLine()) != null) {

					String[] token = app.split_token_log_line(s);
					if(token.length < config.input_log_line_word_valid_count) {
						break;
					}

					et = new st_event();
					ret = et.set(token);
					if(ret < 0) {
						break;
					}

					//System.out.println("ts=" + ts + "/ " + et.a_time + "(" + (et.a_time - ts) + ")");
					//System.out.println(et);

//					if( (line_count & 0x2FFFF) == 0 ) {
//						System.out.print(line_count + ". ");
//					}
					
//					if(et.a_time > (ts+config.group_check_time_ms_half) ) {
//						break;
//					}
					
					if(et.a_time > (ts+config.group_check_time_ms_6hour) ) {
						break;
					} else if(et.a_time < (ts - config.group_check_time_ms_half) ) {
						// delete
					} else {
						event_list.add(et);
					}
					et = null;
					
					line_count++;
//					if(line_count >= 1000) {
//						break;
//					}
				}
				
				remove_past_event(ts);
				
				StringBuilder occ_line = new StringBuilder();
				StringBuilder occ_event = new StringBuilder();
		
				int occ_count = make_subset(val, occ_event);
				if(occ_count > 0) {
					event_count++;
					occ_line.append("" + event_count + ",");
					occ_line.append("" + date_fmt_auto.format(val.attack_time) + ",");
					occ_line.append("" + config.group_check_time_min + ",");
					occ_line.append("" + val.rule_name + ",");
					occ_line.append("" + occ_event + ",");
					occ_line.append("" + occ_count);

					System.out.println(occ_line);
					result_file_writeln(occ_line.toString());
				}
				//break;
			}

		} catch (IOException e) {
			System.err.println(e);
			
		} finally {
			
			result_file_writer_close();
			
			if(in_file != null) {
				try {
					in_file.close();
				} catch (IOException e) {
					System.err.println(e);
				}		
			}
			in_file = null;			
		}


		System.out.println("-----------------------------------------------");
		System.out.println("End ");
		System.out.println("-----------------------------------------------");
		
		return 0;
	}	
	
	
	

}
