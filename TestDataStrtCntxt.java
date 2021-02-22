public class TestDataStrtCntxt {
    public static void main(String[] args) {

        /*
        Algo deste género
        Não esquecer que no fim de um contexto tem de ser invocar a função ds.destroyContext()
        if () {
            inserirVar(x, 1);

            if () {
                inserirVar(x, 2);
                inserirVar(y, 2);
                get(y);
            } 
            if () {
                inserirVar(y, 2);
                get(y);
            }
            get(x);
            get(y);
        }

        */

        DataStrt ds = new DataStrt();
        ds.inserirUniPrim("distancia", "m", "int");                                                     // distancia
        ds.inserirUniPrim("tempo", "s", "int");
        
        ds.insertContext(); // 1
        ds.inserirVar("distancia", "d1");
        System.out.println(ds.getVar("d1"));

        ds.destroyContext();
        System.out.println(ds.getVar("d1"));
        System.out.println(ds.inserirVar("distancia", "d1"));
        System.out.println(ds.getVar("d1"));

        System.out.println(ds.getVar("d1"));

        ds.insertContext(); // 2
        ds.insertContext(); // 3
        System.out.println(ds.inserirVar("tempo", "t1"));
        System.out.println(ds.getVar("d1"));
        System.out.println(ds.getVar("t1"));

        ds.destroyContext();
        System.out.println(ds.getVar("t1"));

    }
}