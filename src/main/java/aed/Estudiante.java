package aed;

public class Estudiante implements Comparable<Estudiante> {
    private int _id, _cantRespuestasCorrectas;
    private boolean _entrego, _sospechosoCopiarse;
    private int[] _examen;

    public Estudiante(int id, int longExamen) {
        _id = id;
        _entrego = false;
        _cantRespuestasCorrectas = 0;
        _examen = new int[longExamen];
        for(int i = 0; i < longExamen; i++){        // O(R)
            _examen[i] = -1;
        }
        _sospechosoCopiarse = false;
    }

    public Estudiante(Estudiante est) {
        _id = est._id;
        _entrego = est._entrego;
        _cantRespuestasCorrectas = est._cantRespuestasCorrectas;
        _examen = est._examen.clone();
        _sospechosoCopiarse = est._sospechosoCopiarse;
    }

    public int[] getExamen() {
        return _examen.clone();
        /* rompe el encapsulamiento si no poníamos clone */
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

    public void reiniciarCantRespuestasCorrectas(){
        this._cantRespuestasCorrectas = 0;
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
        // Boolean.compare(a,b): si a (False) y b (True) devuelve < 0, si a (True) y b (False) devulve > 0

        // primera prioridad si entrego
        if(this._entrego != otro._entrego){
            res = Boolean.compare(this._entrego, otro._entrego);
        } else if( !this._entrego ) {
            res = comparar_por_nota_e_id(otro);

        // si ambos ya entregaron primero chequea si se copió y luego compara por nota y después por id
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
        // diferencia notas 
        // this tiene menor nota -> sube en el heap -> return < 0
        int res = (this._cantRespuestasCorrectas) - (otro._cantRespuestasCorrectas);
        if(res == 0){ // si las notas son iguales desempato por id
        res = this._id - otro._id;
        }
        return res;
    }

}