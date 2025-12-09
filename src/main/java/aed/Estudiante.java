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

    public boolean sospechosoCopiarse(){
        return this._sospechosoCopiarse;
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

        if(this._entrego != otro._entrego){
            res = Boolean.compare(this._entrego, otro._entrego);
            // this entrego -> 1 
        } else if( !this._entrego ) {
            res = comparar_por_nota_e_id(otro);
        } else if(this._sospechosoCopiarse != otro._sospechosoCopiarse){
            res = Boolean.compare(this._sospechosoCopiarse, otro._sospechosoCopiarse);    
        } else {
            res = comparar_por_nota_e_id(otro); 
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

    private int comparar_por_nota_e_id(Estudiante otro){
        // differencia notas 
        // this tiene menor nota -> sube en el heap -> return < 0
        int res = (this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas);
        if(res == 0){ // si las notas son iguales desempato por id
        res = this._id - otro._id;
        }
        return res;
    }

}