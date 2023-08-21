import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class classe {
    // Atributos -----------------------------------
    private int show_id, release_year, contCast, contDirector;
    private String[] type, director, cast, date;
    private LocalDate date_added;

    // Construtores ---------------------------------- 
    public classe(){
        this.show_id = 0;
        this.type = null;
        this.contDirector = 0;
        this.director = new String[contDirector];
        this.contCast = 0;
        this.cast = new String[contCast];
        this.date = null;
        this.date_added = null;
        this.release_year = 0;
    }

    public classe(int show_id, int release_year, int contDirector, int contCast, String type, String[] director, String date, String[] cast, 
     LocalDate date_added) {


        this.show_id = show_id;
        this.type = type;
        this.contDirector = contDirector;
        this.director = director;
        this.contCast = contCast;
        this.cast = cast;
        this.date = date;
        this.date_added = date_added;
        this.release_year = release_year;
}

    // Setters e Getters ----------------------------

    // SHOW ID
    public int getShow_id(){
        return show_id;
    }
    public void setShow_id(int show_id){
        this.show_id = show_id;
    }

    // TYPE
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }

    // CONTDIRECTOR
    public int getContDirector(){
        return contDirector;
    }
    public void setcontDirector(int contDirector){
        this.qtdDirectors = contDirector;
    }

    //  DIRECTOR
    public String[] getDirector(){
        return director;
    }
    public void setDirector(String[] director){
        if (director.length > 0) {
            this.director = director;
        }
        else {
            this.director[0] = "unknown"; // não informado 
        }       
    }

    // CONTCAST
    public int getContCast(){
        return contCast;
    }
    public void setQtdCast(int qtdCast){
        this.qtdCast = contCast;
    }

    // Cast
    public String[] getCast(){
        return cast;
    }
    public void setCast(String[] cast){
        if (cast.length > 0){
            this.cast = cast;
        }
        else{
            this.cast[0] = "unknown"; // não informado
        }
    }

    // BYTE ARRAYS ----------------------

    


}