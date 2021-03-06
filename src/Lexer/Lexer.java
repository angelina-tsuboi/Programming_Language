package Lexer;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
  public static void main(String[] args) {
    
    try {
        File file = new File("src/tests/lexerTest3.txt"); 
        Scanner sc = new Scanner(file); 
        String tokensString = "";
        while (sc.hasNextLine()) {
          tokensString += sc.nextLine(); 
        }
       
        printTokens(lexenize(tokensString));
    }catch(Exception error) {
      System.out.println(error);
      System.exit(1);
    }
  }
  
  private static List<TokenObj> lexenize(String input) {
    // The tokens to return
       String token = "";
       TokenObjSet[] set = TokenObjSet.values();
       ArrayList<TokenObj> tokens = new ArrayList<TokenObj>();

      for(int i = 0; i < input.length(); i++) {
        token += input.charAt(i);
        
        for(int j = 0; j < set.length; j++) {
          Pattern p  = set[j].getPattern();
          Matcher m = p.matcher(token);
          
          if(m.find()) {
            String val = null;
            
            if(set[j].getIncludeValue()) {
              val = m.group();
            }
            
            TokenObj newToken = new TokenObj(set[j], val);
            tokens.add(newToken);
            token = token.substring(m.end());
          }
        }
        
      }

       return tokens;
     }
  
  private static void printTokens(List<TokenObj> tokens) {
    for(TokenObj token: tokens) {
      System.out.println(token);
    }
  }
    
}



