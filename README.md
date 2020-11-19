# String Converter

It converts text strings to other formated strings.

## Usage

```
usage: java -jar string.jar [OPTION..] STRING
 -c,--camel                  Convert all to camel style. AB_Cd_ef ->
                             abCdEf
 -cd,--camel_declaring       Convert all to camel style with declaring.
                             AB_Cd_ef -> private String abCdEf;
 -cj,--camel_java            Convert Java variables to camel style.
                             private String AB_Cd_ef -> private String
                             ABCdEf
 -cjm,--camel_java_method    Convert Java method underscore to camel
                             style. abc.ab_cd_ef_gh() -> abc.abCdEfGh()
 -cm,--camel_mybatis         Convert queries of MyBatis to camel style.
                             <result property="ab_cd_ef"
                             column="ab_cd_ef"/> -> <result
                             property="abCdEf" column="abCdEf"/>
 -cmu,--camel_mybatis_only   Convert variables of MyBatis to camel style
                             only. <result property="ab_cd_ef"
                             column="ab_cd_ef"/> -> <result
                             property="abCdEf" column="ab_cd_ef"/>
 -cs,--convert_special       Convert escaped characters to real
                             characters.
 -cu,--camel_underscore      Convert underscore to camel style only.
                             AB_Cd_ef -> AB_CdEf
 -dt10,--date_10             Current date. Format: YYYY-MM-DD
 -dt8,--date_8               Current date. Format: YYYYMMDD
 -f,--first                  Convert first character to upper case. ab ->
                             Ab
 -gm,--git_message           Convert '-' to ': ' and '_' to ' '.
 -h,--help                   Show this help message.
 -md,--markdown              Convert Redmine syntax to Markdown syntax.
 -mr,--markdown_rough        Convert Redmine syntax to Markdown syntax and
                             make rougth in Korean.
 -p,--pattern <arg>          Make patterned string.
 -pj,--pretty_json           Make JSON pretty.
 -qi,--query_insert          Convert to inserting query.
 -qr,--query_result          Convert to query result object.
 -qs,--query_select          Convert to selecting query.
 -qu,--query_update          Convert to updating query with if statement.
 -qw,--query_where           Convert to where query with if statement.
 -rg,--rough                 Convert respect words to rough in Korean.
 -rm,--redmine               Convert Markdown syntax to Redmine syntax.
 -rr,--redmine_respect       Convert Markdown syntax to Redmine syntax and
                             make respect in Korean.
 -rs,--respect               Convert rougth words to respect in Korean.
 -ru,--remove_underscore     Change underscores to spaces. ab_cd -> Ab Cd
 -sc,--camel_space           Convert camel style to space separate style.
                             AbCdEf -> Ab cd ef
 -sj,--strip_json            Strip escaped strings in JSON.
 -sp,--show_pattern          Show configured patterns.
 -spj,--strip_pretty_json    Make JSON striped and pretty.
 -tx,--text                  Convert Markdown syntax to plain text.
 -u,--underscore             Convert all to underscore style. AbCdEF ->
                             Ab_cd_e_f
 -um,--underscore_mybatis    Convert all to underscore style for mybatis.
                             <result property="abCdEf" column="abCdEf"/>
                             -> <result property="abCdEf"
                             column="ab_cd_ef"/>
 -up,--upper                 Convert all characters to upper case. ab ->
                             AB
 -us,--space_underscore      Convert space camel style to underscore
                             style. Ab Cd ef -> ab_cd_ef
 -yj,--yaml_json             Convert YAML to JSON.
```

If the STRING is not inputed, the clipboard string is used.

## Pattern

It can insert inputed string to a pattern format string.
The pattern configuration file name must be 'patterns.yaml'. And the file must exist in the working directory.

### Examples

patterns.yaml

```yaml
wire: @Autowired
private {u0} {l0};
chk_null: {0} == null ? null : {0}.toString()
```

And run as below.

```
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

* {0}, {1}, ...: Inputed parameters.
* {l0}: Insert first charactor as lower case. Ex: MyCar -> myCar
* {u0}: Insert first charactor as upper case. Ex: myCar -> MyCar
* {s0}: Insert as underscore string from camel string. Ex: MyCar -> my_car
* {c0}: Insert as camel string from underscore string. Ex: my_car -> myCar 

## Java

## Examples

Convert underscore variables to camel case variables in Java.

Clipboard strings:

```
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

> java -jar string.jar -cj

Output:

```
private String id;
private String name;
private String clusterName;
private String systemId;
private String vendor;
private String description;
private String physicalIpAddress;
private String virtualIpAddress;
```
