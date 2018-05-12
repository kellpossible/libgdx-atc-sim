Meeting Agendas
===============

_Section author: Luke Frisken <[l.frisken@gmail.com](mailto:l.frisken@gmail.com)>_

The agendas for all past and future project meetings are stored in the [/meeting_agendas](../../../meeting_agendas) folder as markdown files.


New Minutes Script
---------------------

Minutes and Agenda files can be generated using the [New Minutes Script](New_Minutes_Script.md) for your coding convenience.



File Naming Convention
----------------------

The file naming convention is as follows

> MeetingAgenda_ID_TIME_DAY_MONTH_YEAR.md

where:

 + ID is the id for the meeting, this is in order of date of meeting. It should match up to the [minutes](Meeting_Minutes.md) for that meeting if there is one.
 + TIME is the time of the meeting in hhmm format (e.g. 1200 is 12pm)
 + DAY is the day of the meeting
 + MONTH is the month of the meeting
 + YEAR is the year of the meeting



Template
------------------------
The markdown files for the meeting agendas need to follow this template so that they can be converted into pdf files successfully using with the script [/meeting_agendas/scripts/build_pdfs.sh](../../../meeting_agendas/scripts/build_pdfs.sh) which uses [pandoc](http://pandoc.org/)  

```md
---
author: AUTHOR_FULL_NAME - AUTHOR_STUDENT_NUMBER
---

# Meeting Agenda - Aircraft Direction Prediction
| *Date:*           | DAY/MONTH/YEAR |
| ----------------- | -------------- |
| *Time:*           | TIME           |
| *Location:*       | LOCATION       |


## Agenda Items
+ AGENDA_ITEMS ...
```

where:
 + AUTHOR_FULL_NAME is the full name of the author of this agenda
 + AUTHOR_STUDENT_NUMBER is the student number of the author
 + TIME is the time of the meeting in hh:mm format (e.g. 12:00 is 12pm)
 + DAY is the day of the meeting
 + MONTH is the month of the meeting
 + YEAR is the year of the meeting
 + AGENDA_ITEMS are the agenda items for the meeting
