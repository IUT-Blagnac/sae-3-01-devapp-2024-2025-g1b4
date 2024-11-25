package org.javafxapp;

import org.javafxapp.controller.MainMenu;

import java.util.ArrayList;
import java.util.List;

public class Main {

    // List des salles connues d'où collecter les données
    public static List<String> knownRooms=new ArrayList<>();
    static{
        knownRooms.addAll(List.of(new String[]{"B005", "C105"}));
    }


    public static void main(String[] args) {
        MainMenu.main2(args);
    }
}
