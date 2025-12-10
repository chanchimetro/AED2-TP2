package aed;

import java.util.ArrayList;

public class MinHeap<T extends Comparable<T>> {
    private ArrayList<HandleMinHeap<T>> _lista; // arreglo de heaphandles<T>

    // interfaz con metodos para handles -> devolver valor y actualizar valor 
        // interfaz independiente handle min heap implementa la interfaz con los métodos
        // Ahora todos los elementos de la lista que representa al arraylist son handles 
    
    public  class HandleMinHeap<U extends Comparable<U>> implements Handle<U> {
        private int _posicion;
        private U _elem;

        // agrego el elemento que quiero que sea porque mi handle debe tener todo 
        private HandleMinHeap(int i, U elem) {
            _posicion = i;
            _elem = elem;
        }

        public U valor() {
            return _elem;
        }

        // chequear cuando funcione si hace falta (creo que no)
        public U eliminar() {
            _lista.remove(_posicion);
            return _elem;
        }

        public void actualizar_valor() {
            _posicion = actualizar_valor_en_heap(_posicion);
        }
        // llamar a un método privado del heap 
        // posición del heap o de su propio handle

        public int posicion() {
            return _posicion;
        }

        private void settear_posicion(int y){
            this._posicion = y;
        }

    }

    private int actualizar_valor_en_heap(int index){
        index = subir(index);
        index = bajar(index);
        // lo mueve en la heap y devuelve el indice 
        return index;
    }

    public MinHeap() {
        _lista = new ArrayList<HandleMinHeap<T>>();
    }

    // public T devolverPrimerEstudiante(){
    //     return _lista.get(0).valor();
    // }

    public int encolar(T elem) {
        HandleMinHeap<T> handle = new HandleMinHeap(_lista.size(), elem);
        _lista.add(handle);

        int ret = subir(_lista.size()-1);
        handle.settear_posicion(ret);

        return ret;
    }

    public HandleMinHeap<T> encolarRapido(int i, T elem) {
        HandleMinHeap<T> handle = new HandleMinHeap(i, elem);
        _lista.add(handle); 
        return handle;
    }
    // genera un handle para guardar en el arreglo 
    // es lo que devuelve en el EDR
    // ese handle lo devuelve para afuera

    private int subir(int index) {
        while (
                index != 0 && _lista.get(index) != null &&
                (_lista.get(padre(index)).valor()).compareTo(_lista.get(index).valor()) > 0
            ) {
            int indexPadre = _lista.get(padre(index)).posicion();
            
            intercambiar_posiciones(index, indexPadre);

            index = indexPadre;
        }

        return index;
    }

    private int padre(int index) {
        int ret;

        if(index % 2 == 0) {
            ret = (index - 2) / 2;
        } else {
            ret = (index - 1) / 2;
        }

        return ret;
    }

    private void intercambiar_posiciones(int x, int y){
        _lista.get(x).settear_posicion(y);
        _lista.get(y).settear_posicion(x);
        HandleMinHeap<T> aux = _lista.get(x);
        _lista.set(x, _lista.get(y));
        _lista.set(y, aux);
    }

    // que pasa con los alumnos que estan entre medio de los que intercambié 

    public T desencolar() {
        T ret = _lista.get(0).valor();

        // modifico el valor tanto en el heap como en el handle 
        HandleMinHeap<T> ultimoHandle =  _lista.get(_lista.size() - 1);
        _lista.set(0, ultimoHandle);
        ultimoHandle.settear_posicion(0);
        
        //_lista.set(0, _lista.get(_lista.size() - 1));
        
        _lista.remove(_lista.size() - 1);

        
        if (_lista.size() > 0) {
            bajar(0);
        }
        
        return ret;
    }

    public ArrayList<HandleMinHeap<T>> conseguirKElementos(int k) {
        ArrayList<HandleMinHeap<T>> primerosHandles = new ArrayList<HandleMinHeap<T>>();

        for (int x = 0; x < k; x++) {
            primerosHandles.add(_lista.get(0));
            desencolar();
        }
        for (int x = 0 ; x < k; x++) {
            _lista.add(primerosHandles.get(x));
            int posFinal = subir(_lista.size()-1);
            primerosHandles.get(x).settear_posicion(posFinal);
        }
        
        return primerosHandles;
    }




    private int bajar(int index) {
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

    private boolean hoja(int index) {
        return hijoIzq(index) >= _lista.size();
    }

    private int hijoIzq(int index) {
        return 2 * index + 1;
    }

    private int hijoDer(int index) {
        return 2 * index + 2;
    }

    public boolean esVacio() {
        return _lista.size() == 0;
    }

    public int size() {
        return _lista.size();
    }
}