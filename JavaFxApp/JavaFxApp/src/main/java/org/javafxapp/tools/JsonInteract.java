package org.javafxapp.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.javafxapp.Main;
import org.json.*;

public class JsonInteract {

    private JSONObject appData;

    /**
     * Récupère toutes les données JSon du fichier de l'application
     */
    public JsonInteract(){
        try {
            String content=Files.readString(Paths.get(Main.appDataPath));
            this.appData=new JSONObject(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * S'assure d'écrire le nouveau JSon avec ces modifications dans le fichier
     */
    public void properClose(){
        try {
            Files.writeString(Paths.get(Main.appDataPath),this.appData.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Permet de récupérer les données du JSon a partir du chemin des
     * données au format comprenant une séparation par un point.
     *
     * @param pathToData le chemin des données
     * @return l'Objet trouvé au bout du chemin
     */
    public Object get(String pathToData){
        return this.getThroughArray(pathToData.split("\\."));
    }

    /**
     * Permet de récupérer les données du JSon a partir du chemin des
     * données a partir d'une liste de String contenant les objets du chemin.
     *
     * @param paths les objets formant le chemin des données à récupérer
     * @return l'Objet au bout du chemin
     */
    public Object getThroughArray(String[] paths){
        JSONObject jsOb=this.appData;
        Object temp=null;

        for(String str:paths){
            temp=jsOb.get(str);
            if(temp instanceof JSONObject)
                jsOb=(JSONObject) temp;
            else
                return temp;
        }
        return jsOb;
    }
}
