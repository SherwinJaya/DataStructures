package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		root = this.recursiveBuild(sc.nextLine()); 
	}
	private TagNode recursiveBuild(String tag) {
		TagNode t = null; 
		
		if(tag == null)
			return t; 
		
		if(tag.substring(0,1).equals("<")) {
			tag = this.formatString(tag); 
		
		if(tag.substring(0, 1).equals("/"))
			return null; 
		
		else {
			t = new TagNode(tag, null, null); 
			t.firstChild = this.recursiveBuild(sc.nextLine()); 
		if(sc.hasNext() == false)
			return t; 
		
		t.sibling = this.recursiveBuild(sc.nextLine()); 
		}
		}
		
		else {
			t = new TagNode(tag, null, null); 
			t.sibling = this.recursiveBuild(sc.nextLine()) ;
		}
		return t; 
	}
	
	private String formatString(String p) {
		String newString = ""; 
		
		for(int i = 1; i<p.length()-1; i++)
			newString += p.charAt(i); 
		return newString; 
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		
		ArrayList<String> hello = new ArrayList<String>(); 
		
		hello.add("p"); 
		hello.add("em"); 
		hello.add("b"); 
		
		ArrayList<String> bye = new ArrayList<String>(); 
		
		bye.add("ol"); 
		bye.add("ul"); 
		
		if(hello.contains(oldTag) && hello.contains(newTag))
			this.replaceRecursive(root, oldTag, newTag); 
		else if(bye.contains(oldTag) && bye.contains(newTag))
			this.replaceRecursive(root, oldTag, newTag); 
	}
	
	private void replaceRecursive(TagNode root, String oldTag, String newTag) {
		
		if(root == null)
			return; 
		
		this.replaceRecursive(root.firstChild, oldTag, newTag);
		
		if(root.tag.contentEquals(oldTag) && !root.firstChild.tag.equals(oldTag))
			root.tag = newTag; 
		
		this.replaceRecursive(root.sibling, oldTag, newTag);
		
		if(root.firstChild != null)
			if(root.tag.equals(root.firstChild.tag))
				root.firstChild = root.firstChild.firstChild; 
	}
	
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		
		root = this.recursiveBold(root, row); 
	}
	

	private TagNode recursiveBold(TagNode t, int row){
	
		if(t == null)
			return null; 
		
		t.firstChild = this.recursiveBold(t.firstChild, row); 
		
		if(t.tag.equals("table")) {
			TagNode child = t.firstChild; 
			
			for(int i = 1; i<row; i++)
				child = child.sibling; 
			
			TagNode column = child.firstChild; 
			
			while(column != null) {
				
				TagNode temp = new TagNode("b", column.firstChild, null); 
				column.firstChild = temp; 
				column = column.sibling; 
			}
		}
		t.sibling = this.recursiveBold(t.sibling, row); 
		return t; 
}
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		
		root = this.recursiveDelete(root, tag); 
	}
	
	private TagNode getLast(TagNode t) {
		
		if(t == null)
			return t; 
		
		while(t.sibling != null)
			t = t.sibling; 
		
		return t; 
	}
	
	private TagNode recursiveDelete(TagNode t, String r) {
		
		if( t == null)
			return null; 
		
		String token = t.tag; 
		t.firstChild = this.recursiveDelete(t.firstChild, r);
		
		if((token.equals("p") && token.equals(r)) || (token.equals("em") && token.equals(r)) || (token.equals("b") && token.equals(r))) { 
			this.getLast(t.firstChild).sibling = t.sibling; 
			t = t.firstChild; 
		}
		
		else if((token.equals("ol") && token.equals(r)) || (token.equals("ul") && token.equals(r))) {
			
			TagNode child = t.firstChild; 
			
			while(child != null) {
				
				if(child.tag.equals("li"))
					child.tag = "p"; 
				
				child = child.sibling; 
			}
			
			child = t.firstChild; 
			this.getLast(child).sibling = t.sibling; 
			t = t.firstChild; 
		}
		
		t.sibling = this.recursiveDelete(t.sibling,  r); 
		return t; 
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		ArrayList<String> think = new ArrayList<String>(); 
		
		think.add("?"); 
		think.add(","); 
		think.add("!"); 
		think.add(":"); 
		think.add(";"); 
		think.add("."); 
		
		root = this.recursiveAddTag(root, word, tag, think); 
	}
	
	private TagNode addLast(TagNode t, String tag) {
		
		if(t == null)
			return new TagNode(tag, null, null); 
		
		TagNode temp = t; 
		
		while(temp.sibling != null)
			temp = temp.sibling; 
		
		temp.sibling = new TagNode(tag, null, null); 
		
		return t; 
	}
	
	private TagNode recursiveAddTag(TagNode t, String word, String tag, ArrayList<String> a) {
		
		if(t == null)
			return null; 
		
		t.firstChild = this.recursiveAddTag(t.firstChild, word, tag, a); 
		
		t.sibling = this.recursiveAddTag(t.sibling, word, tag, a); 
		
		String temp = ""; 
		String king = ""; 
		String declare = t.tag + " ";
		TagNode newTag = null; 
		while(declare.length() != 0) {
			
			int index = declare.indexOf(" "); 
			temp = declare.substring(0, index).toLowerCase(); 
			
			if(temp.contains(word) == false)
				king += declare.substring(0, index)+ " ";
			
			if(temp.contains(word) && temp.length() <= word.length() + 1) {
				
				if(temp.length() == word.length() + 1 && a.contains(temp.substring(temp.length() - 1))) {
					
					if(king.length() != 0)
						newTag = this.addLast(newTag, king); 
					
					newTag = this.addLast(newTag, tag); 
					this.getLast(newTag).firstChild = new TagNode(declare.substring(0, index), null, null); 
					king = ""; 
				}
			}
			
			declare = declare.substring(index + 1); 
		}
		
		if(newTag == null)
			return t; 
		
		this.getLast(newTag).sibling = t.sibling; 
		t = newTag;
		
		return t; 
		
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|----");
			} else {
				System.out.print("     ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
