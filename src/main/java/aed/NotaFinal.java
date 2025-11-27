package aed;

public class NotaFinal implements Comparable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;
    }

    public int compareTo(NotaFinal otra){
        if (Double.compare(this._nota, otra._nota) != 0){
            return Double.compare(this._nota, otra._nota);
        }
        return this._id - otra._id;
    }
    // this tiene nota mas alta si > 


    @Override
    public boolean equals(Object otro) {
        if (this == otro){
            return true;
        }
        NotaFinal nota = (NotaFinal)otro;
        return Double.compare(_nota, nota._nota) == 0 && _id == nota._id;
    }

}
