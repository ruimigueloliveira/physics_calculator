import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.Iterator;

public class FuncStrt{

    private HashMap<String, List<Content>> h;
    private DataStrt d;

    public FuncStrt(DataStrt data){
        h = new HashMap<String, List<Content>>();
        d = data;
    }

    public void addFunc(String n, String r, String[] arg_types, String[]arg_id ){
       Content c = new Content(r, arg_types, arg_id);
       if(h.get(n) != null){
           h.get(n).add(c);
       }
       else{
           LinkedList<Content> l = new LinkedList<>();
           l.add(c);
           h.put(n, l);
       }
    }
    public boolean updateFunc(String n, String[] arg_types, String body){
        List<Content> l = h.get(n);
        if(l == null) return false;

        Iterator<Content> it = l.iterator();
        Content c;
        while(it.hasNext()){
            c = it.next();
            if(c.checkArgs(arg_types)){
                c.updateBody(body);
                return true;
            }
        }
        return false;
    }

    public boolean funcExist(String name, String[] types){
        List<Content> l = h.get(name);
        if(l == null) return false;

        for(Content c : l){
            if(c.checkArgs(types)) return true;
        }
        return false;
    }

    public boolean funcExistById(String name, String[] ids){
        for(int i = 0; i<ids.length; i++){
            ids[i] = d.getVar(ids[i]).getUnidade().getNome();
        }
        return funcExist(name, ids);
    }

    public String getReturn(String name, String[] types){
        List<Content> l = h.get(name);
        if(l == null) return null;

        for(Content c : l){
            if(c.checkArgs(types)) return c.getReturn(); 
        }
        return null;
    }

    public String getReturnById(String name, String[] ids){
        for(int i = 0; i<ids.length; i++){
           Variavel v = d.getVar(ids[i]);
           if(v == null){
               ids[i] = "None";
           }
           else{
               ids[i] = v.getUnidade().getNome();
           }
       }
       return getReturn(name, ids);
    }

    public boolean nameExists(String n){
        return h.containsKey(n);
    }

    public String[] names(){
        return h.keySet().toArray(new String[0]);
    }

    public List<Content> getFuncList(String n){
        return h.get(n);
    }

}