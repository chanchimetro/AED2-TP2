package aed;

import java.util.ArrayList;

public class MinHeap<T extends Comparable<T>> {
    private ArrayList<HandleMinHeap<T>> _lista; 
    
    public  class HandleMinHeap<U extends Comparable<U>> implements Handle<U> {
        private int _posicion;
        private U _elem;

        private HandleMinHeap(int i, U elem) {// O(1)
            _posicion = i;
            _elem = elem;
        }

        public U valor() {// O(1)
            return _elem;
        }

        public void actualizar_valor() {// O(log N)
            _posicion = actualizar_valor_en_heap(_posicion);
        }

        private int posicion() {// O(1)
            return _posicion;
        }

        private void setPosicion(int y){ // O(1)
            this._posicion = y;
        }

    }

    private int actualizar_valor_en_heap(int index){ //O(log N)
        index = subir(index); // O(log N)
        index = bajar(index); // O(log N)
        return index;
        /* lo acomoda en el heap y devuelve el indice */ 
    }

    public MinHeap() {// O(1)
        _lista = new ArrayList<HandleMinHeap<T>>();
    }

    public HandleMinHeap<T> minimo(){//O(1)
        return _lista.get(0);
    }

    public HandleMinHeap<T> encolar(T elem) {//O(log N)
        HandleMinHeap<T> handle = new HandleMinHeap(_lista.size(), elem);
        _lista.add(handle);

        int ret = subir(_lista.size()-1);
        handle.setPosicion(ret);

        return handle;
    }

    public HandleMinHeap<T> encolarRapido(int i, T elem) { //O(1)
        HandleMinHeap<T> handle = new HandleMinHeap(i, elem);
        _lista.add(handle); 
        return handle;
    }
    /*  
    genera un handle para guardar en el arreglo 
    es lo que devuelve en el EDR "para afuera"
    */

    public T desencolar() {//O(log N)
        T ret = _lista.get(0).valor();

        /* modifico el valor tanto en el heap como en el handle  */
        HandleMinHeap<T> ultimoHandle =  _lista.get(_lista.size() - 1);
        _lista.set(0, ultimoHandle);
        ultimoHandle.setPosicion(0);
        
        _lista.remove(_lista.size() - 1);

        
        if (_lista.size() > 0) {
            bajar(0);
        }
        
        return ret;
    }

    private int subir(int index) {//O(log N)
        while (  index != 0 && _lista.get(index) != null &&
                (_lista.get(padre(index)).valor()).compareTo(_lista.get(index).valor()) > 0
            ) {
            int indexPadre = _lista.get(padre(index)).posicion();
        
            intercambiar_posiciones(index, indexPadre);

            index = indexPadre;
        }

        return index;
    }

    private int bajar(int index) { //O(log N)
       int ret = index;
        while(!hoja(index) 
            && (_lista.get(index).valor().compareTo(_lista.get(hijoIzq(index)).valor()) > 0 || 
               (hijoDer(index) < _lista.size() && _lista.get(index).valor().compareTo(_lista.get(hijoDer(index)).valor()) > 0))) {
            int menor;
            if(hijoDer(index) >= _lista.size() || _lista.get(hijoIzq(index)).valor().compareTo(_lista.get(hijoDer(index)).valor()) < 0) {
                menor = hijoIzq(index);
            } else {
                menor = hijoDer(index);
            }

            intercambiar_posiciones(index, menor);


            index = menor;
            ret = menor;
        }
        
        return ret;
    }

    private void intercambiar_posiciones(int x, int y){//O(1)
        _lista.get(x).setPosicion(y);
        _lista.get(y).setPosicion(x);
        HandleMinHeap<T> aux = _lista.get(x);
        _lista.set(x, _lista.get(y));
        _lista.set(y, aux);
    }

    private int padre(int index) {//O(1)
        int ret;

        if(index % 2 == 0) {
            ret = (index - 2) / 2;
        } else {
            ret = (index - 1) / 2;
        }

        return ret;
    }

    private boolean hoja(int index) {
        return hijoIzq(index) >= _lista.size();
    }

    private int hijoIzq(int index) {
        return 2 * index + 1;
    }

    private int hijoDer(int index) {
        return 2 * index + 2;
    }

    private int size() {
        return _lista.size();
    }
}