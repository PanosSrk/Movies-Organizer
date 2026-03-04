# MyMovies 🎬

Android application for discovering movies and organizing them into personal collections.

Developed for the course **Mobile Application Development**.

---

## Overview

MyMovies allows users to:

- Browse popular, top-rated, and upcoming movies  
- Search movies by title  
- Filter movies by category  
- Sort results (by release date, title, rating)  
- Save movies into custom collections  
- Switch between Light and Dark theme  

The app is designed to be simple, clean, and user-friendly.

---

## Features

### Home
- Popular movies  
- Upcoming movies  
- Top Rated movies  
- Movie details screen  

### Discover
- Search by title  
- Browse by category  
- Sorting options  

### Collections
- Default lists (Favorites, Seen)  
- Create custom collections  
- Delete collections  
- Add / Remove movies  
- Persistent local storage (SQLite)  

### Theme
- Light / Dark mode toggle  

---

## Architecture

### Core Components

- `MainActivity`  
  Hosts:
  - `HomeFragment`
  - `DiscoverFragment`
  - `CollectionsFragment`
  Handles theme switching via `ThemeHelper`.

- `MovieApiManager`  
  Fetches movie data from the MovieDB API:
  - id
  - title
  - poster
  - release date
  - rating
  - overview

- `MovieAdapter`  
  RecyclerView adapter for displaying movie lists.

- `MovieDetailsActivity`  
  Displays detailed movie information and allows adding to collections.

- `Collections`  
  Handles collection logic (add/delete collections and movies).

- `MyDBhandler`  
  Manages SQLite database and queries.

---

## Tech Stack

- Java  
- Android SDK  
- Fragments  
- RecyclerView  
- SQLite  
- MovieDB API  
- Material Design  

---

## Authors

- Panagiotis / Konstantinos Syrakis  
and 2 more 

---

## Future Improvements

- User authentication  
- Cloud sync  
- Trailer integration  
- UI/UX enhancements  
