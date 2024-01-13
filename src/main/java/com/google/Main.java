package com.google;

import com.google.cloudsql.CloudSQLDataSource;
import com.google.dao.AlbumsDao;
import com.google.dao.SingersDao;
import com.google.dao.SongsDao;
import com.google.models.Album;
import com.google.models.Singer;
import com.google.models.Song;
import com.google.spanner.SpannerDataSource;
import java.sql.SQLException;
import javax.sql.DataSource;

public class Main {

  public static void main(String[] args) {
    try {
      DataSource dataSource = args[0].equals("spanner") ? SpannerDataSource.createConnectionPool()
          : CloudSQLDataSource.createConnectionPool();
      SingersDao singersDao = new SingersDao(dataSource);
      AlbumsDao albumsDao = new AlbumsDao(dataSource);
      SongsDao songsDao = new SongsDao(dataSource);

      // Empties the current database
      songsDao.deleteAll();
      albumsDao.deleteAll();
      singersDao.deleteAll();

      // Inserts a couple of singers
      long singerId1 = singersDao.insert(new Singer("Marc", "Richards"));
      long singerId2 = singersDao.insert(new Singer("Catalina", "Smith"));

      // Inserts a couple of albums
      Album.Id albumId1 = albumsDao.insert(new Album(singerId1, 1, "Total Junk"));
      Album.Id albumId2 = albumsDao.insert(new Album(singerId1, 2, "Go, Go, Go"));
      Album.Id albumId3 = albumsDao.insert(new Album(singerId2, 1, "Green"));
      Album.Id albumId4 = albumsDao.insert(new Album(singerId2, 2, "Forever Hold Your Peace"));
      Album.Id albumId5 = albumsDao.insert(new Album(singerId2, 3, "Terrified"));

      // Inserts a couple of songs
      songsDao.insert(new Song(albumId2, 1, "42", "{\"genre\": \"rock\"}"));
      songsDao.insert(new Song(albumId2, 2, "Nothing Is The Same", "{\"genre\": \"pop\"}"));
      songsDao.insert(new Song(albumId3, 1, "Let's Get Back Together", "{\"genre\": \"rock\"}"));
      songsDao.insert(new Song(albumId3, 2, "Starting Again", "{\"genre\": \"rock\"}"));
      songsDao.insert(new Song(albumId3, 3, "I Knew You Were Magic", "{\"genre\": \"pop\"}"));
      songsDao.insert(new Song(albumId5, 1, "Fight Story", "{\"genre\": \"metal\"}"));

      // Displays the inserted data
      for (Singer singer : singersDao.findAll()) {
        System.out.println(singer);
        for (Album album : albumsDao.findBySinger(singer.getSingerId())) {
          System.out.println(album);
          for (Song song : songsDao.findByAlbum(album.getId())) {
            System.out.println(song);
          }
        }
        System.out.println();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}


