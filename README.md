# tivi_ohPro_simulaattori
This repository contains simulator made by me for fall 2020 project in my school.

The simulator simulates a lunch cafeteria where customers can move between service points.
The simulator includes a graphical user interface and visual representation of the simulation proggress.
The simulator also includes mySQL database connection which it uses to save and fetch data from.
The simulator has extensive JavaDoc documentation which can be found in doc folder
The simulator is written mostly in finnish.

!!!
Notice, the simulator uses Hibernate ORM to handle database connections, hibernate.cfg.xml file in bin folder must be changed in order
for database functionality to work.
The class file in bin/akiko/ruokalasimu/model/SimulaattoriDAO has been modified to not end the running of program if no database is found,
this is to make sure that program can be run without changes to hibernate.cfg.xml. This functionality should be uncommented when database
is working. The program will run into errors if database spesific buttons are used without the database connected.
