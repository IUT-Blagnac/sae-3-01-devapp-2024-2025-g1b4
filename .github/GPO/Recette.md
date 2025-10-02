# Cahier de Recette – Les Amis de Virebent (SAE 5.1)

Université de Toulouse  
IUT de Blagnac – Département Informatique

![Logo IUT Blagnac](../Images/Logo_IUT_Blagnac.png)

![Image de couverture](../Images/image_doc_tech.jpg)

Équipe:  
- BOULOUIHA-GNAOUI Yassir  
- ARGUELLES Alexian  
- DELAPAGNE Titouan  
- LOVIN Alex  
- THEOPHILE Adrien  
- ABAYEV Amina

Version: 1.0  
Destinataire: Association “Les Amis de Virebent”  
Projet: Base de données + site WordPress

Table des matières
- I. Introduction
  - 1. Objet
  - 2. Périmètre (User Stories)
  - 3. Références
- II. Pré-requis & Environnement
  - 1. Pré-requis
  - 2. Environnement de test
  - 3. Données de test
- III. Plan de test par User Story
  - US‑P1: Consulter une fiche produit/œuvre/immeuble (#60)
  - US‑P2: Recherche (immeubles/œuvres/mobiliers) (#59)
  - US‑P3: Comptes adhérents / Authentification (#53)
  - US‑P4: Administration contenus (ajout/édition) (#48)
  - US‑P5: Relations entre contenus (mobilier/immeuble/catalogues) (#71)
  - US‑P6: Galerie multi‑images + marquage “sensible” (#63, #64)
  - US‑P7: Import des données client (#51)
  - (Annexe) Qualité et non-fonctionnel
- IV. Traçabilité (User Stories ↔ Issues ↔ Tests)
- V. Validation & Anomalies

---

## I. Introduction

### 1. Objet
Ce document définit les tests de validation fonctionnelle (recette) du site WordPress connecté à une base de données pour l’association “Les Amis de Virebent”. Il vise à s’assurer que les livrables correspondant aux User Stories et tâches GitHub sont conformes aux besoins.

### 2. Périmètre (User Stories)
- US (#60): Consulter une fiche produit/œuvre/mobilier/immeuble.
- US (#59): Rechercher des biens (immeubles/œuvres/mobiliers).
- US (#53): Créer et gérer des comptes adhérents (authentification).
- US (#48): Ajouter des contenus (CPT/admin panel).
- US (#71): Relier mobilier ↔ immeuble ↔ catalogues.
- Tâches liées aux galeries (#63, #64) et à l’import (#51).

### 3. Références
- Documentation technique SAE 5.1 (WordPress + DB).
- Issues GitHub (TSV fourni).
- Guides d’installation et d’utilisation (issues #65, #61, #58, #70).

---

## II. Pré-requis & Environnement

### 1. Pré-requis
- WordPress opérationnel (version LTS supportée).
- Accès admin WordPress (compte test “admin”).
- PHP 8.x, MySQL/MariaDB accessible.
- Plugins nécessaires activés (CPT, ACF ou équivalent si utilisé).
- Thème/child-theme ou templates actifs.
- Scripts d’import disponibles (pour #51).

### 2. Environnement de test
- Navigateur: Chrome/Firefox récents.
- OS: Windows/macOS/Linux.
- Environnement WordPress de recette (staging) avec BD isolée.
- SMTP ou mailcatcher pour vérifier emails (si nécessaire pour comptes).

### 3. Données de test
- Échantillons d’œuvres/immeubles/mobiliers/catalogues.
- Images “sensibles” vs “non sensibles” (pour galeries).
- Comptes test: 
  - admin (capacité complète),
  - membre bureau (lecture étendue),
  - visiteur (non connecté).
- CSV/Excel client pour import (#51).

---

## III. Plan de test par User Story

Note: Chaque cas liste Action | Résultat attendu | Pré-conditions | Résultat (à compléter en exécution).

### US‑P1: Consulter une fiche produit/œuvre/immeuble (Issue #60)
Sous‑tâches liées: #62 (récup info produit), #66 (affichage info), #63/#64 (galerie).

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| P1-T1 | Ouvrir une fiche (single) | La page affiche titre, description, média, champs spécifiques | CPT “product/œuvre/immeuble” enregistré et contenu publié |  |
| P1-T2 | Afficher galerie liée | La galerie affiche les images dans l’ordre | Galerie associée configurée |  |
| P1-T3 | Naviguer vers éléments liés (catalogues/galerie) | Les liens mènent aux pages associées | Relations enregistrées |  |
| P1-T4 | Absence de données sensibles pour public | Les images marquées “sensibles” n’apparaissent pas au public | Media flag “sensible” présent |  |
| P1-T5 | Affichage complet pour admin | L’admin voit tout (y compris sensibles) | Connecté en admin |  |
| P1-T6 | SEO/slug | Le slug est propre, la balise title et h1 cohérents | Thème actif et template single |  |

### US‑P2: Recherche (immeubles/œuvres/mobiliers) (Issue #59)
Sous‑tâches liées: #67 (formulaire), #68 (style), #69 (résultats), #70 (doc).

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| P2-T1 | Ouvrir la page de recherche | Formulaire avec champs pertinents (type, mots‑clés…) | Page de recherche publiée |  |
| P2-T2 | Rechercher avec critère type | Résultats filtrés par type (immeuble/œuvre/mobilier) | Contenus présents |  |
| P2-T3 | Rechercher par mots‑clés | Résultats pertinents via WP_Query/meta_query | Indexation basique WP |  |
| P2-T4 | Aucun résultat | Message informant “aucun résultat” | Critère volontairement rare |  |
| P2-T5 | Pagination | Résultats paginés et navigation OK | > N résultats |  |
| P2-T6 | Respect du flag “sensible” en search | Images sensibles non montrées au public | Images sensibles présentes |  |

### US‑P3: Comptes adhérents / Authentification (Issue #53)
Sous‑tâches: #54 (choix plugin), #55 (formulaire), #57 (liaison), #58 (docs).

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| P3-T1 | Choix plugin auth | Plugin d’auth compatible et activé | Étude faite (#54) |  |
| P3-T2 | Afficher le formulaire de connexion | Le formulaire s’affiche (shortcode/bloc) | Page de login publiée |  |
| P3-T3 | Connexion valide | Redirection vers tableau de bord / page prévue | Compte test valide |  |
| P3-T4 | Connexion invalide | Message d’erreur explicite | MDP incorrect |  |
| P3-T5 | Déconnexion | Session fermée, retour à la page publique | Compte connecté |  |
| P3-T6 | Rôles et capacités | Admin voit médias sensibles; public non | Rôles configurés |  |

### US‑P4: Administration contenus (ajout/édition) (Issue #48)
Sous‑tâches: #49 (choix plugins), #50 (config plugins), #51 (import), #61/#65 (docs).

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| P4-T1 | Accéder au back‑office CPT | Menus CPT visibles (Produit, Galerie, etc.) | CPT enregistrés |  |
| P4-T2 | Créer un contenu | Sauvegarde OK, statut en “publish/draft” selon choix | Champs requis remplis |  |
| P4-T3 | Éditer un contenu | Modifs persistées après sauvegarde | Contenu existant |  |
| P4-T4 | Supprimer un contenu | Contenu déplacé corbeille/supprimé | Droit suffisant |  |
| P4-T5 | UI Panel Admin plugin | Pages d’admin accessibles (si plugin panel #34) | Plugin installé |  |
| P4-T6 | Documentation | Guide d’installation et d’usage à jour | Issues #61/#65 |  |

### US‑P5: Relations (mobilier ↔ immeuble ↔ catalogues) (Issue #71)

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| P5-T1 | Éditer “mobilier” | Metabox Relations: Catalogues (multi), Immeuble (unique) | CPT “mobilier” actif |  |
| P5-T2 | Sauvegarder relations mobilier | Metas: catalogues = [IDs], immeuble = ID | Sélections effectuées |  |
| P5-T3 | Modifier relations | Anciennes valeurs remplacées proprement | Relations existantes |  |
| P5-T4 | Éditer “immeuble” | Onglet Catalogues (multi) uniquement | CPT “immeuble” actif |  |
| P5-T5 | Pas de relations sur “catalogue” | Aucune metabox Relations | CPT “catalogue” actif |  |
| P5-T6 | Helper get_relations() | Retour structure: {immeuble, catalogues[]} | Helper dispo |  |

### US‑P6: Galerie multi‑images + marquage “sensible” (Issues #63, #64)
Basé sur votre modèle “Cahier_recette.adoc”.

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| P6-T1 | Ajout multiple d’images | Miniatures listées, case “Image sensible”, bouton “Supprimer”, poignée de tri | CPT avec metabox galerie |  |
| P6-T2 | Persistance du flag sensible | Cases cochées restent cochées après reload (meta stockée) | Sauvegarde effectuée |  |
| P6-T3 | Tri par glisser‑déposer | Ordre conservé après reload (meta reflète l’ordre) | Drag‑drop actif |  |
| P6-T4 | Suppression image intermédiaire | Image disparait, meta nettoyée | Sauvegarde effectuée |  |
| P6-T5 | Galerie vidée | Meta supprimée, interface vide | Toutes images supprimées |  |
| P6-T6 | Robustesse indices | Plusieurs ajouts/suppressions/tri avant save → état final correct | Session d’édition |  |
| P6-T7 | Filtrage front public | Sans capacité, seules images non sensibles | Front affiche galerie |  |
| P6-T8 | Filtrage front admin | Admin voit sensibles + non sensibles | Rôle admin |  |

Capacités/rôles pour médias sensibles:

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| P6-C1 | Attribution capacité | Rôle admin possède `la_view_sensitive_media` | Hooks d’init rôles |  |
| P6-C2 | Respect en front | Différence de rendu selon capacité | Comptes configurés |  |

### US‑P7: Import des données client (Issue #51)
Parent: #48

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| P7-T1 | Charger un fichier source | Le script accepte CSV/Excel attendu (colonnes mappées) | Fichier conforme |  |
| P7-T2 | Simuler import à blanc (dry‑run) | Rapport de pré‑import: lignes OK/KO, erreurs de mapping | Option dry‑run si prévue |  |
| P7-T3 | Import réel | Posts/meta créés, logs d’insertion disponibles | BD accessible |  |
| P7-T4 | Idempotence partielle | Second import identique → pas de doublons (ou stratégie gérée) | Clés/identifiants gérés |  |
| P7-T5 | Liens/relations après import | Relations (galeries/catalogues/immeubles) créées si mappées | Données relationnelles présentes |  |
| P7-T6 | Rollback/erreurs | En cas d’erreur, rapport explicite et état BD cohérent | Gestion d’erreurs |  |

---

### (Annexe) Qualité et non-fonctionnel

| ID | Action | Résultat attendu | Pré-conditions | Résultat |
|---|---|---|---|---|
| Q-T1 | Temps d’affichage fiche | < 2s sur 10 items galerie (en staging) | Cache basique actif |  |
| Q-T2 | Sécurité médias sensibles | Aucun accès direct fichier sensible côté public (sans lien) | Protections médias |  |
| Q-T3 | Accessibilité | Images avec alt, contrastes OK (spot‑check) | Thème |  |
| Q-T4 | Responsive | Fiche/galerie s’affichent bien mobile/tablette | Thème responsive |  |

---

## IV. Traçabilité (User Stories ↔ Issues ↔ Tests)

- US #60 → Tâches #62, #66, #63, #64 → Tests P1‑T1..T6, P6‑T1..T8
- US #59 → Tâches #67, #68, #69, #70 → Tests P2‑T1..T6
- US #53 → Tâches #54, #55, #57, #58 → Tests P3‑T1..T6
- US #48 → Tâches #49, #50, #51, #61, #65 → Tests P4‑T1..T6, P7‑T1..T6
- US #71 → (relations) → Tests P5‑T1..T6

Chaque test conserve un ID unique pour faciliter le suivi d’exécution et le lien aux tickets GitHub.

---

## V. Validation & Anomalies

- Pour chaque test: consigner “Succès/Échec”, captures d’écran, logs éventuels.
- Anomalies: créer un ticket GitHub lié au test ID et à l’issue parente.
- Critères de passage: 100% des tests critiques (US #60, #59, #53, #48) en succès; 95% global.
