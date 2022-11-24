package classes;

import interfaces.SubListe1;
import interfaces.SubListe2;

class ListeChainee implements SubListe1, SubListe2 {
    @Override
    public boolean add(Object o) {return true;}
    @Override
    public boolean isEmpty() {return true;}
    public Object get(int i) {return null;}
    public Object peek() {return null;}
    public Object poll() {return null;}
    private void secretLC(){}
}