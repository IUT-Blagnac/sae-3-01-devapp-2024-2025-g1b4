import configparser
import os
import sys

import paho.mqtt.client as mqtt
import json
import time
import threading

config = configparser.ConfigParser()
config.read('./iot/config.ini')

broker = config['adress']['broker']
port = config.getint('adress', 'port')
topics = config['adress']['topics'].split(',')

salles = set(config['donnees']['salles'].split(','))
donnees = config['donnees']['donnees'].split(',')
temps = int(config['donnees']['temps'])

seuil = {}
valeursFinal = {}

if (len(config['seuil']) < len(donnees)):
    print("[WARNING] Seuil manquant dans le fichier de configuration")
else:
    for do in donnees:
        seuil[do] = config['seuil'][do].split(',')


def afficher_valeurs_final():
    while True:
        time.sleep(temps)
        # Ouvrir le fichier en mode écriture avec os.open()
        fd = os.open(config["adress"]["sauvegarde"], os.O_WRONLY | os.O_CREAT | os.O_TRUNC, 0o644)

        # Convertir le descripteur de fichier en un objet fichier avec os.fdopen()
        with os.fdopen(fd, 'w', encoding='utf-8') as fichier:
            json.dump(valeursFinal, fichier, indent=4, ensure_ascii=False)


def load_data():
    global valeursFinal

    file_path = config["adress"]["sauvegarde"]

    if not os.path.exists(file_path):
        # Si le fichier n'existe pas, le créer avec une structure vide (par exemple, un dictionnaire vide)
        with open(file_path, 'w', encoding='utf-8') as fichier:
            json.dump({}, fichier, indent=4,
                      ensure_ascii=False)  # Créer un fichier JSON vide (ou avec un contenu initial)

        print(f"Le fichier {file_path} a été créé.")
        valeursFinal = {}
    else:
        fd = os.open(file_path, os.O_RDONLY)  # Utilisation de O_RDONLY pour la lecture
        # Convertir le descripteur de fichier en un objet fichier
        with os.fdopen(fd, 'r', encoding='utf-8') as fichier:
            # Charger les données JSON du fichier
            valeursFinal = json.load(fichier)


def on_connect(client, userdata, flags, rc):
    print(f"Connecté avec le code de résultat {rc}")
    load_data()
    for topic in topics:
        client.subscribe(topic.strip())


def on_message(client, userdata, msg):
    global valeursFinal
    data = json.loads(msg.payload.decode())
    newMsg = msg.topic.split('/')[0]
    if (newMsg == 'solaredge'):
        print("----------------------------------")
        print("Solar panel")
        print(f"{data["lastUpdateTime"]}\nEnergie : {data["currentPower"]["power"]}")
        if "solarpanel" not in valeursFinal:
            valeursFinal["solarpanel"] = {}
        valeursFinal["solarpanel"][str(len(valeursFinal["solarpanel"]))] = data["currentPower"]["power"]
    elif (newMsg == 'AM107'):
        if (data[1]["room"] in salles):
            print("----------------------------------")
            print(f"Salle -> {data[1]["room"]}")
            newData = {}
            if data[1]["room"] not in valeursFinal:
                valeursFinal[data[1]["room"]] = {}
            for do in donnees:
                newData[do] = float(data[0][do])
                with open("AlertPipe.txt", "w") as alertFile:
                    if float(data[0][do]) <= float(seuil[do][0]):
                        alertFile.write(f"[ALERT] Seuil minimum dépassé -> {do} : {data[0][do]}")
                    elif float(data[0][do]) >= float(seuil[do][1]):
                        alertFile.write(f"[ALERT] Seuil maximum dépassé -> {do} : {data[0][do]}")
                    else:
                        alertFile.write(f"{do} : {data[0][do]}")
            valeursFinal[data[1]["room"]][str(len(valeursFinal[data[1]["room"]]))] = newData


client = mqtt.Client()

client.on_connect = on_connect
client.on_message = on_message

try:
    client.connect(broker, port, 60)
except Exception as e:
    with open("AlertPipe.txt", "w") as alertFile:
        alertFile.write("Alert! \\nLa connection au broker a échouée!")
    exit(1)
if (len(sys.argv) > 1):
    exit(0)

# Création d'un nouveau thread
thread = threading.Thread(target=afficher_valeurs_final)
thread.daemon = True
thread.start()

client.loop_start()
with open("AlertPipe.txt", "w") as alertFile:
    alertFile.write("start")

try:
    while True:
        pass
except KeyboardInterrupt:
    print("\nDéconnexion...")
    client.loop_stop()
    client.disconnect()
