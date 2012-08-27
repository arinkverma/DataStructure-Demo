package in.arinkverma.tree.AVL;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
class AVLTree{
	class Node{
		int N;
		int height=0;
		Node parent=null,left=null,right=null;
		Node(int n,int h,Node P){
			N=n;
			height=h;
			parent=P;
		}
	}

	Node root=null;
	void insert(int N,Node R){
		if(root==null)
			root=new Node(N,1,null);
		else{
			if(N<=R.N){
				if(R.left==null){
					R.left=new Node(N,1,R);
					dobalance(R.left);
				}else
					insert(N,R.left);
			}else{
				if(R.right==null){
					R.right=new Node(N,1,R);
					dobalance(R.right);
				}else
					insert(N,R.right);
			}
		}
	}
	
	void remove(Node x){
		Node y,z=null;
		if(x.left==null || x.right==null)	//atleast one child
			y=x;
		else{								//finding successor
			Node temp=x;
			for(y=temp.parent;y!=null && temp==y.right;y=y.parent){
				temp=y;				
			}
		}									//both child or no child

	//y has atmost one child
		if(y.left!=null)
			z=y.left;
		else
			z=y.right;
			
		if(z!=null)							
			z.parent=y.parent;
		
		if(y.parent==null)					//z was is root
			root=z;
		else{								//removing y
			if(y==y.parent.left)			//y was in left
				y.parent.left=z;	
			else
				y.parent.right=z;			//y was in right
		}
		x.N=y.N;							//copying y to x
		y.height=0;
		
		do{
			y=dobalance(y);
		}while(y!=null);						//balancing till root
	}
/************
	Reforming tree after deletion or insertion 
	operation							***********/
	Node dobalance(Node x){
		int h1=0,h2=0;						//here h1 height of x,h2 height of x`s sibling
		Node n1=null,rent=null;
		Node y=x,z;
		while(y.parent!=null){
			if(y.parent.left==y) n1=y.parent.right;
			else if(y.parent.right==y) n1=y.parent.left;
			h1=y.height;
			if(n1==null)
				h2=0;			
			else
				h2=n1.height;
			if(Math.abs(h2-h1)>1)								
				break;
			y.parent.height=1+Math.max(h1,h2);
			y=y.parent;
		}
		
		if(y.parent==null) 
			return null;		

		z=y.parent;
		rent=z;		
	/*linking z to y and y to x. such that y and x has maximum 
	  height among sibling respectively*/
		h1=(z.left==null)?0:z.left.height;
		h2=(z.right==null)?0:z.right.height;
		if(h1<h2)
			y=z.right;
		else
			y=z.left;

		h1=(y.left==null)?0:y.left.height;
		h2=(y.right==null)?0:y.right.height;
		if(h1<h2)
			x=y.right;
		else
			x=y.left;
		
		y.parent=z; 
		x.parent=y;		

	//finding the type of rotation needed to balance tree
		if(z.left==y){
			if(y.left==x)
				Rrotation(y,z);		//single left rotation
			else{
				Lrotation(x,y);		//double left rotation
				x.height++;
				Rrotation(x,z);			
			}		
		}else{
			if(y.right==x){
				Lrotation(y,z);		//single right rotation
			}else{
				Rrotation(x,y);		//double right rotation
				x.height++;
				Lrotation(x,z);
			}
		}
		
		return rent;
	}
	
/*************
	Right rotation which take two node and rotate second as first take center
																		***********/
	void Rrotation(Node y,Node z){
	//linking y with original parent of z
		y.parent=z.parent;
		if(y.parent==null)
			root=y;
		else if(y.parent.left==z)
			y.parent.left=y;
		else
			y.parent.right=y;

	//rotation b/w y n z
		z.left=y.right;
		if(z.left!=null)
			z.left.parent=z;
		y.right=z;
		z.parent=y;
		z.height--;
	}

/*************
	Left rotation which take two node and rotate second as first take center
																		***********/
	void Lrotation(Node y,Node z){
	//linking y with original parent of z
		y.parent=z.parent;
		if(z.parent==null)
			root=y;
		else if(z.parent.left==z)
			y.parent.left=y;
		else
			y.parent.right=y;

	//rotation b/w x n y
		z.right=y.left;
		if(z.right!=null)
			z.right.parent=z;
		y.left=z;
		z.parent=y;		
		z.height--;
	}

	
/************
	To Search node from tree
						**********/
	Node search(int N,Node r){
		if(r==null) return null;
		if(N==r.N)
			return r;
		else if(N<r.N)
			return search(N,r.left);
		else 
			return search(N,r.right);
	}
	
	static AVLTree B;
	public static void main(String[] args){
		B=new AVLTree();
		new Main();
	}			
			
			
	/**** Classes for grahpics and GUI ***/
	static class Main extends JFrame implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = -2829448395694197965L;

		public Main(){
			this.setSize(500,200);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
			this.setTitle("AVL Tree");
			panel1 = new JPanel();
		/*************buttons******/
			button0 = new JButton("Insert"); 
			button1 = new JButton("Find");
			button2 = new JButton("Delete");
			button3 = new JButton("Show Tree");
			button0.addActionListener(this); 
			button1.addActionListener(this);
			button2.addActionListener(this);
			button3.addActionListener(this);
			panel1.add(button0);
			panel1.add(button1);
			panel1.add(button2);
			panel1.add(button3);
			
			Border b = BorderFactory.createEmptyBorder(45,0,0, 0);
			panel1.setBorder(b);	
			this.add(panel1);			
			this.setVisible(true);	
		}
		JPanel panel1;
		private JButton button0,button1,button2,button3;

		public void actionPerformed(ActionEvent e){		
			if (e.getSource() == button0){
				String s=JOptionPane.showInputDialog("Enter the integer value");
				int i=Integer.parseInt(s);
				B.insert(i,B.root);
			}else if(e.getSource() == button1){
				String s=JOptionPane.showInputDialog("Enter the integer value");
				int i=Integer.parseInt(s);
				if(B.search(i,B.root)==null)
					JOptionPane.showMessageDialog(null,"Not Found");
				else
					JOptionPane.showMessageDialog(null,"Found");
			}else if(e.getSource() == button2){
				String s=JOptionPane.showInputDialog("Enter the integer value");
				int i=Integer.parseInt(s);
				Node temp=B.search(i,B.root);
				if(temp==null)
					JOptionPane.showMessageDialog(null,"Not Found");
				else{
					B.remove(temp);
					JOptionPane.showMessageDialog(null,"Removed");
				}
			}else if(e.getSource() == button3){
				JFrame f = new JFrame("AVL Tree");
				f.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) { }
				});
				Drawtree applet = new Drawtree();
				f.getContentPane().add("Center", applet);
				Toolkit tk = Toolkit.getDefaultToolkit();  
				int xSize = ((int) tk.getScreenSize().getWidth()); 
				applet.init(B.root,xSize-50);
				f.pack();
				f.setSize(new Dimension(xSize,500));
				f.setVisible(true);
			}
		}
	}
	
/************
	Drawing Tree using 2d graphics from java class
									******************/
static class Drawtree extends JApplet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7654352523443329890L;
	final  Color bg = Color.white;
	final  Color fg = Color.black;
    final  Color red = Color.red;
    final  Color white = Color.white;
    final  BasicStroke stroke = new BasicStroke(2.0f);
    final  BasicStroke wideStroke = new BasicStroke(8.0f);

    Dimension totalSize;
	int height,width;
	Node r=null;
	public void init(Node N,int x) {
		//Initialize drawing colors
		setBackground(bg);
		setForeground(fg);
		r=N;
		width=x;
	}
	Graphics2D g2;
        

    public void paint(Graphics g) {
		g2=  (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        getSize();
        inorder(r,0,width,80);
    }
	
	public void draw(int x1,int x2,int y,String n,int d){
	g2.setStroke(stroke);

	g2.setPaint(Color.black);
	int x=(x1+x2)/2;
	if(d==1)
		g2.draw(new Line2D.Double(x2, y-30, x+15, y));
	else if(d==2)
		g2.draw(new Line2D.Double(x+15, y,x1+30 , y-30));
	g2.setPaint(Color.blue);
	Shape circle=new Ellipse2D.Double((x1+x2)/2,y, 30, 30);
        g2.draw(circle);
	g2.fill(circle);
	g2.setPaint(Color.white);
        g2.drawString(n, x+10, y+18);
	}

	int x1=500,y1=30;
	void inorder(Node r,int x1,int x2,int y){
		if(r==null) return;
			
			inorder(r.left,x1,(x1+x2)/2,y+40);
			if(r.parent==null) draw(x1,x2,y,r.N+"",0);
			else{
				if(r.parent.N<r.N)	draw(x1,x2,y,r.N+"",2);	
				else			draw(x1,x2,y,r.N+"",1);	
			}	
			inorder(r.right,(x1+x2)/2,x2,y+40);
	}
}

}

