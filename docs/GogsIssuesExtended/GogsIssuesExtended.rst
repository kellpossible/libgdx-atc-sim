GogsIssuesExtended
====================

.. sectionauthor:: Luke Frisken <l.frisken@gmail.com>

GogsIssuesExtended is a python web service built using `Flask
<http://flask.pocoo.org/>`_, designed to access the
`Gogs <https://gogs.io/>`_ git service
`API <https://github.com/gogits/gogs/tree/master/routers/api/v1>`_ and
extend upon the Gogs issue tracker to provide extra services such as
Gantt Charts, Burndown charts and more. We used this software to help
manage this project.

There is now a published repository for this project at 
https://github.com/kellpossible/gitea-issues-extended which has been
updated to work with the `Gitea <https://github.com/go-gitea/gitea>`_
project instead of Gogs.

Class Diagram
-------------

.. figure:: ./Class_Diagram.svg
	:width: 100 %
	:align: center

	Class Diagram

Routes
------

List of URL routes (and the functions associated with them) available on the
GogsIssuesExtended server.

 - `/`
 - `/gantt-chart`
 - `/login`
 - `/logout`
 - `/kanban-board`
 - `/gantt-chart-REST`
 - `/gantt-chart-REST/link`
 - `/gantt-chart-REST/task/#taskid`