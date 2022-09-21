# StudyMate

## What is it?

A convenient application that provides students an organized and convenient location for all their class
and extracurricular-related deadlines and assignments. 

StudyMate will provide users the opportunity to:
- Enter and organize their class timetables
- Stay on top of upcoming assignments, and their progression
- Keep track of upcoming exams
- Balance other extracurricular activities into their schedule 

## What inspired StudyMate?
I tried several other applications to try to stay organized, but I did not have anything that could merge all of these
features into one convenient application. This is my attempt at compiling all the useful features into something I can 
use daily.

## User Stories
- As a user, I want to be able to add classes and their times to this application
- As a user, I want to be able to see all my classes and assignments for the current day
- As a user, I want to be able to add assignments, their due dates, and how far I have progressed through them 
to each of my classes individually
- As a user, I want to be able to add exam dates for specific classes, and to see them on my calendar
- As a user, I want to be able to save my classes, assignments and exams
- As a user, I want to be able to load my classes, assignments and exams

## Phase 4: Task 2
- Adding class times to a class: Loaded class times into *class name*  
- Adding assignment to a class: Added **assignment name** to *class name*  
- Adding exam to a class: Added **exam name** to *class name*  
- Loading data from save file: Loaded data from save file  
- Saving data to save file: Saved data to save file

## Phase 4: Task 3
I would add bidirectional relationships between assignments/exams and classes. This will allow me to know which
assignment/exam belongs to which class, playing further into better UI features that make staying organized easier.

I would make the EventUI more abstract to encompass AddClassUI as well. 

To make design patterns better, I might not use the interface DateToCalendar as a way to add a common method. There are 
likely better approaches.

Finally, I would like to re-organize the contents of StudyMateAppUI better. Currently, everything is aggregated within 
one class. More abstraction and better organization may make the project easier to navigate, and easier to 
change/implement new ideas.
