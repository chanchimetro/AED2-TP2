package aed;

public interface Handle<T extends Comparable<T>> { // generico 
    /**
     * Devuelve el valor del elemento
     *  
     */
    T valor();
    
    /**
     * Actualiza el heap en el que se encuentra el handle 
     * 
     */
     void actualizar_valor();
}
