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

        public void actualizarHeap() {
            subir(index);
            bajar(index);
        }

    }

    public MinHeap() {
        _size = 0;
        _lista = new Estudiante[_size];
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
            Estudiante aux = new Estudiante(_lista[padre(index)]);

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
        Estudiante ret = new Estudiante(_lista[0]);
        
        _lista[0] = _lista[_size - 1];
        
        Estudiante[] aux = new Estudiante[_size - 1];
        for (int i = 0; i < _size-1; i ++) {
            aux[i] = _lista[i];
        }

        _lista = aux;
        _size -= 1;
        
        bajar(0);
        
        return ret;
    }

    private void bajar(int index) {
        while(!hoja(index) && (_lista[index].compareTo(_lista[hijoIzq(index)]) > 0 || _lista[index].compareTo(_lista[hijoDer(index)]) > 0)) {
            int menor;
            if(_lista[hijoIzq(index)].compareTo(_lista[hijoDer(index)]) < 0) {
                menor = hijoIzq(index);
            } else {
                menor = hijoDer(index);
            }

            Estudiante aux = new Estudiante(_lista[index]);
            _lista[index] = _lista[menor];
            _lista[menor] = aux;
        }
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
