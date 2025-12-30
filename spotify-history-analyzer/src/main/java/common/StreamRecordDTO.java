package common;
import java.io.Serializable;
import java.util.Set;
public class StreamRecordDTO implements Serializable{
    public String ts;
    public String platform;
    public long ms_played;
    public String conn_country;
    public String ip_addr;
    public String master_metadata_track_name;
    public String master_metadata_album_artist_name;
    public String master_metadata_album_album_name;
    public String spotify_track_uri;
    public String episode_name;
    public String episode_show_name;
    public String audiobook_title;
    public String audiobook_uri;
    public String audiobook_chapter_uri;
    public String audiobook_chapter_title;
    public String reason_start;
    public String reason_end;
    public Boolean shuffle;
    public Boolean skipped;
    public Boolean offline;
    public String offline_timestamp;
    public Boolean incognito_mode;
    public Set<String> recent;
    
@Override
    public String toString() {
        return "Canzone: " + master_metadata_track_name + " - " + ms_played + "ms";
    }
}
