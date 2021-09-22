package me.hardstyles.bot.base.spotify;

import com.neovisionaries.i18n.CountryCode;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.tracks.GetTrackRequest;
import me.hardstyles.bot.Bot;

import java.io.IOException;
import java.util.ArrayList;

public class SpotifyAPI {
    private final Bot bot;
    private long lastGen = 0;

    public SpotifyAPI(Bot bot) {
        this.bot = bot;
        this.generateNewSync();
    }

    private void invalidCheck() {
        if (System.currentTimeMillis() - (3600 * 1000) > lastGen) {
            this.generateNewSync();
        }
    }

    public String simpleTrack(Track track) {
        this.invalidCheck();
        StringBuilder builder = new StringBuilder();
        for (ArtistSimplified artist : track.getArtists()) {
            builder.append(artist.getName()).append(" ");
        }
        builder.append(" ").append(track.getName());

        return builder.toString();
    }

    public String simpleTrack(String id) {
        this.invalidCheck();
        Track track = getTrack(id);

        StringBuilder builder = new StringBuilder();
        for (ArtistSimplified artist : track.getArtists()) {
            builder.append(artist.getName()).append(" ");
        }
        builder.append(" ").append(track.getName());

        return builder.toString();
    }

    public Track getTrack(String id) {
        this.invalidCheck();
        System.out.println("Passed id" + id);
        GetTrackRequest trackRequest = spotifyApi.getTrack(id)
                .market(CountryCode.US)
                .build();
        try {
            return trackRequest.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<String> fuck(String id) {
        this.invalidCheck();
        try {
            ArrayList<String> list = new ArrayList<>();
            Playlist b = spotifyApi.getPlaylist(id).build().execute();
            int limit = 16;
            for (PlaylistTrack item : b.getTracks().getItems()) {
                limit--;
                if (limit == 0) break;
                Track track = getTrack(item.getTrack().getId());
                list.add(simpleTrack(track));
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static final String clientId = "e5a9e8bf3c464534b5f0a9f7e5828c20";
    private static final String clientSecret = "1d657e02eb3744f995ff6ea67ee3f66f";

    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyApi.clientCredentials()
            .build();

    private void generateNewSync() {
        try {
            lastGen = System.currentTimeMillis();
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();
            spotifyApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (org.apache.hc.core5.http.ParseException e) {
            e.printStackTrace();
        }
    }


}
