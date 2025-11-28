package aed;

import java.util.ArrayList;

public class MinHeap<T extends Comparable<T>> {
    private ArrayList<T> _lista;
    //private int _size;

    public class HandleMinHeap {
        private int index;
        private T _elem;

        public HandleMinHeap(int i) {
            index = i;
            _elem = _lista.get(index);
        }

        public T getElemento() {
            return _elem;
        }

        public void actualizarHeap() {
            // Buscar la posición actual del estudiante en el heap
            int posicionActual = -1;
            int i = 0;
            while (i < _lista.size() && posicionActual == -1) {
                if (_lista.get(i) == _elem) {
                    posicionActual = i;
                }
                i++;
            }
            
            if (posicionActual != -1) {
                index = posicionActual;
                index = subir(index);
                index = bajar(index);
            }
        }
    }

    public MinHeap() {
        _lista = new ArrayList<T>();
    }

    public T devolverPrimerEstudiante(){
        return _lista.get(0);
    }

    public int encolar(T elem) {
        _lista.add(elem);
        int ret = subir(_lista.size()-1);

        return ret;
    }

    public void encolarRapido(T elem) {
        _lista.add(elem); 
    }

    private int subir(int index) {
        while (index != 0 && _lista.get(index) != null && _lista.get(padre(index)) != null && _lista.get(padre(index)).compareTo(_lista.get(index)) > 0) {
            T aux = _lista.get(padre(index));

            _lista.set(padre(index), _lista.get(index));
            _lista.set(index, aux);
            index = padre(index);
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

    public T desencolar() {
        T ret = _lista.get(0);
        
        _lista.set(0, _lista.get(_lista.size() - 1));
        
        _lista.remove(_lista.size() - 1);

        
        if (_lista.size() > 0) {
            bajar(0);
        }
        
        return ret;
    }

    public ArrayList<T> conseguirKEstudiantes(int k) {
        ArrayList<T> res = new ArrayList<T>();

        for (int x = 0; x < k; x++) {
            res.add(this.desencolar());
        }
        for (int x = 0; x < k; x++) {
            this.encolar(res.get(x));
        }

        return res;
    }


    private int bajar(int index) {
       int ret = index;
        while(!hoja(index) && (_lista.get(index).compareTo(_lista.get(hijoIzq(index))) > 0 || 
               (hijoDer(index) < _lista.size() && _lista.get(index).compareTo(_lista.get(hijoDer(index))) > 0))) {
            int menor;
            if(hijoDer(index) >= _lista.size() || _lista.get(hijoIzq(index)).compareTo(_lista.get(hijoDer(index))) < 0) {
                menor = hijoIzq(index);
            } else {
                menor = hijoDer(index);
            }

            T aux = _lista.get(index);
            _lista.set(index, _lista.get(menor));
            _lista.set(menor, aux);
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