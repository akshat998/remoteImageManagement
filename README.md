# remoteImageManagement

A remote image management system, utilising several design patterns. The program is able to grab images from a 
selected directory(and sub-directories) via a recurssive algorithm. All the images can be viewed in the GUI. There
are several things that can be done to them, however, they can be categorized into three parts:
  * Associating images with tags
  * Creating image catalogues and,
  * Applying unique filters to images
  
Inorder to ensure easy addition of features, the following were strictly followed:
  * Model-View-Controller design pattern
  * Strategy design pattern
  * Dependency Injection design pattern
  * Singleton pattern

RUNNING THE CODE ON COMMAND LINE:

Please follow the following steps :

1. cd inside the 'src' directory. 'ls' should produce(JUnit/ManageImage/GuiController/GuiView)

2. compile all the files using the following command: "javac GuiView/Main.java".

3. Run the program with the following command: "java GuiView.Main".

A directory chooser should pop-up. Select a directory, and have fun!

There is a set of sample images (which can be selected via the GUI) in : JUnit->testImages
Please only mark ImageFileTest and ManagerTest for the JUnit tests.

The independent tags is under "Edit Tags". The selected tags will be permanently deleted with the Delete button.
To add a tag independently, type the new tag in the TextField and press the "+" button.
Selected tags (left column) will be added/removed from selected images(right column).