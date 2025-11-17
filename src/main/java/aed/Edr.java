package aed;
import java.util.ArrayList;
import java.util.List;

import aed.MinHeap.HandleMinHeap;
import aed.NotaFinal;

public class Edr {

    private MinHeap _heapEstudiantes;
    private int _ladoAula;
    private HandleMinHeap[] _handlesEstudiantes;
    private int[] _examenCanonico;
    private ArrayList<HandleMinHeap> _handlesSospechosos;

    
    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _handlesEstudiantes = new HandleMinHeap[Cant_estudiantes];
        _handlesSospechosos = new ArrayList<HandleMinHeap>();
        _heapEstudiantes = new MinHeap();
        _ladoAula = LadoAula;
        _examenCanonico = ExamenCanonico;
        
        for (int i = 0; i < Cant_estudiantes; i ++) {                           // O(E)
            Estudiante est = new Estudiante(i, ExamenCanonico.length);          // O(R)
            int index = _heapEstudiantes.encolar(est);
            _handlesEstudiantes[i] = _heapEstudiantes.new HandleMinHeap(index);
        }
    }
    // Complejidad -> O(E*R)

    private static int[] obtenerPosicion(int id, int n) {
        int columnasUsadas = (n+1) / 2;
        int fila = id / columnasUsadas;
        int columna = (id % columnasUsadas) * 2;

        return new int[]{fila, columna};
    }
    // Complejidad -> O(1)

    private static int obtenerId(int fila, int colReal, int n) {
        int columnasUsadas = (n + 1) / 2;
        int col = colReal / 2;
        return fila * columnasUsadas + col;
    }
    // Complejidad -> O(1)


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
    // Complejidad -> O(1)



//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        int cantPreguntas = _examenCanonico.length;
        double[] resultado = new double[_handlesEstudiantes.length];
        for (int i = 0; i < _handlesEstudiantes.length; i++) {                                  // O(E)
            Estudiante estudiante = _handlesEstudiantes[i].getEstudiante();
            resultado[i] = 100 * ((double) estudiante.getRespuestasCorrectas() / cantPreguntas);
        }
        return resultado;
       
    }
    // Complejidad -> O(E)
    
//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        int[] pos = obtenerPosicion(estudiante, _ladoAula);
        int[] examenEstudiante = _handlesEstudiantes[estudiante].getEstudiante().getExamen();

        List<Integer> vecinos = conseguirVecinos(pos[0], pos[1], _ladoAula);

        int cantRespuestas = 0;
        // primer componente es el indice de mi respuesta y el segundo es la respuesta 
        int[] primerRespuesta = new int[2];
        int idVecino = 0; 

        for(int i = 0; i < vecinos.size(); i++) {                                       // O(v) -> como mucho 3 
            Estudiante vecino = _handlesEstudiantes[vecinos.get(i)].getEstudiante();
            int[] examenVecino = vecino.getExamen();
            int cantRespuestasAux = 0;
            int[] primerRespuestaAux = new int[2];

            for(int x = 0; x < examenVecino.length; x++) {                              // O(R)
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
        resolver(estudiante, primerRespuesta[0], primerRespuesta[1]);                   // O(log E)
        _handlesEstudiantes[estudiante].getEstudiante().cambiarCopiarVecino();
    }
    // Complejidad -> O(R + log E)


//-----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        _handlesEstudiantes[estudiante].getEstudiante().cambiarExamen(NroEjercicio, res, _examenCanonico);
        _handlesEstudiantes[estudiante].actualizarHeap();                               // O(log E)
    }
    // Complejidad -> O(log E)

//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

        public void consultarDarkWeb(int n, int[] examenDW) {
        for (int x = 0; x < n; x++){                                                     // O(k) -> Copiones DarkWeb
            Estudiante estudianteQueUsaDW = _heapEstudiantes.devolverPrimerEstudiante();
            for (int i = 0; i < examenDW.length; i++){                                      // O(R)
                estudianteQueUsaDW.cambiarExamen(i, examenDW[i], _examenCanonico);
            }
            estudianteQueUsaDW.cambiarCopiarDW();
            _handlesEstudiantes[estudianteQueUsaDW.getId()].actualizarHeap();               // O(log E)
        }
    }
    // Complejidad -> O(K*(R+log E))
    
//-------------------------------------------------ENTREGAR-------------------------------------------------------------

 
    public void entregar(int estudiante) {              
        _handlesEstudiantes[estudiante].getEstudiante().entregarExamen();
        _handlesEstudiantes[estudiante].actualizarHeap();                               // O(log E)
    }
    // Complejidad -> O(log E)

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        List<Estudiante> estudiantesOrdenados = new ArrayList<>();
        int estudiantesQueNoSeCopiaron = _heapEstudiantes.size() - _handlesSospechosos.size();
        
        for (int x = 0; x < estudiantesQueNoSeCopiaron; x++) {
            estudiantesOrdenados.add(_heapEstudiantes.desencolar());
        }
        for (int x = 0; x < estudiantesQueNoSeCopiaron; x++) {
            _heapEstudiantes.encolar(estudiantesOrdenados.get(x));
        }
        
        NotaFinal[] NotasOrdenadas = new NotaFinal[estudiantesOrdenados.size()];
        for (int i = 0; i < estudiantesQueNoSeCopiaron; i++){
            int id = estudiantesOrdenados.get(i).getId();
            double nota = 100 * ((double) estudiantesOrdenados.get(i).getRespuestasCorrectas() / _examenCanonico.length);
            NotaFinal a = new NotaFinal(nota, id );
            NotasOrdenadas[i] = a;
        }
        return NotasOrdenadas;
    }
    // Complejidad -> O(E * log E)

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        // chequear que todos hayan terminado todos todos FALTA O(E)

        int[][] contadorRespuestas = new int[10][_examenCanonico.length];

        for(int i = 0; i < contadorRespuestas.length; i++) {                    // O(R)
            for(int r = 0; r < 10; r++) {
                contadorRespuestas[i][r] = 0;
            }
        }
        
        for(int i = 0; i < _handlesEstudiantes.length; i++){
            int[] examen = _handlesEstudiantes[i].getEstudiante().getExamen();
            for(int r = 0; r < examen.length; r++) {
                if (examen[r] != -1) {
                    contadorRespuestas[r][examen[r]]++;
                }
            }
        }

        for(int e = 0; e < _handlesEstudiantes.length; e++) {                       // O(E)
            // chequear por pregunta si en el examen del estudiante 
            //la respuesta que puso es > al 25 o no sin contarlo
            boolean sospechoso = true;
            int r = 0;
            int contadorVacio = 0;

            while(r < 10 && sospechoso == true) {
                int res = _handlesEstudiantes[e].getEstudiante().getExamen()[r];
                if(res == -1){
                    contadorVacio++;
                } else if( contadorRespuestas[r][res] - 1  <  (int)(_handlesEstudiantes.length / 4)){
                    sospechoso = false;
                }
                r++;
            }
            if(sospechoso == true && contadorVacio < _examenCanonico.length) {
                _handlesSospechosos.add(_handlesEstudiantes[e]);
            } 
        }

        int[] ret = new int[_handlesSospechosos.size()];
        for(int i = 0; i < _handlesSospechosos.size(); i++) {
            ret[i] = _handlesSospechosos.get(i).getEstudiante().getId();
        }

        return ret;
    }
    // Complejidad -> O(E*R)
}
