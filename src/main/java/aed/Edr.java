package aed;
import java.util.ArrayList;
import java.util.List;

import aed.MinHeap.HandleMinHeap;

public class Edr {

    private MinHeap _heapEstudiantes;
    private int _ladoAula;
    private HandleMinHeap[] _handlesEstudiantes;
    private int[] _examenCanonico;
    private HandleMinHeap[] _handlesSospechosos;

    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _handlesEstudiantes = new HandleMinHeap[Cant_estudiantes];
        _handlesEstudiantes = new HandleMinHeap[0];
        _heapEstudiantes = new MinHeap();
        _ladoAula = LadoAula;
        _examenCanonico = ExamenCanonico;
        
        for (int i = 0; i < Cant_estudiantes; i ++) {
            Estudiante est = new Estudiante(i, ExamenCanonico.length);
            int index = _heapEstudiantes.encolar(est);
            _handlesEstudiantes[i] = _heapEstudiantes.new HandleMinHeap(index);
        }
    }

    private static int[] obtenerPosicion(int id, int n) {
        int columnasUsadas = (n+1) / 2;
        int fila = id / columnasUsadas;
        int columna = (id % columnasUsadas) * 2;

        return new int[]{fila, columna};
    }

    private static int obtenerId(int fila, int colReal, int n) {
        int columnasUsadas = (n + 1) / 2;
        int col = colReal / 2;
        return fila * columnasUsadas + col;
    }

    public static List<Integer> conseguirVecinos(int fila, int colReal, int n) {
        List<Integer> res = new ArrayList<>();

        // --- Celda delantera (arriba) ---
        if (fila > 0) {
            res.add(obtenerId(fila - 1, colReal, n));
        }

        // --- Izquierda ---
        if (colReal - 2 >= 0) {
            res.add(obtenerId(fila, colReal - 2, n));
        }

        //derecha
        if (colReal + 2 < n) {
            res.add(obtenerId(fila, colReal + 2, n));
        }

        return res;
    }



//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        throw new UnsupportedOperationException("Sin implementar");
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        int[] pos = obtenerPosicion(estudiante, _ladoAula);
        int[] examenEstudiante = _handlesEstudiantes[estudiante].getEstudiante().getExamen();

        List<Integer> vecinos = conseguirVecinos(pos[0], pos[1], _ladoAula);

        int cantRespuestas = 0;
        // primer componente es el indice de mi respuesta y el segundo es la respuesta 
        int[] primerRespuesta = new int[2];
        int idVecino = 0; 

        for(int i = 0; i < vecinos.size(); i++) {
            Estudiante vecino = _handlesEstudiantes[vecinos.get(i)].getEstudiante();
            int[] examenVecino = vecino.getExamen();
            int cantRespuestasAux = 0;
            int[] primerRespuestaAux = new int[2];

            for(int x = 0; x < examenVecino.length; x++) {
                if (examenEstudiante[x] == -1 && examenVecino[x] != -1){
                    if(cantRespuestasAux == 0){
                        primerRespuestaAux[0] = x;
                        primerRespuestaAux[1] = examenVecino[x];
                    }
                    cantRespuestasAux++;
                }
            }

            if( cantRespuestasAux > cantRespuestas || (cantRespuestas == cantRespuestasAux && vecino.getId() > idVecino)){
                cantRespuestas = cantRespuestasAux;
                primerRespuesta = primerRespuestaAux;
                idVecino = vecino.getId();
            }
        }
        resolver(estudiante, primerRespuesta[0], primerRespuesta[1]);
    }


//-----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        _handlesEstudiantes[estudiante].getEstudiante().cambiarExamen(NroEjercicio, res, _examenCanonico);
        _handlesEstudiantes[estudiante].actualizarHeap();
    }

//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        throw new UnsupportedOperationException("Sin implementar");
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        // chequear que todos hayan terminado todos todos
        // hacer una lista de listas con las respuestas de cada estudiante examenes
        // por respuesta comparar entre todos los estudiantes 
        int[int[]] examenesResueltos = new int[new int[_handlesEstudiantes.length]];
        for(int i = 0; i < _handlesEstudiantes.length; i++){
            examenesResueltos += _handlesEstudiantes[i].getEstudiante().getExamen();
        }
        return null;
    }
}
