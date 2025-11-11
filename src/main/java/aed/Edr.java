package aed;
import java.util.ArrayList;

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
            Estudiante est = new Estudiante(i);
            int index = _heapEstudiantes.encolar(est);

            _handlesEstudiantes[i] = _heapEstudiantes.new HandleMinHeap(index);
        }
    }

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        throw new UnsupportedOperationException("Sin implementar");
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------



    public void copiarse(int estudiante) {
        throw new UnsupportedOperationException("Sin implementar");
    }


//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        throw new UnsupportedOperationException("Sin implementar");
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
        throw new UnsupportedOperationException("Sin implementar");
    }
}
