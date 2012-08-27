package in.arinkverma.Synonyms.datastructure;

public class Tries{
	class node{		
		boolean end=false;
		Object data=null;
		node child[];
		node(Object data1){
			child = new node[50];		//[a,...,z] => [0,...,25] and [0,..,9] => [26,...,36] 37 => space or _
			data=data1;
		}
	}
	node root=new node(null);


	int decode(char c){
		int key=0;
		if(c>='0' && c<='9')
			key = (int) c-'0' + 26;
		else if(c=='_' || c=='-' || c==' ')
			key = 37;
		else if(c=='\'')
			key = (int) c-'\'';
		else if(c=='/')
			key = (int) c-'/';
		else if(c=='.')
			key = (int) c-'.';
		else
			key = (int) c-'a';
		return key;
	}

	public void insert(String s,Object data){		
		s=s.toLowerCase( );
		node temp=root;

		for(int i=0;i<s.length();i++){
			int key=decode(s.charAt(i));
			if(temp.child[key]==null)
				temp.child[key] = new node(data);
			temp = temp.child[key];		
		}
		temp.end=true;
	}



	public Object get(String s){
		s=s.toLowerCase();
		node temp=root;
		long[] rent=new long[0];
		for(int i=0;i<s.length();i++){
			int key=decode(s.charAt(i));
			if(temp.child[key]==null){
				break;
			}
			temp = temp.child[key];
		}

		if(temp.end == true) rent = (long[]) temp.data;
		return rent;
	}

	public boolean search(String s){
		s=s.toLowerCase();
		node temp=root;
		for(int i=0;i<s.length();i++){
			int key=decode(s.charAt(i));
			if(temp.child[key]==null){
				return false;
			}
			temp = temp.child[key];
		}

		if(temp.end == true) return true;
		return false;
	}
}