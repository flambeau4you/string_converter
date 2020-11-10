# String Converter

It converts text strings to other formated strings.

## Usage

```
usage: java -jar string.jar [OPTION..] STRING
 -c,--camel                  Convert all to camel style. AB_Cd_ef ->
                             abCdEf
 -cd,--camel_declaring       Convert all to camel style with declaring.
                             AB_Cd_ef -> private String abCdEf;
 -cj,--camel_java            Convert java variables to camel style.
                             AB_Cd_ef -> ABCdEf
 -cjm,--camel_java_method    Convert java method underscore to camel
                             style. abc.ab_cd_ef_gh() -> abc.abCdEfGh()
 -cm,--camel_mybatis         Convert queries of MyBatis to camel style.
 -cmu,--camel_mybatis_only   Convert queries of MyBatis to camel style
                             only.
 -cp,--copy                  Copy the results to the clipboard.
 -cs,--convert_special       Convert special characters.
 -cu,--camel_underscore      Convert underscore to camel style only.
                             AB_Cd_ef -> AB_CdEf
 -dt10,--date_10             Current date. (YYYY-MM-DD)
 -dt8,--date_8               Current date. (YYYYMMDD)
 -f,--first                  Convert fist character to upper case.
 -fjc,--find_java_camel      Find camel variables in the directory path.
 -gm,--git_message           Convert '-' to ': ' and '_' to ' '.
 -h,--help                   Show this help message.
 -md,--markdown              Convert Redmine syntax to Markdown syntax.
 -mr,--markdown_rough        Convert Redmine syntax to Markdown syntax and
                             make rougth.
 -nc,--new_code              Convert old style code constant to new code
                             constant style.
 -pj,--pretty_json           Make JSON pretty.
 -qi,--query_insert          Convert to inserting query.
 -qr,--query_result          Convert to query result object.
 -qs,--query_select          Convert to seleting query.
 -qu,--query_update          Convert to updating query with if statement.
 -qw,--query_where           Convert to where query with if statement.
 -rg,--rough                 Convert respect words to rough in Korean.
 -rm,--redmine               Convert Markdown syntax to Redmine syntax.
 -rr,--redmine_respect       Convert Markdown syntax to Redmine syntax and
                             make respect.
 -rs,--respect               Convert rougth words to respect in Korean.
 -ru,--remove_underscore     Remove all underscore.
 -sc,--camel_space           Convert camel style to space speration style.
 -sj,--strip_json            Strip escaped strings in JSON.
 -spj,--strip_pretty_json    Make JSON striped and pretty.
 -tx,--text                  Convert Markdown syntax to plain text.
 -u,--underscoee             Convert all to underscore style.
 -um,--underscore_mybatis    Convert all to underscore style for mybatis.
 -up,--upper                 Convert all character to upper case.
 -us,--space_underscore      Convert space camel style to underscore
                             style.
 -yj,--yaml_json             Convert YAML to JSON.
If the STRING is not inputed, the clipboard string is used.
```

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
