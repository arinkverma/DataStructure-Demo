package in.arinkverma.tree.RedBlack;


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
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;




class RBtree{

	class Node{
		int N;
		boolean isRED;
		Node left=null,right=null,parent=null;
		Node(int k,Node P,boolean C){
			N=k;
			parent=P;
			isRED=C;
		}			
	}
	Node root=null;
	
/*********
	insertion of new red node in tree
									*********/
	void insert(int N,Node R){
		if(root==null)
			root=new Node(N,null,false);
		else{
			if(N>R.N){
				if(R.right==null){
					R.right=new Node(N,R,true);
					balance_after_ins(R.right);			//balancing tree after insertion
				}else
					insert(N,R.right);
			}else{
				if(R.left==null){
					R.left=new Node(N,R,true);
					balance_after_ins(R.left);			//balancing tree after insertion
				}else
					insert(N,R.left);
			}
		}		
	}

/*********
	balancing tree after insertion
							*********/
	void balance_after_ins(Node x){		
		if(x.parent==null||x.parent.parent==null||!(x.parent.isRED==true&&x.isRED==true))
			return;
		Node y,z;
		y=x.parent;
		z=x.parent.parent;

		if(z.left==y){
			if(y.left==x){
				if(z.right==null || z.right.isRED==false){
					Rrotation(y,z);					//Right rotation
					z.isRED=true;
					y.isRED=false;					
				}else{
					z.isRED=true;
					y.isRED=false;
					z.right.isRED=false;
					if(z==root)
						z.isRED=false;				//making root black
					else
						balance_after_ins(z);		//balancing tree goin up
				}			
			}else{
				Lrotation(x,y);	
				if(z.right==null || z.right.isRED==false){
					Rrotation(x,z);					//Right rotation
					z.isRED=true;
					x.isRED=false;
				}else{
					z.isRED=true;
					x.isRED=false;
					z.right.isRED=false;
					if(z==root)
						z.isRED=false;				//making root black
					else
						balance_after_ins(z);		//balancing tree goin up
				}
			}		
		}else{
			if(y.right==x){
				if(z.left==null || z.left.isRED==false){
					Lrotation(y,z);					//left rotation
					z.isRED=true;
					y.isRED=false;
				}else{
					z.isRED=true;
					y.isRED=false;
					z.left.isRED=false;
					if(z==root)
						z.isRED=false;				//making root black
					else
						balance_after_ins(z);		//balancing tree goin up
				}
			}else{
				Rrotation(x,y);
				if(z.left==null || z.left.isRED==false){
					Lrotation(x,z);					//left rotation
					z.isRED=true;
					x.isRED=false;
				}else{
					z.isRED=true;
					x.isRED=false;
					z.left.isRED=false;
					if(z==root)
						z.isRED=false;				//making root black
					else
						balance_after_ins(z);		//balancing tree while goin up
				}
			}
		}
	}


	
/*********
	removual of red node in tree
									*********/
	void remove(Node x){	
		if(x==root&&x.left==null&&x.right==null){
			root=null;
			return;
		}
		Node y,z=null;
		boolean onechild=false;
		if(x.left==null || x.right==null){	//atleast one child
			y=x;
			onechild=true;
		}else{								//finding successor from right subtree
			Node temp=x.right;
			for(y=temp;y.left!=null;y=y.left);
			
		}
boolean isLeft=false;
	//because y has atmost one child
		if(y.left!=null)
			z=y.left;
		else
			z=y.right;
			
		if(z!=null)							
			z.parent=y.parent;
		
		if(y.parent==null)				//z was is root
			root=z;
		else{							//removing y
			if(y==y.parent.left){		//y was in left
				y.parent.left=z;	
				isLeft=false;
			}else{
				isLeft=true;
				y.parent.right=z;		//y was in right
			}
		}
		x.N=y.N;						//copying y to x

		Node a=null,b=null,c=null,d=null;
		int case1;Node sibling=null;int i=0;
do{		
		i++;
//finding case	to make tree proper red-black	
		if(y.parent==null){
			root=y;
			y.isRED=false;
				return;
		}

		case1=0;
		
		boolean has_red=false;
		if(y.parent.isRED){
			if(isLeft==false)
				sibling=y.parent.right;
			else
				sibling=y.parent.left;
				
			has_red=false;
			if(sibling!=null){
				if(sibling.left!=null){
					has_red=sibling.left.isRED;
					c=sibling.left;
				}else if(sibling.right!=null){
					has_red=has_red||sibling.right.isRED;
					c=sibling.right;
				}
			}
			if(sibling.isRED==false&&has_red==true)
				case1=1;		
			else if(sibling.isRED==false)
				case1=2;
		}else{
			
			if(isLeft==false){
				sibling=y.parent.right;
			}else{
				sibling=y.parent.left;
			}
			
			has_red=false;
			if(sibling!=null){
				if(sibling.isRED==true){			
					if(isLeft&&sibling.right!=null){
						c=sibling.right;
						if(sibling.right.left!=null){
							has_red=sibling.right.left.isRED;							
							d=sibling.right.left;
						}if(has_red)
							case1=3;
						
					}else if(has_red!=true && sibling.left!=null){
						c=sibling.left;
						if(sibling.left.right!=null){
							has_red=sibling.left.right.isRED;							
							d=sibling.left.right;
						}if(has_red)
							case1=4;
					}
					if(has_red==false)
							case1=5;
				}else{
					boolean has_red1=false;
					if(sibling.left!=null){
						has_red=sibling.left.isRED;
					}
					if(sibling.right!=null){
						has_red1=sibling.right.isRED;
					}					
					if(has_red1 || has_red){
						case1=6;
						if(has_red)
							c=sibling.left;
						else
							c=sibling.right;
						System.out.println(c.N);
					}else
						case1=7;
				}
			}
		}
		
		b=sibling;
		System.out.println("reforming tree by case "+case1);	
		
		switch(case1){
				case 1:
					a=y.parent;
					b=sibling;
					b.parent=a;
					c.parent=b;
					if(a.left==b){
						if(b.left==c){
							Rrotation(b,a);
						}else{
							Lrotation(c,b);
							Rrotation(c,a);
						}
					}else{
						if(b.right==c){
							Lrotation(b,a);
						}else{
							Rrotation(c,b);
							Lrotation(c,a);
						}
					}
					break;
				case 2:
					a=y.parent;
					b.parent=a;
					a.isRED=false;
					b.isRED=true;
					break;
				case 3:
					a=y.parent;
					b.parent=a;
					c.parent=b;
					d.parent=c;
					if(a.left==b){
						Rrotation(b,a);
						d.isRED=false;
						c.isRED=true;
						b.isRED=false;
					}else{
						Lrotation(b,a);
						Rrotation(a,b);
						d.isRED=false;
						c.isRED=true;
						b.isRED=false;
					}
					break;
				case 4:
					a=y.parent;
					b.parent=a;
					c.parent=b;
					d.parent=c;
					if(a.left==b){
						Lrotation(c,b);
						Rrotation(c,a);
						d.isRED=false;
					}else{
						Lrotation(b,a);
						b.isRED=false;
						c.isRED=true;
						d.isRED=false;				
					}
					break;
				case 5:
					a=y.parent;
					b.parent=a;
					c.parent=b;
					if(a.left==b){
						Rrotation(b,a);
						b.isRED=false;
						c.isRED=true;
					}else{
						Lrotation(b,a);
						b.isRED=false;
						c.isRED=true;
					}
					break;
				case 6:		
					a=y.parent;
					b.parent=a;
					c.parent=b;
					if(a.left==b){
						if(b.left==c){
							Rrotation(b,a);
							b.isRED=false;
							c.isRED=false;
							a.isRED=false;
						}else{
							Lrotation(c,b);
							Rrotation(c,a);
							c.isRED=false;
						}
					}else{
						if(b.right==c){
							Lrotation(b,a);
							b.isRED=false;
							c.isRED=false;
							a.isRED=false;
						}else{
							Rrotation(c,b);
							Lrotation(c,a);
							c.isRED=false;
						}
					}
					break;
				case 7:
					a=y.parent;
					b.parent=a;
					b.isRED=true;					
					y=a;
					if(y.parent.left==y)
						isLeft=false;
					else
						isLeft=true;
					break;
			}
		}while(case1==7);
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

	
	static RBtree B;
	public static void main(String[] args){
		Random rand=new Random();
		int A[]=new int[500000];
		B=new RBtree();
		try{
		Main click=new Main();
		}catch(Exception e){
		}
	}

	
	
	/**** Classes for grahpics and GUI ***/
	static class Main extends JFrame implements ActionListener{
		/**
		 * 
		 */
		private static final long serialVersionUID = 3899223275951481509L;

		public Main(){
			this.setSize(500,200);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			this.setLocationRelativeTo(null);
			this.setTitle("Red Black Tree");
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
				JFrame f = new JFrame("Red-Black Tree");
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
			Color fg3D = Color.lightGray;
			inorder(r,0,width,80);
		}
		int x1=width/2;
		public void draw(int x1,int x2,int y,Node n,int d){
			g2.setStroke(stroke);
			g2.setPaint(Color.black);
			int x=(x1+x2)/2;
			if(d==1)
				g2.draw(new Line2D.Double(x2, y-30, x+15, y));
			else if(d==2)
				g2.draw(new Line2D.Double(x+15, y,x1+30 , y-30));
		
			if(n.isRED)	g2.setPaint(Color.red);
			else	g2.setPaint(Color.black);
			Shape circle=new Ellipse2D.Double((x1+x2)/2,y, 30, 30);
			g2.draw(circle);
			g2.fill(circle);
			g2.setPaint(Color.white);
			g2.drawString(n.N+" ", x+10, y+18);
		}

		void inorder(Node r,int x1,int x2,int y){
			if(r==null) return;			
			inorder(r.left,x1,(x1+x2)/2,y+40);
			if(r.parent==null) draw(x1,x2,y,r,0);
			else{
				if(r.parent.left==r)	draw(x1,x2,y,r,1);	
				else			draw(x1,x2,y,r,2);	
			}	
			inorder(r.right,(x1+x2)/2,x2,y+40);
		}
	}
}
