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

Please feel free to run the Unit Tests!
