import configparser
import paho.mqtt.client as mqtt
import json

config = configparser.ConfigParser()
config.read('config.ini')

broker = config['adress']['broker']
port = config.getint('adress', 'port')
topics = config['adress']['topics'].split(',')

salles = set(config['donnees']['salles'].split(','))
donnees = config['donnees']['donnees'].split(',')

def on_connect(client, userdata, flags, rc):
    print(f"Connecté avec le code de résultat {rc}")
    for topic in topics:
        client.subscribe(topic.strip())

def on_message(client, userdata, msg):
    data = json.loads(msg.payload.decode())
    newMsg = msg.topic.split('/')[0]
    if (newMsg == 'solaredge'):
        print("----------------------------------")
        print("Solar panel")
        print(f"{data["lastUpdateTime"]}\nEnergie : {data["currentPower"]["power"]}")
    elif (newMsg == 'AM107'):
        if (data[1]["room"] in salles):
            print("----------------------------------")
            print(f"Salle -> {data[1]["room"]}")
            for do in donnees:
                print(f"{do} : {data[0][do]}")


client = mqtt.Client()

client.on_connect = on_connect
client.on_message = on_message

try:
    client.connect(broker, port, 60)
except Exception as e:
    print(f"Erreur lors de la connexion au broker : {e}")
    exit(1)

client.loop_start()

try:
    while True:
        pass
except KeyboardInterrupt:
    print("\nDéconnexion...")
    client.loop_stop()
    client.disconnect()
