public class Content{

    private String r;
    private String[] arg_types;
    private String[] ids;
    private String body = "";

    public Content(String r, String[] arg_types, String[] ids){
        this.ids = ids;
        this.r = r;
        this.arg_types = arg_types;
    }

    public boolean checkArgs(String[] t){
        if(t.length == arg_types.length){
            for(int i = 0; i<t.length; i++){
                if(!t[i].equals(arg_types[i]) || t[i].equals("None")) return false;
            }
            return true;
        }
        return false;
    }

    public void updateBody(String body){
        this.body = this.body+body;
    }

    public String getBody(){ return body;}
    public String getReturn(){ return r;}
    public String[] getTypes(){ return arg_types;}
    public String[] getIds(){ return ids;}
}