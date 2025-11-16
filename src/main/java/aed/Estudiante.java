package aed;

public class Estudiante {
    public int _id;
    public boolean _entrego;
    public int _cantRespuestasCorrectas;
    public int[] _examen;

    public Estudiante(int id, int longExamen) {
        _id = id;
        _entrego = false;
        _cantRespuestasCorrectas = 0;
        _examen = new int[longExamen];
        for(int i = 0; i < longExamen; i++){
            _examen[i] = -1;
        }
    }

    public Estudiante(Estudiante est) {
        _id = est._id;
        _entrego = est._entrego;
        _cantRespuestasCorrectas = est._cantRespuestasCorrectas;
        _examen = est._examen.clone();
    }

    public int[] getExamen() {
        return _examen;
    }

    public int getId() {
        return _id;
    }

    public int getRespuestasCorrectas() {
        return _cantRespuestasCorrectas;
    }

    public void cambiarExamen(int punto, int respuesta, int[] examenCanonico){
        _examen[punto] = respuesta; 
        if(examenCanonico[punto] == respuesta){
            _cantRespuestasCorrectas++;
        }
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
