package wave1;

import java.util.ArrayList;

public class app {
	public static void Sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	
	public static String[] split_token(String line) {
		int i_err = 0;

		ArrayList<String> token = new ArrayList<String>();
		
		int index = 0;
		String last;
		String word;
		for(;;) {
			last = line.substring(index).trim();
			if(last.charAt(0) == '"') {
				index = last.indexOf('"', 1);
				if(index<0) {
					break;
				}
				word = last.substring(1, index - 1 );
				index = last.indexOf(',', index + 1);
				if(index<0) {
					break;
				}
				index++;

			} else {
				index = last.indexOf(',', 0);
				if(index<0) {
					word = last;
					break;
				} else {
					word = last.substring(0, index - 1 );
					index++;
				}
			}
			
			token.add(word);
			
			if(i_err < 0) {
				break;
			}
		}

		String[] ret = token.toArray(new String[token.size()]);	
		return ret;
	}
	
	
	
	public static String[] split_token_log_line(String line) {
		int i_err = 0;

		ArrayList<String> token = new ArrayList<String>();
		
		int index = 0;
		String word;
		String last = line;

		for(int i=0; i<config.input_log_line_word_count; i++) {
			
			last = last.substring(index).trim();
			if(i==config.input_log_line_event_name_index) {
				index = last.indexOf("  ", 0);
			} else {
				index = last.indexOf(' ', 0);
			}

			if(index<0) {
				word = last;
				token.add(word);
				break;
			} else {
				word = last.substring(0, index);
				token.add(word);
				index++;
			}
			
			if(i_err < 0) {
				break;
			}
		}

		String[] ret = token.toArray(new String[token.size()]);	
		return ret;
	}	


	public static String[] split_token_csv(String line) {
		int i_err = 0;

		ArrayList<String> token = new ArrayList<String>();
		
		int index = 0;
		String word;
		String last = line;
		
		for(;;) {
			last = last.substring(index).trim();
			if(last.charAt(0) == '"') {
				index = last.indexOf('"', 1);
				if(index<0) {
					break;
				}
				word = last.substring(1, index);
				token.add(word);
				
				index = last.indexOf(',', index + 1);
				if(index<0) {
					break;
				}
				index++;

			} else {
				index = last.indexOf(',', 0);
				if(index<0) {
					word = last;
					token.add(word);
					break;
				} else {
					word = last.substring(0, index);
					token.add(word);
					index++;
				}
			}
			
			if(i_err < 0) {
				break;
			}
		}

		String[] ret = token.toArray(new String[token.size()]);	
		return ret;
	}
	
	
}
