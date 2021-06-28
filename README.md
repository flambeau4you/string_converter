# String Converter

It converts text strings to other formatted strings.

## Usage

```txt
usage: java -jar string.jar [OPTION..] STRING
 -c,--camel                 Convert all to camel style. AB_Cd_ef -> abCdEf
 -cs,--convert_special      Convert escaped characters to real characters.
 -cy,--check_yaml           Check YAML format.
 -db,--decode_base64        Decode base64 encoding to original.
 -dt10,--date_10            Current date. Format: YYYY-MM-DD
 -dt8,--date_8              Current date. Format: YYYYMMDD
 -eb,--encode_base64        Encode original to base64 encoding.
 -f,--first                 Convert first character of lines to upper
                            case. ab -> Ab
 -gm,--git_message          Convert '-' to ': ' and '_' to ' '.
 -h,--help                  Show this help message.
 -jc,--java_camel           Convert Java variables to camel style. private
                            String AB_Cd_ef -> private String ABCdEf
 -jd,--java_declaring       Convert all to camel style with declaring.
                            AB_Cd_ef -> private String abCdEf;
 -md,--markdown             Convert Redmine to Markdown syntax.
 -mr,--markdown_rough       Convert Redmine to Markdown syntax and make
                            rougth in Korean.
 -p,--pattern <arg>         Make patterned string.
 -pj,--pretty_json          Make JSON pretty.
 -qi,--query_insert         Make inserting query from column names in
                            MyBatis.
 -qr,--query_result         Make result object in MyBatis.
 -qs,--query_select <arg>   Make listing columns in selection query.
 -qu,--query_update         Make listing columns with if statement in
                            MyBatis.
 -qw,--query_where          Make where query with if statement in MyBatis.
 -rg,--rough                Convert respect words to rough in Korean.
 -rm,--redmine              Convert Markdown to Redmine syntax.
 -rr,--redmine_respect      Convert Markdown to Redmine syntax and make
                            respect in Korean.
 -rs,--respect              Convert rougth words to respect in Korean.
 -sc,--camel_space          Convert camel style to space separate style.
                            AbCdEf -> Ab cd ef
 -sj,--strip_json           Strip escaped strings in JSON. \" to ", "{ to
                            {, }" to }
 -sp,--show_pattern         Show configured patterns.
 -spj,--strip_pretty_json   Make JSON striped and pretty.
 -tx,--text                 Convert Markdown text to plain text.
 -u,--underscore            Convert all to underscore style. AbCdEF ->
                            Ab_cd_e_f
 -up,--upper                Convert all characters to upper case. ab -> AB
 -us,--underscore_space     Change underscores to spaces. ab_cd -> Ab Cd
 -yj,--yaml_json            Convert YAML to JSON.

```

If the STRING is not inputted, the clipboard string is used.

## Pattern

It can insert inputted string to a pattern format string.
The pattern configuration file name must be 'patterns.yaml'. And the file must exist in the working directory.

### Examples

patterns.yaml

```yaml
wire: @Autowired
private {u0} {l0};
chk_null: {0} == null ? null : {0}.toString()
```

And run as below.

```bash
$ java -jar string.jar -p wire StudentService
@Autowired
private StudentService studentService;
$ java -jar string.jar -p chk_null student
student == null ? null : student.toString()
```

You can copy the results to the clipboard as below.

* Windows: `java -jar string.jar -p wire StudentService | clip`
* Mac: `java -jar string.jar -p wire StudentService | pbcopy`
* Linux: `java -jar string.jar -p wire StudentService | xclip -selection clipboard`

### Keywords

Below keywords are special meaning in the patterns.yaml.

* {0}, {1}, ...: Inputted parameters.
* {l0}: Insert first character as lower case. Ex: MyCar -> myCar
* {u0}: Insert first character as upper case. Ex: myCar -> MyCar
* {s0}: Insert as underscore string from camel string. Ex: MyCar -> my_car
* {c0}: Insert as camel string from underscore string. Ex: my_car -> myCar

## Java

## Examples

Convert underscore variables to camel case variables in Java.

Clipboard strings:

```txt
private String id;
private String name;
private String cluster_name;
private String system_id;
private String vendor;
private String description;
private String physical_ip_address;
private String virtual_ip_address;
```

Execute

> java -jar string.jar -jc

Output:

```txt
private String id;
private String name;
private String clusterName;
private String systemId;
private String vendor;
private String description;
private String physicalIpAddress;
private String virtualIpAddress;
```
