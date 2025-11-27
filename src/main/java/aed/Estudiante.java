package aed;

public class Estudiante {
    private int _id;
    private boolean _entrego;
    private int _cantRespuestasCorrectas;
    private int[] _examen;
    private boolean _copioDW;
    private boolean _copioVecinos;

    public Estudiante(int id, int longExamen) {
        _id = id;
        _entrego = false;
        _cantRespuestasCorrectas = 0;
        _examen = new int[longExamen];
        for(int i = 0; i < longExamen; i++){        // O(R)
            _examen[i] = -1;
        }
        _copioDW = false;
        _copioVecinos = false;
    }

    public Estudiante(Estudiante est) {
        _id = est._id;
        _entrego = est._entrego;
        _cantRespuestasCorrectas = est._cantRespuestasCorrectas;
        _examen = est._examen.clone();
        _copioDW = est._copioDW;
        _copioVecinos = est._copioVecinos;
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

    public void cambiarCopiarDW() {
        this._copioDW = !_copioDW;
    }

    public void cambiarCopiarVecino(){
        this._copioVecinos = true;
    }

    public void reiniciarExamen(){
        this._cantRespuestasCorrectas = 0;
    }

    public void entregarExamen(){
        _entrego = true;
        // compareTo, si entrego va abajo de todo 
    }

    public void cambiarExamen(int punto, int respuesta, int[] examenCanonico){
        _examen[punto] = respuesta; 
        if(examenCanonico[punto] == respuesta){
            _cantRespuestasCorrectas++;
        }
    }

    public int compareTo(Estudiante otro) {
        int res = 0;
        if(this._entrego == true && otro._entrego == false){
            res = 1;
        } else if(this._entrego == false && otro._entrego == true){
            res = -1;
        } else if(this._entrego == false ) {
            if ((this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas) != 0) {
                        res = (this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas);
                    } else {
                        res = this._id - otro._id;
                    }
        } else{
            // si ambos entregaron sortea por copiones antes de ver notas e id
            if(this._copioVecinos == true && otro._copioVecinos == false){
                res = 1;
            } else if(this._copioVecinos == false && otro._copioVecinos == true){
                res = -1;
            } else{
                if(this._copioDW == true &&  otro._copioDW == false){
                    res = 1; // donde estoy baja
                } else if(this._copioDW == false &&  otro._copioDW == true){
                    res = -1; // donde estoy sube (en un minheap)
                } else {
                    if ((this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas) != 0) {
                        res = (this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas);
                    } else {
                        res = this._id - otro._id;
                    }
                }
        }
        }
        // entregar -> copio de vecinos -> copio de la DW -> criterio de nota -> ID orden de desempate 
        return res ; 

        // this - otro 
            // negativo si this < otro
            // Desempata por ID gana el de mayor ID
            // positivo si this > otro
    }
}