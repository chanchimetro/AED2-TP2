package aed;


public class MinHeap {
    private Estudiante[] _lista;
    private int _size;

    public class HandleMinHeap {
        private int index;
        private Estudiante _est;

        public HandleMinHeap(int i) {
            index = i;
            _est = _lista[index];
        }

        public Estudiante getEstudiante() {
            return _est;
        }

        public void seCopio() {
            _est.cambiarCopiarVecino();
        }

        public void actualizarHeap() {
            // Buscar la posición actual del estudiante en el heap
            int posicionActual = -1;
            int i = 0;
            while (i < _size && posicionActual == -1) {
                if (_lista[i] == _est) {
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
        _size = 0;
        _lista = new Estudiante[_size];
    }

    public Estudiante devolverPrimerEstudiante(){
        return _lista[0];
    }

    public int encolar(Estudiante est) {
        Estudiante[] aux = new Estudiante[_size + 1];
        
        for (int i = 0; i < _size; i ++) {
            aux[i] = _lista[i];
        }
        aux[_size] = est;
        
        _lista = aux;
        int ret = subir(_size);
        
        _size += 1;

        return ret;
    }

    private int subir(int index) {
        while (index != 0 && _lista[padre(index)].compareTo(_lista[index]) > 0) {
            Estudiante aux = _lista[padre(index)];

            _lista[padre(index)] = _lista[index];
            _lista[index] = aux;
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

    public Estudiante desencolar() {
        Estudiante ret = _lista[0];
        
        _lista[0] = _lista[_size - 1];
        
        Estudiante[] aux = new Estudiante[_size - 1];
        for (int i = 0; i < _size-1; i ++) {
            aux[i] = _lista[i];
        }

        _lista = aux;
        _size -= 1;
        
        if (_size > 0) {
            bajar(0);
        }
        
        return ret;
    }

    public Estudiante[] conseguirKEstudiantes(int k) {
        Estudiante[] res = new Estudiante[k];

        for (int x = 0; x < k; x++) {
            res[x] = this.desencolar();
        }
        for (int x = 0; x < k; x++) {
            this.encolar(res[x]);
        }

        return res;
    }


    private int bajar(int index) {
        int ret = index;
        while(!hoja(index) && (_lista[index].compareTo(_lista[hijoIzq(index)]) > 0 || 
               (hijoDer(index) < _size && _lista[index].compareTo(_lista[hijoDer(index)]) > 0))) {
            int menor;
            if(hijoDer(index) >= _size || _lista[hijoIzq(index)].compareTo(_lista[hijoDer(index)]) < 0) {
                menor = hijoIzq(index);
            } else {
                menor = hijoDer(index);
            }

            Estudiante aux = _lista[index];
            _lista[index] = _lista[menor];
            _lista[menor] = aux;
            index = menor;
            ret = menor;
        }
        
        return ret;
    }

    private boolean hoja(int index) {
        return hijoIzq(index) >= _size;
    }

    private int hijoIzq(int index) {
        return 2 * index + 1;
    }

    private int hijoDer(int index) {
        return 2 * index + 2;
    }

    public boolean esVacio() {
        return _size == 0;
    }

    public int size() {
        return _size;
    }
}