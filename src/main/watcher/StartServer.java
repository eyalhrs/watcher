package main.watcher;


import main.watcher.controller.DirectoryWatcherController;

public class StartServer {

   public static void  main(String[] args) throws Exception {
      new DirectoryWatcherController();
      System.out.println("Started listening on port 4567...");
   }
}
