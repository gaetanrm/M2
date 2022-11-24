package classes;

import interfaces.SubListe2;

class ListeTableau implements SubListe2 {
    @Override
    public boolean add(Object o) {return true;}
    @Override
    public boolean isEmpty() {return true;}
    public Object get(int i) {return null;}
    private void secretLT(){}
    public static void staticLT() {}
    int nbLT;
}