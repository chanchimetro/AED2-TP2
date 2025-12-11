package aed;
import java.util.ArrayList;
import java.util.List;


public class Edr {
    private Handle<Estudiante>[] _handlesEstudiantes;
    /* aca usamos la interfaz del Handle */
    private MinHeap<Estudiante> _heapEstudiantes;
    private int[] _examenCanonico;

    private int _estudiantesSospechosos, _cantEstudiantes, _ladoAula; 


    public Edr(int LadoAula, int Cant_estudiantes, int[] ExamenCanonico) {
        _handlesEstudiantes = new Handle[Cant_estudiantes];
        _heapEstudiantes = new MinHeap<Estudiante>();
        _ladoAula = LadoAula;
        _examenCanonico = ExamenCanonico;

        _estudiantesSospechosos = 0;
        _cantEstudiantes = Cant_estudiantes;
        
        for (int i = 0 ; i < Cant_estudiantes; i++) { // O(E)
            Estudiante est = new Estudiante(i, ExamenCanonico.length); // O(R)
            _handlesEstudiantes[i] = (_heapEstudiantes.encolarRapido(i, est)); // O(1)
            /*  encolar rapido: podemos asegurar que insertando al fondo del heap se mantiene el orden buscado
                Todos los estudiantes arrancan con la misma cant. de respuestas correctas (0)
                e insertando los estudiantes por id decreciente es el orden pedido. */
        }
    }
    // Complejidad -> O(E*R)



//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        int cantPreguntas = _examenCanonico.length;
        double[] resultado = new double[_cantEstudiantes];

        for (int i = 0; i < _cantEstudiantes; i++) { // O(E)
            Estudiante estudiante =  _handlesEstudiantes[i].valor();
            resultado[i] = 100 * ((double) estudiante.getRespuestasCorrectas() / cantPreguntas);
        }

        return resultado;
    }
    // Complejidad -> O(E)
    
//------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int estudiante) {
        int[] pos = obtenerPosicion(estudiante, _ladoAula); 
        Estudiante estudianteCopiando =  _handlesEstudiantes[estudiante].valor(); 
        int[] examenEstudiante = estudianteCopiando.getExamen();  

        List<Integer> vecinos = conseguirVecinos(pos[0], pos[1], _ladoAula);  

        int contRespuestasDisponibles = 0;
        int indicePrimerRespuestaCopiar = 0;
        int ValorRespuestaACopiar = -1;
        int idMejorVecino = 0; 

        /* En el peor caso hay tres vecinos (arriba, izquierda, derecha) O(3) => O(1)*/
        for(int i = 0; i < vecinos.size(); i++) { 
            Estudiante vecino =  _handlesEstudiantes[vecinos.get(i)].valor();
            
            int contRespuestasDisponiblesAux = 0;
            int indicePrimeraResp = -1;
            int valorPrimeraResp = -1;
            int[] examenVecino = vecino.getExamen();

            /* Buscar respuestas a copiar */
            for(int x = 0; x < examenVecino.length; x++) { //O(R) 
                if (examenEstudiante[x] == -1 && examenVecino[x] != -1){
                    if(contRespuestasDisponiblesAux == 0){ 
                        /*  Solo guardamos la primer respuesta que se pueda copiar */
                        indicePrimeraResp = x;
                        valorPrimeraResp = examenVecino[x];
                    }
                    contRespuestasDisponiblesAux++;
                }
            }
        
            if(  contRespuestasDisponiblesAux > contRespuestasDisponibles || 
                (contRespuestasDisponiblesAux == contRespuestasDisponibles && vecino.getId() > idMejorVecino)){
                    
                contRespuestasDisponibles = contRespuestasDisponiblesAux;
                indicePrimerRespuestaCopiar = indicePrimeraResp;
                ValorRespuestaACopiar = valorPrimeraResp;
                idMejorVecino = vecino.getId();
            }
        }

        /* Solo si hay respuestas a copiar se copia O(log E) */
        if(indicePrimerRespuestaCopiar != -1) {  
            resolver(estudiante, indicePrimerRespuestaCopiar, ValorRespuestaACopiar); // O(log E)
        }
    }
    // Complejidad -> O(R + log E)
        /* O( v * R + log E ) = O( 1 * R + log E) = O( R + log E) */


    private int[] obtenerPosicion(int id, int n) {
        int columnasUsadas = (n+1) / 2;
        int fila = id / columnasUsadas;
        int columna = (id % columnasUsadas) * 2;
        return new int[]{fila, columna};
    }
    // Complejidad -> O(1)

    private int obtenerId(int fila, int colReal, int n) {
        int columnasUsadas = (n + 1) / 2;
        int col = colReal / 2;
        return fila * columnasUsadas + col;
    }
    // Complejidad -> O(1)


    private List<Integer> conseguirVecinos(int fila, int colReal, int n) {
        List<Integer> res = new ArrayList<>();
        int id;

        id = obtenerId(fila - 1, colReal, n);
        if (fila > 0 && !( _handlesEstudiantes[id].valor()).entrego()) {
            res.add(id);
        }

        id = obtenerId(fila, colReal - 2, n);
        if (colReal - 2 >= 0 && !( _handlesEstudiantes[id].valor()).entrego()) {
            res.add(id);
        }

        id = obtenerId(fila, colReal + 2, n);
        if (colReal + 2 < n && !( _handlesEstudiantes[id].valor()).entrego()) {
            res.add(id);
        }

        return res;
    }
    // Complejidad -> O(1)


//-----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        ( _handlesEstudiantes[estudiante].valor()).cambiarExamen(NroEjercicio, res, _examenCanonico); //O(1)
        _handlesEstudiantes[estudiante].actualizar_valor(); // O(log E)
    }
    // Complejidad -> O(log E)

//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        int[] ids_desencolados = new int[n];

        for (int x = 0; x < n; x++){ // O(k) -> Copiones DarkWeb
            Estudiante estudiante_peor_nota = _heapEstudiantes.desencolar();
            ids_desencolados[x] = estudiante_peor_nota.getId();
        }

        for (int x = 0; x < n; x++){ // O(k) 
            int id_desencolado = ids_desencolados[x];
            Estudiante estudianteQueUsaDW = _handlesEstudiantes[id_desencolado].valor();
            estudianteQueUsaDW.reiniciarCantRespuestasCorrectas();

            for (int i = 0; i < examenDW.length; i++){ // O(R)
                estudianteQueUsaDW.cambiarExamen(i, examenDW[i], _examenCanonico);
            }

            _handlesEstudiantes[id_desencolado] = _heapEstudiantes.encolar(estudianteQueUsaDW); // O(log E)
        }
    }
    // Complejidad -> O(K*(R+log E))
        /* O(k * (log E) + k * (R + log E)) = O(k * (R + 2log E + 1)) = O(k * (R + log E)) */
    
//-------------------------------------------------ENTREGAR-------------------------------------------------------------

 
    public void entregar(int estudiante) {              
        ( _handlesEstudiantes[estudiante].valor()).entregarExamen();
        _handlesEstudiantes[estudiante].actualizar_valor(); // O(log E)
    }
    // Complejidad -> O(log E)

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        int estudiantesQueNoSeCopiaron = _cantEstudiantes - _estudiantesSospechosos;
        NotaFinal[] _NotasCorregidas = new NotaFinal[estudiantesQueNoSeCopiaron];

        for (int i = estudiantesQueNoSeCopiaron - 1; i >= 0; i--){ // O(E) -> en el peor caso                    
            Estudiante estudianteACorregir = _heapEstudiantes.desencolar(); // O(log E) -> asumimos que no vamos a necesitar el handle después de este método
                int id = estudianteACorregir.getId();
                double nota = 100 * ((double) estudianteACorregir.getRespuestasCorrectas() / _examenCanonico.length);
                NotaFinal Nota_actual = new NotaFinal(nota, id );

                _NotasCorregidas[i] = Nota_actual;
        }
        return _NotasCorregidas;
    }
    // Complejidad -> O(E * log E)

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        int[][] contadorRespuestas = new int[_examenCanonico.length][10];

        for(int i = 0; i < _cantEstudiantes; i++){ // O(E)
            int[] examen = ( _handlesEstudiantes[i].valor()).getExamen();
            for(int indicePregunta = 0; indicePregunta < examen.length; indicePregunta++) { // O(R)
                if (examen[indicePregunta] != -1) {
                    contadorRespuestas[indicePregunta][examen[indicePregunta]]++;
                }
            }
        }

        for(int e = 0; e < _cantEstudiantes; e++) { // O(E)
            /* chequear -por pregunta (p)- si en el examen del estudiante 
            la respuesta que puso es > al 25 o no sin contarlo */
            boolean sospechoso = true;
            int indicePregunta = 0;
            int contadorVacio = 0;
            
            while(indicePregunta < _examenCanonico.length && sospechoso == true) { // O(R)
                int res = ( _handlesEstudiantes[e].valor()).getExamen()[indicePregunta];
                if(res == -1){
                    contadorVacio++;
                } else if( contadorRespuestas[indicePregunta][res] - 1  <  (int)(_cantEstudiantes / 4)){
                    sospechoso = false;
                }
                indicePregunta++;
            }

            if(sospechoso == true && contadorVacio < _examenCanonico.length) {
                ( _handlesEstudiantes[e].valor()).cambiarSospechosoCopiarse();
                _estudiantesSospechosos ++;
                _handlesEstudiantes[e].actualizar_valor();                               // O(log E)
            } 
        }


        int[] ids_sospechosos = new int[_estudiantesSospechosos];
        int index = 0;
        for(int i = 0; i < _cantEstudiantes; i++) { // O(E)
            if(( _handlesEstudiantes[i].valor()).sospechosoCopiarse()){
                ids_sospechosos[index] = i;
                index ++;
            }
        }

        return ids_sospechosos;
    }
    // Complejidad -> O(E*R)
        /* O( (E * R) + (E * (R + log E)) + E ) = O( E * (2R + log E + 1)) = O(E * R) */
}
