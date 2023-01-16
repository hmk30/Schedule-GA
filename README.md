# Schedule GA

## A custom genetic algorithm made with Java to optimize school schedules.

See output.txt for an example of the output of this program.

A more in-detail overview of the program can be found in Analysis.pdf.
The purpose of the analysis was to compare different input parameters.
Some relevant plots can be found in the plots directory.

The schedules are created based on a pre-determined list of available rooms, timeperiods, professors, and courses.
This hard-coded data is visualized in the images directory (professors are derived from the courses).
The respective Course.java, Professor.java, Room.java, and Timeperiod.java files also embed this information.

Rules that the GA follows:

- Courses that require a multimedia room must be assigned a multimedia room
- Rooms must be large enough to accommodate the number of students enrolled.
- A course (CRN) must only be on the schedule exactly 1 time.
- Professors may only teach one course at a time.
- Rooms may only hold one course at a time.

Things that the GA attempts to optimize:

- Professors do not want to teach 3 or more courses in a row.
  - -10 points per instance of this (-20pts for 4 classes in a row)
- Professors typically do not want a large amount of time between courses.
  - -15 points for each delay of 3 or more hours (-30pts for a 4hr delay)
- Professors like teaching multiple courses in the same room.
  - +5 points for each time a professor reuses a room.
- Large rooms should not hold courses with a small number of enrolled students.
  - -1 point for each unoccupied seat during a course.

A sample solution:

```
--------------------------------------------------------------------
|                   Schedule with fitness: -190                    |
|                        from generation 96                        |
--------------------------------------------------------------------
| CRN | Course  | Professor | Size | Multimedia | Room      | Time |
--------------------------------------------------------------------
| 16  | Soc100  | Meg       | 45   | true       | BL138     | 1    |
| 17  | Soc100  | Meg       | 40   | true       | BL138     | 10   |
| 6   | cs015   | Sandro    | 35   | true       | BL138     | 12   |
| 18  | Soc100  | Meg       | 35   | true       | BL138     | 9    |
| 1   | cs456   | Bilitski  | 20   | true       | KR206     | 2    |
| 2   | cs456   | Bilitski  | 20   | true       | KR206     | 12   |
| 4   | cs455   | Ohl       | 20   | true       | KR206     | 1    |
| 5   | cs455   | Ohl       | 20   | true       | KR206     | 3    |
| 24  | cs045   | Ohl       | 20   | true       | KR206     | 9    |
| 25  | cs045   | Ohl       | 20   | true       | KR206     | 11   |
| 26  | cs015   | Sandro    | 20   | true       | BL134     | 3    |
| 3   | cs1783  | Bilitski  | 15   | true       | KR206     | 10   |
| 19  | CS047   | Stewie    | 15   | true       | BL134     | 1    |
| 20  | CS047   | Stewie    | 15   | true       | BL134     | 2    |
| 15  | math002 | Brian     | 60   | false      | ES100     | 10   |
| 12  | math001 | Peter     | 60   | false      | ES100     | 1    |
| 14  | math002 | Brian     | 50   | false      | BL138     | 2    |
| 11  | math001 | Peter     | 50   | false      | BL138     | 11   |
| 10  | math001 | Peter     | 40   | false      | KR224     | 2    |
| 13  | math002 | Brian     | 40   | false      | KR224     | 11   |
| 8   | cs015   | Mr xxx    | 35   | false      | Biddle123 | 3    |
| 7   | cs015   | Mr xxx    | 35   | false      | Biddle123 | 12   |
| 22  | PSY200  | Glen      | 35   | false      | Biddle123 | 1    |
| 9   | cs015   | Frederick | 35   | false      | Biddle123 | 10   |
| 21  | PSY200  | Glen      | 30   | false      | BL134     | 12   |
| 23  | PSY200  | Glen      | 30   | false      | BL134     | 10   |
--------------------------------------------------------------------
```

Explanation of fitness:

```
Sum from unused seats: -240
Professor Bilitski bonus from duplicate rooms: 10
Professor Bilitski penalty for large gaps 0
Professor Bilitski penalty for consecutive classes:0
Professor Ohl bonus from duplicate rooms: 15
Professor Ohl penalty for large gaps 0
Professor Ohl penalty for consecutive classes:0
Professor Sandro bonus from duplicate rooms: 0
Professor Sandro penalty for large gaps 0
Professor Sandro penalty for consecutive classes:0
Professor Mr xxx bonus from duplicate rooms: 5
Professor Mr xxx penalty for large gaps 0
Professor Mr xxx penalty for consecutive classes:0
Professor Frederick bonus from duplicate rooms: 0
Professor Frederick penalty for large gaps 0
Professor Frederick penalty for consecutive classes:0
Professor Peter bonus from duplicate rooms: 0
Professor Peter penalty for large gaps 0
Professor Peter penalty for consecutive classes:0
Professor Brian bonus from duplicate rooms: 0
Professor Brian penalty for large gaps 0
Professor Brian penalty for consecutive classes:0
Professor Meg bonus from duplicate rooms: 10
Professor Meg penalty for large gaps 0
Professor Meg penalty for consecutive classes:0
Professor Stewie bonus from duplicate rooms: 5
Professor Stewie penalty for large gaps 0
Professor Stewie penalty for consecutive classes:0
Professor Glen bonus from duplicate rooms: 5
Professor Glen penalty for large gaps 0
Professor Glen penalty for consecutive classes:0
```

This example can also be seen in the BEST.txt file
