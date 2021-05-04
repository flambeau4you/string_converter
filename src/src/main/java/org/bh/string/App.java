/*
Copyright 2020 Jung Bong-Hwa

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.bh.string;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
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
	private static final String FILE_PATTERN = "patterns.yaml";
	private static Options options = new Options();

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		// define arguments
		options.addOption("h", "help", false, "Show this help message.");
		options.addOption("p", "pattern", true, "Make patterned string.");
		options.addOption("sp", "show_pattern", false, "Show configured patterns.");
		options.addOption("dt8", "date_8", false, "Current date. Format: YYYYMMDD");
		options.addOption("dt10", "date_10", false, "Current date. Format: YYYY-MM-DD");
		options.addOption("c", "camel", false, "Convert all to camel style. AB_Cd_ef -> abCdEf");
		options.addOption("cd", "camel_declaring", false, "Convert all to camel style with declaring. AB_Cd_ef -> private String abCdEf;");
		options.addOption("cj", "camel_java", false, "Convert Java variables to camel style. private String AB_Cd_ef -> private String ABCdEf");
		options.addOption("sc", "camel_space", false, "Convert camel style to space separate style. AbCdEf -> Ab cd ef");
		options.addOption("us", "space_underscore", false, "Convert space camel style to underscore style. Ab Cd ef -> ab_cd_ef");
		options.addOption("u", "underscore", false, "Convert all to underscore style. AbCdEF -> Ab_cd_e_f");
		options.addOption("us", "underscore_space", false, "Change underscores to spaces. ab_cd -> Ab Cd");
		options.addOption("f", "first", false, "Convert first character of lines to upper case. ab -> Ab");
		options.addOption("up", "upper", false, "Convert all characters to upper case. ab -> AB");
		options.addOption("qs", "query_select", true, "Make listing columns in selection query.");
		options.addOption("qu", "query_update", false, "Make listing columns with if statement in MyBatis.");
		options.addOption("qi", "query_insert", false, "Make inserting query from column names in MyBatis.");
		options.addOption("qw", "query_where", false, "Make where query with if statement in MyBatis.");
		options.addOption("qr", "query_result", false, "Make result object in MyBatis.");
		options.addOption("rm", "redmine", false, "Convert Markdown to Redmine syntax.");
		options.addOption("md", "markdown", false, "Convert Redmine to Markdown syntax.");
		options.addOption("rr", "redmine_respect", false, "Convert Markdown to Redmine syntax and make respect in Korean.");
		options.addOption("mr", "markdown_rough", false, "Convert Redmine to Markdown syntax and make rougth in Korean.");
		options.addOption("tx", "text", false, "Convert Markdown text to plain text.");
		options.addOption("rs", "respect", false, "Convert rougth words to respect in Korean.");
		options.addOption("rg", "rough", false, "Convert respect words to rough in Korean.");
		options.addOption("sj", "strip_json", false, "Strip escaped strings in JSON. \\\" to \", \"{ to {, }\" to }");		
		options.addOption("pj", "pretty_json", false, "Make JSON pretty.");
		options.addOption("spj", "strip_pretty_json", false, "Make JSON striped and pretty.");		
		options.addOption("yj", "yaml_json", false, "Convert YAML to JSON.");
		options.addOption("cy", "check_yaml", false, "Check YAML format.");
		options.addOption("cs", "convert_special", false, "Convert escaped characters to real characters.");
		options.addOption("gm", "git_message", false, "Convert '-' to ': ' and '_' to ' '.");
		options.addOption("eb", "encode_base64", false, "Encode original to base64 encoding.");
		options.addOption("db", "decode_base64", false, "Decode base64 encoding to original.");

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

			if (cmd.hasOption("h") 
					|| (!cmd.hasOption("sp") && !cmd.hasOption("dt8") && !cmd.hasOption("dt10") 
							&& (cmd.getOptions().length == 0 || inputString == null || inputString.isEmpty()))
					) {
				// show help message.
				exitWithHelp();
			} else {
				String str = inputString;
				
				if (cmd.hasOption("p") || cmd.hasOption("sp")) {
					Map<String, String> patterns = null;
					Yaml yaml = new Yaml();
					try {
                        patterns = (Map<String, String>) yaml.load(new FileReader(new File(FILE_PATTERN)));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                        return;
                    }
					
					if (cmd.hasOption("sp")) {
                        for (Map.Entry<String, String> entry : patterns.entrySet()) {
                            System.out.println(entry.getKey() + ": " + entry.getValue());
                        }
                        return;
					} else {
						String patternCode = cmd.getOptionValue("p");
						str = makePatternedString(patterns, patternCode, str);
					}
				}
				
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
				
				if (cmd.hasOption("cj")) {
					str = convertJavaUnderscoreToCarmel(str);
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
				
				if (cmd.hasOption("us")) {
					str = convertUnderscoreToSpace(str);
				}

				if (cmd.hasOption("f")) {
					str = convertUpperCastFirst(str);
				}

				if (cmd.hasOption("up")) {
					str = convertUpperCast(str);
				}

				if (cmd.hasOption("qs")) {
					String tableName = cmd.getOptionValue("qs");
					str = convertSelectingQuery(str, tableName);
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
				if (cmd.hasOption("cy")) {
					str = checkYamlFormat(str);
				}

				if (cmd.hasOption("cs")) {
					str = convertSpecialCharacters(str);
				}

				if (cmd.hasOption("gm")) {
					str = convertBranchNameToGitMessage(str);
				}

				if (cmd.hasOption("eb")) {
					str = encodeToBase64(str);
				}
				if (cmd.hasOption("db")) {
					str = decodeFromBase64(str);
				}

				if (str != null && !str.isEmpty()) {
					System.out.println(str.trim());
				} else {
					System.out.println("There are no changings.");
				}
			}
		} catch (ParseException e) {
			exitWithHelp(e.getMessage());
		} catch (Exception e) {
			System.out.println("An error is occured!");
			e.printStackTrace();
		}
	}
	
	private static String makePatternedString(Map<String, String> patterns, String code, String inputedString) {
		if (patterns == null || patterns.size() == 0) {
            System.err.println("The clip file contents are empty.");
            return null;
        }

        if (code == null || code.isEmpty()) {
            System.err.println("The code is undefined.");
            return null;
        }

        String pattern = patterns.get(code);
        if (pattern == null) {
            System.err.println("The code '" + code + "' is unknown.");
            return null;
        }

        // replace with parameter
        String[] params = inputedString.split(" ");
        for (int i = 0; i < params.length; i++) {
            String param = params[i];
            if (param.length() > 1) {
                // upper case first character
                pattern = pattern.replace("{u" + i + "}", param.substring(0, 1).toUpperCase() + param.substring(1));
                // lower case first character
                pattern = pattern.replace("{l" + i + "}", param.substring(0, 1).toLowerCase() + param.substring(1));
            }

            if (param.length() > 2 && param.indexOf('_') > 0) {
                // under bar to camel case
                pattern = pattern.replace("{c" + i + "}", convertUnderscoreToCarmel(param));
            }
            pattern = pattern.replace("{s" + i + "}", convertCarmelToUnderscore(param));
            pattern = pattern.replace("{" + i + "}", params[i]);
        }
        return pattern;
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

	private static String convertUnderscoreToSpace(String inputString) {
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

	private static String convertSelectingQuery(String inputString, String tableName) {
		StringBuffer convertedString = new StringBuffer();
		String[] lines = inputString.split("\n");
		
		int count = 0;
		for (String line : lines) {
			String name = line.trim();
			if (name.endsWith("CREATED_TIME") || name.endsWith("UPDATED_TIME")) {
				convertedString.append("date_format(" + name + ", '%Y-%m-%d %H:%i:%s') as " + name);
			} else {
				convertedString.append(name);
				if (name.endsWith("_CD")) {
					String codeColumn = name.substring(0, name.length() - 3);
					convertedString.append(",\r\ngetCodeValue('" + tableName + "', '" + codeColumn + "', " + name + ") " + codeColumn + "_VALUE");
				} else if (name.endsWith("STATE") || name.endsWith("STATUS"))
				{
					convertedString.append(", \r\ngetCodeValue('" + tableName + "', '" + name + "', " + name + ") " + name + "_VALUE");
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
			convertedString.append("<if test=\"" + camelName + " != null");
			if (!name.toLowerCase().startsWith("is_")) {				
				convertedString.append(" and " + camelName + ".length() > 0");
			}
			convertedString.append("\">\r\n"+ "    and " + name + " = #{" + camelName + "}\r\n</if>");
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
					replaceAll("^  +\\* ", "** ").
					replaceAll("^`", "@").
					replaceAll("`$", "@").
					replaceAll("<http([^>]*)>", "http$1").
					replaceAll("\\([0-9]+-[0-9]+\\)$", "").
					replaceAll("\\([0-9]+-[0-9]+-[0-9]+\\)$", "");
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
				replaceAll("낸다\\.", "냅니다.").
				replaceAll("는다\\.", "습니다.").
				replaceAll("한다\\.", "합니다.").
				replaceAll("온다\\.", "오네요.").
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
				replaceAll("보입니다\\.", "보인다.").
				replaceAll("입니다\\.", "이다.").
				replaceAll("진니다\\.", "진다.").
				replaceAll("큰니다\\.", "크다.").
				replaceAll("옵니다\\.", "온다.").
				replaceAll("웁니다\\.", "운다.").
				replaceAll("줍니다\\.", "준다.").
				replaceAll("납니다\\.", "난다.").
				replaceAll("셉니다\\.", "세다.").
				replaceAll("봅니다\\.", "보다.").
				replaceAll("냅니다\\.", "낸다.").
				replaceAll("합니다\\.", "한다.").
				replaceAll("오네요\\.", "온다.").
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
		JsonElement jsonElement = null;
		try
		{
			jsonElement = JsonParser.parseString(json);
		} catch (Exception e)
		{
			System.out.println("Invalid JSON!\n" + json);
			e.printStackTrace();
			System.exit(0);
		}
		
		if (jsonElement != null) {
			String prettyJson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create().toJson(jsonElement);		
			return prettyJson;
		} else {
			return null;
		}
	}
	
	private static String yamlToJson(String yamlString) {
	    Yaml yaml= new Yaml();
	    @SuppressWarnings("unchecked")
		Map<String, Object> map= (Map<String, Object>) yaml.load(yamlString);

	    Gson gson = new Gson(); 
	    return makeJsonPretty(gson.toJson(map));
	}
	
	private static String checkYamlFormat(String yamlString) {
	    Yaml yaml= new Yaml();
	    try {
		    @SuppressWarnings("unchecked")
			Map<String, Object> map= (Map<String, Object>) yaml.load(yamlString);
		    return "Valid";
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	return "Invalid!";
	    }
	}
	
	private static String encodeToBase64(String inputString) {
		return Base64.getEncoder().encodeToString(inputString.getBytes());
	}

	private static String decodeFromBase64(String encodedString) {
		byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
		return new String(decodedBytes);
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
		System.out.println("If the STRING is not inputed, the clipboard string is used.");
		HelpFormatter formater = new HelpFormatter();
		formater.printHelp("java -jar string.jar [OPTION..] STRING", options);
		System.exit(0);
	}
}
