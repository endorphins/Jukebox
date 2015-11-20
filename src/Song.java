import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.*;
import javax.sound.sampled.*;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;


/**
 *
 * @author tpacheco
 */
public class Song {
    private String album;
    private String artist;
    private String genre;
    private int length;
    private String location;
    private int rating;
    private boolean repeat;
    private int timesPlayed;
    private String title;
    private int totalBytes;
    private String year;
    private FileInputStream file;
    private Player play;
    
    public Song(String l){
    	location = l;
    	try{
            file = new FileInputStream(location);
            totalBytes = file.available();
        } catch (Exception e){
            System.out.println(e);
        }
    	File testFile = new File(l);
    	MP3File mp3file = null;
        try {
			mp3file = (MP3File)AudioFileIO.read(testFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        MP3AudioHeader audioHeader = (MP3AudioHeader) mp3file.getAudioHeader();
        Tag tag = mp3file.getTag();
        album = tag.getFirst(FieldKey.ALBUM);
        artist = tag.getFirst(FieldKey.ARTIST);
        genre = tag.getFirst(FieldKey.GENRE);
        length = audioHeader.getTrackLength();
        rating = 0;
        repeat = false;
        timesPlayed = 0;
        title = tag.getFirst(FieldKey.TITLE);
        year = tag.getFirst(FieldKey.YEAR);
    }
    
    public void setAlbum(String a){
        album = a;
    }
    
    public void setArtist(String a){
        artist = a;
    }
    
    public void setGenre(String g){
        album = g;
    }
    
    public void setLocation(String l){
        location = l;
    }
    
    public void setRating(int r){
        rating = r;
    }
    
    public void setRepeat(boolean b){
    	repeat = b;
    }
    
    public void setTitle(String t){
        title = t;
    }
    
    public void setYear(String y){
        year = y;
    }
        
    public String getAlbum(){
        return album;
    }    
  
    public String getArtist(){
        return artist;
    }
    
    public String getGenre(){
        return genre;
    }
    
    public int getLength(){
        return length;
    }
    
    public String getLengthString(){
    	int modLength = length%60;
    	String sLength = length/60 + ":";
    	if(modLength >= 10)
    		sLength = sLength + modLength;
    	else
    		sLength = sLength + "0" + modLength;
    	return sLength;
    }
    
    public String getLocation(){
        return location;
    }
    
    public int getRating(){
        return rating;
    }
    
    public boolean getRepeat(){
    	return repeat;
    }
    
    public String getTitle(){
        return title;
    }
    
    public String getYear(){
        return year;
    }
    
    public Player getPlayer(){
    	return play;
    }
    
    public void play(){ //need to modify
    	//System.out.println(timesPlayed);
        try{
            file = new FileInputStream(location);
            //length = file.available();
            play = new Player(file);
            
            play.play();
        } catch (Exception e){
            System.out.println(e);
        }
        timesPlayed ++;
        //System.out.println(timesPlayed);
        //System.out.println("Playing " + this);
    }
    
    public void pause(){
        //do stuff
    	//int remaining = file.available();
    	//int currentPos = totalBytes - remaining;
    	System.out.println("Pausing");
    }
    
    public void stop(){
        //do stuff
        System.out.println("Stopping");
        //if(play != null){
        	play.close();
        //}
        play = null;
    }
    
    public void rewind(){
        //do stuff
        System.out.println("Rewinding");
    }
    
    public void fastforward(){
        //do stuff
        System.out.println("Fastforwarding");
    }
    
    public boolean nullPlayer(){
    	return play == null;
    }
    
    public void Wait() throws InterruptedException{
    	long time = 1000;
    	wait(time);
    }
    
    @Override
    public String toString(){
        return title;
    }    
}
