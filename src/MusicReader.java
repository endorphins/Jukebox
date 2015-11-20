import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class MusicReader{
	private BufferedWriter outfile;
	private File infile;
	private String playlistsPath = System.getProperty("user.home") + "/playlists.txt";
	public String eol = System.getProperty("line.separator");
	
	public void savePlaylists(DefaultListModel playlist){
		try{
        	outfile = new BufferedWriter(new FileWriter(playlistsPath));
        }catch(IOException e){
            e.printStackTrace();
            System.exit(1);
        }
		
		for(int i = 0; i < playlist.getSize(); i++){
			try{
                outfile.write(playlist.getElementAt(i) + eol);
                int n = 0;
                List<Song> songList = ((Playlist)playlist.getElementAt(i)).songs;
                while(n < songList.size()){
                	outfile.write(songList.get(n).getLocation() + eol);
                	
                	n++;
                }
                outfile.write(eol);
            }catch(IOException e){
                System.out.println("Saving Task: Error, IOException!");
            }
		}
		
		try{
            outfile.close();
        }catch(IOException e){
            System.out.println("Could not close outfile.");
        }
	}
	
	public void loadPlaylist(DefaultListModel playlist){
		Scanner in = null;
		infile = new File(playlistsPath);
		//if it hasn't been made before, make it
		if(!infile.exists())
		{
			savePlaylists(playlist);
		}
		try{
            in = new Scanner(infile);
        } catch(FileNotFoundException e){
            e.printStackTrace();
            System.exit(1);
        }
		int n = 0;
		while(in.hasNextLine()){
			String s = in.nextLine();
			if(s.contains("\\"+"\\") || s.contains("//"))
				((Playlist)playlist.getElementAt(n)).getMediaPanel().addNonDuplicateSong(new Song(s));
			else if(s.contains("\\") || s.contains("/")){
				StringBuilder tempBuff = new StringBuilder(s);
				for(int i = 0; i < tempBuff.length(); i++){
					if(tempBuff.charAt(i) == '\\'){
						tempBuff.insert(i, '\\');
						i++;
					}
					else if(tempBuff.charAt(i) == '/'){
						tempBuff.insert(i, '/');
						i++;
					}
				}
				((Playlist)playlist.getElementAt(n)).getMediaPanel().addNonDuplicateSong(new Song(tempBuff.toString()));				
			}
			else if(s.equals(""))
				n++;
			else
				playlist.addElement(new Playlist(s));
		}
	}
}