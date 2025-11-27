package aed;


public class ListaEnlazada<T> {
    private Nodo primero;
    private Nodo ultimo;

    private class Nodo {
        T valor; 
        // uso tipo T no int 
        Nodo sig;
        Nodo ant; 

        Nodo(T v) { valor = v; }
    }

    public ListaEnlazada() {
        primero = null;
        ultimo = null;
    }

    public int longitud() {
        int longitud = 0;
        if(primero != null){
        Nodo actual = primero;
        while(actual != null){
            longitud += 1;
            actual = actual.sig;
        }
        }else {if(ultimo != null){longitud = 1;}}
        return longitud;
    }

    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        if(primero != null){
            primero.ant = nuevo; 
            nuevo.sig = primero;
        }else{ultimo = nuevo;}
        primero = nuevo; 

    }

    public void agregarAtras(T elem) {
        Nodo nuevo = new Nodo(elem);
        if(ultimo != null){
            nuevo.ant = ultimo; 
            ultimo.sig = nuevo; 
        }else {primero = nuevo;}
        ultimo = nuevo; 
    }

    public T obtener(int i) {
        Nodo actual = primero;
        int n = 0; 
        while(n < i){
            actual = actual.sig; 
            n += 1;
        }
        return actual.valor;
    }

    public void eliminar(int i) {
        Nodo actual = primero;
        Nodo prev = primero;
        Nodo siguiente = primero.sig; 
        for(int j = 0; j < i; j++){
            prev = actual;
            actual = actual.sig;
            if(siguiente.sig != null){
            siguiente = siguiente.sig;
            }
        }
        if(i == 0){
            if(this.longitud() == 1){
                ultimo = actual.sig;
            }
            primero = actual.sig;
            
        } else {
            prev.sig = actual.sig;
            siguiente.ant = prev;
        }

    }

    public void modificarPosicion(int indice, T elem) {
        Nodo prev = primero;
        Nodo actual = primero;
        Nodo siguiente = primero.sig; 
        for(int j = 0; j < indice; j++){
            prev = actual;
            if(actual.sig != null){
            actual = actual.sig;
                if(siguiente.sig != null){
                siguiente = siguiente.sig;
            }
        }
        }
        actual.valor = elem;
        if(prev != primero){
        prev.sig = actual;}
        if(siguiente.sig != null){
        siguiente.ant = actual;
    }
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        Nodo actual = lista.primero;
        while (actual != null){
            agregarAtras(actual.valor);
            actual = actual.sig;
        }
        
    }
    
    @Override
    public String toString() {
        Nodo actual = primero; 
        String salida = "[";
        while(actual != null){
            if(actual != ultimo){
            salida += actual.valor.toString() + ", ";
            } else {salida += actual.valor.toString() + "]";}
            actual = actual.sig;
        }
        return salida;
    }

    public class ListaIterador {
    	// Completar atributos privados
        Integer contador = 0; 
        Nodo dedito; 

        public boolean haySiguiente() {
	        return contador != longitud();
        }
        
        public boolean hayAnterior() {
	        return contador != 0;
        }

        public T siguiente() {
            T s = obtener(contador);
            contador += 1;
            return s;
        }
        
        public T anterior() {
	        contador -= 1; 
            return obtener(contador);
        }
    }

    public ListaIterador iterador() {
	    return new ListaIterador();
    }

}
