package aed;
import java.util.ArrayList;
import java.util.List;

import aed.MinHeap.HandleMinHeap;
import aed.NotaFinal;

public class Edr {

    private MinHeap<Estudiante> _heapEstudiantes;
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
        
        for (int i = Cant_estudiantes - 1; i >= 0; i --) {                           // O(E)
            Estudiante est = new Estudiante(i, ExamenCanonico.length);          // O(R)
            _heapEstudiantes.encolarRapido(est);          // O(1)
            _handlesEstudiantes[i] = _heapEstudiantes.new HandleMinHeap(Cant_estudiantes - i - 1);     // O(1)
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
            Estudiante estudiante = (Estudiante) _handlesEstudiantes[i].getElemento();
            resultado[i] = 100 * ((double) estudiante.getRespuestasCorrectas() / cantPreguntas);
        }
        return resultado;
       
    }
    // Complejidad -> O(E)
    
//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        int[] pos = obtenerPosicion(estudiante, _ladoAula);
        int[] examenEstudiante = ((Estudiante) _handlesEstudiantes[estudiante].getElemento()).getExamen();

        List<Integer> vecinos = conseguirVecinos(pos[0], pos[1], _ladoAula);

        int cantRespuestas = 0;
        // primer componente es el indice de mi respuesta y el segundo es la respuesta 
        int[] primerRespuesta = new int[2];
        int idVecino = 0; 

        for(int i = 0; i < vecinos.size(); i++) {                                       // O(v) -> como mucho 3 
            Estudiante vecino = (Estudiante) _handlesEstudiantes[vecinos.get(i)].getElemento();
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
    }
    // Complejidad -> O(R + log E)


//-----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        ((Estudiante) _handlesEstudiantes[estudiante].getElemento()).cambiarExamen(NroEjercicio, res, _examenCanonico);
        _handlesEstudiantes[estudiante].actualizarHeap();                               // O(log E)
    }
    // Complejidad -> O(log E)

//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        ArrayList<Estudiante> estudiantesDW = _heapEstudiantes.conseguirKEstudiantes(n);

        for (int x = 0; x < n; x++){                                                     // O(k) -> Copiones DarkWeb
            Estudiante estudianteQueUsaDW = estudiantesDW.get(x);
            estudianteQueUsaDW.reiniciarExamen();
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
        ((Estudiante) _handlesEstudiantes[estudiante].getElemento()).entregarExamen();
        _handlesEstudiantes[estudiante].actualizarHeap();                               // O(log E)
    }
    // Complejidad -> O(log E)

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        int estudiantesQueNoSeCopiaron = _heapEstudiantes.size() - _handlesSospechosos.size();
        ArrayList<Estudiante> estudiantesOrdenados = new ArrayList<Estudiante>();
        
        estudiantesOrdenados = _heapEstudiantes.conseguirKEstudiantes(estudiantesQueNoSeCopiaron);
        
        
        // NotaFinal[] NotasOrdenadas = new NotaFinal[estudiantesOrdenados.size()];
        ListaEnlazada<NotaFinal> _NotasCorregidas = new ListaEnlazada<NotaFinal>();

        NotaFinal Nota_Anterior = null;

        for (int i = 0; i < estudiantesQueNoSeCopiaron; i++){                    // Complejidad E en el peor caso 
            int id = estudiantesOrdenados.get(i).getId();
            double nota = 100 * ((double) estudiantesOrdenados.get(i).getRespuestasCorrectas() / _examenCanonico.length);
            NotaFinal Nota_actual = new NotaFinal(nota, id );

            if( Nota_Anterior == null || Nota_actual.compareTo(Nota_Anterior) > 0){
                _NotasCorregidas.agregarAdelante(Nota_actual);
            } else {
                _NotasCorregidas.agregarAtras(Nota_actual);
            }
            // Si tienen la misma nota el heap se encarga de ordenarlos decrecientemente por ID
            // Si no tienen la misma nota significa que deben ir adelante 
            Nota_Anterior = Nota_actual;
        }

        NotaFinal[] Array_NotasOrdenadas = ListaEnlazada_a_Array(_NotasCorregidas);

        return Array_NotasOrdenadas;
    }

    private NotaFinal[] ListaEnlazada_a_Array(ListaEnlazada<NotaFinal> Notas_desordenadas){     
        ListaEnlazada<NotaFinal>.ListaIterador Iterador_Notas = Notas_desordenadas.iterador();
        NotaFinal[] NotasOrdenadas = new NotaFinal[Notas_desordenadas.longitud()];
        int i = 0;
        while( Iterador_Notas.haySiguiente()){                                              // Complejidad E en el peor caso 
            NotasOrdenadas[i] = Iterador_Notas.siguiente();
            i++;
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
            int[] examen = ((Estudiante) _handlesEstudiantes[i].getElemento()).getExamen();
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
                int res = ((Estudiante) _handlesEstudiantes[e].getElemento()).getExamen()[r];
                if(res == -1){
                    contadorVacio++;
                } else if( contadorRespuestas[r][res] - 1  <  (int)(_handlesEstudiantes.length / 4)){
                    sospechoso = false;
                }
                r++;
            }
            if(sospechoso == true && contadorVacio < _examenCanonico.length) {
                ((Estudiante) _handlesEstudiantes[e].getElemento()).cambiarCopiarVecino();
                _handlesSospechosos.add(_handlesEstudiantes[e]);
                _handlesEstudiantes[e].actualizarHeap();                               // O(log E)
            } 
        }

        int[] ret = new int[_handlesSospechosos.size()];
        for(int i = 0; i < _handlesSospechosos.size(); i++) {
            ret[i] = ((Estudiante) _handlesSospechosos.get(i).getElemento()).getId();
        }

        return ret;
    }
    // Complejidad -> O(E*R)
}
