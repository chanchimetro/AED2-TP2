package aed;

public class Estudiante {
    private int _id;
    private boolean _entrego;
    private int _cantRespuestasCorrectas;

    public Estudiante(int id) {
        _id = id;
        _entrego = false;
        _cantRespuestasCorrectas = 0;
    }

    public Estudiante(Estudiante est) {
        _id = est._id;
        _entrego = est._entrego;
        _cantRespuestasCorrectas = est._cantRespuestasCorrectas;
    }

    public int compareTo(Estudiante otro) {
        int res = 0;
        if ((this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas) != 0) {
            res = (this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas);
        } else {
            res = otro._id - this._id ;
        }
        return res ; 
        // this - otro 
            // negativo si this < otro
            // Desempata por ID gana el de mayor ID
            // positivo si this > otro
    }
}
