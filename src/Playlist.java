/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author jkingsbury
 */
public class Playlist 
{
	public List<Song> songs = new ArrayList<Song>();
    //public Song[] songs;                          //list of songs
    public ArrayList<Integer> shuffledList = new ArrayList<Integer>();
    public String title;                            //playlist title
    public double length;                           //length of whole playlist
    public boolean repeat;
    public boolean shuffle;
    public MediaPanel mediaPanel = new MediaPanel();
    
    public MediaPanel getMediaPanel(){
    	return mediaPanel;
    }
    
    public String toString(){
    	return title;
    }
    //Constructors
    public Playlist(List<Song> songs, String title)
    {
        this.title = title;
        this.songs = songs;
        repeat = false;
        shuffle = false;
    }
    //Empty playlist with only a title
    public Playlist(String title)
    {
        this.title = title;
        repeat = false;
        shuffle = false;
    }
    public String getTitle()
    {
        return title;
    }    
    public Object getSongs()
    {
        return songs;
    }
    public double getLength()
    {
        return length;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }
    public void setSongs(List<Song> songs)
    {
        this.songs = songs;
    }
    public void repeat()
    {
        repeat = true;
    }
    public void shuffle()
    {
        shuffle = !shuffle;
        if(shuffle){
	        int numSongs = songs.size();
	        for(int i = 0; i < numSongs; i++){
	        	int randomIndex = (int) (Math.random()*numSongs);
	        	while(shuffledList.contains(randomIndex)){
	            	randomIndex = (int) (Math.random()*numSongs);
	            }
	        	shuffledList.add(randomIndex);
	        }
        }
    }
    
    public class MediaPanel extends JPanel{

        public static final int LOCATION_COLUMN = 0;
        
        DefaultTableModel model;
        JTable table;
        JScrollPane scroll;
        Thread thread;
        int indexOfPlaying = -1;
        
        public List<Song> getSongs(){
        	return songs;
        }
        
        public int getLength()
        {
    		int length = ((Song)model.getValueAt(table.getSelectedRow(), LOCATION_COLUMN)).getLength();
    		return length;
        }
        
        public void setRepeatSong(boolean b){
        	if(indexOfPlaying != -1)
        		((Song)model.getValueAt(indexOfPlaying, LOCATION_COLUMN)).setRepeat(b);
        }
        
        public void setRepeatPlaylist(boolean b){
        	repeat = b;
        }
        
        public boolean skipSong(){
        	if (indexOfPlaying != -1 && (indexOfPlaying != table.getRowCount() - 1 || repeat)){
        		if(indexOfPlaying == table.getRowCount() - 1){
        			table.getSelectionModel().setSelectionInterval(0, 0);
        			return true;
        		}
        		else{
        			System.out.println("Stops currently playing");
	        		int selectedIndex = indexOfPlaying;
	        		table.getSelectionModel().setSelectionInterval(selectedIndex + 1, selectedIndex + 1);
	        		System.out.println("About to play the next song");
	        		return true;
        		}
        	}
        	return false;
        }
        
        public boolean prevSong(){
        	if (indexOfPlaying != -1 && indexOfPlaying != 0){
        		int selectedIndex = indexOfPlaying;
        		table.getSelectionModel().setSelectionInterval(selectedIndex - 1, selectedIndex - 1);
        		return true;
        	}
        	return false;
        }
        
        public void addNonDuplicateSong(Song theSong){
        	if(!songs.contains(theSong))
        		songs.add(theSong);
            model.addRow(new Object[] {theSong, theSong.getArtist(), theSong.getAlbum(),
                    theSong.getYear(), theSong.getLengthString(), theSong.getGenre(), 
                    theSong.getRating()});
        }
        
        public void addSong(Song theSong){
        	if(model.getRowCount() == 0){
        		if(!songs.contains(theSong))
            		songs.add(theSong);
                model.addRow(new Object[] {theSong, theSong.getArtist(), theSong.getAlbum(),
                        theSong.getYear(), theSong.getLengthString(), theSong.getGenre(), 
                        theSong.getRating()});
        	}
        	else
        	{
        		int count = 0;
    			
    	    	for(int i = 0; i < model.getRowCount(); i++)
    	    	{
    	    		if(model.getValueAt(i, 1).equals(theSong.getArtist())) {
    	    			count++;
    	    			if(model.getValueAt(i,2).equals(theSong.getAlbum()))
    	    			{
    	    					count++;

    	    			}
    					if(model.getValueAt(i, 3).equals(theSong.getYear()))
    						count++;
    					if(model.getValueAt(i, 4).equals(theSong.getLengthString()))
    						count++;
    	    		}

    	    	}
    	    	if(count >= 4)
    	    	{
    	    		  int choice = JOptionPane.showOptionDialog(null, 
    	    			      "This song already exists, add it anyway?", 
    	    			      "Duplicate Song Alert", 
    	    			      JOptionPane.YES_NO_OPTION, 
    	    			      JOptionPane.QUESTION_MESSAGE, 
    	    			      null, null, null);

    	    			  // interpret the user's choice
    	    			  if (choice == JOptionPane.YES_OPTION)
    	    			  {
    	    				  if(!songs.contains(theSong))
    	    		        		songs.add(theSong);
    	    		            model.addRow(new Object[] {theSong, theSong.getArtist(), theSong.getAlbum(),
    	    		                    theSong.getYear(), theSong.getLengthString(), theSong.getGenre(), 
    	    		                    theSong.getRating()});	
    	    			  }	    		
    	    	}
    	    	else{
    	    		if(!songs.contains(theSong))
    	        		songs.add(theSong);
    	            model.addRow(new Object[] {theSong, theSong.getArtist(), theSong.getAlbum(),
    	                    theSong.getYear(), theSong.getLengthString(), theSong.getGenre(), 
    	                    theSong.getRating()});
    	    	}
        	}
        }
        
        public void deleteSong(){
            if (table.getSelectedRow() != -1){
                //model.removeRow(table.getSelectedRow());            
                int returnVal = JOptionPane.showConfirmDialog(null,
                		"Are you sure you want to delete " + table.getValueAt(table.getSelectedRow(), LOCATION_COLUMN)
                		+ " by: " + table.getValueAt(table.getSelectedRow(), 1) + "?",
                		"Confirm Delete", JOptionPane.OK_CANCEL_OPTION);
                
                if(returnVal == 0){
                	if(indexOfPlaying == table.getSelectedRow()){
                		stopSong();
                	}
                	else if(indexOfPlaying > table.getSelectedRow()){
                		indexOfPlaying --;
                	}
                	int row = table.getSelectedRow();
                	model.removeRow(row);
                	songs.remove(row);
                }
            }
        }
        
        public void stopSong(){
        	if(indexOfPlaying != -1){
        		System.out.println("indexOfPlaying = " + indexOfPlaying);
        		((Song)model.getValueAt(indexOfPlaying, LOCATION_COLUMN)).stop();
        		indexOfPlaying = -1;
        	}
        }
        
        public void playSong(){
        	/*if(indexOfPlaying != -1){
        		System.out.println("indexOfPlaying = " + indexOfPlaying);
        		((Song)model.getValueAt(indexOfPlaying, LOCATION_COLUMN)).stop();
        	}*/
        	stopSong();
        	indexOfPlaying = table.getSelectedRow();
        	if(indexOfPlaying != -1){
        		do
        			((Song)model.getValueAt(indexOfPlaying, LOCATION_COLUMN)).play();
        		while(((Song)model.getValueAt(indexOfPlaying, LOCATION_COLUMN)).getRepeat() == true);
        	}
        }
        
        public void searchSong(String s){
        	s = s.toLowerCase();
        	for(int i = 0; i < model.getRowCount();){
        		//if(!model.getValueAt(i, LOCATION_COLUMN).toString().toLowerCase().contains(s)){
        		model.removeRow(i);
        		//}
        		//else
        			//i++;
        	}
        	for(Song song : songs){
        		if(song.toString().toLowerCase().contains(s)){
        			addNonDuplicateSong(song);
        		}
        	}
        }

        public MediaPanel() {
            model = new UneditableDefaultTableModel();
            table = new JTable(model);
            model.addColumn("Title");
            model.addColumn("Artist");
            model.addColumn("Album");
            model.addColumn("Year");
            model.addColumn("Length");
            model.addColumn("Genre");
            model.addColumn("Rating");
            
            scroll = new JScrollPane(table);
            add(scroll);
            
            table.addMouseListener(new MouseListener(){

    			@Override
    			public void mouseClicked(MouseEvent arg0) {
    				/*int index = table.getSelectedRow();
    				System.out.println("index = " + index);
    				//Object obj = model.getValueAt(0, 7);
    				//System.out.println(obj);
    				System.out.println(model.getValueAt(table.getSelectedRow(), LOCATION_COLUMN));
    				//playSong();
    				thread = new BasicThread(); TODO
    				thread.start();*/
    				
    			}

    			@Override
    			public void mouseEntered(MouseEvent arg0) {
    				// TODO Auto-generated method stub
    				
    			}

    			@Override
    			public void mouseExited(MouseEvent arg0) {
    				// TODO Auto-generated method stub
    				
    			}

    			@Override
    			public void mousePressed(MouseEvent arg0) {
    				// TODO Auto-generated method stub
    				
    			}

    			@Override
    			public void mouseReleased(MouseEvent arg0) {
    				// TODO Auto-generated method stub
    				
    			}
            });
        }
    	
    	public class BasicThread extends Thread{
        	/*public void run(){
        		do
        			playSong();
        		while(skipSong());
        		//repeat
        	}*/
        	
        	public void run(){
        		playSong();
        	}
        }
    	
    	public class UneditableDefaultTableModel extends DefaultTableModel{ 
    	  	@Override
    	 	public boolean isCellEditable(int row, int column){
    	 		return false;
    	 	} 
    	 }
    }
}
