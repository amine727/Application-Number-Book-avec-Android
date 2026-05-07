# TP 20 — Application Number Book

**Cours :** Programmation Mobile — Android avec Java

---

## 📋 Contexte général

Ce lab consiste à développer une application Android nommée **Number Book**. L'application lit les contacts enregistrés dans le téléphone, les affiche dans une interface mobile, puis les envoie vers un serveur distant pour les stocker dans une base de données. Une fois les données enregistrées, l'application permet d'effectuer une recherche distante par **nom** ou **numéro de téléphone**.

---

## 📱 Aperçu de l'application

<img width="339" height="655" alt="1" src="https://github.com/user-attachments/assets/ea852c71-505d-43bc-bd91-76e8520c44aa" />

---

## 🎯 Objectifs pédagogiques

- Accéder aux données système Android (contacts du téléphone)
- Gérer les permissions d'accès à l'exécution
- Mettre en place une communication client/serveur
- Sérialiser et désérialiser des données JSON
- Utiliser **Retrofit** pour consommer une API REST
- Persister des données dans une base MySQL distante

---

## 🏗️ Architecture du projet

### Backend PHP — `numberbook-api/`

| Dossier | Rôle |
|---|---|
| `config/` | Connexion PDO à MySQL |
| `model/` | Objet métier Contact |
| `service/` | Logique d'accès aux données (CRUD) |
| `api/` | Points d'entrée HTTP (insertContact, getAllContacts, searchContact) |

### Application Android

| Fichier | Rôle |
|---|---|
| `Contact.java` | Modèle de données |
| `ApiResponse.java` | Modèle de réponse JSON du serveur |
| `ContactApi.java` | Interface Retrofit (définition des endpoints) |
| `RetrofitClient.java` | Singleton Retrofit |
| `ContactAdapter.java` | Adapter pour le RecyclerView |
| `MainActivity.java` | Logique principale de l'application |

---

## 🔄 Scénario de fonctionnement

1. L'utilisateur appuie sur **"Charger les contacts"** → l'app lit les contacts du téléphone
2. L'utilisateur appuie sur **"Synchroniser vers le serveur"** → les contacts sont envoyés à l'API PHP via Retrofit
3. L'utilisateur saisit un mot-clé et appuie sur **"Rechercher"** → l'app interroge le serveur et affiche les résultats

---

## 🛠️ Technologies utilisées

- **Android Studio** — Java
- **Retrofit 2** — Communication HTTP
- **Gson** — Sérialisation JSON
- **RecyclerView** — Affichage de la liste
- **PHP / PDO** — Backend serveur
- **MySQL** — Base de données distante

---

## ⚙️ Configuration requise

- Android API minimum : 21
- Serveur local : XAMPP ou WAMP
- Modifier l'adresse IP dans `RetrofitClient.java` selon votre réseau local
- Activer `usesCleartextTraffic` dans le `AndroidManifest.xml` si le serveur est en HTTP

---

