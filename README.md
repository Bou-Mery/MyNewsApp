# NewsApp

Une application de recherche d'actualités permettant aux utilisateurs de consulter et de rechercher des articles récents de manière simple et interactive.

## 📖 Aperçu du Projet

**NewsApp** est une application mobile Android développée en Kotlin, qui permet aux utilisateurs de rechercher et de consulter les dernières actualités par mot-clé. Les utilisateurs peuvent également ajouter leurs articles préférés aux favoris et les gérer via des options de suppression par swipe, le tout dans une interface moderne et intuitive.

## ✨ Fonctionnalités

- **Recherche d'Articles** : Recherche d'actualités basée sur des mots-clés saisis par l'utilisateur.
- **Consulter les Titres** : Affichage de la liste des titres d'articles pour une vue rapide des dernières nouvelles.
- **Détails de l'Article** : Cliquez sur un titre pour ouvrir une vue détaillée de l'article complet.
- **Favoris** : Ajout d'articles aux favoris pour un accès facile aux articles préférés.
- **Suppression des Favoris par Glissement** : Les articles favoris peuvent être supprimés en swipant l'élément dans la liste.
- **Gestion des Erreurs** : Affichage de messages d'erreur en cas de problèmes de connexion ou d'erreurs de requête.

## 🚀 Installation

1. **Cloner le dépôt :**
   ```bash
   git clone  https://github.com/Bou-Mery/MyNewsApp.git
   
   ```

2. **Ouvrir le projet :** Importez le projet dans Android Studio.

3. **Installer les dépendances :** Android Studio détectera automatiquement les dépendances et vous proposera d'installer les versions requises.

## ⚙️ Configuration

1. **API News** : Cette application utilise une API de nouvelles. Inscrivez-vous pour obtenir une clé d'API et ajoutez-la dans votre projet.

2. **Permissions Réseau** : Assurez-vous que les permissions Internet sont ajoutées dans le fichier `AndroidManifest.xml` :
   ```xml
   <uses-permission android:name="android.permission.INTERNET"/>
   ```

## 💻 Utilisation

1. **Démarrer l'application** : Lancez l'application depuis Android Studio.
2. **Utiliser la Recherche** : Entrez un mot-clé dans la barre de recherche pour trouver des articles spécifiques.
3. **Consulter les articles** : Cliquez sur un titre pour afficher l'article complet.
4. **Ajouter aux Favoris** : Sélectionnez l'icône des favoris pour enregistrer un article pour une lecture ultérieure.
5. **Supprimer un Favori** : Faites glisser un article favori vers la gauche ou la droite pour le retirer de la liste.
6. **Gestion des erreurs** : En cas de problème de connexion, un message d'erreur s'affiche et un bouton de réessai est disponible.

## 🛠️ Technologies

- **Kotlin** : Langage principal utilisé pour le développement.
- **Android Jetpack Components** :
  - *ViewModel* et *LiveData* : Pour une gestion efficace de l'interface utilisateur et des données.
  - *Navigation Component* : Pour une navigation facile entre les écrans.
- **Retrofit** : Client HTTP pour les appels à l'API.
- **Coroutines** : Gestion des tâches asynchrones pour les appels réseau.
- **Glide** : Chargement des images.
- **RecyclerView** : Affichage des articles et gestion des favoris.

## 🧩 Dépendances
Ajoutez les dépendances suivantes à votre fichier build.gradle :

```
dependencies {
    
    // Architectural Components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Room
    implementation ("androidx.room:room-runtime:2.6.0")
    ksp ("androidx.room:room-compiler:2.6.0")

    // Kotlin Extensions and Coroutines support for Room
    implementation ("androidx.room:room-ktx:2.6.0")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")

    // Coroutine Lifecycle Scopes
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.5.0")

    // Navigation Components
    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.5")

    // Glide
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    ksp ("com.github.bumptech.glide:compiler:4.12.0")
    }
```
N'oubliez pas de synchroniser votre projet après avoir ajouté ces dépendances.

## 📜 Auteur

BOUKHRAIS Meryem

---
