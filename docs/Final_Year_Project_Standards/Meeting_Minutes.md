Meeting Minutes
===================

_Section author: Luke Frisken <[l.frisken@gmail.com](mailto:l.frisken@gmail.com)>_

The minutes for all project meetings are stored in the [/meeting_minutes](../../../meeting_minutes) folder as markdown files.


New Minutes Script
---------------------

Minutes and Agenda files can be generated using the [New Minutes Script](New_Minutes_Script.md) for your coding convenience.


File Naming Convention
----------------------

The file naming convention is as follows

> MeetingMinutes_ID_TIME_DAY_MONTH_YEAR.md

where:

 + ID is the id for the meeting, this is in order of date of meeting. It should match up to the [agenda](Meeting_Agendas.md) for that meeting if there is one.
 + TIME is the time of the meeting in hhmm format (e.g. 1200 is 12pm)
 + DAY is the day of the meeting
 + MONTH is the month of the meeting
 + YEAR is the year of the meeting


Template
------------------------
The markdown files for the meeting minutes need to follow this template so that they can be converted into pdf files successfully using with the script [/meeting_minutes/scripts/build_pdfs.sh](../../../meeting_minutes/scripts/build_pdfs.sh) which uses [pandoc](http://pandoc.org/)  

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

## Notes
NOTES ...

## Action Items
ACTION_ITEMS ...
```

where:
 + AUTHOR_FULL_NAME is the full name of the author of this agenda
 + AUTHOR_STUDENT_NUMBER is the student number of the author
 + TIME is the time of the meeting in hh:mm format (e.g. 12:00 is 12pm)
 + DAY is the day of the meeting
 + MONTH is the month of the meeting
 + YEAR is the year of the meeting
 + AGENDA_ITEMS are the agenda items to be covered for the meeting
 + NOTES are notes taken during the meeting
 + ACTION_ITEMS are items which need to be acted upon after the meeting and who is responsible for each of those items
