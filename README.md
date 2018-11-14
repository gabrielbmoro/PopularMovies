# Popular Movies - Udacity Course

- To this project it is necessary to put the api key in strings.xml, for example:

> \<string name="api_key"\>your key\<\/string\>

### Stage 1 - Implementation

- Instructions to the User

I created two screens, the first has a list of movies, and the second has details about the movie selected in the first screen. The user can choose one movie using a simple click, after that the details screen will be appear. Besides that, the user can change the sort list in the first screen, there are two options: top rated or popular movies to list.

- About the project

The project was created using three layers: 

    * model - Store the value objects;
    * ui - Define the user interface behavior, screens, and others visual things;
    * util - Provide services for all objects.
    
If you want to see the version has with functionalities, click [here](https://github.com/tido4410/popularmovies_udacitycourse/releases/tag/stage1v1.0.0)

### Stage 2 - Implementation

- Instructions to the User

The main screen has three options: popular, top rated, and favorite movies. The user can choose the option using the spinner component. Besides that, the user can make a movie as favorite using the details screen to do that. At the details screen the user can watch the trailers using the youtube app or the browser.

- About the project

The project was created using three layers: 

    * model - Store the value objects;
    * model->database - The room library is used here to manage the data persistence.
    * ui - Define the user interface behavior, screens, and others visual things;
    * util - Provide services for all objects.

If you want to see the version has with functionalities, click [here](https://github.com/tido4410/popularmovies_udacitycourse/releases/tag/stage2v1.0.1)
