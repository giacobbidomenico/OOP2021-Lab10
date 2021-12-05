package it.unibo.oop.lab.lambda.ex02;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class representing a musical group.
 */
public final class MusicGroupImpl implements MusicGroup {

    private final Map<String, Integer> albums = new HashMap<>();
    private final Set<Song> songs = new HashSet<>();
    /**
     * {@inheritDoc}
     */
    @Override
    public void addAlbum(final String albumName, final int year) {
        this.albums.put(albumName, year);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSong(final String songName, final Optional<String> albumName, final double duration) {
        if (albumName.isPresent() && !this.albums.containsKey(albumName.get())) {
            throw new IllegalArgumentException("invalid album name");
        }
        this.songs.add(new MusicGroupImpl.Song(songName, albumName, duration));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<String> orderedSongNames() {
        return this.songs.stream().map(e -> e.getSongName()).sorted();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<String> albumNames() {
        return this.albums.keySet().stream();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Stream<String> albumInYear(final int year) {
        return this.albums.entrySet().stream().filter(e -> e.getValue() == year).map(e -> e.getKey());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int countSongs(final String albumName) {
        return (int) this.songs.stream()
                .filter(song -> song.getAlbumName().isPresent())
                .filter(song -> song.getAlbumName().get().equals(albumName))
                .count();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int countSongsInNoAlbum() {
        return (int) this.songs.stream()
                .filter(song -> song.getAlbumName()
                .isEmpty()).count();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public OptionalDouble averageDurationOfSongs(final String albumName) {
        return this.songs.stream()
                .filter(song -> song.getAlbumName().isPresent())
                .filter(song -> song.getAlbumName().get().equals(albumName))
                .mapToDouble(song -> song.getDuration())
                .average();
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> longestSong() {
        return this.songs.stream()
                .max((x, y) -> Double.compare(x.getDuration(), y.getDuration()))
                .map(song -> song.getSongName());
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> longestAlbum() {
        return this.songs.stream()
                .filter(x -> x.getAlbumName().isPresent())
                .collect(Collectors.groupingBy(Song :: getAlbumName, Collectors.summingDouble(Song :: getDuration)))
                .entrySet().stream()
                .max((x, y) -> Double.compare(x.getValue(), y.getValue()))
                .flatMap(x -> x.getKey());
    }
    /**
     * Class representing a song.
     */
    private static final class Song {

        private final String songName;
        private final Optional<String> albumName;
        private final double duration;
        private int hash;
        /**
         * Builds a new {@link Song}.
         * 
         * @param name
         * @param album
         * @param len
         */
        Song(final String name, final Optional<String> album, final double len) {
            super();
            this.songName = name;
            this.albumName = album;
            this.duration = len;
        }
        /**
         * Method that returns the name of the song.
         * 
         * @return the name of the song
         */
        public String getSongName() {
            return songName;
        }
        /**
         * Method that returns the name of the album where is the song.
         * 
         * @return the name of the album where is the song
         */
        public Optional<String> getAlbumName() {
            return albumName;
        }
        /**
         * Method that returns the duration of the song.
         * 
         * @return the duration of the song.
         */
        public double getDuration() {
            return duration;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            if (hash == 0) {
                hash = songName.hashCode() ^ albumName.hashCode() ^ Double.hashCode(duration);
            }
            return hash;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof Song) {
                final Song other = (Song) obj;
                return albumName.equals(other.albumName) && songName.equals(other.songName)
                        && duration == other.duration;
            }
            return false;
        }
        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return "Song [songName=" + songName + ", albumName=" + albumName + ", duration=" + duration + "]";
        }

    }

}
