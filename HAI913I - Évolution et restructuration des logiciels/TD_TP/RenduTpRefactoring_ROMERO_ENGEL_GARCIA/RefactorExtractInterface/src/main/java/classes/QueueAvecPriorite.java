package classes;

import interfaces.SubListe1;

class QueueAvecPriorite implements SubListe1 {
    @Override
    public boolean add(Object o) {return true;}
    @Override
    public boolean isEmpty() {return true;}
    public Object peek() {return null;}
    public Object poll() {return null;}
    public Object comparator() {return null;}
    private void secretQAP(){}
}