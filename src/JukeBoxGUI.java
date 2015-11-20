import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.event.*;

public class JukeBoxGUI{
	private ImageIcon prevIcon = new ImageIcon("../images/prev.jpg");
	private ImageIcon playIcon = new ImageIcon("../images/play.jpg");
	private ImageIcon stopIcon = new ImageIcon("../images/stop.jpg");
	private ImageIcon skipIcon = new ImageIcon("../images/skip.jpg");
	
	private MusicReader ioReader = new MusicReader();
	private Color backgroundColor = new Color(238, 238, 238);
	
	private JFrame frame = new JFrame("JukeBox");
	private JPanel mainPanel = new JPanel();	
	private JMenuBar menuBar = new JMenuBar();
	
	private JMenu fileMenu = new JMenu("File");
	private JMenuItem addPlaylist = new JMenuItem("Add Playlist");
	private JMenuItem delPlaylist = new JMenuItem("Delete Playlist");
	private JMenuItem addSong = new JMenuItem("Add Song");
	private JMenuItem delSong = new JMenuItem("Delete Song");
	
	private JMenu editMenu = new JMenu("Edit");
	private JMenuItem changeColor = new JMenuItem("Change Background Color");
	
	private JPanel leftPanel = new JPanel();
	private JPanel tLeftPanel = new JPanel();
	private JPanel tRightPanel = new JPanel();
	private JPanel rightPanel = new JPanel();
	private JPanel bLeftPanel = new JPanel();
	private JPanel bRightPanel = new JPanel(new CardLayout());
	
	private JTextField searchBar = new JTextField(20);
	private JButton searchButton = new JButton("Search");
	private JProgressBar progressBar = new JProgressBar();
	private JButton prev = new JButton(prevIcon);
	private JButton play = new JButton(playIcon);
	private JButton stop = new JButton(stopIcon);
	private JButton skip = new JButton(skipIcon);
	
	private DefaultListModel playlistModel = new DefaultListModel();
	private JList playList = new JList(playlistModel);
	private JScrollPane scrollPane = new JScrollPane(playList);
	private Playlist.MediaPanel mediaPanel;
	
	private JPanel checkBoxes = new JPanel();
	private CheckboxGroup repeats = new CheckboxGroup();
	private JCheckBox repeatSong = new JCheckBox("Repeat Song", false);
	private JCheckBox repeatPlaylist = new JCheckBox("Repeat Playlist", false);
	
	private Timer timer = null;	//timer for progressbar
	static int counter;

	private Thread thread;
	
	public JukeBoxGUI(){
		initPane();
	}
	
	private void initPane(){		
		fileMenu.add(addPlaylist);
		fileMenu.add(delPlaylist);
		fileMenu.add(addSong);
		fileMenu.add(delSong);
		delPlaylist.setEnabled(false);
		addPlaylist.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JPanel addPL = new JPanel();
				addPL.setLayout(new BoxLayout(addPL, BoxLayout.Y_AXIS));
				JLabel prompt = new JLabel("Enter the name of the playlist:");
				JTextField plName = new JTextField(20);
				addPL.add(prompt);
				addPL.add(plName);
				int returnVal = JOptionPane.showConfirmDialog(null, addPL,
						"New Playlist", JOptionPane.OK_CANCEL_OPTION);
				if(returnVal == 0){
					playlistModel.addElement(new Playlist(plName.getText()));
				}
			}			
		});
		delPlaylist.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(playList.getSelectedIndex() > -1){
					//playlistModel.removeElement(playList.getSelectedValue());
					int returnVal = JOptionPane.showConfirmDialog(null,
	                		"Are you sure you want to remove the playlist '" + playList.getSelectedValue() + "'?",
	                		"Confirm Removal", JOptionPane.OK_CANCEL_OPTION);	                
	                if(returnVal == 0){
	                	mediaPanel.stopSong();
	                	playlistModel.removeElement(playList.getSelectedValue());
	                	playList.getSelectionModel().setSelectionInterval(0, 0);
	                }
	                delPlaylist.setEnabled(false);
				}
			}			
		});	
		
		addSong.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				int returnVal = fileChooser.showOpenDialog(null);
				
				if(returnVal == JFileChooser.APPROVE_OPTION){
					File file = fileChooser.getSelectedFile();
					mediaPanel.addSong(new Song(file.getPath()));
					if(((Playlist)playlistModel.elementAt(0)).getMediaPanel() != mediaPanel){
						int i = 0;
						boolean exists = false;
						while(i < ((Playlist)playlistModel.elementAt(0)).getMediaPanel().table.getRowCount()){
							if(((Song)((Playlist)playlistModel.elementAt(0)).getMediaPanel().table.getValueAt(i, 0))
									.getLocation().equals(file.getPath())){
								exists = true;
							}
							i++;
						}
						if(!exists)
							((Playlist)playlistModel.elementAt(0)).getMediaPanel().addSong(new Song(file.getPath()));
					}
				}
			}			
		});
		
		delSong.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				mediaPanel.deleteSong();
			}			
		});
		
		editMenu.add(changeColor);
		changeColor.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				JColorChooser colorChooser = new JColorChooser();
				int returnVal = JOptionPane.showConfirmDialog(null,
	            		colorChooser, "Select a Color", JOptionPane.OK_CANCEL_OPTION);
				
				if(returnVal == 0){
					backgroundColor = colorChooser.getColor();
					tLeftPanel.setBackground(backgroundColor);
					bLeftPanel.setBackground(backgroundColor);
					leftPanel.setBackground(backgroundColor);
					tRightPanel.setBackground(backgroundColor);
					bRightPanel.setBackground(backgroundColor);
					rightPanel.setBackground(backgroundColor);
					playList.setBackground(new Color(backgroundColor.getRed(), 
							backgroundColor.getGreen(), backgroundColor.getBlue(), backgroundColor.getAlpha()/2));
					mediaPanel.setBackground(backgroundColor);
					mediaPanel.table.getParent().setBackground(new Color(backgroundColor.getRed(), 
							backgroundColor.getGreen(), backgroundColor.getBlue(), backgroundColor.getAlpha()/2));
					checkBoxes.setBackground(backgroundColor);
					repeatSong.setBackground(backgroundColor);
					repeatPlaylist.setBackground(backgroundColor);
					mainPanel.setBackground(backgroundColor);
					frame.validate();
					frame.repaint();
				}
			}
		});
		
		menuBar.add(fileMenu);
		menuBar.add(editMenu);
		
		tLeftPanel.setLayout(new BoxLayout(tLeftPanel, BoxLayout.X_AXIS));
		tLeftPanel.add(Box.createHorizontalStrut(25));
		tLeftPanel.add(searchBar);
		tLeftPanel.add(searchButton);
		tLeftPanel.setBackground(backgroundColor);
		
		searchBar.setMaximumSize(new Dimension(1000, 20));
		searchBar.setMinimumSize(new Dimension(1000, 20));
		
		bLeftPanel.setPreferredSize(new Dimension(150, 430));
		bLeftPanel.setLayout(new BoxLayout(bLeftPanel, BoxLayout.Y_AXIS));
		bLeftPanel.setBackground(backgroundColor);
		bLeftPanel.add(scrollPane);
		playList.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if(mediaPanel != ((Playlist) playList.getSelectedValue()).getMediaPanel()){
					bRightPanel.add(((Playlist) playList.getSelectedValue()).getMediaPanel(),
							((Playlist) playList.getSelectedValue()).toString());
					CardLayout cl = (CardLayout)(bRightPanel.getLayout());
					cl.show(bRightPanel, ((Playlist) playList.getSelectedValue()).toString());
					mediaPanel = ((Playlist) playList.getSelectedValue()).getMediaPanel();
					if(playList.getSelectedIndex() == 0)
						delPlaylist.setEnabled(false);
					else
						delPlaylist.setEnabled(true);
					String searchString = searchBar.getText();
					mediaPanel.searchSong(searchString);
					initKeyListeners();
					if(timer != null)
					{
						timer.stop();
					}
				}
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
		ioReader.loadPlaylist(playlistModel);
		/*playList.getSelectionModel().setSelectionInterval(0, 0);
		mediaPanel = ((Playlist) playList.getSelectedValue()).getMediaPanel();//*/
		if(playlistModel.getSize() < 1)
		{
			playlistModel.addElement(new Playlist("Main Library"));	
		}
		playList.getSelectionModel().setSelectionInterval(0, 0);
		mediaPanel = ((Playlist) playList.getSelectedValue()).getMediaPanel();//*/
		mediaPanel.setBackground(backgroundColor);
		
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		leftPanel.add(tLeftPanel);
		leftPanel.add(Box.createVerticalStrut(25));
		leftPanel.add(bLeftPanel);
		leftPanel.setBackground(backgroundColor);
		
		tRightPanel.setLayout(new BoxLayout(tRightPanel, BoxLayout.X_AXIS));
		tRightPanel.add(progressBar);
		progressBar.setIndeterminate(true);
		tRightPanel.add(Box.createHorizontalStrut(25));
		tRightPanel.add(prev);
		tRightPanel.add(play);
		tRightPanel.add(stop);
		tRightPanel.add(skip);
		tRightPanel.add(Box.createHorizontalStrut(25));
		tRightPanel.setBackground(backgroundColor);
		
		bRightPanel.setPreferredSize(new Dimension(460, 430));
		bRightPanel.add(mediaPanel, "Default Library");
		bRightPanel.setBackground(backgroundColor);
		
		checkBoxes.add(repeatSong);
		checkBoxes.add(repeatPlaylist);
		checkBoxes.setBackground(backgroundColor);
				
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.add(tRightPanel);
		rightPanel.add(Box.createVerticalStrut(12));
		rightPanel.add(bRightPanel);
		rightPanel.add(Box.createVerticalStrut(5));
		rightPanel.add(checkBoxes);
		rightPanel.setBackground(backgroundColor);
		
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		mainPanel.add(leftPanel);
		mainPanel.add(Box.createHorizontalStrut(100));
		mainPanel.add(rightPanel);
		mainPanel.setBackground(backgroundColor);
		
		initKeyListeners();		
		
		frame.setJMenuBar(menuBar);
		frame.add(mainPanel);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.pack();
		
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				System.out.println("CLOSING SAVE NOW!");
				ioReader.savePlaylists(playlistModel);
			}
		});
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		searchButton.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String searchString = searchBar.getText();
				mediaPanel.searchSong(searchString);
			}			
		});
		
		prev.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				prevHelper();	
				mediaPanel.table.requestFocus();
			}			
		});
		
		play.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				if(timer != null)
					timer.stop();
				for(int i = 0; i < bRightPanel.getComponentCount(); i++){
					mediaPanel = (Playlist.MediaPanel) bRightPanel.getComponent(i);
					mediaPanel.stopSong();
				}
				((Song)mediaPanel.model.getValueAt(mediaPanel.table.getSelectedRow(), mediaPanel.LOCATION_COLUMN))
					.setRepeat(repeatSong.isSelected());
				mediaPanel.setRepeatPlaylist(repeatPlaylist.isSelected());
				thread = mediaPanel.new BasicThread();
				thread.start();
				progressBar();
				mediaPanel.table.requestFocus();
			}			
		});
		
		stop.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				stopHelper();
				mediaPanel.table.requestFocus();
			}			
		});
		
		skip.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				skipHelper();
				mediaPanel.table.requestFocus();
			}			
		});
		
		repeatSong.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				if(repeatSong.isSelected())
					repeatPlaylist.setSelected(false);
				mediaPanel.setRepeatSong(repeatSong.isSelected());
				mediaPanel.table.requestFocus();
			}			
		});
		
		repeatPlaylist.addItemListener(new ItemListener(){
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if(repeatPlaylist.isSelected())
					repeatSong.setSelected(false);
				mediaPanel.setRepeatPlaylist(repeatPlaylist.isSelected());
				mediaPanel.table.requestFocus();
			}			
		});		

		prev.setContentAreaFilled(false);
		prev.setBorderPainted(false);
		play.setContentAreaFilled(false);
		play.setBorderPainted(false);
		stop.setContentAreaFilled(false);
		stop.setBorderPainted(false);
		skip.setContentAreaFilled(false);
		skip.setBorderPainted(false);
	}
	
	/*Does all the work of actually playing, action listener calls this*/
	public void playHelper(){
		/*if(timer != null)
			progressBarStop();
		for(int i = 0; i < bRightPanel.getComponentCount(); i++){
			mediaPanel = (Playlist.MediaPanel) bRightPanel.getComponent(i);
			mediaPanel.stopSong();
		}
		progressBar.setValue(0);
		resetCounter();
		thread = mediaPanel.new BasicThread();
		thread.start();
		progressBar();*/
		
		if(timer != null)
			timer.stop();
		for(int i = 0; i < bRightPanel.getComponentCount(); i++){
			mediaPanel = (Playlist.MediaPanel) bRightPanel.getComponent(i);
			mediaPanel.stopSong();
		}
		((Song)mediaPanel.model.getValueAt(mediaPanel.table.getSelectedRow(), mediaPanel.LOCATION_COLUMN))
			.setRepeat(repeatSong.isSelected());
		mediaPanel.setRepeatPlaylist(repeatPlaylist.isSelected());
		thread = mediaPanel.new BasicThread();
		thread.start();
		progressBar();
		mediaPanel.table.requestFocus();
	}
	
	/*Does all the work of actually stopping, action listener calls this*/
	public void stopHelper(){
		progressBarStop();
		for(int i = 0; i < bRightPanel.getComponentCount(); i++){
			mediaPanel = (Playlist.MediaPanel) bRightPanel.getComponent(i);
			mediaPanel.stopSong();
		}
	}
	
	/*Does all the work of actually skipping, action listener calls this*/
	public void skipHelper(){
		//mediaPanel = (Playlist.MediaPanel) bRightPanel.getComponent(0);
		if(mediaPanel.skipSong()){ //try to skip a song, if you succeed, play the next song
			progressBar.setValue(0);
			resetCounter();
			thread = mediaPanel.new BasicThread();
			thread.start();
		}
	}
	
	/*Does all the work of actually skipping, action listener calls this*/
	public void prevHelper(){
		//mediaPanel = (Playlist.MediaPanel) bRightPanel.getComponent(0);
		if(mediaPanel.prevSong()){ //try to skip a song, if you succeed, play the next song
			progressBar.setValue(0);
			resetCounter();
			thread = mediaPanel.new BasicThread();
			thread.start();
		}
	}
	
	public void initKeyListeners(){
		//mediaPanel = (Playlist.MediaPanel) bRightPanel.getComponent(0);
		//mediaPanel = ((Playlist)playList.getSelectedValue()).getMediaPanel();
		mediaPanel.table.removeKeyListener(mediaPanel.table.getKeyListeners()[0]);
		mediaPanel.table.setFocusable(true);
		mediaPanel.table.setFocusTraversalKeysEnabled(false);
		
		//This is where the Key listeners for Jtable get modified
		//mediaPanel.table.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0));
		//mediaPanel.table.getInputMap().remove(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));

		mediaPanel.table.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_RIGHT){
					System.out.println("Right");
					skipHelper();
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT){
					System.out.println("Left");
					prevHelper();
				}
				else if (e.getKeyCode() == KeyEvent.VK_SPACE){
					playHelper();
					System.out.println("Space");					
				}
				else if (e.getKeyCode() == KeyEvent.VK_S){
					stopHelper();
					System.out.println("S");
				}
			}
		});
	}
	public void progressBar(){
		final int length;
		counter = 0;
		progressBar.setIndeterminate(false);
		length = mediaPanel.getLength(); 
		progressBar.setMaximum(length);
		progressBar.setValue(0);
		
		System.out.println("Length" + length);
		
		timer = new Timer(1000, new ActionListener() {
			  public void actionPerformed(ActionEvent evt) {
				  if (counter == length){
				  
					  timer.stop();
					  progressBar.setValue(0);
				  }
				  System.out.println("Counter: " + counter);
				  counter ++;
				  progressBar.setValue(counter);
			  }
			 });
		timer.start();
	}
	
	public void progressBarStop(){
		timer.stop();
		resetCounter();
		progressBar.setValue(0);
		progressBar.setIndeterminate(true);
	}
	public void resetCounter(){ //for timer on progress bar
		counter = 0;
	}

	
	public static void main(String[] args){
		JukeBoxGUI JukeBox = new JukeBoxGUI();
	}
	
	
}