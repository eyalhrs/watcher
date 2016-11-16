## Overview

This project is using java 7 SDK (WatchService) for watching a given source directory.
Each file or directory that is added to source directory is moved to a given target directory with the following rules applied:
- Text files are moved as they are
- All other files are renamed by adding the current unix timestamp to the end of the file

## Main Classes

- DirectoryWatcherController - exposes two APIs: startListen and stopListen
- DirectoryWatcherService - handling registration to watched source directory
- DirectoryWatcherHandler - using WatchService class for registering to source directory and spawning a thread for processing incoming events
- EventCreateHandler - handles the "create" event, moving files to target directory
- StartServer - entry point for running a lightweight web server

## Tests

- DirectoryWatcherHandlerTest - tests DirectoryWatcherHandler end to end
- EventCreateHandlerTest - tests EventCreateHandler class by covering the case in which a non text file is being moved (timestamp should be added)

## API

- startListen GET (used instead of POST for simplicity)
  - srcDir e.g. /Users/Eyal/src
  - targetDir e.g. /Users/Eyal/target
- stopListen GET (used instead of POST for simplicity)
  - srcDir e.g. /Users/Eyal/src
