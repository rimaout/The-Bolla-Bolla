## The Bolla Bolla

This is a remake of the classic game [**Bubble Bobble**](https://en.wikipedia.org/wiki/Bubble_Bobble_(video_game)) (NES version), built entirely in Java without any external libraries. 

### 🏫 University Project

This project was developed as part of the "Metodologie di Programmazione" (Programming Methodologies) course at the University of Sapienza, during my first year of studies in Computer Science. The objective was to recreate the [Bubble Bobble](https://en.wikipedia.org/wiki/Bubble_Bobble_(video_game)) game using Java and respecting the following constraints:
- No external libraries
- Appropriate use of MVC, Observer Observable and other design patterns
- Effective use of Streams
- Use of JavaSwing or JavaFX for the GUI
- Profile Management (nickname, avatar, score, score board)
- Game Play (8 levels, 3 enemies, 10 power-ups, 2 special bubbles, 
- Presence of audio and special effects

The assignment also consisted in the creation of a [UML diagram](docs/UML.pdf), [Project Thesis](docs/relazione.pdf) (it's in italian) and [Java Doc](https://rimaout.github.io/The-Bolla-Bolla/).

This project marked a significant milestone for me, as it is the largest and most complex project I have undertaken so far. Notably, it was my first experience managing a project composed by multiple classes and packages, and implementing the MVC pattern was a new concept for me. 

Also, this was the first time I had to work on a project with predefined guidelines and constraints, rather than having the freedom to choose my own approach. However, I learned a lot from this experience and I am really proud of how it turned out.

> [!NOTE]
> To see the original version submitted for the exam, check the `uni-project-version` branch. I have made some changes to the game after the exam, including a significant restructuring of the project to use Maven for building the JAR file.

### 🏎️ How to Run

There are 3 ways to run the game:

>**1. Using the JAR file**
>
>Download the JAR file from the [releases page](https://github.com/rimaout/The-Bolla-Bolla/releases) and simply run it by double-clicking on it. Make sure you have Java installed on your computer.

>**2. Run from IDE**:
>
> Clone the repository and open it in your favorite IDE. Run the `Main` class located in the `src/main/java/com/rima/App` file.


>**3. Building from Source** 
>
>Clone the repository and run the following command in the root directory of the project:
>   
>```shell
>mvn clean package
>```
>
>This will create a JAR file in the `target` directory. You can run the JAR file by double-clicking on it or using the following command:
>```shell
>java -jar target/The-Bolla-Bolla-1.1.jar
>```
>
>⚠️ You need to have [Maven](https://maven.apache.org/) installed. Try to use your package manager to install it.

### 🕹️ How to Play

The game is a 2D platformer where the player controls a dragon that can shoot bubbles to trap enemies. The objective is to clear the level of all enemies by trapping them in bubbles and popping them.

This game is played using only the keyboard. The controls are:
- To move the player use the `arrow keys`, `WASD keys`, or `HJKL keys`.
- To jump, press the `space bar` or the `X key`.
- To shoot bubbles, press the `enter key` or `Z key`.
- The game can be paused by pressing the `esc key`.

The menus can be navigated using the `arrow keys`, `WASD keys`, or `HJKL keys`. Use the `enter key` to select.

<video src='https://github.com/user-attachments/assets/1145273f-07bd-4c0a-91e2-df467fba263e'> </video>

---

### Sources
- Audio: [Sound-Resource](https://www.sounds-resource.com/nes/bubblebobble/sound/3719/)
- Sprites: [Spriters-resource](https://www.spriters-resource.com/nes/bublbobl/sheet/70239/) by [Jermungandr](https://www.spriters-resource.com/submitter/Jermungandr/) and [Black Squirrel](https://www.spriters-resource.com/submitter/Black+Squirrel/)
- Fonts: [Nes Font](https://fontstruct.com/fontstructions/show/406653/nintendo_nes_font) by [Goatmeal](https://fontstruct.com/fontstructors/140159/goatmeal)

I also want to thank [Karin Gaming](https://youtube.com/playlist?list=PL4rzdwizLaxYmltJQRjq18a9gsSyEQQ-0&feature=shared) for their incredible tutorial on how to create a platform game in Java. They have been a great help in laying the foundation for this project.