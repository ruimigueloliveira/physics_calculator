import java.io.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

/*
   acrescentar constantes k m ...
*/


public class CompVisitor extends CompilerBaseVisitor<String>{

    private String name;
    private DataStrt d;
    private FuncStrt fs;
    private PrintWriter pw;
    private int contexto;
    private boolean alarm = false;
    private String msg = null;

    public CompVisitor(String n, DataStrt s, FuncStrt f, PrintWriter pw, int contexto){
        name = n;
        d = s;
        fs = f;
        this.pw = pw;
        this.contexto = contexto;
    }

    public String visitProgram(CompilerParser.ProgramContext ctx) {
        int i = 0, num = 0;
        boolean error = false;
        while(ctx.stat(i) != null){
            pw.print(visit(ctx.stat(i)));
            i++;
            if(alarm){
                System.out.print(msg);
                alarm = false;
                error = true;
                num++;
                msg = null;
            }
        }
        if(error){ 
            System.out.println("Número de erros: "+num);
            pw.close();
            new File(name+".java").delete();
            System.exit(1);
        }
        return null;
    }


    public String visitDefineId(CompilerParser.DefineIdContext ctx) {
        if(tratarErro(d.inserirVar(ctx.type.getText(), ctx.id.getText(), contexto))){
            return escreverContexto(contexto)+"double "+ctx.id.getText()+";\n";
        }
        return "";
    }

    public String visitDefineOperacaoValue(CompilerParser.DefineOperacaoValueContext ctx) {
        Unidade u = verifyOp(ctx.value);
        // erros da op
        if(u == null){
            alarm = true;
            msg = "instruction: "+"Atribuição de um número sem unidade a "+ctx.id.getText()+"\n";
            return "";
        }
        Unidade t = d.getUni(ctx.type.getText());
        if(t == null){
            alarm = true;
            msg = "instruction: "+"O Tipo "+ctx.type.getText()+" não existe\n";
            return "";
        }
        else if(u.getSimbolo().equals(t.getSimbolo()) || u.getNome().equals("numero")){
            String r = visit(ctx.value);
            if(tratarErro(d.inserirVar(ctx.type.getText(), ctx.id.getText(), contexto))){
                return escreverContexto(contexto)+"double "+ctx.id.getText()+" = "+r+";\n";
            }
        }
        else{
            alarm = true;
            msg = "instruction: "+"Atribuição de um tipo incompativel a "+ctx.id.getText()+"\n";
            return "";
        }
        return "";
    }

    public String visitUpdate(CompilerParser.UpdateContext ctx) {
        Unidade u1 = verifyOp(ctx.value);
        if(u1 == null){
            alarm = true;
            msg = "instruction: "+"Atribuição de um número sem unidade a "+ctx.id.getText()+"\n";
            return "";
        }
        Variavel v = d.getVar(ctx.id.getText());
        if(v == null){
            alarm = true;
            msg = "instruction: "+"Variável "+ctx.id.getText()+" não foi declarada\n";
            return "";
        }
        Unidade t = v.getUnidade();

        if(t.getSimbolo().equals(u1.getSimbolo()) || u1.getNome().equals("numero")){
            String r = visit(ctx.value), f = ctx.id.getText()+" = ";
            return escreverContexto(contexto)+f+r+";\n";
        }
        alarm = true;
        msg = "instruction: "+"Atribuição de um tipo incompativel a "+ctx.id.getText()+"\n";
        return "";
    }

    public String visitMstr(CompilerParser.MstrContext ctx) {
        return escreverContexto(contexto)+"System.out.printf("+ctx.STRING().getText()+");\n";
    }

    public String visitMoper(CompilerParser.MoperContext ctx) {
        Unidade u = verifyOp(ctx.operacao());
        if(u == null){
            alarm = true;
            msg = "Introdução de um número sem um tipo no show\n";
            return "";
        }
        return escreverContexto(contexto)+"if(("+visit(ctx.operacao())+")>0.001 && ("+visit(ctx.operacao())+")<10000) System.out.printf(\"%.4f %s\","+visit(ctx.operacao())+",\""+u.getSimbolo()+"\");\n"+escreverContexto(contexto)+"else System.out.printf(\"%e %s\","+visit(ctx.operacao())+",\""+u.getSimbolo()+"\");\n";
    }

    public String visitMulti(CompilerParser.MultiContext ctx) {
        return visit(ctx.l1)+ctx.op.getText()+visit(ctx.l2);
    }

    public String visitSum(CompilerParser.SumContext ctx) {
        return visit(ctx.l1)+ctx.op.getText()+visit(ctx.l2);
    }


    public String visitParenteses(CompilerParser.ParentesesContext ctx) {
        return "("+visit(ctx.operacao())+")";
    }

    public String visitOperando(CompilerParser.OperandoContext ctx) {
        try{
            double valor = Double.parseDouble(ctx.v.getText());
            return ctx.v.getText();
        }catch(Exception e){
            System.out.println(ctx.v.getText());
            Variavel v = d.getVar(ctx.v.getText());
            if(v == null){
                return null;
            }
            if(v.getValor() == null){
                return ctx.v.getText();
            }
            else{
                return v.getValor()+"";
            }
        }
    }

    public String visitMoreargs(CompilerParser.MoreargsContext ctx) {
        return visit(ctx.callArgsFunction());
    }

    public String visitNoargs(CompilerParser.NoargsContext ctx) {
        return visit(ctx.callNoArgFunction());
    }

    public String visitCallnoarg(CompilerParser.CallnoargContext ctx){
        return escreverContexto(contexto)+visit(ctx.callNoArgFunction())+";\n";
    }

    public String visitCallarg(CompilerParser.CallargContext ctx){
        return escreverContexto(contexto)+visit(ctx.callArgsFunction())+";\n";
    }



    public String visitDecisao(CompilerParser.DecisaoContext ctx) {
        String f = escreverContexto(contexto)+"if("+visit(ctx.condicao())+"){\n";
        contexto++;
        int i = 0;
        while(ctx.stat(i) != null){
            f = f+visit(ctx.stat(i));
            i++;
        }
        d.destroyContext();
        f = f+escreverContexto(--contexto)+"}\n";
        if(ctx.decaux() != null) f= f+visit(ctx.decaux());
        return f;
    }

    public String visitCondicaoSimples(CompilerParser.CondicaoSimplesContext ctx) {
        if(verifyCondicao(ctx)){
            if(ctx.logica() == null) return visit(ctx.op1)+ctx.op.getText()+visit(ctx.op2);
            return visit(ctx.op1)+ctx.op.getText()+visit(ctx.op2)+visit(ctx.logica());
        }
        alarm = true;
        msg = "instruction: "+"condição dentro do if inválida\n";
        return "";
    }

    private boolean verifyCondicao(CompilerParser.CondicaoContext ctx){
        if(ctx instanceof CompilerParser.CondicaoSimplesContext){
             CompilerParser.CondicaoSimplesContext ct = (CompilerParser.CondicaoSimplesContext) ctx;
             Unidade u1 = verifyOp(ct.op1);
             System.out.println(u1);
             Unidade u2 = verifyOp(ct.op2);
             System.out.println(u2);
             boolean cond = ((u1.getNome().equals("numero") || u2.getNome().equals("numero")) || (u1.getSimbolo().equals(u2.getSimbolo())));
             if(ct.logica() != null){
                 return cond && verifyCondicao(ct.logica().condicao());
             }
             return cond;

        }
        if(ctx instanceof CompilerParser.CondicaoNegadaContext){
            CompilerParser.CondicaoNegadaContext ct = (CompilerParser.CondicaoNegadaContext) ctx;
            Unidade u1 = verifyOp(ct.op1);
            Unidade u2 = verifyOp(ct.op2);
            boolean cond = ((u1.getNome().equals("numero") || u2.getNome().equals("numero")) || (u1.getSimbolo().equals(u2.getSimbolo())));
            if(ct.logica() != null){
                return cond && verifyCondicao(ct.logica().condicao());
            }
            return cond;
        }
        CompilerParser.CondicaoParentesesContext ct = (CompilerParser.CondicaoParentesesContext) ctx;

        if(ct.logica() != null){
            return verifyCondicao(ct.condicao()) && verifyCondicao(ct.logica().condicao());
        }
        return verifyCondicao(ct.condicao());
    }

    public String visitCondicaoNegada(CompilerParser.CondicaoNegadaContext ctx) {
        if(verifyCondicao(ctx)){
            if(ctx.logica() == null) return "!("+visit(ctx.op1)+ctx.op.getText()+visit(ctx.op2)+")";
            return "!("+visit(ctx.op1)+ctx.op.getText()+visit(ctx.op2)+")"+visit(ctx.logica());
        }
        alarm = true;
        msg = "instruction: "+"condição dentro do if inválida\n";
        return "";
    }

    public String visitCondicaoParenteses(CompilerParser.CondicaoParentesesContext ctx){
       if(verifyCondicao(ctx)){
           if(ctx.logica() == null) return "("+visit(ctx.condicao())+")";
           return "("+visit(ctx.condicao())+")"+visit(ctx.logica());
       }
       alarm = true;
       msg = "instruction: "+"condição dentro do if inválida\n";
       return "";     
    }

    public String visitLogica(CompilerParser.LogicaContext ctx){
        return " "+ctx.op.getText()+" "+visit(ctx.condicao());
    }

    public String visitMultipleElseIf(CompilerParser.MultipleElseIfContext ctx) {
        String f = escreverContexto(contexto++)+"else if("+visit(ctx.condicao())+"){\n";
        int i = 0;
        while(ctx.stat(i) != null){
           f = f+visit(ctx.stat(i));
           i++;
        }
        d.destroyContext();
        f = f+escreverContexto(--contexto)+"}\n";
        f = f+visit(ctx.decaux());
        return f;
    }

    public String visitSingleElseIf(CompilerParser.SingleElseIfContext ctx) {
        String f = escreverContexto(contexto++)+"else if("+visit(ctx.condicao())+"){\n";
        int i = 0;
        while(ctx.stat(i) != null){
            f = f+visit(ctx.stat(i));
            i++;
        }
        d.destroyContext();
        f = f+escreverContexto(--contexto)+"}\n";
        return f;
    }

    public String visitElse(CompilerParser.ElseContext ctx) {
        String f = escreverContexto(contexto++)+"else{\n";
        int i = 0;
        while(ctx.stat(i) != null){
            f = f+visit(ctx.stat(i));
            i++;
        }
        d.destroyContext();
        f = f+escreverContexto(--contexto)+"}\n";
        return f;
    }

    public String visitCicloWhile(CompilerParser.CicloWhileContext ctx) {
        String f = escreverContexto(contexto++)+"while("+visit(ctx.condicao())+"){\n";
        int i = 0;
        while(ctx.stat(i) != null){
            f= f+visit(ctx.stat(i));
            i++;
        }
        d.destroyContext();
        f = f+escreverContexto(--contexto)+"}\n";
        return f;
    }

    public String visitFunction(CompilerParser.FunctionContext ctx) {
        boolean end = false;
        String[] tipos;
        String[] ids;
        if(ctx.arg() == null){
            tipos = new String[0];
            ids = new String[0];
            fs.addFunc(ctx.name.getText(), ctx.type.getText(), tipos, ids);
        } 
        else{
            String[] a = visit(ctx.arg()).split(",");
            tipos = new String[a.length];
            ids = new String[a.length];
            for(int i = 0; i<a.length; i++){
                String[] s = a[i].split(" ");
                tipos[i] = s[0];
                ids[i] = s[1];
                if(!tratarErro(d.inserirVar(tipos[i], ids[i], contexto))){
                    end = true;
                    break;
                }
            }
            if(end) return "";
            fs.addFunc(ctx.name.getText(), ctx.type.getText(), tipos, ids);
        }
        int i = 0;
        String body = "";
        while(ctx.stat(i) != null){
            body = body+visit(ctx.stat(i));
            i++;
        }
        if(ctx.devolve() == null){
            if(!ctx.type.getText().equals("void")){
                alarm = true;
                msg = name+": Definição da função "+ctx.type.getText()+" "+ctx.name.getText()+"() não termina com um return\n";
            }
            else fs.updateFunc(ctx.name.getText(), tipos, body);
        }
        else{
           if(ctx.devolve().operacao() == null && ctx.type.getText().equals("void")){
              body = body+visit(ctx.devolve());
              fs.updateFunc(ctx.name.getText(), tipos, body); 
           }
           else if(ctx.devolve().operacao() != null && !(ctx.type.getText().equals("void"))){
              Unidade u = verifyOp(ctx.devolve().operacao());
              if(u == null){
                  alarm = true;
                  msg = name+": Atribuição de um número sem unidade num return\n";
                  return "";
              }
              if(u.getNome().equals("numero") || u.getNome().equals(ctx.type.getText())){
                  body = body+visit(ctx.devolve());
                  fs.updateFunc(ctx.name.getText(), tipos, body);
                  return "";
              }
              alarm = true;
              msg = name+": O tipo do return não corresponde ao da função \n";
              return "";
           }
           else{
               alarm = true;
               msg = name+": função "+ctx.name.getText()+"() do tipo "+ctx.type.getText()+" não possui um return do mesmo tipo\n";
           } 
        }
        d.destroyContext();
        return "";
    }

    public String visitCallNoArgFunction(CompilerParser.CallNoArgFunctionContext ctx) {
        if(fs.funcExist(ctx.WORD().getText(), new String[0])){
            return ctx.WORD().getText()+"()";
        }
        alarm = true;
        msg = name+": "+"A função "+ctx.WORD().getText()+"() não foi defenida\n";
        return "";
    }

    public String visitCallArgsFunction(CompilerParser.CallArgsFunctionContext ctx) {
        String f = ctx.WORD().getText()+"("+visit(ctx.operacao(0));
        int i = 1;
        while(ctx.operacao(i) != null){
            f = f+","+visit(ctx.operacao(i));
            i++;
        }
        f = f+")";
        System.out.println(f);
        String[] ids = f.substring(f.indexOf("(")+1, f.length()-1).split(",");
        for(String id : ids) System.out.println(id);
        if(fs.funcExistById(ctx.WORD().getText(), ids)) return f;
        alarm = true;
        msg = name+": A função "+f+" não foi defenida\n";
        return "";
    }

    public String visitArg(CompilerParser.ArgContext ctx){
        int i = 2;
        String a = ctx.WORD(0).getText()+" "+ctx.WORD(1).getText();
        while(ctx.WORD(i) != null){
            a = a+","+ctx.WORD(i).getText()+" "+ctx.WORD(i+1).getText();
            i = i+2;
        }
        return a;
    }

    public String visitD(CompilerParser.DContext ctx){
        return escreverContexto(contexto)+ctx.WORD().getText()+ctx.op.getText()+";\n";
    }

    public String visitA(CompilerParser.AContext ctx){
        return escreverContexto(contexto)+ctx.op.getText()+ctx.WORD().getText()+";\n";
    } 

    public String visitDevolve(CompilerParser.DevolveContext ctx){
       if(ctx.operacao() == null){
          return escreverContexto(contexto)+"return ;";     
       }
       return escreverContexto(contexto)+"return "+visit(ctx.operacao())+";\n";
    }

    private Unidade verifyOp(CompilerParser.OperacaoContext ctx){
        if(ctx instanceof CompilerParser.OperandoContext){
            CompilerParser.OperandoContext ct = (CompilerParser.OperandoContext) ctx ;
            double valor;
            try{
                valor = Double.parseDouble(ct.v.getText());
                return d.getUni("numero");
            }
            catch(Exception e){
                Variavel v =  d.getVar(ct.v.getText());
                if(v == null){
                    return null;
                }
                return v.getUnidade();
            }
        } 
        if(ctx instanceof CompilerParser.NoargsContext){
            CompilerParser.NoargsContext ct = (CompilerParser.NoargsContext) ctx ;
            return d.getUni(fs.getReturn(ct.callNoArgFunction().WORD().getText(), new String[0]));
        } 
        // pode dar null
        if(ctx instanceof CompilerParser.MoreargsContext){
           CompilerParser.MoreargsContext ct = (CompilerParser.MoreargsContext) ctx ;
           String s = visit(ct.callArgsFunction()); 
           int f = s.indexOf("(");
           s = s.substring(f+1, s.length()-1);
           String[] ids = s.split(",");
           return d.getUni(fs.getReturnById(ct.callArgsFunction().WORD().getText(), ids));//pode dar null
        }

        if(ctx instanceof CompilerParser.MultiContext){
            CompilerParser.MultiContext ct = (CompilerParser.MultiContext) ctx ;
            Unidade u1 = verifyOp(ct.l1);
            Unidade u2 = verifyOp(ct.l2);
            if(u1 == null || u2 == null) return null;
            return d.unidadeOp(u1, u2, ct.op.getText());
        }

        if(ctx instanceof CompilerParser.SumContext){
            CompilerParser.SumContext ct = (CompilerParser.SumContext) ctx ;
            Unidade u1 = verifyOp(ct.l1);
            Unidade u2 = verifyOp(ct.l2);
            if(u1 == null || u2 == null) return null;
            return d.unidadeOp(u1, u2, ct.op.getText());
        }
        CompilerParser.ParentesesContext ct = (CompilerParser.ParentesesContext) ctx ;
        return verifyOp(ct.operacao());
    }

    private String escreverContexto(int contexto){
        String c = "";
        for(int i = 0; i<contexto; i++){
            c = c + "\t";
        }
        return c;
    }

    private boolean tratarErro(String erro){
        if(erro.equals("")) return true;
        alarm = true;
        msg = erro;
        return false;
    }

}