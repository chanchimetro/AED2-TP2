package aed;


public class MinHeap {
    private Estudiante[] _lista;
    private int _size;

    public MinHeap() {
        _size = 0;
        _lista = new Estudiante[_size];
    }

    public void encolar(Estudiante est) {
        Estudiante[] aux = new Estudiante[_size + 1];
        
        for (int i = 0; i < _size; i ++) {
            aux[i] = _lista[i];
        }
        aux[_size] = est;
        
        subir(_size);
        
        _size += 1;
        _lista = aux;
    }

    private void subir(int index) {
        while (index != 0 && _lista[padre(index)].compareTo(_lista[index]) > 0) {
            Estudiante aux = _lista[padre(index)]; // ALIASING! ARREGLARRRRRR

            _lista[padre(index)] = _lista[index];
            _lista[index] = aux;
        }
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
        Estudiante ret = _lista[0]; //ALIASING!!

        
    }
}
