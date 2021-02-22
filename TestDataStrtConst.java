public class TestDataStrtConst {
    public static void main(String[] args) {

        DataStrt ds = new DataStrt();
        System.out.println(ds.getVar("k"));
        ds.inserirUniPrim("tempo", "t", "double");
        System.out.println(ds.inserirConst("tempo", "andre", 2.0));
        System.out.println(ds.getVar("andre"));
        System.out.println(ds.unidadeOp(ds.getVar("andre").getUnidade(), ds.getVar("da").getUnidade(), "+"));
        System.out.println(ds.unidadeOp(ds.getVar("andre").getUnidade(), ds.getVar("da").getUnidade(), "*"));
        System.out.println(ds.verifyUni(ds.getVar("andre").getUnidade().getNome(), ds.getVar("da").getUnidade().getNome()));

    }
}