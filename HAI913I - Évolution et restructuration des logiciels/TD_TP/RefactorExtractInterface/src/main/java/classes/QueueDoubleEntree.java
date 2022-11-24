package classes;

import interfaces.SubListe1;

class QueueDoubleEntree implements SubListe1 {
    @Override
    public boolean add(Object o) {return true;}
    @Override
    public boolean isEmpty() {return true;}
    public Object peek() {return null;}
    public Object poll() {return null;}
    private void secretQDE(){}
}
