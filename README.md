# RestaurantConsoleApp

Java console application for the management of a restaurant and its orders with an enhanced user experience thanks to the [ConsoleUI library](https://github.com/awegmann/consoleui).

The application enables:
  - customers to place orders
  - administrators to consult statistics (value of sales for the current day, all time favourite product for customers, products that have been ordered on the current day)
  - administrators to save reports in .txt files (sales for the current day, most popular products)

# How to run
As the application uses ConsoleUI, it must be compiled as a .jar file in order to be run in the terminal(if you're not changing anything to the current version, this step can be skipped as there is already a compiled .jar file in the project files). Afterwards, follow the next steps:
  - go to the directory where the exported .jar is located (this is the path for using IntelliJ IDEA as your IDE)
     ```sh
    cd .\out\artifacts\proj_jar\
    ```
  - Run the jar
    ```sh
    java -jar proj.jar
    ```
