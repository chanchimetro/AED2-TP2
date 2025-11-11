package aed;

public class Estudiante {
    private int _id;
    private boolean _entrego;
    private int _nota;

    public Estudiante(int id) {
        _id = id;
        _entrego = false;
        _nota = 0;
    }

    public int compareTo(Estudiante other) {
        return this._nota - other._nota; //agregar desempate por ID
    }
}
