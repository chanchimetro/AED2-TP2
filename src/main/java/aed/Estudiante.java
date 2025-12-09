package aed;

public class Estudiante implements Comparable<Estudiante> {
    private int _id;
    private boolean _entrego;
    private int _cantRespuestasCorrectas;
    private int[] _examen;
    private boolean _copioDW;
    private boolean _sospechosoCopiarse;

    public Estudiante(int id, int longExamen) {
        _id = id;
        _entrego = false;
        _cantRespuestasCorrectas = 0;
        _examen = new int[longExamen];
        for(int i = 0; i < longExamen; i++){        // O(R)
            _examen[i] = -1;
        }
        _copioDW = false;
        _sospechosoCopiarse = false;
    }

    public Estudiante(Estudiante est) {
        _id = est._id;
        _entrego = est._entrego;
        _cantRespuestasCorrectas = est._cantRespuestasCorrectas;
        _examen = est._examen.clone();
        _copioDW = est._copioDW;
        _sospechosoCopiarse = est._sospechosoCopiarse;
    }

    public int[] getExamen() {
        return _examen;
    }

    public boolean entrego() {
        return _entrego;
    }

    public int getId() {
        return _id;
    }

    public int getRespuestasCorrectas() {
        return _cantRespuestasCorrectas;
    }

    public void reiniciarExamen(){
        this._cantRespuestasCorrectas = 0;
    }

    public void cambiarCopiarDW() {
        this._copioDW = !_copioDW;
        // cambia el compareTo
    }

    public void cambiarSospechosoCopiarse(){
        this._sospechosoCopiarse = true;
        // cambia el compareTo
    }

    public void entregarExamen(){
        _entrego = true;
        /*  
            compareTo si entrego va abajo de todo, 
            para evitar volver a desencolarlo en metodos que modifiquen el examen
        */
    }

    public void cambiarExamen(int punto, int respuesta, int[] examenCanonico){
        _examen[punto] = respuesta; 
        if(examenCanonico[punto] == respuesta){
            _cantRespuestasCorrectas++;
        }
    }

    @Override
    public int compareTo(Estudiante otro) {
        int res = 0;
        // distintas las condiciones de entrega -> sabemos que uno es distinto del otro. 

        if(this._entrego == true && otro._entrego == false){
            res = 1;
        } else if(this._entrego == false && otro._entrego == true){
            res = -1;
        } else if( this._entrego == false ) { 
            // si ambos NO entregaron ordeno por nota decreciente y desempato por ID 
            if (this._cantRespuestasCorrectas - otro._cantRespuestasCorrectas != 0) {
                res = (this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas);
            } else {
                res = this._id - otro._id;
            }
        } else {
            // si ambos SI entregaron sortea por copiones antes de ver notas e id
            if(this._sospechosoCopiarse == true && otro._sospechosoCopiarse == false){
                res = 1;
            } else if(this._sospechosoCopiarse == false && otro._sospechosoCopiarse == true){
                res = -1;
            } else{
                // borrar comparación de copio DW -> el insertar debería devolver un handle
                // edr debería guardarse en posicion estudiante ese nuevo handle 
                if(this._copioDW == true &&  otro._copioDW == false){
                    res = 1; // donde estoy baja
                } else if(this._copioDW == false &&  otro._copioDW == true){
                    res = -1; // donde estoy sube (en un minheap)
                } else {
                    if ((this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas) != 0) {
                        res = (this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas);
                    } else {
                        res = this._id - otro._id;
                        // negativo -> este id < otro id
                        // positivo -> este id > otro id 
                                    // como los ids son únicos nunca van a ser iguales 
                    }
                }
            }
        }
        return res ; 
        /* 
        ordenamos la lógica en torno a la nota 

            res > 0 --> "baja" en el heap 

            Orden de prioridad de desempate 
                entregar --> copio de vecino --> copio de la DW --> criterio de nota decreciente --> ID orden de desempate gana el mayor ID
                
            res < 0 --> "sube" en el heap
        */ 
    }
}