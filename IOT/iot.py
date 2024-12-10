import configparser
import os
import paho.mqtt.client as mqtt
import json
import time
import threading

# Chargement du fichier de configuration
config = configparser.ConfigParser()
config.read('config.ini')

# Lecture des informations de configuration pour MQTT et les données
broker = config['adress']['broker']  # Adresse du broker MQTT
port = config.getint('adress', 'port')  # Port du broker MQTT
topics = config['adress']['topics'].split(',')  # Liste des topics MQTT à souscrire

# Paramètres des salles, données et intervalle de sauvegarde
salles = set(config['donnees']['salles'].split(','))  # Salles surveillées
donnees = config['donnees']['donnees'].split(',')  # Types de données surveillées (température, humidité, etc.)
temps = int(config['donnees']['temps'])  # Intervalle de temps pour la sauvegarde des données

# Lecture des seuils d'alerte
seuil = {}  # Dictionnaire pour stocker les seuils pour chaque type de donnée
valeursFinal = {}  # Dictionnaire pour stocker les valeurs collectées

# Vérification de la correspondance entre les seuils définis et les données attendues
if len(config['seuil']) != len(donnees):
    print("[WARNING] Seuil manquant dans le fichier de configuration")
else:
    for do in donnees:
        seuil[do] = config['seuil'][do].split(',')

# Fonction pour sauvegarder périodiquement les données dans un fichier JSON
def afficher_valeurs_final():
    while True:
        time.sleep(temps)  # Attendre l'intervalle défini
        # Ouvrir le fichier en mode écriture avec gestion des permissions
        fd = os.open(config["adress"]["sauvegarde"], os.O_WRONLY | os.O_CREAT | os.O_TRUNC, 0o644)
        with os.fdopen(fd, 'w', encoding='utf-8') as fichier:
            # Sauvegarder les données en format JSON
            json.dump(valeursFinal, fichier, indent=4, ensure_ascii=False)

# Fonction pour charger les données à partir du fichier JSON, ou le créer s'il n'existe pas
def load_data():
    global valeursFinal
    file_path = config["adress"]["sauvegarde"]

    if not os.path.exists(file_path):  # Vérifier si le fichier existe
        with open(file_path, 'w', encoding='utf-8') as fichier:
            json.dump({}, fichier, indent=4, ensure_ascii=False)  # Créer un fichier JSON vide
        print(f"Le fichier {file_path} a été créé.")
        valeursFinal = {}  # Initialiser les données
    else:
        fd = os.open(file_path, os.O_RDONLY)  # Ouvrir en lecture seule
        with os.fdopen(fd, 'r', encoding='utf-8') as fichier:
            valeursFinal = json.load(fichier)  # Charger les données du fichier JSON

# Fonction appelée lorsque le client se connecte au broker
def on_connect(client, userdata, flags, rc):
    print(f"Connecté avec le code de résultat {rc}")
    load_data()  # Charger les données sauvegardées
    for topic in topics:
        client.subscribe(topic.strip())  # Souscrire aux topics spécifiés

# Fonction appelée lorsqu'un message est reçu
def on_message(client, userdata, msg):
    global valeursFinal
    data = json.loads(msg.payload.decode())  # Décoder le message JSON
    newMsg = msg.topic.split('/')[0]  # Identifier le type de capteur à partir du topic

    if newMsg == 'solaredge':  # Gestion des données des panneaux solaires
        print("----------------------------------")
        print("Solar panel")
        print(f"{data['lastUpdateTime']}\nEnergie : {data['currentPower']['power']}")
        if "solarpanel" not in valeursFinal:
            valeursFinal["solarpanel"] = {}
        valeursFinal["solarpanel"][str(len(valeursFinal["solarpanel"]))] = data["currentPower"]["power"]

    elif newMsg == 'AM107':  # Gestion des données des capteurs AM107
        if data[1]["room"] in salles:  # Vérifier si la salle est surveillée
            print("----------------------------------")
            print(f"Salle -> {data[1]['room']}")
            if data[1]["room"] not in valeursFinal:
                valeursFinal[data[1]["room"]] = {}
            newData = {}
            for do in donnees:
                newData[do] = float(data[0][do])  # Convertir la donnée en float
                # Vérifier les seuils et afficher des alertes si nécessaire
                if float(data[0][do]) <= float(seuil[do][0]):
                    print(f"[ALERT] Seuil minimum dépassé -> {do} : {data[0][do]}")
                elif float(data[0][do]) >= float(seuil[do][1]):
                    print(f"[ALERT] Seuil maximum dépassé -> {do} : {data[0][do]}")
                else:
                    print(f"{do} : {data[0][do]}")
            valeursFinal[data[1]["room"]][str(len(valeursFinal[data[1]["room"]]))] = newData

# Configuration du client MQTT
client = mqtt.Client()

client.on_connect = on_connect  # Attacher la fonction de connexion
client.on_message = on_message  # Attacher la fonction de réception de message

# Connexion au broker MQTT
try:
    client.connect(broker, port, 60)
except Exception as e:
    print(f"Erreur lors de la connexion au broker : {e}")
    exit(1)

# Démarrage du thread pour la sauvegarde périodique des données
thread = threading.Thread(target=afficher_valeurs_final)
thread.daemon = True
thread.start()

# Boucle principale du client MQTT
client.loop_start()
try:
    while True:
        pass  # Garder le programme en exécution
except KeyboardInterrupt:
    print("\nDéconnexion...")
    client.loop_stop()  # Arrêter la boucle MQTT
    client.disconnect()  # Déconnecter le client MQTT
