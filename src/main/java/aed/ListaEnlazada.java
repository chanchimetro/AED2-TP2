package aed;

public class ListaEnlazada<T>{
    private Nodo primero;
    private Nodo ultimo;
    private int longitud;

    private class Nodo {
        T valor;
        Nodo sig;
        Nodo prev;

        Nodo(T v) { 
            sig = null;
            prev = null;
            valor = v;
        }
    }

    public ListaEnlazada() {
        primero = null;
        ultimo = null;
        longitud = 0;
    }

    public int longitud() {
        return longitud;
    }

    public void agregarAdelante(T elem) {
        Nodo nuevo = new Nodo(elem);
        
        if (primero == null) {
            primero = nuevo;
            ultimo = nuevo;
        } else {
            nuevo.sig = primero;
            primero.prev = nuevo;
            primero = nuevo;
        }

        longitud += 1;
    }

    public void agregarAtras(T elem) {
        Nodo nuevo = new Nodo(elem);

        if (ultimo == null) {
            ultimo = nuevo;
            primero = nuevo;
        } else {
            ultimo.sig = nuevo;
            nuevo.prev = ultimo;
            ultimo = nuevo;
        }

        longitud += 1;
    }

    public T getPrimero(){
        return primero.valor;
    }

    public T obtener(int i) {
        T ret = null;
        int cont = 0;

        Nodo actual = primero;

        if (i < longitud) {
            while (cont < i) {
                actual = actual.sig;
                cont += 1;
            }

            ret = actual.valor;
        }

        return ret;
    }

    public void eliminar(int i) {
        int cont = 0;

        Nodo actual = primero;
        Nodo sig = actual.sig;
        Nodo prev = actual.prev;

        if (i < longitud) {
            while (cont < i) {
                prev = actual;
                actual = actual.sig;
                sig = actual.sig;

                cont += 1;
            }
            
            if (actual == primero) {
                primero = sig;
            }
            if (actual == ultimo) {
                ultimo = prev;
            }

            if (prev != null) {
                prev.sig = sig;
            }
            if (sig != null) {
                sig.prev = prev;
            }
        }

        longitud -= 1;
    }

    public void modificarPosicion(int indice, T elem) {
        int cont = 0;

        Nodo actual = primero;

        if (indice < longitud) {
            while (cont < indice) {
                actual = actual.sig;
                cont += 1;
            }

            actual.valor = elem;
        }
    }

    public ListaEnlazada(ListaEnlazada<T> lista) {
        Nodo actual = lista.primero;
        longitud = 0;

        while (actual != null) {
            Nodo nuevo = new Nodo(actual.valor);
            if (primero == null) {
                primero = nuevo;
                ultimo = nuevo;
                longitud += 1;
            } else {
                agregarAtras(actual.valor);
            }

            actual = actual.sig;
        }
    }
    
    @Override
    public String toString() {
        String ret = "";
        Nodo actual = primero;

        while (actual != null) {
            ret += actual.valor.toString();
            if (actual.sig != null) {
                ret += ", ";
            }
            
            actual = actual.sig;
        }

        return "[" + ret + "]";
    }

    public class ListaIterador{
        int actual = 0;

        public boolean haySiguiente() {
	        return actual != longitud;
        }
        
        public boolean hayAnterior() {
	        return actual != 0;
        }

        public T siguiente() {
	        T ret = obtener(actual);
            actual += 1;

            return ret;
        }
        

        public T anterior() {
            actual -= 1;
            T ret = obtener(actual);

            return ret;
        }
    }

    public ListaIterador iterador() {
	    ListaIterador ret = new ListaIterador();
        
        return ret;
    }

}
