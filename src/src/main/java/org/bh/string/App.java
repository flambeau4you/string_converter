package org.bh.string;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.yaml.snakeyaml.Yaml;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class App {
	private static Options options = new Options();

	public static void main(String[] args) {
		// define arguments
		options.addOption("h", "help", false, "Show this help message.");
		options.addOption("cp", "copy", false, "Copy the results to the clipboard.");
		options.addOption("dt8", "date_8", false, "Current date. (YYYYMMDD)");
		options.addOption("dt10", "date_10", false, "Current date. (YYYY-MM-DD)");
		options.addOption("c", "camel", false, "Convert all to camel style. AB_Cd_ef -> abCdEf");
		options.addOption("cd", "camel_declaring", false, "Convert all to camel style with declaring. AB_Cd_ef -> private String abCdEf;");
		options.addOption("cu", "camel_underscore", false, "Convert underscore to camel style only. AB_Cd_ef -> AB_CdEf");
		options.addOption("cj", "camel_java", false, "Convert java variables to camel style. AB_Cd_ef -> ABCdEf");
		options.addOption("cjm", "camel_java_method", false, "Convert java method underscore to camel style. abc.ab_cd_ef_gh() -> abc.abCdEfGh()");
		options.addOption("cm", "camel_mybatis", false, "Convert queries of MyBatis to camel style.");
		options.addOption("cmu", "camel_mybatis_only", false, "Convert queries of MyBatis to camel style only.");
		options.addOption("sc", "camel_space", false, "Convert camel style to space speration style.");
		options.addOption("us", "space_underscore", false, "Convert space camel style to underscore style.");
		options.addOption("u", "underscore", false, "Convert all to underscore style.");
		options.addOption("um", "underscore_mybatis", false, "Convert all to underscore style for mybatis.");
		options.addOption("ru", "remove_underscore", false, "Remove all underscore.");
		options.addOption("f", "first", false, "Convert fist character to upper case.");
		options.addOption("up", "upper", false, "Convert all character to upper case.");
		options.addOption("qs", "query_select", false, "Convert to seleting query.");
		options.addOption("qu", "query_update", false, "Convert to updating query with if statement.");
		options.addOption("qi", "query_insert", false, "Convert to inserting query.");
		options.addOption("qw", "query_where", false, "Convert to where query with if statement.");
		options.addOption("qr", "query_result", false, "Convert to query result object.");
		options.addOption("fjc", "find_java_camel", false, "Find camel variables in the directory path.");
		options.addOption("nc", "new_code", false, "Convert old style code constant to new code constant style.");
		options.addOption("rm", "redmine", false, "Convert Markdown syntax to Redmine syntax.");
		options.addOption("md", "markdown", false, "Convert Redmine syntax to Markdown syntax.");
		options.addOption("rr", "redmine_respect", false, "Convert Markdown syntax to Redmine syntax and make respect.");
		options.addOption("mr", "markdown_rough", false, "Convert Redmine syntax to Markdown syntax and make rougth.");
		options.addOption("tx", "text", false, "Convert Markdown syntax to plain text.");
		options.addOption("rs", "respect", false, "Convert rougth words to respect in Korean.");
		options.addOption("rg", "rough", false, "Convert respect words to rough in Korean.");
		options.addOption("sj", "strip_json", false, "Strip escaped strings in JSON.");		
		options.addOption("pj", "pretty_json", false, "Make JSON pretty.");		
		options.addOption("spj", "strip_pretty_json", false, "Make JSON striped and pretty.");		
		options.addOption("yj", "yaml_json", false, "Convert YAML to JSON.");		
		options.addOption("cs", "convert_special", false, "Convert special characters.");
		options.addOption("gm", "git_message", false, "Convert '-' to ': ' and '_' to ' '.");

		// parsing arguments
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
			String inputString = null;
			String[] lastArgs = cmd.getArgs();

			// check code value
			if (lastArgs.length > 0 && lastArgs[0] != null && lastArgs[0].length() > 0) {
				inputString = lastArgs[0];
			}

			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

			// the inputed string is not defined. the clipboard string is used.
			if (inputString == null || inputString.isEmpty()) {
				Transferable t = clipboard.getContents(null);
				if (t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
					inputString = (String) t.getTransferData(DataFlavor.stringFlavor);
				}
			}

			if (cmd.hasOption("h") || cmd.getOptions().length == 0 || inputString == null || inputString.isEmpty()) {
				// show help message.
				exitWithHelp();
			} else {
				String str = inputString;
				
				if (cmd.hasOption("dt8")) {	
					str = (new SimpleDateFormat("yyyyMMdd", Locale.getDefault())).format(new Date());
				}
				
				if (cmd.hasOption("dt10")) {
					str = (new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())).format(new Date());
				}
				
				if (cmd.hasOption("c")) {
					str = convertUnderscoreToCarmel(str);
				}
				
				if (cmd.hasOption("cd")) {
					str = convertUnderscoreToCarmelWithDeclaring(str);
				}
				
				if (cmd.hasOption("cu")) {
					str = convertUnderscoreToCarmelOnly(str);
				}

				if (cmd.hasOption("cj")) {
					str = convertJavaUnderscoreToCarmel(str);
				}
				
				if (cmd.hasOption("cjm")) {
					str = convertJavaMethodUnderscoreToCarmel(str);
				}

				if (cmd.hasOption("cm")) {
					str = convertMyBatisUnderscoreToCarmel(str);
				}
				
				if (cmd.hasOption("cmu")) {
					str = convertMyBatisUnderscoreToCarmelOnly(str);
				}
				
				if (cmd.hasOption("sc")) {
					str = convertCarmelToSpace(str);
				}
				
				if (cmd.hasOption("us")) {
					str = convertSpaceToUnderscore(str);
				}
				
				if (cmd.hasOption("u")) {
					str = convertCarmelToUnderscore(str);
				}
				
				if (cmd.hasOption("um")) {
					str = convertCarmelToUnderscoreMyBatis(str);
				}

				if (cmd.hasOption("ru")) {
					str = removeUnderscore(str);
				}

				if (cmd.hasOption("f")) {
					str = convertUpperCastFirst(str);
				}

				if (cmd.hasOption("up")) {
					str = convertUpperCast(str);
				}

				if (cmd.hasOption("qs")) {
					str = convertSelectingQuery(str);
				}

				if (cmd.hasOption("qu")) {
					str = convertUpdatingQueryWithIfStatement(str);
				}

				if (cmd.hasOption("qi")) {
					str = convertInsertingQuery(str);
				}
				
				if (cmd.hasOption("qw")) {
					str = convertWhereQueryWithIfStatement(str);
				}

				if (cmd.hasOption("qr")) {
					str = convertQueryResultObject(str);
				}

				if (cmd.hasOption("s")) {
					str = convertStaticString(str);
				}

				if (cmd.hasOption("fjc")) {
					str = findJavaCamelVariables(str);
				}

				if (cmd.hasOption("nc")) {
					str = convertNewCodeConstantStyle(str);
				}

				if (cmd.hasOption("rm")) {
					str = convertMarkdownToRedmine(str);
				}
				if (cmd.hasOption("md")) {
					str = convertRedmineToMarkdown(str);
				}

				if (cmd.hasOption("rr")) {
					str = convertMarkdownToRedmine(str);
					str = convertToRespect(str);
				}
				if (cmd.hasOption("mr")) {
					str = convertRedmineToMarkdown(str);
					str = convertToRough(str);
				}

				if (cmd.hasOption("tx")) {
					str = convertMarkdownToText(str);
				}

				if (cmd.hasOption("rs")) {
					str = convertToRespect(str);
				}

				if (cmd.hasOption("rg")) {
					str = convertToRough(str);
				}

				if (cmd.hasOption("sj")) {
					str = stripEscapedJson(str);
				}
				if (cmd.hasOption("pj")) {
					str = makeJsonPretty(str);
				}
				if (cmd.hasOption("spj")) {
					str = stripEscapedJson(str);
					str = makeJsonPretty(str);
				}

				if (cmd.hasOption("yj")) {
					str = yamlToJson(str);
				}

				if (cmd.hasOption("cs")) {
					str = convertSpecialCharacters(str);
				}

				if (cmd.hasOption("gm")) {
					str = convertBranchNameToGitMessage(str);
				}

				if (str != null && !str.isEmpty()) {
					if (cmd.hasOption("cp")) {
						StringSelection selection = new StringSelection(str);
						clipboard.setContents(selection, selection);
					}
					System.out.println(str);
				} else
					System.out.println("There are no changings.");

			}
		} catch (ParseException e) {
			exitWithHelp(e.getMessage());
		} catch (Exception e) {
			System.out.println("An error is occured!");
			e.printStackTrace();
		}
	}

	/**
	 * convert underline to camel style string
	 * 
	 * @param inputString
	 * @return
	 */
	private static String convertUnderscoreToCarmel(String inputString) {
		inputString = inputString.toLowerCase();

		StringBuffer convertedString = new StringBuffer();
		boolean needUpperCase = false;
		for (char currentChar : inputString.toCharArray()) {
			if (currentChar == '_') {
				needUpperCase = true;
			} else {
				if (needUpperCase) {
					currentChar = Character.toUpperCase(currentChar);
					needUpperCase = false;
				}
				convertedString.append(currentChar);
			}
		}

		return convertedString.toString();
	}
	
	private static String convertUnderscoreToCarmelWithDeclaring(String inputString) {
		String outputString = convertUnderscoreToCarmel(inputString);
		StringBuffer convertedString = new StringBuffer();
		String[] lines = outputString.split("\n");
		for (String line : lines) {
			line = line.trim();
			convertedString.append("private String " + line + ";\r\n");
			if (line.endsWith("_CD")) {
				String codeColumn = line.substring(0, line.length() - 3);
				convertedString.append("private String " + codeColumn + "Value;\r\n");
			} else if (line.endsWith("State") || line.endsWith("Status")) {
				convertedString.append("private String " + line + "Value;\r\n");
			}
		}
		return convertedString.toString();
	}
	
	private static String convertUnderscoreToCarmelOnly(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		boolean needUpperCase = false;
		char lastChar = 'A';
		for (char currentChar : inputString.toCharArray()) {
			if (currentChar == '_') {
				if (Character.isLowerCase(lastChar)) {
					needUpperCase = true;
				} else {
					convertedString.append(currentChar);
				}
			} else {
				if (needUpperCase) {
					currentChar = Character.toUpperCase(currentChar);
					needUpperCase = false;
				}
				convertedString.append(currentChar);
			}
			lastChar = currentChar;
		}

		return convertedString.toString();
	}

	private static String convertJavaUnderscoreToCarmel(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		boolean needUpperCase = false;
		int spaceCount = 0;
		int lastChar = ' ';
		for (char currentChar : inputString.toCharArray()) {
			if (lastChar != ' ' && lastChar != '\t' && currentChar == ' ') {
				++spaceCount;
			} else if (currentChar == ';') {
				spaceCount = 0;
			}
			
			if (currentChar == '_') {
				needUpperCase = true;
			} else {
				if (needUpperCase) {
					currentChar = Character.toUpperCase(currentChar);
					needUpperCase = false;
				} else if (spaceCount > 2 && Character.isUpperCase(currentChar)) {
					currentChar = Character.toLowerCase(currentChar);
				}
				convertedString.append(currentChar);
			}
			lastChar = currentChar;
		}

		return convertedString.toString();
	}

	private static String convertJavaMethodUnderscoreToCarmel(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		boolean needUpperCase = false;
		boolean starting = false;
		char lastChar = 'a';
		for (char currentChar : inputString.toCharArray()) {
			if (currentChar == '.') {
				starting = true;
			} else if (currentChar == '(' || currentChar == '"' || currentChar == '/' || currentChar == '\'' || currentChar == ';') {
				starting = false;
			}

			if (starting) {
				if (Character.isLowerCase(lastChar) && currentChar == '_') {
					needUpperCase = true;
				} else {
					if (needUpperCase) {
						currentChar = Character.toUpperCase(currentChar);
						needUpperCase = false;
					} 
					convertedString.append(currentChar);
				}
			} else {
				convertedString.append(currentChar);
			}
			lastChar = currentChar;
		}

		return convertedString.toString();
	}

	private static String convertMyBatisUnderscoreToCarmel(String inputString) {
		boolean starting = false;

		StringBuffer convertedString = new StringBuffer();
		boolean needUpperCase = false;
		StringBuffer last = new StringBuffer();
		for (char currentChar : inputString.toCharArray()) {
			if (currentChar == '"' || currentChar == '{' || currentChar == '}') {
				starting = !starting;
				if (!starting) {
					last = new StringBuffer();
				}
			}

			if (starting) {
				if (currentChar == '_') {
					needUpperCase = true;
				} else {
					if (needUpperCase) {
						currentChar = Character.toUpperCase(currentChar);
						needUpperCase = false;
					} else if (Character.isUpperCase(currentChar)) {
						currentChar = Character.toLowerCase(currentChar);
					}
					convertedString.append(currentChar);
				}
			} else {
				convertedString.append(currentChar);
				last.append(currentChar);
			}
		}

		return convertedString.toString();
	}
	
	private static String convertMyBatisUnderscoreToCarmelOnly(String inputString) {
		boolean starting = false;

		StringBuffer convertedString = new StringBuffer();
		boolean needUpperCase = false;
		char lastChar = 'A';
		StringBuffer last = new StringBuffer();
		for (char currentChar : inputString.toCharArray()) {
			if (currentChar == '"' || currentChar == '{' || currentChar == '}') {
				starting = !starting;
				if (!starting)
					last = new StringBuffer();
			}

			if (starting) {
				if (Character.isLowerCase(lastChar) && currentChar == '_' && !last.toString().endsWith(" column=")) {
					needUpperCase = true;
				} else {
					if (needUpperCase) {
						currentChar = Character.toUpperCase(currentChar);
						needUpperCase = false;
					} 
					convertedString.append(currentChar);
				}
			} else {
				convertedString.append(currentChar);
				last.append(currentChar);
			}
			
			lastChar = currentChar;
		}

		return convertedString.toString();
	}
	
	private static String convertCarmelToSpace(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		int count = 0;
		for (char currentChar : inputString.toCharArray()) {
			if (Character.isUpperCase(currentChar)) {
				if (count > 0) {
					convertedString.append(' ');
				}
				convertedString.append(Character.toLowerCase(currentChar));
			} else {
				convertedString.append(currentChar);
			}
			++count;
		}

		return convertedString.toString();
	}
	
	private static String convertSpaceToUnderscore(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		for (char currentChar : inputString.toCharArray()) {
			if (Character.isUpperCase(currentChar)) {
				convertedString.append(Character.toLowerCase(currentChar));
			} else if (currentChar == ' ') {
				convertedString.append('_');
			} else {
				convertedString.append(currentChar);
			}
		}

		return convertedString.toString();
	}

	private static String convertCarmelToUnderscore(String inputString) {
		//inputString = inputString.toLowerCase();

		StringBuffer convertedString = new StringBuffer();
		int count = 0;
		for (char currentChar : inputString.toCharArray()) {
			if (Character.isUpperCase(currentChar)) {
				if (count > 0) {
					convertedString.append('_');
				}
				convertedString.append(Character.toLowerCase(currentChar));
			} else {
				convertedString.append(currentChar);
			}
			++count;
		}

		return convertedString.toString();
	}
	
	private static String convertCarmelToUnderscoreMyBatis(String inputString) {
		//inputString = inputString.toLowerCase();

		StringBuffer convertedString = new StringBuffer();
		int count = 0;
		StringBuilder last = new StringBuilder();
		for (char currentChar : inputString.toCharArray()) {
			if (currentChar == '"') {
				++count;
			}
			if (currentChar == '>') {
				count = 0;
				last = new StringBuilder();
			}
					
			if (count == 3 && last.toString().endsWith(" column=") && Character.isUpperCase(currentChar)) {
				convertedString.append('_');
				convertedString.append(Character.toLowerCase(currentChar));
			} else {
				convertedString.append(currentChar);
			}
			

			if (count < 3) {
				last.append(currentChar);
			}
			
		}

		return convertedString.toString();
	}

	private static String removeUnderscore(String inputString) {
		inputString = inputString.toLowerCase();

		StringBuffer convertedString = new StringBuffer();
		boolean needUpperCase = false;
		int cnt = 0;
		for (char currentChar : inputString.toCharArray()) {
			if (cnt == 0) {
				convertedString.append(Character.toUpperCase(currentChar));
			} else {
				if (currentChar == '_') {
					needUpperCase = true;
					convertedString.append(' ');
				} else {
					if (needUpperCase) {
						currentChar = Character.toUpperCase(currentChar);
						needUpperCase = false;
					}
					convertedString.append(currentChar);
				}
			}
			++cnt;
		}

		return convertedString.toString();
	}

	/**
	 * Convert first character to upper case.
	 * 
	 * @param inputString
	 * @return
	 */
	private static String convertUpperCastFirst(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		boolean needUpperCase = true;
		for (char currentChar : inputString.toCharArray()) {
			if (currentChar == '\n' || currentChar == '\r') {
				needUpperCase = true;
			} else if (needUpperCase) {
				currentChar = Character.toUpperCase(currentChar);
				needUpperCase = false;
			}
			convertedString.append(currentChar);
		}

		return convertedString.toString();
	}

	/**
	 * Convert all characters to upper case.
	 * 
	 * @param inputString
	 * @return
	 */
	private static String convertUpperCast(String inputString) {
		return inputString.toUpperCase();
	}

	private static String convertSelectingQuery(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		String[] lines = inputString.split("\n");
		int count = 0;
		for (String line : lines) {
			String name = line.trim();
			if (name.endsWith("CREATED_TIME") || name.endsWith("UPDATED_TIME")) {
				convertedString.append("date_format(" + name + ", '%Y-%m-%d %H:%i:%s') as " + name);
			} else {
				convertedString.append(name);
				if (name.endsWith("_CD"))
				{
					String codeColumn = name.substring(0, name.length() - 3);
					convertedString.append(",\r\ngetCodeValue('', '" + codeColumn + "', " + name + ") " + codeColumn + "_VALUE");
				} else if (name.endsWith("STATE") || name.endsWith("STATUS"))
				{
					convertedString.append(", \r\ngetCodeValue('', '" + name + "', " + name + ") " + name + "_VALUE");
				}
			}
			++count;
			if (count < lines.length) {
				convertedString.append(",");
				convertedString.append("\r\n");
			}
		}

		return convertedString.toString();
	}

	/**
	 * Convert to updating query with if statement.
	 * 
	 * @param inputString
	 * @return
	 */
	private static String convertUpdatingQueryWithIfStatement(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		String[] lines = inputString.split("\n");
		int count = 0;
		for (String line : lines) {
			++count;
			String name = line.trim();
			if (name.endsWith("CREATED_TIME") || name.endsWith("UPDATED_TIME")) {
				convertedString.append(name + " = CURRENT_TIMESTAMP,");
			} else {
                convertedString.append("<if test=\"" + convertUnderscoreToCarmel(name) + " != null\">\r\n    " 
                		+ name 
                        + " = #{" + convertUnderscoreToCarmel(name) + "},\r\n</if>");
			}
			if (count < lines.length) {
				convertedString.append("\r\n");
			}
		}

		return convertedString.toString();
	}
	
	private static String convertInsertingQuery(String inputString) {
		StringBuffer convertedColumnString = new StringBuffer();
		StringBuffer convertedValueString = new StringBuffer();
		String[] lines = inputString.split("\n");
		int count = 0;
		for (String line : lines) {
			String name = line.trim();
			convertedColumnString.append(name);
			if (name.endsWith("CREATED_TIME") || name.endsWith("UPDATED_TIME")) {
				convertedValueString.append("CURRENT_TIMESTAMP");
			} else {
				convertedValueString.append("#{" + convertUnderscoreToCarmel(name) + "}");
			}
			
			++count;
			if (count < lines.length) {
				convertedColumnString.append(",");
				convertedValueString.append(",");
                convertedColumnString.append("\r\n");
                convertedValueString.append("\r\n");
			}
		}

		return convertedColumnString.toString() + "\r\n) values (\r\n" + convertedValueString.toString() + "\r\n)";
	}
	
	private static String convertWhereQueryWithIfStatement(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		String[] lines = inputString.split("\n");
		int count = 0;
		for (String line : lines) {
			String name = line.trim();
			String camelName = convertUnderscoreToCarmel(name);
			convertedString.append("<if test=\"" + camelName + " != null and " + camelName + ".length() > 0\">\r\n" 
					+ "    and " + name + " = #{" + camelName + "}\r\n</if>");
			++count;
			if (count < lines.length) {
				convertedString.append("\r\n");
			}
		}

		return convertedString.toString();
	}
	
	private static String convertQueryResultObject(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		String[] lines = inputString.split("\n");
		int count = 0;
		for (String line : lines) {
			String name = line.trim();
			String camelName = convertUnderscoreToCarmel(name);
			if (name.endsWith("_ID")) {
				convertedString.append("<id property=\"" + camelName + "\" column=\"" + name + "\" />");
			} else {
				convertedString.append("<result property=\"" + camelName + "\" column=\"" + name + "\" />");
			}
			++count;
			if (count < lines.length) {
				convertedString.append("\r\n");
			}
		}

		return convertedString.toString();
	}
	
	private static String findJavaCamelVariables(String dirPath) {
		// inputString = inputString.toLowerCase();
		
		List<File> fileList = new ArrayList<>();
		listFiles(dirPath, fileList); 

		StringBuffer found = new StringBuffer();
	    for (File file : fileList) {
	    	if (file.getName().endsWith(".java")) {
			    String inputString;
				try {
					inputString = readFile(file);
				} catch (IOException e) {
					return e.toString();
				}
				
				int lineCount = 1;
				int spaceCount = 0;
				int lastChar = ' ';
				String lastString = "";
				for (char currentChar : inputString.toCharArray()) {
					if (currentChar == '\n') {
						++lineCount;
					}
					if (lastChar != ' ' && lastChar != '\t' && currentChar == ' ') {
						++spaceCount;
					} else if (currentChar == ';' || currentChar == '{' || currentChar == '=' || currentChar == '\t' || currentChar == '\n') {
						spaceCount = 0;
						lastString = "";
					} else if (lastString.indexOf("public String") >= 0) {
						break;
					}
					
					if (spaceCount == 2 && lastString.indexOf("private ") >= 0 && Character.isUpperCase(currentChar)) {
						found.append(file.getAbsolutePath() + ": " + lineCount + " (" + lastString.trim() + "...)\n");
						spaceCount = 0;
					}
					lastChar = currentChar;
					
					if (spaceCount < 2) {
						lastString += currentChar;
					}
				}
				
			}
	    }

		return found.toString();
	}

	/**
	 * Convert to 'public static final String STR = \"STR\";'
	 * 
	 * @param inputString
	 * @return
	 */
	private static String convertStaticString(String inputString) {
		StringBuffer convertedString = new StringBuffer();
		for (String line : inputString.split("\n")) {
			String name = line.trim();
			convertedString.append("public static final String " + name + " = \"" + name + "\";\r\n");
		}

		return convertedString.toString();
	}
	
	private static String convertNewCodeConstantStyle(String inputString) {
		String convertedString = null;
		if (inputString.indexOf('.') > 0) {
			String[] ss = inputString.split("\\.");
			if (ss.length == 2) {
				convertedString = "CodeConstant.scodeByValue(\"" + ss[0] + "\", \"" + ss[1] + "\")";
			} 
		} else {
			convertedString = "CodeConstant.scodeByValue(\"" + inputString + ", )";
		}
		
		return convertedString;
	}
	
	private static String convertMarkdownToRedmine(String inputString) {
		int startNo = 2;
		if (inputString.indexOf("###") == -1) {
			startNo = 3;
		}
		StringBuffer convertedString = new StringBuffer();
		String[] lines = inputString.split("\n");
		boolean openPre = false;
		String codeClass = null;
		String newLine;
		for (String line : lines) {
			if (line.startsWith("```")) {				
				openPre = !openPre;
				if (openPre) {
					if (line.length() > 3) {
						codeClass = line.substring(3);
					}
				}
				
				if (openPre) {
					newLine = "<pre>" + (codeClass == null ? "" : "<code class=\"" + codeClass + "\">");
				} else {
					newLine = (codeClass == null ? "" : "</code>") + "</pre>";
					codeClass = null;
				}
			} else {
				newLine = line.replaceAll("^## ", "h" + startNo + ". ").
					replaceAll("\n## ", "\nh" + startNo + ". ").
					replaceAll("^#+ ", "h3. ").
					replaceAll("^1\\. ", "# ").
					replaceAll("^\t1\\. ", "## ").
					replaceAll("^    1\\. ", "## ").
					replaceAll("^1\\. ", "# ").
					replaceAll("^\t1\\. ", "## ").
					replaceAll("^    1\\. ", "## ").
					replaceAll("^\t\\* ", "** ").
					replaceAll("^    \\* ", "** ").
					replaceAll("^`", "@").
					replaceAll("`$", "@").
					replaceAll("\\([0-9]+-[0-9]+\\)", "").
					replaceAll("\\([0-9]+-[0-9]+-[0-9]+\\)", "");
			}
			convertedString.append(newLine + "\r\n");
		}
		return convertedString.toString();
	}
	
	private static String convertRedmineToMarkdown(String inputString) {
		String convertedString = inputString.replaceAll("^# ", "1. ").
				replaceAll("\n# ", "\n1. ").
				replaceAll("^## ", "    1. ").
				replaceAll("\n## ", "\n    1. ").
				replaceAll("^\\*\\* ", "    * ").
				replaceAll("\n\\*\\* ", "\n    * ").
				replaceAll("^h3\\. ", "### ").
				replaceAll("\nh3\\. ", "\n### ").
				replaceAll("^h2\\. ", "## ").
				replaceAll("\nh2\\. ", "\n## ").
				replaceAll("\\(@", "(").
				replaceAll("@\\)", ")").
				replaceAll("</pre>", "```").
				replaceAll("<pre>", "```");
		
		return convertedString;
	}

	private static String convertMarkdownToText(String inputString) {
		String convertedString = inputString.
				replaceAll("^#+ (.*)", "< $1 >").
				replaceAll("\n#+ (.*)", "\n< $1 >").
				replaceAll("^1\\. ", "* ").
				replaceAll("\n1\\. ", "\n* ").
				replaceAll("```\n\n", "------------------------------\n\n").
				replaceAll("```", "------------------------------");
		
		return convertedString;
	}
	
	private static String convertToRespect(String inputString) {
		String convertedString = inputString.
				replaceAll("않는다\\.", "않습니다.").
				replaceAll("한다\\.", "합니다.").
				replaceAll("하다\\.", "합니다.").
				replaceAll("했다\\.", "했습니다.").
				replaceAll("렇다\\.", "렇습니다.").
				replaceAll("된다\\.", "됩니다.").
				replaceAll("이다\\.", "입니다.").
				replaceAll("인다\\.", "입니다.").
				replaceAll("진다\\.", "진니다.").
				replaceAll("크다\\.", "큰니다.").
				replaceAll("온다\\.", "옵니다.").
				replaceAll("운다\\.", "웁니다.").
				replaceAll("준다\\.", "줍니다.").
				replaceAll("난다\\.", "납니다.").
				replaceAll("세다\\.", "셉니다.").
				replaceAll("보다\\.", "봅니다.").
				replaceAll("([^니])다\\.", "$1습니다.");
		
		return convertedString;
	}
	
	private static String convertToRough(String inputString) {
		String convertedString = inputString.
				replaceAll("않습니다\\.", "않는다.").
				replaceAll("합니다\\.", "하다.").
				replaceAll("했습니다\\.", "했다.").
				replaceAll("렇습니다\\.", "렇다.").
				replaceAll("됩니다\\.", "된다.").
				replaceAll("입니다\\.", "이다.").
				replaceAll("보입니다\\.", "보인다.").
				replaceAll("진니다\\.", "진다.").
				replaceAll("큰니다\\.", "크다.").
				replaceAll("옵니다\\.", "온다.").
				replaceAll("웁니다\\.", "운다.").
				replaceAll("줍니다\\.", "준다.").
				replaceAll("납니다\\.", "난다.").
				replaceAll("셉니다\\.", "세다.").
				replaceAll("봅니다\\.", "보다.").
				replaceAll("습니다\\.", "다.");
		
		return convertedString;
	}
	
	private static String convertSpecialCharacters(String inputString) {
		String convertedString = inputString.
				replaceAll("\\\\n", "\n").
				replaceAll("\\\\t", "    ").
				replaceAll("\\\\\"", "\"");
		
		return convertedString;
	}
	
	private static String convertBranchNameToGitMessage(String inputString) {
		String convertedString = inputString.
				replaceAll("-", ": ").
				replaceAll("_", " ");
		
		return convertedString;
	}

	/**
	 * \" to "
	 * "{ to {
	 * }" to }
	 * @param inputString
	 * @return
	 */
	private static String stripEscapedJson(String inputString) {
		return inputString.replaceAll("\\\\\"", "\"").
				replaceAll("\"\\{", "\\{").
				replaceAll("\\}\"", "\\}");
	}
	
	private static String makeJsonPretty(String inputString) {
		String json = inputString.replaceAll("\n", "").replaceAll("\r", "");
		JsonElement jsonElement = JsonParser.parseString(json);
		String prettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);		
		return prettyJson;
	}
	
	private static String yamlToJson(String yamlString) {
	    Yaml yaml= new Yaml();
	    @SuppressWarnings("unchecked")
		Map<String, Object> map= (Map<String, Object>) yaml.load(yamlString);

	    Gson gson = new Gson(); 
	    return makeJsonPretty(gson.toJson(map));
	}

	/**
	 * exit with showing help message.
	 */
	private static void exitWithHelp() {
		exitWithHelp(null);
	}

	/**
	 * exit with showing help message.
	 * 
	 * @param badArgument
	 *            invalid parameter.
	 */
	private static void exitWithHelp(String badArgument) {
		if (badArgument != null && badArgument.length() > 0)
			System.out.println("Invalid argument: " + badArgument);
		System.out.println("Convert clipboard strings to other formated strings.");
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("java -jar string.jar [OPTION..] STRING", options);
		System.out.println("If the STRING is not inputed, the clipboard string is used.");
		System.exit(0);
	}
	
	private static void listFiles(String directoryName, List<File> files) {
	    File directory = new File(directoryName);

	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    for (File file : fList) {
	        if (file.isFile()) {
	            files.add(file);
	        } else if (file.isDirectory()) {
	            listFiles(file.getAbsolutePath(), files);
	        }
	    }
	}
	
	private static String readFile(File file) throws IOException {
	    String result = "";
	    BufferedReader in = new BufferedReader(new FileReader(file));
	    String str;
	    while ((str = in.readLine()) != null) 
	        result += str + "\n";
	    in.close();
	    return result;
	}
}
