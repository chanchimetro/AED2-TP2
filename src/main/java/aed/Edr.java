package aed;
import java.util.ArrayList;
import java.util.List;

import aed.MinHeap.HandleMinHeap;
import aed.NotaFinal;

public class Edr {

    private MinHeap<Estudiante> _heapEstudiantes;
    private int _ladoAula;
    private MinHeap<Estudiante>.HandleMinHeap<Estudiante>[] _handlesEstudiantes;
    private int[] _examenCanonico;

    private int _estudiantesSospechosos; 
    private int _cantEstudiantes;

    
    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _handlesEstudiantes = new HandleMinHeap[Cant_estudiantes];
        _heapEstudiantes = new MinHeap<Estudiante>();
        _ladoAula = LadoAula;
        _examenCanonico = ExamenCanonico;

        _estudiantesSospechosos = 0;
        _cantEstudiantes = Cant_estudiantes;
        
        for (int i = 0 ; i < Cant_estudiantes; i++) {                           // O(E)
            Estudiante est = new Estudiante(i, ExamenCanonico.length);          // O(R)
            _handlesEstudiantes[i] = (_heapEstudiantes.encolarRapido(i, est));          // O(1)

            /*
                Usamos encolar rapido, ya que en este caso puntual, podemos asegurar que insertando al fondo del heap se mantiene el orden buscado
                sin necesidad de encolar u ordenar el heap devuelta. Todos los estudiantes arrancan con la misma cant. de respuestas correctas (0)
                e insertando los estudiantes por id decreciente es el orden correcto.
            */
        }
    }
    // Complejidad -> O(E*R)


//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        int cantPreguntas = _examenCanonico.length;
        double[] resultado = new double[_handlesEstudiantes.length];

        for (int i = 0; i < _handlesEstudiantes.length; i++) {                                  // O(E)
            Estudiante estudiante = (Estudiante) _handlesEstudiantes[i].valor();
            resultado[i] = 100 * ((double) estudiante.getRespuestasCorrectas() / cantPreguntas);
        }

        return resultado;
    }
    // Complejidad -> O(E)
    
//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        int[] pos = obtenerPosicion(estudiante, _ladoAula);
        int[] examenEstudiante = ((Estudiante) _handlesEstudiantes[estudiante].valor()).getExamen();

        List<Integer> vecinos = conseguirVecinos(pos[0], pos[1], _ladoAula);

        int cantRespuestas = 0;
        /* primer componente es la pregunta y el segundo es la respuesta */  
        int[] primerRespuesta = {0,-1};
        int idVecino = 0; 

        for(int i = 0; i < vecinos.size(); i++) {                                       // O(v) -> como mucho 3 
            Estudiante vecino = (Estudiante) _handlesEstudiantes[vecinos.get(i)].valor();
            int[] examenVecino = vecino.getExamen();
            int cantRespuestasAux = 0;
            int[] primerRespuestaAux = {0,-1};

            for(int x = 0; x < examenVecino.length; x++) {                              // O(R)
                if (examenEstudiante[x] == -1 && examenVecino[x] != -1){
                    if(cantRespuestasAux == 0){
                        /* solo guardo 1° sin contestar */
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
    }
    // Complejidad -> O(R + log E)

    private List<Integer> conseguirVecinos(int fila, int colReal, int n) {
        List<Integer> res = new ArrayList<>();
        int id;

        id = obtenerId(fila - 1, colReal, n);
        if (fila > 0 && !((Estudiante) _handlesEstudiantes[id].valor()).entrego()) {
            res.add(id);
        }

        id = obtenerId(fila, colReal - 2, n);
        if (colReal - 2 >= 0 && !((Estudiante) _handlesEstudiantes[id].valor()).entrego()) {
            res.add(id);
        }

        id = obtenerId(fila, colReal + 2, n);
        if (colReal + 2 < n && !((Estudiante) _handlesEstudiantes[id].valor()).entrego()) {
            res.add(id);
        }

        return res;
    }
    // Complejidad -> O(1)

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


//-----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        ((Estudiante) _handlesEstudiantes[estudiante].valor()).cambiarExamen(NroEjercicio, res, _examenCanonico);

        _handlesEstudiantes[estudiante].actualizar_valor();                               // O(log E)
    }
    // Complejidad -> O(log E)

//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        int[] ids_desencolados = new int[n];

        for (int x = 0; x < n; x++){
            Estudiante estudiante_peor_nota = _heapEstudiantes.desencolar();
            ids_desencolados[x] = estudiante_peor_nota.getId();
        }

        for (int x = 0; x < n; x++){                                                     // O(k) -> Copiones DarkWeb
            int id_desencolado = ids_desencolados[x];
            Estudiante estudianteQueUsaDW = _handlesEstudiantes[id_desencolado].valor();
            estudianteQueUsaDW.reiniciarExamen();

            for (int i = 0; i < examenDW.length; i++){                                      // O(R)
                estudianteQueUsaDW.cambiarExamen(i, examenDW[i], _examenCanonico);
            }

            _handlesEstudiantes[id_desencolado] = _heapEstudiantes.encolar(estudianteQueUsaDW);                // O(log E)
        }
    }

    // Complejidad -> O(K*(R+log E))

    // cuando se vuelven a agregar el estudiante nuevo con el examen modificado con el intersar 
    // crear un handle -> lo quiero agregar al heap 
    // lo pongo al final y lo hago subir. 
    // ese handle lo tengo que devolver porque ese handle tiene que ir en la posicion de ese estudiante
    
//-------------------------------------------------ENTREGAR-------------------------------------------------------------

 
    public void entregar(int estudiante) {              
        ((Estudiante) _handlesEstudiantes[estudiante].valor()).entregarExamen();
        _handlesEstudiantes[estudiante].actualizar_valor();                               // O(log E)

    }
    // Complejidad -> O(log E)

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
       
            int estudiantesQueNoSeCopiaron = _cantEstudiantes - _estudiantesSospechosos;
            ArrayList<NotaFinal> _NotasCorregidas = new ArrayList<NotaFinal>(estudiantesQueNoSeCopiaron);

            for (int i = 0; i < estudiantesQueNoSeCopiaron; i++){                    
                Estudiante estudianteACorregir = _heapEstudiantes.desencolar();
                //if (!estudianteACorregir.sospechosoCopiarse()){
                    int id = estudianteACorregir.getId();
                    double nota = 100 * ((double) estudianteACorregir.getRespuestasCorrectas() / _examenCanonico.length);
                    NotaFinal Nota_actual = new NotaFinal(nota, id);

                    _NotasCorregidas.add(0, Nota_actual);
                //}
        }

            NotaFinal[] res = _NotasCorregidas.toArray(new NotaFinal[_NotasCorregidas.size()]);
            return res;
    }

    // Complejidad -> O(E * log E)

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
            int[][] contadorRespuestas = new int[_examenCanonico.length][10];
                /*  filas = puntos del examen  
                    columnas = respuestas posibles -> 1 al 10 
                    java ya inicializa los contadores en 0  */
            
            for(int i = 0; i < _handlesEstudiantes.length; i++){ // O(E*R)              
                int[] examen = ((Estudiante) _handlesEstudiantes[i].valor()).getExamen();
                for(int p = 0; p < examen.length; p++) {
                    if (examen[p] != -1) {
                        contadorRespuestas[p][examen[p]]++;
                    }
                }
            }

            for(int e = 0; e < _cantEstudiantes; e++) {// O(E*R) -> en el peor caso
                /* chequear -por pregunta- si en el examen del estudiante 
                la respuesta que puso es > al 25 o no sin contarlo */
                boolean sospechoso = true;
                int p = 0;
                int contadorPuntoVacio = 0;

                while(p < _examenCanonico.length && sospechoso == true) {
                    int res = ((Estudiante) _handlesEstudiantes[e].valor()).getExamen()[p];
                    if(res == -1){
                        contadorPuntoVacio++;
                    } else if( contadorRespuestas[p][res] - 1  <  (int)(_cantEstudiantes / 4)){
                        sospechoso = false;
                    }
                    p++;
                }
                if(sospechoso == true && contadorPuntoVacio < _examenCanonico.length) {
                    ((Estudiante) _handlesEstudiantes[e].valor()).cambiarSospechosoCopiarse();
                    _estudiantesSospechosos ++;
                    _handlesEstudiantes[e].actualizar_valor();                               // O(log E)
                } 
            }

            int[] ret = new int[_estudiantesSospechosos];
            int index = 0;
            for(int i = 0; i < _cantEstudiantes; i++) {                                         // O(E)
                if(((Estudiante) _handlesEstudiantes[i].valor()).sospechosoCopiarse()){
                    ret[index] = i;
                    index ++;
                }
            }
            return ret;
    }
    // O(E*R + E) = O(E*(R+1)) = O(E * R)
    // Complejidad -> O(E*R)
}
