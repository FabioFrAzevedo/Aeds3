package Model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.LocalDate;

public class Netflix {

    // --------------- Atributos ---------------

    private int show_id, release_year, qtdDirectors, qtdCast, qtdCountries;
    private String type, title, rating, duration, dateString;
    private String[] director, cast, country;
    private LocalDate date_added;

    // --------------- Construtores ---------------

    public Netflix() {
        this.show_id = 0;
        this.type = null;
        this.title = null;
        this.qtdDirectors = 0;
        this.director = new String[qtdDirectors];
        this.qtdCast = 0;
        this.cast = new String[qtdCast];
        this.qtdCountries = 0;
        this.country = new String[qtdCountries];
        this.dateString = null;
        this.date_added = null;
        this.release_year = 0;
        this.rating = null;
        this.duration = null;
    }

    public Netflix(int show_id, int release_year, int qtdDirectors, int qtdCast, int qtdCountries, String type, String title, String[] director, String rating,
            String duration, String dateString, String[] cast, String[] country, LocalDate date_added) {
        this.show_id = show_id;
        this.type = type;
        this.title = title;
        this.qtdDirectors = qtdDirectors;
        this.director = director;
        this.qtdCast = qtdCast;
        this.cast = cast;
        this.qtdCountries = qtdCountries;
        this.country = country;
        this.dateString = dateString;
        this.date_added = date_added;
        this.release_year = release_year;
        this.rating = rating;
        this.duration = duration;
    }

    // --------------- Setters e Getters ---------------

    // Show ID
    public int getShow_id() {return show_id;}
    public void setShow_id(int show_id) {this.show_id = show_id;}

    // Type
    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    // Title
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}

    // qtdDirectors
    public int getQtdDirectors() {return qtdDirectors;}
    public void setQtdDirectors(int qtdDirectors) {this.qtdDirectors = qtdDirectors;}

    // Director
    public String[] getDirector() {return director;}
    public void setDirector(String[] director) {
        if (director.length > 0) {this.director = director;}
        else {this.director[0] = "Nao informado!";}       
    }

    // qtdCast
    public int getQtdCast() {return qtdCast;}
    public void setQtdCast(int qtdCast) {this.qtdCast = qtdCast;}

    // Cast
    public String[] getCast() {return cast;}
    public void setCast(String[] cast) {
        if (cast.length > 0) {this.cast = cast;}
        else {this.cast[0] = "Nao informado!";}
    }

    // qtdCountries
    public int getQtdCountries() {return qtdCountries;}
    public void setQtdCountries(int qtdCountries) {this.qtdCountries = qtdCountries;}

    // Country
    public String[] getCountry() {return country;}
    public void setCountry(String[] country) {
        if (country.length > 0) {this.country = country;}
        else {this.country[0] = "Nao informado!";}
    }

    // Date String
    public String getDateString() {return dateString;}
    public void setDateString(String dateString) {this.dateString = dateString;}

    // Date Added
    public LocalDate getDate_added() {return date_added;}
    public void setDate_added(String dateString) {this.date_added = LocalDate.parse(dateString);}

    // Release Year
    public int getRelease_year() {return release_year;}
    public void setRelease_year(int release_year) {this.release_year = release_year;}

    // Rating
    public String getRating() {return rating;}
    public void setRating(String rating) {this.rating = rating;}

    // Duration
    public String getDuration() {return duration;}
    public void setDuration(String duration) {this.duration = duration;}

    // --------------- Byte Arrays ---------------

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(); // cria um byte array
        DataOutputStream dos = new DataOutputStream(baos); // cria um data output stream

        // escreve os dados no byte array
        dos.writeInt(this.getShow_id());
        dos.writeUTF(this.getType());
        dos.writeUTF(this.getTitle());
        dos.writeInt(this.getQtdDirectors());
        for (int i = 0; i < this.getQtdDirectors(); i++) {
            dos.writeUTF(this.getDirector()[i]);
        }
        dos.writeInt(this.getQtdCast());
        for (int i = 0; i < this.getQtdCast(); i++) {
            dos.writeUTF(this.getCast()[i]);
        }
        dos.writeInt(this.getQtdCountries());
        for (int i = 0; i < this.getQtdCountries(); i++) {
            dos.writeUTF(this.getCountry()[i]);
        }
        dos.writeUTF(this.getDateString());
        dos.writeInt(this.getRelease_year());
        dos.writeUTF(this.getRating());
        dos.writeUTF(this.getDuration());

        // fecha os streams para evitar leaks
        dos.close();
        baos.close(); 

        return baos.toByteArray();
    }

    public void fromByteArray(byte[] ba) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(ba); // cria um byte array
        DataInputStream dis = new DataInputStream(bais); // cria um data input stream

        // le os dados do byte array
        this.show_id = dis.readInt();
        this.type = dis.readUTF();
        this.title = dis.readUTF();
        this.qtdDirectors = dis.readInt();
        this.director = new String[this.getQtdDirectors()];
        for (int i = 0; i < this.getQtdDirectors(); i++) {
            this.director[i] = dis.readUTF();
        }
        this.qtdCast = dis.readInt();
        this.cast = new String[this.qtdCast];
        for (int i = 0; i < this.getQtdCast(); i++) {
            this.cast[i] = dis.readUTF();
        }
        this.qtdCountries = dis.readInt();
        this.country = new String[this.qtdCountries];
        for (int i = 0; i < this.getQtdCountries(); i++) {
            this.country[i] = dis.readUTF();
        }
        this.dateString = dis.readUTF();
        this.release_year = dis.readInt();
        this.rating = dis.readUTF();
        this.duration = dis.readUTF();

        //Preenche os atributos que nao foram lidos do arquivo
        if(this.getDateString().equals("Sem data")){
            this.setDateString("1010-10-10");
        }
        this.setDate_added(this.getDateString());

    }
}
