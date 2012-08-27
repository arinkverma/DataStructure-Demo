/* Application with find out the synset of the word. According to WordNet, a synset or synonym set is defined 
 * as a set of one or more synonyms that are interchangeable in some context without changing the truth value 
 * of the proposition in which they are embedded. * 
 * All the words get inserted into Tries data structure with file offset in tail, pointing to its synonym
 * 
 * @author ArinkVerma 
 * */

package in.arinkverma.Synonyms;
import in.arinkverma.Synonyms.datastructure.Tries;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Synonyms{
	private static final int SkipHeaderOffset = 1740;
	
	/*TRIES for different part of speech
	---------------------------------------
	*  verb -> T[0]
	*  noun -> T[1]
	*  adj  -> T[2]
	*  adv  -> T[3]
	---------------------------------------*/
	private static	 Tries []tries = new Tries[4];
	private static final	String []TYPE = {"verb","noun","adv","adj"};
	

	public static Tries scan(String file) throws IOException {
			RandomAccessFile raf = new RandomAccessFile(new File(file), "r");
			raf.seek(SkipHeaderOffset);
			
			Tries T = new Tries();
			int synset_cnt=0,p_cnt,offset_index;
			
			String line;
			while((line= raf.readLine())!= null){
				String[] token = line.split("[ ]+");
				synset_cnt 	= Integer.parseInt(token[2]);
				p_cnt		= Integer.parseInt(token[3]);
				offset_index	= p_cnt+6;
				long[] offset = new long[synset_cnt];
				for(int i=0;i<synset_cnt;i++){
					offset[i] = Integer.parseInt(token[i+offset_index]);
				}
				T.insert(token[0],offset);
			}
			
			raf.close();
			return T;		
	}


	public static List<String> search(String word,Tries T,String data_file){
		Log(word+" "+data_file);
		
		List<String> S = new ArrayList<String>();
				
		try{
		File data  = new File(data_file);
		RandomAccessFile raf = new RandomAccessFile(data,"r");
		long offset[]= (long[]) T.get(word);

		String line;
		Tries dublicateCheck=new Tries();
		dublicateCheck.insert(word,null);
		for(int i=0;i<offset.length;i++){
			Log(offset[i]+"");
			raf.seek(offset[i]);
			line=raf.readLine();
			String[] Words_Example = line.split("\\|");
			String[] token = Words_Example[0].split("[ ]+");
			String Examples = Words_Example[1];
						
			int n_word=Integer.parseInt(token[3],16);
			for(int j=0;j<2*n_word;j+=2){
				if(dublicateCheck.search(token[j+4]) == false){
					dublicateCheck.insert(token[j+4],null);
					S.add(token[j+4]);
				}
			}
			S.add(Examples+".");
		}

		raf.close();

		return S;
	}catch(IOException e){
			return null;
		}
	}

	static ApplicationFrame thisFrame = null;
	public static void main(String [] args){
		thisFrame = new ApplicationFrame();
		
		/**
		 * Creating and Initializing DataSrtucture
		 */
		final JDialog dlg = new JDialog(thisFrame, "Initializing", true);
	    final JProgressBar dpb = new JProgressBar(0, 4);
	    JLabel message = new JLabel("Building Data Structure");
	    dlg.add(BorderLayout.CENTER, dpb);
	    dlg.add(BorderLayout.NORTH, message);
	    dlg.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	    dlg.setSize(300, 75);
	    dlg.setLocationRelativeTo(thisFrame);
	    
	    Thread t = new Thread(new Runnable() {
	        public void run() {
	          dlg.setVisible(true);
	        }
	      });
	      t.start();
	      
	      int i=0;
          for(i=0;i<4;i++){
	  			System.out.println("data/index."+TYPE[i]);
	  			try {
	  				tries[i]= (Tries) scan("data/index."+TYPE[i]);
	  				 dpb.setValue(i+1);
	  			} catch (IOException e) {
	  				dlg.setTitle("Error");
	  				message.setText(e.getLocalizedMessage());
	  				e.printStackTrace();
	  				break;
	  			}
  			}
          if(i==4){
        	  dlg.setVisible(false);
          }else{
        	  dlg.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);          
          }
	}
	
	
/*
 * JAVA Swing Application frame class
 */
	static class ApplicationFrame extends JFrame{

		private static final long serialVersionUID = 7935214460207473953L;
		private  JTextField inputField;
		private  JLabel statusLabel,creditLabel;
		private  JCheckBox verb;
		private  JCheckBox noun;
		private  JCheckBox adverb;
		private  JCheckBox adject;
		private  JButton searchButton;
		private  JTextArea textArea;
		private  ButtonListener bl;
		
		public ApplicationFrame(){
			this.setTitle("Synonymus");
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
			 bl = new ButtonListener ();

			JPanel panel = new JPanel();
			panel.setLayout(new GridBagLayout());

			addItem(panel, new JLabel("Synonymus WordNet"),1, 1, 3, 1, GridBagConstraints.CENTER);
			
			inputField = new JTextField(23);
			statusLabel = new JLabel("O words");
			creditLabel = new JLabel("Created by Arink Verma");

			Box typeBox1 = Box.createHorizontalBox();
			typeBox1.add(inputField);
			
			
			

			searchButton = new JButton("Search");
			searchButton.addActionListener(bl);
			typeBox1.add(searchButton);
			addItem(panel, typeBox1, 1, 2, 3, 1,GridBagConstraints.NORTH);
			
			Box typeBox = Box.createHorizontalBox();
			verb = new JCheckBox("Verb");
			noun = new JCheckBox("Noun");
			adverb = new JCheckBox("Adverb");
			adject = new JCheckBox("Adjective");
			typeBox.add(verb);
			typeBox.add(noun);
			typeBox.add(adverb);
			typeBox.add(adject);
			verb.addActionListener(bl);
			noun.addActionListener(bl);
			adverb.addActionListener(bl);
			adject.addActionListener(bl);

			typeBox.setBorder(BorderFactory.createTitledBorder("Type"));
			addItem(panel, typeBox, 1, 3, 3, 1,GridBagConstraints.NORTH);

			Box textBox = Box.createHorizontalBox();
			textArea = new JTextArea(20, 30);
			textArea.setWrapStyleWord(true);
			textArea.setLineWrap(true);
			
			JScrollPane scroll = new JScrollPane(textArea,	JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			textBox.add(scroll);
			addItem(panel, textBox, 1, 4, 3, 1,GridBagConstraints.NORTH);

			Box footerBox = Box.createHorizontalBox();
			footerBox.add(statusLabel);
			footerBox.add(creditLabel);
			addItem(panel, statusLabel,1, 5, 3, 1, GridBagConstraints.WEST);
			addItem(panel, creditLabel,2, 5, 3, 1, GridBagConstraints.EAST);

			this.add(panel);
			this.pack();

			this.setSize(400, 500);
			this.setVisible(true);
		}
		
		private class ButtonListener implements ActionListener{
			public void actionPerformed(ActionEvent e){
				if (e.getSource() == searchButton){
					String P=inputField.getText();
					int count=0;
					textArea.setText("\n");
					if(verb.isSelected()){
						count += PrintType(P,0);
					}
					if(noun.isSelected()){
						count += PrintType(P,1);
					}
					if(adverb.isSelected()){
						count += PrintType(P,2);
					}
					if(adject.isSelected()){
						count += PrintType(P,3);
					}
					
					statusLabel.setText(count+" words found");
				}
			}			
		}
		
		int PrintType(String word,int t){
			List<String> S = search(word,tries[t],"data/data."+TYPE[t]);
			textArea.append(TYPE[t]+"\n----\n");
			
			int len = 0;
			if(S == null || S.isEmpty()){
				textArea.append("No words");
			}else{
				len = S.size()-1;
				for(int j=0;j<len;j++){
					textArea.append((j+1)+". "+S.get(j)+"\n");
				}
				String[] Examples = S.get(len).split("[;]+");
				if(Examples!=null){
					textArea.append("\nExamples :-\n");
					for(int j=0;j<Examples.length;j++){
						textArea.append((j+1)+". "+Examples[j]+"\n");
					}
				}
			}	
			
			textArea.append("\n\n");
			return len;
		}
		
		private static void addItem(JPanel p, JComponent c,int x, int y, int width, int height, int align){
			GridBagConstraints gc = new GridBagConstraints();
			gc.gridx = x;
			gc.gridy = y;
			gc.gridwidth = width;
			gc.gridheight = height;
			gc.weightx = 100.0;
			gc.weighty = 100.0;
			gc.insets = new Insets(5, 5, 5, 5);
			gc.anchor = align;
			gc.fill = GridBagConstraints.NONE;
			p.add(c, gc);
		}
		
	}
	
	static void Log(String s){
		System.out.println(s);
	}
	
}
