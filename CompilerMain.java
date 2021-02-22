import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import java.util.Scanner;
import java.util.List;
import java.io.*;


public class CompilerMain {
   public static void main(String[] args) throws Exception {
       DataStrt d = new DataStrt();
       FuncStrt f = new FuncStrt(d);
       Scanner scf = new Scanner(new File(args[0]));
       PrintWriter pw = new PrintWriter(new File(args[1]+".java"));
       while(scf.hasNextLine()){
          // create a CharStream that reads from standard input:
         CharStream input = CharStreams.fromString(scf.nextLine()+"\n");
         // create a lexer that feeds off of input CharStream:
           TypesInterpreterLexer lexer = new TypesInterpreterLexer(input);
          // create a buffer of tokens pulled from the lexer:
          CommonTokenStream tokens = new CommonTokenStream(lexer);
           // create a parser that feeds off the tokens buffer:
           TypesInterpreterParser parser = new TypesInterpreterParser(tokens); 
           // replace error listener:
           //parser.removeErrorListeners(); // remove ConsoleErrorListener
           //parser.addErrorListener(new ErrorHandlingListener());
           // begin parsing at principal rule:
           ParseTree tree = parser.file();
           if (parser.getNumberOfSyntaxErrors() == 0) {
           // print LISP-style tree:
           // System.out.println(tree.toStringTree(parser));
           IVisitor visitor0 = new IVisitor(d);
           visitor0.visit(tree);
           }  
      }
      scf = new Scanner(new File(args[1]));
      pw.println("public class "+args[1]+"{\n\n\tpublic static void main(String[] args){");
      int contexto = 2;
      String fr = "";
      while(scf.hasNextLine()){
         fr = fr + scf.nextLine()+"\n";
      }
      CharStream input = CharStreams.fromString(fr);
      // create a lexer that feeds off of input CharStream:
      CompilerLexer lexer = new CompilerLexer(input);
      // create a buffer of tokens pulled from the lexer:
      CommonTokenStream tokens = new CommonTokenStream(lexer);
      // create a parser that feeds off the tokens buffer:
      CompilerParser parser = new CompilerParser(tokens); 
      // replace error listener:
      //parser.removeErrorListeners(); // remove ConsoleErrorListener
      //parser.addErrorListener(new ErrorHandlingListener());
      // begin parsing at principal rule:
      ParseTree tree = parser.program();
      if (parser.getNumberOfSyntaxErrors() == 0) {
         // print LISP-style tree:
         // System.out.println(tree.toStringTree(parser));
         CompVisitor visitor1 = new CompVisitor(args[1],d, f, pw, contexto);
         visitor1.visit(tree);
      }  
      pw.println("\t}\n\n");

      String[] names = f.names();
      for(String n : names){
         List<Content> l = f.getFuncList(n);
         for(Content c : l){
            pw.print("\tpublic static ");
            if(c.getReturn().equals("void")) pw.print("void ");
            else pw.print("double ");
            pw.print(n+"(");
            String[] t = c.getTypes();
            String[] ids = c.getIds();
            if(t.length == 0){}
            else{
               pw.print("double "+ids[0]);
               for(int i = 1; i<ids.length; i++) pw.print(", double"+ids[i]); //corrigir
            }
            pw.print("){\n"+c.getBody()+"\t}\n\n");
         }
      }
      pw.print("}");
      pw.close();
   }
}
