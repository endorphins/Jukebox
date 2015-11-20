import java.io.File;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class SongTester{
    public static void main(String[] args){
    	//File testFile = new File("C:\\Users\\Tyler\\Documents\\Java\\Jukebox\\src\\test.mp3");
        Song song = new Song("C:\\Users\\Tyler\\Documents\\Java\\Jukebox\\src\\test.mp3");
        /*MP3File mp3file = null;
        try {
			mp3file = (MP3File)AudioFileIO.read(testFile);
		} catch (CannotReadException | IOException | TagException
				| ReadOnlyFileException | InvalidAudioFrameException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        MP3AudioHeader audioHeader = (MP3AudioHeader) mp3file.getAudioHeader();
        Tag tag = mp3file.getTag();
        int trackLength = audioHeader.getTrackLength();
        String album = tag.getFirst(FieldKey.ALBUM);
        String artist = tag.getFirst(FieldKey.ARTIST);
        String title = tag.getFirst(FieldKey.TITLE);
        String year = tag.getFirst(FieldKey.YEAR);
        System.out.println(trackLength);
        System.out.println(album);
        System.out.println(artist);
        System.out.println(title);
        System.out.println(year);*/
        
        
        song.play();
        
        /*JFrame frame = new JFrame("Test");
        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        model.addColumn("Col1");
        model.addColumn("Col2");
        String[] row0 = {"first","second"};
        String[] row1 = {"one","two","three"};
        String[] row2 = {"uno", "dos"};
        model.addRow(row0);
        model.addRow(row1);
        JScrollPane scrollPane = new JScrollPane(table);
        model.addRow(row2);
        frame.add(scrollPane);
        frame.pack();
        frame.setVisible(true);*/
    }
}