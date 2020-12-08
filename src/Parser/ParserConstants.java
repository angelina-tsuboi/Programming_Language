/* Generated By:JavaCC: Do not edit this line. ParserConstants.java */

/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface ParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int DECIMAL = 6;
  /** RegularExpression Id. */
  int NUMBER = 7;
  /** RegularExpression Id. */
  int STRING = 8;
  /** RegularExpression Id. */
  int BOOLEAN = 9;
  /** RegularExpression Id. */
  int IDENTCAPITALIZED = 10;
  /** RegularExpression Id. */
  int IDENTLOWERCASE = 11;
  /** RegularExpression Id. */
  int IDENTUNDERSCORE = 12;
  /** RegularExpression Id. */
  int OPADD = 13;
  /** RegularExpression Id. */
  int OPSUBTRACT = 14;
  /** RegularExpression Id. */
  int OPMULTIPLY = 15;
  /** RegularExpression Id. */
  int OPDIVIDE = 16;
  /** RegularExpression Id. */
  int OPMODULO = 17;
  /** RegularExpression Id. */
  int OPEQUALTO = 18;
  /** RegularExpression Id. */
  int OPNOTEQUALTO = 19;
  /** RegularExpression Id. */
  int OPGREATERTHAN = 20;
  /** RegularExpression Id. */
  int OPLESSTHAN = 21;
  /** RegularExpression Id. */
  int OPGREATERTHANEQUALTO = 22;
  /** RegularExpression Id. */
  int OPLESSTHANEQUALTO = 23;
  /** RegularExpression Id. */
  int OPAND = 24;
  /** RegularExpression Id. */
  int OPOR = 25;
  /** RegularExpression Id. */
  int OPNOT = 26;
  /** RegularExpression Id. */
  int PUNCQUESTION = 27;
  /** RegularExpression Id. */
  int PUNCCOLON = 28;
  /** RegularExpression Id. */
  int PUNCSEMICOLON = 29;
  /** RegularExpression Id. */
  int PUNCCOMMA = 30;
  /** RegularExpression Id. */
  int PUNCLEFTPAREN = 31;
  /** RegularExpression Id. */
  int PUNCRIGHTPAREN = 32;
  /** RegularExpression Id. */
  int PUNCLEFTCURLY = 33;
  /** RegularExpression Id. */
  int PUNCRIGHTCURLY = 34;
  /** RegularExpression Id. */
  int KEYIF = 35;
  /** RegularExpression Id. */
  int KEYFOR = 36;
  /** RegularExpression Id. */
  int KEYWHILE = 37;
  /** RegularExpression Id. */
  int KEYBLOCK = 38;
  /** RegularExpression Id. */
  int KEYEMIT = 39;
  /** RegularExpression Id. */
  int KEYTASK = 40;
  /** RegularExpression Id. */
  int KEYNULL = 41;
  /** RegularExpression Id. */
  int KEYPASS = 42;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"[COMMENT]\"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"[DECIMAL \\\\d+(\\\\.\\\\d{1,2})?]\"",
    "\"[NUMBER (?<=\\\\s|^)\\\\d+(?=\\\\s|$)]\"",
    "\"[STRING \\\"[^\\\"]*\\\"]\"",
    "\"[BOOLEAN true(?=[^_A-Za-z])|false(?=[^_A-Za-z])]\"",
    "\"[IDENTCAPITALIZED  [A-Z][_A-Za-z]*(?=[^_A-Za-z])]\"",
    "\"[IDENTLOWERCASE [a-z][_A-Za-z]*(?=[^_A-Za-z])]\"",
    "\"[IDENTUNDERSCORE _[_A-Za-z]*(?=[^_A-Za-z])]\"",
    "\"[OPADD]\"",
    "\"[OPSUBTRACT]\"",
    "\"[OPMULTIPLY]\"",
    "\"[OPDIVIDE]\"",
    "\"[OPMODULO]\"",
    "\"[OPEQUALTO]\"",
    "\"[OPNOTEQUALTO]\"",
    "\"[OPGREATERTHAN]\"",
    "\"[OPLESSTHAN]\"",
    "\"[OPGREATERTHANEQUALTO]\"",
    "\"[OPLESSTHANEQUALTO]\"",
    "\"[OPAND]\"",
    "\"[OPOR]\"",
    "\"[OPNOT]\"",
    "\"[PUNCQUESTION]\"",
    "\"[PUNCCOLON]\"",
    "\"[PUNCSEMICOLON]\"",
    "\"[PUNCCOMMA]\"",
    "\"[PUNCLEFTPAREN]\"",
    "\"[PUNCRIGHTPAREN]\"",
    "\"[PUNCLEFTCURLY]\"",
    "\"[PUNCRIGHTCURLY]\"",
    "\"[KEYIF]\"",
    "\"[KEYFOR]\"",
    "\"[KEYWHILE]\"",
    "\"[KEYBLOCK]\"",
    "\"[KEYEMIT]\"",
    "\"[KEYTASK]\"",
    "\"[KEYNULL]\"",
    "\"[KEYPASS]\"",
  };

}
