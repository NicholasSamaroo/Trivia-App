# Trivia-App

**OVERVIEW**
----------
Utilized the jservice API to create a trivia app in the form of a Java Android mobile application. Users have the option to either play "blitz" or "main show". In blitz, they have to answer 5 randomly pulled questions with 30 seconds to answer each question. In the "main show", they play a longer game consisting of 4 categories with 4 clues each; the same 30 second time limit applies here as well.

**TECHNICAL OVERVIEW**
--------------------
Data is pulled from the API by using **Retrofit** and **RxJava / RxAndroid**. The clues for "blitz" can be retrieved with one GET request but getting the data for "main show" requires working with **RxJava / RxAndroid**; more specifically, we need to retrieve the categories from the API and then for **each** category returned we need to use its ID to get the clues for that category. So, **RxJava / RxAndroid** provides more flexibility in terms of avoiding nested callbacks. 

The board for the "main show" mode is generated dynamically in code by using **GridLayout**. There are 20 cells on that board so hard coding those in the layout XML file would be inefficient and tedious; also knowing which cell they clicked would result in a large chunk of hard coded values in the Java code. So, as mentioned, each view is dynamically created and has a click listener set on it as well as a tag which corresponds to its position in the ArrayList where the data is held. This results in the right clue being displayed when the user clicks on a cell. **Shape drawables** defined in XML are used for styling the cells as well as the views that display the highscores on the main screen and the views that display clue information. **CountDownTimer** is used to enforce the 30 second time limit.

**MAIN SHOW DEMO**
----------------
![MainShowDemo](https://github.com/NicholasSamaroo/Trivia-App/blob/main/demo/main_show_demo.gif)

**BLITZ DEMO**
------------
![BlitzDemo](https://github.com/NicholasSamaroo/Trivia-App/blob/main/demo/blitz_demo.gif)

**STATIC APP IMAGES**
-------------------
![LaunchScreen](https://github.com/NicholasSamaroo/Trivia-App/blob/main/staticImages/launch_screen.png)

![MainShowBoard](https://github.com/NicholasSamaroo/Trivia-App/blob/main/staticImages/main_show_board.png)

![MainShowQuestion](https://github.com/NicholasSamaroo/Trivia-App/blob/main/staticImages/main_show_question.png)

![BlitzQuestion](https://github.com/NicholasSamaroo/Trivia-App/blob/main/staticImages/blitz_question.png)
