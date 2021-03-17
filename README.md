# Unbeaned

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Our app is a coffee review, similar to a social media app. Users are able to like and share coffee shops that you enjoyed and leave reviews. They can also use their location to discover new coffee shops and see proximity on a map.

### App Evaluation
- **Category:** Food/Social
- **Mobile:** Use Google Maps, Camera App, Locations
- **Story:** Allows users to discover new coffee shops near them and share their opinions.
- **Market:** Coffee Lovers
- **Habit:** Users can use the app whenever in need of a new cafe or coffee shop location and want the opinions of other coffee lovers.
- **Scope:** Viewing nearby coffee shops and showing reviews from other users. We hope that eventually users can review drinks from coffee shops after exposure.

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* Show a list of places to visit that are near the user
* Obtain geographical information from user
* Show a map of the place with places to visit being displayed
* Connect to Foursquare API and query based on keywords
* Ability for user to create an account
* Ability for users to save favorite places
* Ability to like places and write reviews
* Show similar places based on the currently viewed place
* Show nearby places in a search view

**Optional Nice-to-have Stories**

* Ability to have recommendations based on previously liked or recommended places.
* Recommender system that learns from previous choices
* Cache common requests reduce API calls

### 2. Screen Archetypes

* Login/SignUp Screen
   * Ability to create an account
* Feed/Timeline Screen
   * Lists recommended places to visit based on geographical location
   * Connect to Foursquare API and query based on keywords
   * Show similar places based on the currently viewed page
* Place detail View
    * Displays reviews (could be from Yelp or similar) of the place
    * Show place information through API
    * Cache commonly viewed details to reduce latency from API calls
* Search View
    * Displays closest places in a list with pictures or in a grid
* User Account
    * Displays user account, list of liked places, and options to change username and user information

### 3. Navigation

**Tab Navigation** (Tab to Screen)

* Search Places
* Nearby Places
* User Account

**Flow Navigation** (Screen to Screen)

* Login
   * Nearby Places
* Nearby Places
   * Detail Screens
       * Reviews Feed
       * Comments
   * Profile (from Reviews Card)
* User Page
    * Logout (to Login)
    * Detail Page of Reviews
    * Comments of reviews

## Wireframes
<img src="https://i.imgur.com/jHQqBuu.png" width=600>

### [BONUS] Digital Wireframes & Mockups

### [BONUS] Interactive Prototype

## Schema 
[This section will be completed in Unit 9]
### Models
[Add table of models]
### Networking
- [Add list of network requests by screen ]
- [Create basic snippets for each Parse network request]
- [OPTIONAL: List endpoints if using existing API such as Yelp]
