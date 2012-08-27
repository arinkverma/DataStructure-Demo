DataStructure-Demo
==================

Implementation of effective data structure to build real life application with utility

Synonyms | WordNet
=========
Application which find out the synset of the word. According to WordNet, a synset or synonym set is defined as a set of one or more synonyms that are interchangeable in some context without changing the truth value of the proposition in which they are embedded.
![Alt frame](https://github.com/arinkverma/DataStructure-Demo/raw/master/Synonyms/synonyms.jpg "Displaying result for word open")

<b>Tries DataStructure</b>
A trie is a special form of n-ary tree which can efficiently store strings. There is one child node for every common prefix string. It is particularly suited for dictionary-style applications.

<b>Time complexity</b>
<ul><li>Preprocessing : O(length of Total Word)</li>
<li>Search : O(Word Lenth)</li>
</ul>


AVL Tree
=========
![Alt frame](https://github.com/arinkverma/DataStructure-Demo/raw/master/Tree/src/in/arinkverma/tree/AVL/avltree.jpg "Displaying AVL Tree")
AVL tree is a self-balancing binary search tree, and it was the first such data structure to be invented. In an AVL tree, the heights of the two child subtrees of any node differ by at most one. Lookup, insertion, and deletion all take O(log n) time in both the average and worst cases, where n is the number of nodes in the tree prior to the operation.


RedBlack Tree
=========
![Alt frame](https://github.com/arinkverma/DataStructure-Demo/raw/master/Tree/src/in/arinkverma/tree/RedBlack/redblack.jpg "Displaying redblacktree")
constraints enforce a critical property of red–black trees: that the path from the root to the furthest leaf is no more than twice as long as the path from the root to the nearest leaf. The result is that the tree is roughly height-balanced. Since operations such as inserting, deleting, and finding values require worst-case time proportional to the height of the tree, this theoretical upper bound on the height allows red–black trees to be efficient in the worst case, unlike ordinary binary search trees.